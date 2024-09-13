package com.vedic.pagination.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.vedic.pagination.R
import com.vedic.pagination.core.RecyclerViewEventHandler
import com.vedic.pagination.core.RecyclerViewPaginationScrollListener
import com.vedic.pagination.core.UiStateResource
import com.vedic.pagination.databinding.ActivityRecyclerViewPaginationBinding
import com.vedic.pagination.ui.recyclerViewAdapter.WallPaperAdapter
import com.vedic.pagination.ui.viewModel.WallPaperViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RecyclerViewPaginationActivity : AppCompatActivity(), RecyclerViewEventHandler {
    private lateinit var binding: ActivityRecyclerViewPaginationBinding

    @Inject
    lateinit var wallPaperAdapter: WallPaperAdapter
    private var recyclerViewPaginationScrollListener: RecyclerViewPaginationScrollListener? = null

    private val wallPaperViewModel: WallPaperViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler_view_pagination)
        setRecyclerView()
        setObserver()
    }

    private fun setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                wallPaperViewModel.wallPaperList.collectLatest {
                    Log.d("Abhishek", "collect items size ${it.size}")
                    wallPaperAdapter.differ.submitList(it)
                }
            }
        }
    }

    private fun setRecyclerView() {
        val layoutManager = GridLayoutManager(this, 2)
        recyclerViewPaginationScrollListener = RecyclerViewPaginationScrollListener(layoutManager).apply {
            setRecyclerViewEventHandler(this@RecyclerViewPaginationActivity)
        }
        binding.rv.apply {
            this.layoutManager = layoutManager
            adapter = wallPaperAdapter
            recyclerViewPaginationScrollListener?.let {
                this.addOnScrollListener(it)
            }
        }
    }

    override fun loadMoreItems() {
        wallPaperViewModel.fetchWallPaper()
    }

    override fun isLastPage(): Boolean {
        return wallPaperViewModel.isLastPage
    }

    override fun isLoading(): Boolean {
        return wallPaperViewModel.uiState.value is UiStateResource.Loading
    }

    override fun isLastRequestSuccess(): Boolean {
        return wallPaperViewModel.uiState.value is UiStateResource.Success
    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerViewPaginationScrollListener?.let {
            binding.rv.removeOnScrollListener(it)
            it.clearHandler()
        }
    }
}