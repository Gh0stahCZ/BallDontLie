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

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.tomaschlapek.nba.core.data.OfflineFirstPlayerRepository
import com.tomaschlapek.nba.core.data.PlayerRemoteMediator
import com.tomaschlapek.nba.core.data.PlayerRepository
import com.tomaschlapek.nba.core.data.util.ConnectivityManagerNetworkMonitor
import com.tomaschlapek.nba.core.data.util.NetworkMonitor
import com.tomaschlapek.nba.core.database.PlayerItemEntity
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsPlayerRepository(
        playerRepository: OfflineFirstPlayerRepository
    ): PlayerRepository

    @Binds
    fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor

}

val fakePlayers = listOf("One", "Two", "Three")
