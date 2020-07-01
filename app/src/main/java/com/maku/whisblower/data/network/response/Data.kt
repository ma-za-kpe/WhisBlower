package com.maku.whisblower.data.network.response


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("attackerType")
    val attackerType: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("organisation")
    val organisation: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("__v")
    val v: Int
)