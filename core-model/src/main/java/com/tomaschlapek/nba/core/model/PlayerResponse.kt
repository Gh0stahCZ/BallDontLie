package com.tomaschlapek.nba.core.model

import android.os.Parcelable
import com.tomaschlapek.nba.core.database.PlayerItemEntity
import com.tomaschlapek.nba.core.database.TeamEntity
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class PlayersResponse(

    @SerialName("data")
    val data: List<PlayerItem> = mutableListOf(),

    @SerialName("meta")
    val meta: Meta? = null
) : Parcelable

@Parcelize
@Serializable
data class PlayerItem(

    @SerialName("id")
    val id: Int = 0,

    @SerialName("weight_pounds")
    val weightPounds: Int? = null,

    @SerialName("height_feet")
    val heightFeet: Int? = null,

    @SerialName("height_inches")
    val heightInches: Int? = null,

    @SerialName("last_name")
    val lastName: String? = null,

    @SerialName("position")
    val position: String? = null,

    @SerialName("team")
    val team: Team? = null,

    @SerialName("first_name")
    val firstName: String? = null
) : Parcelable

@Parcelize
@Serializable
data class Meta(

    @SerialName("next_page")
    val nextPage: Int? = null,

    @SerialName("per_page")
    val perPage: Int? = null,

    @SerialName("total_count")
    val totalCount: Int? = null,

    @SerialName("total_pages")
    val totalPages: Int? = null,

    @SerialName("current_page")
    val currentPage: Int? = null
) : Parcelable

@Parcelize
@Serializable
data class Team(

    @SerialName("division")
    val division: String? = null,

    @SerialName("conference")
    val conference: String? = null,

    @SerialName("full_name")
    val fullName: String? = null,

    @SerialName("city")
    val city: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("id")
    val id: Int? = null,

    @SerialName("abbreviation")
    val abbreviation: String? = null
) : Parcelable

fun PlayerItemEntity.asExternalModel() = PlayerItem(
    id = id,
    weightPounds = weightPounds,
    heightFeet = heightFeet,
    heightInches = heightInches,
    lastName = lastName,
    position = position,
    firstName = firstName,
    team = team?.asExternalModel()
)

fun PlayerItem.asEntity(page: Int) = PlayerItemEntity(
    id = id,
    weightPounds = weightPounds,
    heightFeet = heightFeet,
    heightInches = heightInches,
    lastName = lastName,
    position = position,
    firstName = firstName,
    team = team?.asEntity(),
    page = page,
)

fun Team.asEntity() = TeamEntity(
    id = id,
    abbreviation = abbreviation,
    city = city,
    conference = conference,
    division = division,
    fullName = fullName,
    name = name
)

fun TeamEntity.asExternalModel() = Team(
    id = id,
    abbreviation = abbreviation,
    city = city,
    conference = conference,
    division = division,
    fullName = fullName,
    name = name
)

fun List<PlayerItem>.asEntityList(page: Int): List<PlayerItemEntity> {
    return map { playerItem ->
        playerItem.asEntity(page)
    }
}
fun List<PlayerItemEntity>.asExternalList(): List<PlayerItem> {
    return map { playerEntity ->
        playerEntity.asExternalModel()
    }
}
