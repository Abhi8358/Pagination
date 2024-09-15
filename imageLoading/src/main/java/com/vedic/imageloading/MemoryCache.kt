package com.vedic.imageloading

import android.graphics.Bitmap
import androidx.collection.LruCache

internal class MemoryCache(maxSize: Int): LruCache<String, Bitmap>(maxSize) {
}