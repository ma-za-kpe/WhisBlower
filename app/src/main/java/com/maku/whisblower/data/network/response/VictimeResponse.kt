package com.maku.whisblower.data.network.response


import com.google.gson.annotations.SerializedName

data class VictimeResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("pagination")
    val pagination: Pagination,
    @SerializedName("success")
    val success: Boolean
)