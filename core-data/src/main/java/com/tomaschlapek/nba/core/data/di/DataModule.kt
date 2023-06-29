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

package com.tomaschlapek.nba.core.data.di

import androidx.paging.PagingData
import com.tomaschlapek.nba.core.data.DefaultPlayerRepository
import com.tomaschlapek.nba.core.data.PlayerRepository
import com.tomaschlapek.nba.core.model.PlayerItem
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsPlayerRepository(
        playerRepository: DefaultPlayerRepository
    ): PlayerRepository
}

/*class FakePlayerRepository @Inject constructor() : PlayerRepository {
    override fun getPlayers(): Flow<PagingData<PlayerItem>> {
        //TODO return flowOf(fakePlayers)
        return flowOf{}
    }
}*/

val fakePlayers = listOf("One", "Two", "Three")
