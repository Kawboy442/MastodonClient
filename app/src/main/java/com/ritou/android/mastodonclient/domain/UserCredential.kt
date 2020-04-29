package com.ritou.android.mastodonclient.domain

data class UserCredential (
    val instanceUrl: String,
    var username: String? = null,
    var accessToken: String? = null
)