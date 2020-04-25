package com.ritou.android.mastodonclient.data

import com.ritou.android.mastodonclient.domain.Toot
import retrofit2.http.GET

interface MastodonApi {

    @GET ("api/v1/timelines/public")
    suspend fun fetchPublicTimeline (
    ): List<Toot>
}