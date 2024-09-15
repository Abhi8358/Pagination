package com.vedic.imageloading

import android.content.Context

sealed class ImageSource {
    internal abstract fun getKey(): String
    data class Url(val url: String) : ImageSource() {
        override fun getKey(): String {
            return url.hashCode().toString()
        }
    }

    data class ResourceId(val context: Context, val id: Int) : ImageSource() {
        override fun getKey(): String {
            return id.hashCode().toString()
        }
    }
}