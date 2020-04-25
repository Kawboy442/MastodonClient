package com.ritou.android.mastodonclient.domain

import com.squareup.moshi.Json

data class Media(
    val id: String,
    val type: String,
    val url: String,
    @Json(name = "preview_url") val previewUrl: String
)