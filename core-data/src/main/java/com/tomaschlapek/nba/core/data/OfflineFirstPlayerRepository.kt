/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tomaschlapek.nba.core.data

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.tomaschlapek.nba.core.database.PlayerDao
import com.tomaschlapek.nba.core.database.PlayerItemEntity
import com.tomaschlapek.nba.core.model.PlayerItem
import com.tomaschlapek.nba.core.model.asExternalModel
import com.tomaschlapek.nba.core.network.Constants.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class OfflineFirstPlayerRepository @Inject constructor(private val playerDao: PlayerDao, private val playerRemoteMediator: PlayerRemoteMediator) :
    PlayerRepository {
    override fun getPlayers(): Flow<PagingData<PlayerItem>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                pageSize = NETWORK_PAGE_SIZE,
            ),
            pagingSourceFactory = {
                playerDao.pagingSource()
            },
            remoteMediator = playerRemoteMediator
        ).flow.map { data ->
            data.map { it.asExternalModel() }
        }
    }
    override fun hasCachedData(): Flow<Boolean> {
        return playerDao.hasAnyPlayer()
    }
}
