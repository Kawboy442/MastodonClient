package com.ritou.android.mastodonclient.data

import com.ritou.android.mastodonclient.domain.Toot
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MastodonApi {

    @GET ("api/v1/timelines/public")
    suspend fun fetchPublicTimeline (
         @Query("max_id") maxId: String? = null,
         @Query("only_media") onlyMedia: Boolean = false
    ): List<Toot>

    @GET ("api/v1/timelines/home")
    suspend fun feachHomeTimeline(
        @Header("Authorization") accessToken: String,
        @Query("max_Id") maxId: String? = null
    )
}