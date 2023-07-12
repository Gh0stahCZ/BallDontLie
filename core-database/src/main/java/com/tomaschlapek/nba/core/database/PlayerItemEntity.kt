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

package com.tomaschlapek.nba.core.database

import androidx.paging.PagingSource
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/*data class PlayerEntity(
    val name: String
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}*/

@Entity(tableName = "playerentity")
data class PlayerItemEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "weight_pounds")
    val weightPounds: Int? = null,

    @ColumnInfo(name = "height_feet")
    val heightFeet: Int? = null,

    @ColumnInfo(name = "height_inches")
    val heightInches: Int? = null,

    @ColumnInfo(name = "last_name")
    val lastName: String? = null,

    @ColumnInfo(name = "position")
    val position: String? = null,

    @ColumnInfo(name = "first_name")
    val firstName: String? = null,

    @ColumnInfo(name = "page")
    var page: Int
)

@Dao
interface PlayerDao {

    @Insert
    suspend fun insertPlayer(item: PlayerItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<PlayerItemEntity>)

    @Query("SELECT * FROM playerentity ORDER BY page")
    fun pagingSource(): PagingSource<Int, PlayerItemEntity>

    @Query("DELETE FROM playerentity")
    suspend fun clearPlayers()

    @Query("SELECT EXISTS(SELECT 1 FROM playerentity)")
    fun hasAnyPlayer(): Flow<Boolean>
}
