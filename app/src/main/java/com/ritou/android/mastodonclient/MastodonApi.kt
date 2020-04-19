package com.ritou.android.mastodonclient

import okhttp3.ResponseBody
import retrofit2.http.GET

interface MastodonApi {

    @GET ("api/vi/timelines/public")
    suspend fun fetchPublicTimeline (
    ): ResponseBody
}