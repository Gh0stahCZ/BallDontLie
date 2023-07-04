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

package com.tomaschlapek.nba.feature.player.ui.stats

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.tomaschlapek.nba.core.model.PlayerItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BallerStatsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(BallerStatsState(savedStateHandle["baller"]))
    val state: StateFlow<BallerStatsState> = _state

    init {
        if (_state.value.player == null) {
            _state.update {
                it.copy(
                    loading = false,
                    errorText = "General error") // TODO Move into resources
            }
        }
    }

    fun repeat() {
        _state.update {
            it.copy(errorText = null)
        }
    }

    data class BallerStatsState(
        val player: PlayerItem? = null,
        val loading: Boolean = false,
        val errorText: String? = null,
    )
}
