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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tomaschlapek.nba.core.model.PlayerItem
import com.tomaschlapek.nba.core.model.Team
import com.tomaschlapek.nba.core.ui.BallersTheme
import com.tomaschlapek.nba.core.ui.components.navigationBarHeight
import com.tomaschlapek.nba.core.ui.status.ErrorScreen
import com.tomaschlapek.nba.feature.player.R

/**
 * Screen for displaying player stats.
 */
@Composable
fun BallerStatsScreen(
    modifier: Modifier = Modifier,
    viewModel: BallerStatsViewModel = hiltViewModel(),
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    BallerStatsContent(
        modifier,
        state,
        onRepeaClick = viewModel::repeat
    )
}

/**
 * Content of the screen.
 */
@Composable
private fun BallerStatsContent(
    modifier: Modifier,
    state: BallerStatsViewModel.BallerStatsState,
    onRepeaClick: () -> Unit = {}
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .paint(
                painterResource(id = R.drawable.ball1),
                contentScale = ContentScale.FillWidth
            )
            .padding(16.dp)
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
            state.player?.let {
                BallerStatContent(state.player)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BallerStatContent(player: PlayerItem) {

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var skipPartiallyExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    // Sheet content
    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState,
            windowInsets = WindowInsets(bottom = LocalContext.current.navigationBarHeight),

            ) {
            ModalBottomSheetContent(player)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
            )
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "⛹🏽‍♂️ ${player.firstName} ${player.lastName} ⛹🏼‍♀️",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Position: ${player.position ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ClickableTeamText(
                    teamFullName = player.team?.fullName ?: "N/A",
                    modifier = Modifier.padding(bottom = 8.dp),
                    onClick = {
                        openBottomSheet = !openBottomSheet
                    }
                )

                Text(
                    text = "Height: ${player.heightFeet?.let { "${it}ft" } ?: "N/A"} ${player.heightInches?.let { "${it}in" } ?: ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Weight: ${player.weightPounds?.let { "$it lbs" } ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
private fun ModalBottomSheetContent(player: PlayerItem) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),

        ) {

        player.team?.fullName?.let {
            Text(
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                text = "\uD83C\uDFC0 " + it + " 🏀",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            )
        }
        Text(
            text = "Division: " + (player.team?.division ?: "N/A"),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "City: " + (player.team?.city ?: "N/A"),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Conference: " + (player.team?.conference ?: "N/A"),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ClickableTeamText(modifier: Modifier, teamFullName: String, onClick: () -> Unit = {}) {
    val annotatedString = buildAnnotatedString {
        val teamName = "Team: "
        append(teamName)
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(teamFullName)
            addStyle(
                SpanStyle(textDecoration = TextDecoration.Underline),
                start = teamName.length,
                end = length
            )
            addStringAnnotation("teamFullName", teamFullName, 0, length)
        }
    }

    ClickableText(
        modifier = modifier,
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations("teamFullName", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    val clickedTeamFullName = annotation.item
                    // Perform action when the text is clicked
                    println("$clickedTeamFullName was clicked!")
                    onClick()
                }
        },
        style = MaterialTheme.typography.bodyMedium,
    )
}


// Previews

@Preview(showBackground = true)
@Composable
private fun BottomSheetPreview() {
    BallersTheme {
        ModalBottomSheetContent(
            player = PlayerItem(
                id = 1,
                firstName = "Tomas",
                lastName = "Chlapek",
                team = Team(
                    id = 1,
                    name = "Team",
                    city = "City",
                    conference = "Conference",
                    division = "Division",
                    fullName = "XXXX YYY ZZZ"
                )
            )
        )
    }
}

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
                        division = "Division"
                    )
                )
            ), {}
        )
    }
}


@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitErrorPreview() {
    BallersTheme {
        BallerStatsContent(
            Modifier,
            BallerStatsViewModel.BallerStatsState(
                errorText = "Error",
            ), {}
        )
    }
}


