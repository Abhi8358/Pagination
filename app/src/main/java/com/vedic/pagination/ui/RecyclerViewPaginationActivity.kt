package com.vedic.pagination.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.vedic.pagination.R
import com.vedic.pagination.core.ErrorStringType
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
        setUiState()
    }

    private fun setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                wallPaperViewModel.wallPaperList.collectLatest {
                    wallPaperAdapter.differ.submitList(it)
                }
            }
        }
    }

    private fun setRecyclerView() {
        val layoutManager = GridLayoutManager(this, 2)
        recyclerViewPaginationScrollListener =
            RecyclerViewPaginationScrollListener(layoutManager).apply {
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


    private fun setUiState() {
        Log.d("Abhishek Rawat", "state type ${wallPaperViewModel.uiState.value}")
        val state = wallPaperViewModel.uiState
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                state.collectLatest { state ->
                    when (state) {
                        is UiStateResource.Error -> {
                            when(state.stringType) {
                                is ErrorStringType.StringResource -> {
                                    binding.errorText.text = resources.getString(state.stringType.stringId)
                                }
                                is ErrorStringType.StringText -> {
                                    binding.errorText.text = state.stringType.apiErrorString
                                }
                            }

                            binding.errorScreen.visibility = if (state.isFirstPage) View.VISIBLE else View.GONE
                            binding.loadingBar.visibility = View.GONE
                        }

                        is UiStateResource.Loading -> {
                            binding.errorScreen.visibility = View.GONE
                            binding.loadingBar.visibility = View.VISIBLE
                        }

                        is UiStateResource.Success -> {
                            binding.errorScreen.visibility = View.GONE
                            binding.loadingBar.visibility = View.GONE
                        }
                    }
                }
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