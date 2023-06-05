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

package com.tomaschlapek.nba.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import com.tomaschlapek.nba.core.data.DefaultPlayerRepository
import com.tomaschlapek.nba.core.database.Player
import com.tomaschlapek.nba.core.database.PlayerDao

/**
 * Unit tests for [DefaultPlayerRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultPlayerRepositoryTest {

    @Test
    fun players_newItemSaved_itemIsReturned() = runTest {
        val repository = DefaultPlayerRepository(FakePlayerDao())

        repository.add("Repository")

        assertEquals(repository.players.first().size, 1)
    }

}

private class FakePlayerDao : PlayerDao {

    private val data = mutableListOf<Player>()

    override fun getPlayers(): Flow<List<Player>> = flow {
        emit(data)
    }

    override suspend fun insertPlayer(item: Player) {
        data.add(0, item)
    }
}
