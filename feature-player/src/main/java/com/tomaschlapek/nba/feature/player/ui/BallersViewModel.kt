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
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tomaschlapek.nba.core.data.DefaultPlayerRepository
import com.tomaschlapek.nba.core.model.PlayerItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class BallersViewModel @Inject constructor(
    defaultPlayerRepository: DefaultPlayerRepository
) : ViewModel() {

    val _currentResult: Flow<PagingData<PlayerItem>> =
        defaultPlayerRepository.getPlayers()
    val currentResult = _currentResult.cachedIn(viewModelScope)

}
