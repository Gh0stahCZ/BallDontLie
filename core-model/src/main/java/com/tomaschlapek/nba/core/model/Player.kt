package com.tomaschlapek.nba.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Player(

    @SerialName("id")
    val id: String,

    val name: String
)
