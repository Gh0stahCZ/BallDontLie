package com.tomaschlapek.nba.core.network.retrofit

import com.tomaschlapek.nba.core.model.PlayersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkDataSource {

    //get players
    @GET(value = "players")
    suspend fun getPlayers(
        @Query("per_page") per_page: Int?,
        @Query("page") page: Int?,
    ): PlayersResponse
}
