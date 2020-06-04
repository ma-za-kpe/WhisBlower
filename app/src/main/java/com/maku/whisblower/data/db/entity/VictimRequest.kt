package com.maku.whisblower.data.db.entity


import com.google.gson.annotations.SerializedName

data class VictimRequest(
    @SerializedName("attacter_type")
    val attacterType: String,
    val message: String,
    val organisation: String,
    @SerializedName("spouse_number")
    val spouseNumber: String
)
