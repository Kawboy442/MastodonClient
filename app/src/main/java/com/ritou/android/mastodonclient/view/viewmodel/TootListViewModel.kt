package com.ritou.android.mastodonclient.view.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.ritou.android.mastodonclient.domain.Toot
import com.ritou.android.mastodonclient.domain.TootRepository
import com.ritou.android.mastodonclient.domain.UserCredential
import com.ritou.android.mastodonclient.domain.UserCredentialRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class TootListViewModel(
    private val instanceUrl: String,
    private val username: String,
    private val coroutineScope: CoroutineScope,
    application: Application
): AndroidViewModel(application), LifecycleObserver {

    private val userCredentialRepository = UserCredentialRepository(
        application
    )

    private lateinit var tootRepository: TootRepository

    private lateinit var userCredential: UserCredential

    val isLoading = MutableLiveData<Boolean>()
    var hasNext = true

    val tootList = MutableLiveData<ArrayList<Toot>>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        coroutineScope.launch {
            userCredential = userCredentialRepository
                .find(instanceUrl, username) ?: return@launch
            tootRepository = TootRepository(userCredential)
            loadNext()
        }
    }

    fun clear() {
        val tootListSnapshot = tootList.value ?: return
        tootListSnapshot.clear()
    }

    fun loadNext() {
        coroutineScope.launch {
            isLoading.postValue(true)

            val tootListSnapshot = tootList.value ?: ArrayList()

            val maxId = tootListSnapshot.lastOrNull()?.id
            val tootListResponse = tootRepository.feachHomeTimeline(
                maxId = maxId
            )
            tootListSnapshot.addAll(tootListResponse)
            tootList.postValue(tootListSnapshot)

            hasNext = tootListResponse.isNotEmpty()
            isLoading.postValue(false)
        }
    }
}