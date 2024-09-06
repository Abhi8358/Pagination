package com.vedic.imageloading

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

internal class ImageFetcher(
    private val memoryCache: MemoryCache,
) {

    suspend fun fetchImage(
        imageSource: ImageSource,
        reqWidth: Int? = null,
        reqHeight: Int? = null
    ): Bitmap? {
        // Fetch from the source
        val bitmap = when (imageSource) {
            is ImageSource.Url -> downloadBitmapFromUrl(imageSource.url, reqWidth, reqHeight)
            is ImageSource.ResourceId -> {
                if (reqWidth != null && reqHeight != null) {
                    decodeSampledBitmapFromResource(
                        imageSource.context,
                        imageSource.id,
                        reqWidth,
                        reqHeight
                    )
                } else {
                    decodeBitmapFromResource(imageSource.context, imageSource.id)
                }
            }
        }

        // Cache the bitmap
        bitmap?.let {
            memoryCache.put(imageSource.getKey(), it)
        }
        return bitmap
    }

    private suspend fun downloadBitmapFromUrl(
        url: String,
        reqWidth: Int?,
        reqHeight: Int?
    ): Bitmap? {
        Log.d("Abhishek length", "reqWidth -: $reqWidth  and reheight -: $reqHeight")
        return withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                connection.connect()
                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    connection.disconnect()
                    return@withContext null
                }
                val inputStream = connection.inputStream
                val bitmap = if (reqWidth != null && reqHeight != null) {
                    decodeSampledBitmapFromStream(inputStream, reqWidth, reqHeight)
                } else {
                    BitmapFactory.decodeStream(inputStream)
                }

                // this is for increase or decrease the quality of image
             /*   val options = BitmapFactory.Options()
                options.inSampleSize = 2*/
                //val bitmap = BitmapFactory.decodeStream(inputStream,null,null)
                inputStream.close()
                connection.disconnect()
                bitmap
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun decodeSampledBitmapFromResource(
        context: Context,
        resId: Int,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap? {
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeResource(context.resources, resId, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(context.resources, resId, options)
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (
                (halfHeight / inSampleSize) >= reqHeight &&
                (halfWidth / inSampleSize) >= reqWidth
            ) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    private suspend fun decodeSampledBitmapFromStream(
        inputStream: InputStream,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                // First decode with inJustDecodeBounds=true to check dimensions
                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }

                inputStream.mark(inputStream.available()) // Mark the stream to reset it later
                //BitmapFactory.decodeStream(inputStream, null, options)

                // Calculate the sample size to scale down the image
                options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
                //Log.d("Abhishek onSample", "onSampleSize :- ${options.inSampleSize}")

                // Reset the stream and decode it with inSampleSize applied
                //inputStream.reset()
                options.inJustDecodeBounds = false // Now decode the actual bitmap
                BitmapFactory.decodeStream(inputStream, null, options)
            } catch (e: Exception) {
                e.printStackTrace() // Log or handle exceptions appropriately
                null
            } finally {
                inputStream.close() // Ensure the InputStream is closed
            }
        }
    }

    private fun decodeBitmapFromResource(context: Context, resId: Int): Bitmap? {
        return BitmapFactory.decodeResource(context.resources, resId)
    }
}
