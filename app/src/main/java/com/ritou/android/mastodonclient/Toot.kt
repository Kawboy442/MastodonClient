package com.ritou.android.mastodonclient

import com.squareup.moshi.Json

data class Toot (
    val id: String,
    @Json(name = "created_at") val createdAt: String,
    val sensitive: Boolean,
    val url: String,
    val context: String,
    val account: Account
)