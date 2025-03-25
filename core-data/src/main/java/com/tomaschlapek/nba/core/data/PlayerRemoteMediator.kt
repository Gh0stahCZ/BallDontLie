package com.tomaschlapek.nba.core.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.tomaschlapek.nba.core.database.AppDatabase
import com.tomaschlapek.nba.core.database.PlayerDao
import com.tomaschlapek.nba.core.database.PlayerItemEntity
import com.tomaschlapek.nba.core.database.RemoteKeys
import com.tomaschlapek.nba.core.database.RemoteKeysDao
import com.tomaschlapek.nba.core.model.asEntityList
import com.tomaschlapek.nba.core.network.ApiService
import com.tomaschlapek.nba.core.network.Constants.CACHE_EXPIRATION
import com.tomaschlapek.nba.core.network.Constants.NETWORK_PAGE_SIZE
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PlayerRemoteMediator @Inject constructor(
    private val database: AppDatabase,
    private val playerDao: PlayerDao,
    private val remoteKeysDao: RemoteKeysDao,
    private val apiService: ApiService
) : RemoteMediator<Int, PlayerItemEntity>() {

    /**
     * This method is called when the PagingData is first accessed and no data is currently
     * cached in the database. It triggers a remote refresh and returns
     * [RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH] to signal that a network request
     * should be triggered.
     *
     * It checks whether cached data is out of date and decide whether to trigger a remote refresh.
     */
    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(CACHE_EXPIRATION, TimeUnit.HOURS)

        return if (System.currentTimeMillis() - (remoteKeysDao.getCreationTime()
                ?: 0) < cacheTimeout
        ) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            return InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    /**
     * This method is called when PagingData is first accessed and no data is currently
     * cached in the database. It triggers a remote refresh and returns
     * [RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH] to signal that a network request
     * should be triggered.
     *
     */
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PlayerItemEntity>
    ): MediatorResult {
        Log.d("TAG", "load | $loadType")

        try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                    prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                    nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
            }

            val response = apiService.getPlayers(NETWORK_PAGE_SIZE, page)
            val players = response.data
            val endOfPaginationReached = players.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.playerDao().clearPlayers()
                    database.remoteKeysDao().clearRemoteKeys()
                }

                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (endOfPaginationReached) null else page + 1
                val remoteKeys = players.map {
                    RemoteKeys(
                        playerId = it.id,
                        prevKey = prevKey,
                        currentPage = page,
                        nextKey = nextKey
                    )
                }

                remoteKeysDao.insertAll(remoteKeys)
                playerDao.insertAll(players.asEntityList(page))
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            Log.e("TAG", "Error loading players", e)
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            Log.e("TAG", "Error loading players", e)
            return MediatorResult.Error(e)
        }
    }

    /**
     * Based on anchorPosition from the state, we can get the closest Player item to that position
     * by calling closestItemToPosition and retrieve RemoteKeys from database.
     * If RemoteKeys is null, we return the first page number which is 1 in our example.
     */
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, PlayerItemEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeysDao.getRemoteKeyByPlayerId(id)
            }
        }
    }

    /**
     * Gets first Player loaded from the database.
     */
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PlayerItemEntity>): RemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { player ->
            remoteKeysDao.getRemoteKeyByPlayerId(player.id)
        }
    }

    /**
     * Gets last Player loaded from the database.
     */
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PlayerItemEntity>): RemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { player ->
            remoteKeysDao.getRemoteKeyByPlayerId(player.id)
        }
    }

}
