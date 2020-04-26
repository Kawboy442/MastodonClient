package com.ritou.android.mastodonclient.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ritou.android.mastodonclient.R
import com.ritou.android.mastodonclient.data.MastodonApi
import com.ritou.android.mastodonclient.domain.Toot
import com.ritou.android.mastodonclient.databinding.FragmentTootListBinding
import com.ritou.android.mastodonclient.view.viewadapter.TootListAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.atomic.AtomicBoolean

class TootListFragment: Fragment(R.layout.fragment_toot_list) {

    companion object {
        val TAG = TootListFragment::class.java.simpleName

        private const val API_BASE_URL = "https://androidbook2020.keiji.io"
    }

    private var binding: FragmentTootListBinding? = null

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val api = retrofit.create(MastodonApi::class.java)

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private lateinit var adapter: TootListAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var isLoading = MutableLiveData<Boolean>()
    private var hasNext = AtomicBoolean().apply { set(true) }

    private val loadNextScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val isLoadingSnapshot = isLoading.value ?: return
            if (isLoadingSnapshot || !hasNext.get()) {
                return
            }

            val visibleItemCount = recyclerView.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            if ((totalItemCount - visibleItemCount) <= firstVisibleItemPosition) {
                loadNext()
            }
        }
    }

    private var tootList = ArrayList<Toot>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter =
            TootListAdapter(
                layoutInflater,
                tootList
            )

        layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false)

        val bindingData: FragmentTootListBinding? = DataBindingUtil.bind(view)
        binding = bindingData ?: return

        bindingData.recyclerView.also {
            it.layoutManager = layoutManager
            it.adapter = adapter
            it.addOnScrollListener(loadNextScrollListener)
        }
        bindingData.swipeRefreshLayout.setOnRefreshListener {
            tootList.clear()
            loadNext()
        }

        isLoading.observe(viewLifecycleOwner, Observer {
            binding?.swipeRefreshLayout?.isRefreshing = it
        })

        loadNext()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding?.unbind()
    }

    override fun onDestroy() {
        super.onDestroy()

        coroutineScope.cancel()
    }

    private fun loadNext() {
        lifecycleScope.launch {
            isLoading.set(true)

            val tootListResponse = withContext(Dispatchers.IO) {
                api.fetchPublicTimeline(
                    maxId = tootList.lastOrNull()?.id,
                    onlyMedia = true
                )
            }
            Log.d(TAG, "fetchPublicTimeline")

            tootList.addAll(tootListResponse.filter { !it.sensitive })
            Log.d(TAG, "addAll")

            reloadTootList()
            Log.d(TAG, "reloadTootList")

            isLoading.set(false)
            hasNext.set(tootListResponse.isNotEmpty())
            Log.d(TAG, "dismissProgress")
        }
    }

    private suspend fun reloadTootList() = withContext(Dispatchers.Main) {
        adapter.notifyDataSetChanged()
    }
}