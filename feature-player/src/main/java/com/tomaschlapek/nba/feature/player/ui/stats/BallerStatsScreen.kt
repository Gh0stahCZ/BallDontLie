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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tomaschlapek.nba.core.model.PlayerItem
import com.tomaschlapek.nba.core.model.Team
import com.tomaschlapek.nba.core.ui.BallersTheme
import com.tomaschlapek.nba.core.ui.status.ErrorScreen

@Composable
fun BallerStatsScreen(
    modifier: Modifier = Modifier,
    viewModel: BallerStatsViewModel = hiltViewModel(),
    navigateToTeam: (Team) -> Unit = {}
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    BallerStatsContent(
        modifier,
        state,
        onTeamClick = navigateToTeam,
        onRepeaClick = viewModel::repeat
    )
}

@Composable
private fun BallerStatsContent(
    modifier: Modifier,
    state: BallerStatsViewModel.BallerStatsState,
    onTeamClick: (Team) -> Unit = {},
    onRepeaClick: () -> Unit = {}
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AnimatedVisibility(
            visible = state.loading,
            enter = fadeIn(
                // Overwrites the initial value of alpha to 0.4f for fade in, 0 by default
                initialAlpha = 0.4f
            ),
            exit = fadeOut(
                // Overwrites the default animation with tween
                animationSpec = tween(durationMillis = 250)
            )
        ) {
            CircularProgressIndicator(color = ProgressIndicatorDefaults.linearColor)
        }

        AnimatedVisibility(
            visible = state.errorText?.isNotBlank() ?: false,
            enter = fadeIn(
                // Overwrites the initial value of alpha to 0.4f for fade in, 0 by default
                initialAlpha = 0.4f
            ),
            exit = fadeOut(
                // Overwrites the default animation with tween
                animationSpec = tween(durationMillis = 250)
            )
        ) {
            state.errorText?.let {
                ErrorScreen(description = state.errorText, onActionButtonClick = onRepeaClick)
            }
        }


        AnimatedVisibility(
            visible = state.player != null,
            enter = fadeIn(
                // Overwrites the initial value of alpha to 0.4f for fade in, 0 by default
                initialAlpha = 0.4f
            ),
            exit = fadeOut(
                // Overwrites the default animation with tween
                animationSpec = tween(durationMillis = 250)
            )
        ) {
            Text(modifier = modifier.clickable {
                state.player?.team?.let {
                    onTeamClick(it)
                }
            }, text = state.player.toString())
        }
    }


}


// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    BallersTheme {
        BallerStatsContent(
            Modifier,
            BallerStatsViewModel.BallerStatsState(
                player = PlayerItem(
                    id = 1,
                    firstName = "Tomas",
                    lastName = "Chlapek",
                    team = Team(
                        id = 1,
                        name = "Team",
                        city = "City",
                        conference = "Conference",
                        division = "Division"
                    )
                )
            ), {}
        )
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    BallersTheme {
        BallerStatsContent(
            Modifier,
            BallerStatsViewModel.BallerStatsState(
                player = PlayerItem(
                    id = 1,
                    firstName = "Tomas",
                    lastName = "Chlapek",
                    team = Team(
                        id = 1,
                        name = "Team",
                        city = "City",
                        conference = "Conference",
                        division = "Division"
                    )
                )
            ), {}
        )
    }
}
