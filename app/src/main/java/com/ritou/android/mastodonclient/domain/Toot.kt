package com.ritou.android.mastodonclient.domain

import com.squareup.moshi.Json

data class Toot (
    val id: String,
    @Json(name = "created_at") val createdAt: String,
    val sensitive: Boolean,
    val url: String,
    val content: String,
    val account: Account
)