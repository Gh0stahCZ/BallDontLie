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

package com.tomaschlapek.nba.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.tomaschlapek.nba.core.model.PlayerItem
import com.tomaschlapek.nba.feature.player.ui.BallersScreen
import com.tomaschlapek.nba.feature.player.ui.stats.BallerStatsScreen
import com.tomaschlapek.nba.ui.destinations.BallerDetailScreenDestination


@RootNavGraph(start = true)
@NavGraph
annotation class BallersNavGraph(
    val start: Boolean = false
)

@BallersNavGraph(start = true)
@Destination
@Composable
fun BallersScreen(navigator: DestinationsNavigator) {
    BallersScreen(modifier = Modifier.padding(16.dp), navigateToDetail = { baller ->
        navigator.navigate(
            direction = BallerDetailScreenDestination(baller = baller),
            onlyIfResumed = true
        )
    })
}

@BallersNavGraph
@Destination
@Composable
fun BallerDetailScreen(navigator: DestinationsNavigator, baller: PlayerItem) {
    BallerStatsScreen(modifier = Modifier.padding(16.dp), navigateToTeam = { /*baller ->
        navigator.navigate(
            direction = BallerDetailScreenDestination(baller = baller),
            onlyIfResumed = true
        )*/
    })
}
