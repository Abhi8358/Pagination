package com.vedic.pagination

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.vedic.pagination.databinding.ActivityRecyclerViewPaginationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecyclerViewPaginationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecyclerViewPaginationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler_view_pagination)


    }
}