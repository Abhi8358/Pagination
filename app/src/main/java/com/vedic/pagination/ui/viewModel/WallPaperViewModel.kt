package com.vedic.pagination.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vedic.pagination.core.ErrorStringType
import com.vedic.pagination.core.LoadingType
import com.vedic.pagination.core.UiStateResource
import com.vedic.pagination.data.models.PhotoViewData
import com.vedic.pagination.data.models.WallPaperViewData
import com.vedic.pagination.domain.WallPaperUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallPaperViewModel @Inject constructor(private val wallPaperUseCase: WallPaperUseCase) :
    ViewModel() {

    private var pageNumber: Int = 1
    private var lastFetchedPageNumber = -1
    var isLastPage = false
    private var _uiState: MutableStateFlow<UiStateResource<WallPaperViewData>> =
        MutableStateFlow(UiStateResource.Loading(LoadingType.INITIAL))
    private val _wallPaperList: MutableStateFlow<List<PhotoViewData?>> =
        MutableStateFlow(emptyList())

    val uiState = _uiState.asStateFlow()
    val wallPaperList = _wallPaperList.asStateFlow()

    init {
        fetchWallPaper()
    }

    fun fetchWallPaper() {
        viewModelScope.launch {
            Log.d("Abhishek", "pageNumber -: $pageNumber")
            wallPaperUseCase.getWallPapers(
                pageNumber,
                20,
                start = { loadingType: LoadingType ->
                    _uiState.value = UiStateResource.Loading(loadingType)
                },
                onError = { errorStringType: ErrorStringType ->
                    _uiState.value = UiStateResource.Error(errorStringType)
                }
            ).collectLatest { wallPaperViewData ->
                val listOfWallPaper = mutableListOf<PhotoViewData?>()
                listOfWallPaper.addAll(wallPaperList.value)
                listOfWallPaper.addAll(wallPaperViewData.photos ?: emptyList())
                _uiState.value = UiStateResource.Success(wallPaperViewData)
                _wallPaperList.value = listOfWallPaper.toList()
                isLastPage = wallPaperViewData.nextPage == null
                pageNumber =
                    if (wallPaperViewData.nextPage != null) pageNumber + 1 else pageNumber
                Log.d(
                    "Abhishek",
                    "isLastPage -: $isLastPage  pageNumber -: $pageNumber"
                )
            }
        }
    }
}