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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.tomaschlapek.nba.core.model.PlayerItem
import com.tomaschlapek.nba.core.ui.BallersTheme
import com.tomaschlapek.nba.core.ui.status.ErrorScreen
import com.tomaschlapek.nba.feature.player.R
import kotlinx.coroutines.flow.flowOf

@Composable
fun BallersScreen(
    modifier: Modifier = Modifier,
    viewModel: BallersViewModel = hiltViewModel(),
    navigateToDetail: (PlayerItem) -> Unit = {},
    windowWidth: WindowWidthSizeClass
) {
    val players = viewModel.currentResult.collectAsLazyPagingItems()
    BallersContent(
        modifier,
        players,
        windowWidth,
        onPlayerClick = navigateToDetail
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
private fun BallersContent(
    modifier: Modifier,
    players: LazyPagingItems<PlayerItem>,
    windowWidth: WindowWidthSizeClass,
    onPlayerClick: (PlayerItem) -> Unit = {}
) {

    when (players.loadState.refresh) {
        LoadState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        is LoadState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ErrorScreen(
                    description = stringResource(R.string.something_went_wrong),
                    onActionButtonClick = {
                        players.refresh()
                    }
                )
            }
        }

        else -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(if (windowWidth == WindowWidthSizeClass.Expanded) 2 else 1),
                modifier = modifier.paint(
                    painterResource(id = R.drawable.ball1),
                    contentScale = ContentScale.FillWidth
                ),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
            ) {
                items(
                    count = players.itemCount,
                    key = players.itemKey(),
                    contentType = players.itemContentType()
                ) { index ->
                    val item = players[index]
                    item?.let {
                        BallerCard(
                            player = it,
                            onPlayerClick = onPlayerClick
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BallerCard(player: PlayerItem, onPlayerClick: (PlayerItem) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        onClick = { onPlayerClick(player) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${player.firstName} ${player.lastName}",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Position: ${player.position ?: "N/A"}",
            )
            Text(
                text = "Team: ${player.team?.fullName ?: "N/A"}",
            )

        }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    BallersTheme {
        BallersContent(
            Modifier,
            flowOf(
                PagingData.empty<PlayerItem>()
            ).collectAsLazyPagingItems(),
            WindowWidthSizeClass.Medium
        )
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    BallersTheme {
        BallersContent(
            Modifier,
            flowOf(
                PagingData.empty<PlayerItem>()
            ).collectAsLazyPagingItems(), WindowWidthSizeClass.Medium
        )
    }
}
