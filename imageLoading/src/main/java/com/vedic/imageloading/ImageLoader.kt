package com.vedic.imageloading

import android.util.Log
import android.view.ViewTreeObserver
import android.widget.ImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.WeakHashMap

object ImageLoader {

    private val memoryCache: MemoryCache by lazy {
        MemoryCache(getDefaultMemoryCacheSize())
    }

    private val imageFetcher: ImageFetcher by lazy {
        ImageFetcher(memoryCache)
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val imageViewJobs = WeakHashMap<ImageView, Job>()

    /**
     * Loads an image into the provided ImageView from the given ImageSource.
     *
     * @param imageView The target ImageView.
     * @param imageSource The source of the image (URL, resource, etc.).
     * @param placeholder Optional placeholder resource ID.
     * @param errorPlaceholder Optional error resource ID.
     */
    fun loadImage(
        imageView: ImageView,
        imageSource: ImageSource,
        placeholder: Int? = null,
        errorPlaceholder: Int? = null
    ) {
        // Cancel any existing job for this ImageView
        imageViewJobs[imageView]?.cancel()

        imageView.setImageBitmap(null)
        // Set placeholder if provided
        placeholder?.let {
            imageView.setImageResource(it)
        }

        val key = imageSource.getKey()


        // Tag the ImageView with the key to manage request validity
        imageView.tag = key

        // Attempt to load from memory cache
        val cachedBitmap = memoryCache.get(key)
        if (cachedBitmap != null) {
            imageView.setImageBitmap(cachedBitmap)
            return
        }

        // Get ImageView dimensions for scaling
        val reqWidth = imageView.width
        val reqHeight = imageView.height

        // If dimensions are not yet measured, use a global layout listener
        if (reqWidth == 0 || reqHeight == 0) {
            imageView.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    imageView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    loadImage(imageView, imageSource, placeholder, errorPlaceholder)
                }
            })
            return
        }

        // Launch a coroutine to load the image
        val job = coroutineScope.launch {
            val bitmap = imageFetcher.fetchImage(imageSource, reqWidth, reqHeight)
            Log.d("Abhishek", "bitmap :- $bitmap   ${imageSource.getKey()}")
            if (bitmap != null) {
                withContext(Dispatchers.Main) {
                    // Ensure the ImageView is still expecting this image
                    if (imageView.tag == key) {
                        imageView.setImageBitmap(bitmap)
                    }
                }
            } else {
                // Loading failed, set error placeholder if provided
                errorPlaceholder?.let {
                    withContext(Dispatchers.Main) {
                        if (imageView.tag == key) {
                            imageView.setImageResource(it)
                        }
                    }
                }
            }
        }

        // Associate the job with the ImageView for potential cancellation
        imageViewJobs[imageView] = job
    }

    private fun getDefaultMemoryCacheSize(): Int {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        return maxMemory / 8 // Use 1/8th of the available memory for caching
    }
}


