package com.vedic.pagination.core

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

interface RecyclerViewEventHandler {
    /**
     * [loadMoreItems] is used to load next page.
     */
    fun loadMoreItems()

    /**
     * [isLastPage] is used to check whether current page is last page or not.
     * Another use of this function can be, we can pass false when a screen do not have pagination.
     */
    fun isLastPage(): Boolean

    /**
     * [isLoading] function is used to tract about current onGoing request.
     */
    fun isLoading(): Boolean

    /**
     * this[isLastRequestSuccess] function protect increase in page count when last request got failed.
     */
    fun isLastRequestSuccess(): Boolean
}

class RecyclerViewPaginationScrollListener(private val layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {
    private var recyclerViewEventHandler: WeakReference<RecyclerViewEventHandler>? = null
    private var totalItemCountInLastRequest = 0

    /**
     * This method is used to put another api call if we reach to end of page.
     * This method does not work when error comes in onGoing request in that case we have handle api request based of error from UI.
     */
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount: Int = layoutManager.childCount
        val totalItemCount: Int = layoutManager.itemCount
        val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
        //Log.d("Rawat", "visibleCOunt :- $visibleItemCount  && totalItemCount -: $totalItemCount  firstVisibleItemPosition -: $firstVisibleItemPosition  last :- ${layoutManager.findLastVisibleItemPosition()}")
        recyclerViewEventHandler?.get()?.let { recyclerViewEventHandler ->
            if (!recyclerViewEventHandler.isLoading() && !recyclerViewEventHandler.isLastPage() && recyclerViewEventHandler.isLastRequestSuccess()) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCountInLastRequest != totalItemCount) {
                    totalItemCountInLastRequest = totalItemCount
                    recyclerViewEventHandler.loadMoreItems()
                }
            }
        }

    }

    /**
     * set listener from this method when we set recyclerView scrollListener.
     * unregistered this in onDestroy on fragment/activity.
     */
    fun setRecyclerViewEventHandler(recyclerViewEventHandler: RecyclerViewEventHandler?) {
        this.recyclerViewEventHandler = WeakReference(recyclerViewEventHandler)
    }

    fun clearHandler() {
        recyclerViewEventHandler?.clear()
    }
}