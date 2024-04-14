package com.example.referee.network.model.mediawiki.List

import com.google.gson.annotations.SerializedName

data class ContinueInfo (
    @SerializedName("sroffset") val sroffset:Int,
    @SerializedName("continue") val continuMessage:String
        )