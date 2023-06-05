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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.tomaschlapek.nba.core.data.PlayerRepository
import com.tomaschlapek.nba.feature.player.ui.PlayerUiState.Error
import com.tomaschlapek.nba.feature.player.ui.PlayerUiState.Loading
import com.tomaschlapek.nba.feature.player.ui.PlayerUiState.Success
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerRepository: PlayerRepository
) : ViewModel() {

    val uiState: StateFlow<PlayerUiState> = playerRepository
        .players.map<List<String>, PlayerUiState> { Success(data = it) }
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addPlayer(name: String) {
        viewModelScope.launch {
            playerRepository.add(name)
        }
    }
}

sealed interface PlayerUiState {
    object Loading : PlayerUiState
    data class Error(val throwable: Throwable) : PlayerUiState
    data class Success(val data: List<String>) : PlayerUiState
}
