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

package com.tomaschlapek.nba.feature.player.ui

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tomaschlapek.nba.core.data.PlayerRepository
import com.tomaschlapek.nba.core.data.util.NetworkMonitor
import com.tomaschlapek.nba.core.model.PlayerItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BallersViewModel @Inject constructor(
    defaultPlayerRepository: PlayerRepository,
    networkMonitor: NetworkMonitor,
) : ViewModel() {

    private val _hasCachedData = defaultPlayerRepository.hasCachedData()
    val hasCachedData = _hasCachedData.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    private val _currentResult: Flow<PagingData<PlayerItem>> =
        defaultPlayerRepository.getPlayers()
    val currentResult = _currentResult.cachedIn(viewModelScope)

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

}
