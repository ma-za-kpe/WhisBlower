package com.maku.whisblower.data.db.entity


import com.google.gson.annotations.SerializedName

data class VictimeRequest(
    @SerializedName("attackerType")
    val attackerType: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("organisation")
    val organisation: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String
)