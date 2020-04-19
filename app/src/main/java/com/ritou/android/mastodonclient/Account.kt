package com.ritou.android.mastodonclient

import com.squareup.moshi.Json

data class Account (
    val id: String,
    val userName: String,
    @Json(name = "display_name") val displayName: String,
    val url: String
)