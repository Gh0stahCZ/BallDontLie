package com.tomaschlapek.nba.core.network.retrofit

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tomaschlapek.nba.core.model.PlayerItem
import com.tomaschlapek.nba.core.network.ApiService
import com.tomaschlapek.nba.core.network.Constants.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerDataSource @Inject constructor(
    val networkApi: ApiService
) : PagingSource<Int, PlayerItem>() {

    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, PlayerItem> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = networkApi.getPlayers(params.loadSize, page)
            val players = mutableListOf<PlayerItem>()
            players.addAll(response.data as MutableList<PlayerItem>)
            PagingSource.LoadResult.Page(
                data = players,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (response.meta?.nextPage == null) null else page + 1
            )

        } catch (exception: IOException) {
            val error = IOException("Please Check Internet Connection")
            PagingSource.LoadResult.Error(error)
        } catch (exception: HttpException) {
            PagingSource.LoadResult.Error(exception)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, PlayerItem>): Int? {
        return state.anchorPosition
    }
}

