package com.tomaschlapek.nba.core.data

import androidx.paging.PagingData
import com.tomaschlapek.nba.core.model.PlayerItem
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    fun getPlayers(): Flow<PagingData<PlayerItem>>
}
