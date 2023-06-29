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

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tomaschlapek.nba.core.model.PlayerItem
import com.tomaschlapek.nba.core.network.ApiService
import com.tomaschlapek.nba.core.network.Constants.NETWORK_PAGE_SIZE
import com.tomaschlapek.nba.core.network.retrofit.PlayerDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultPlayerRepository @Inject constructor(private val apiService: ApiService) : PlayerRepository {
    override fun getPlayers(): Flow<PagingData<PlayerItem>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = {
                PlayerDataSource(apiService)
            }
        ).flow
    }
}
