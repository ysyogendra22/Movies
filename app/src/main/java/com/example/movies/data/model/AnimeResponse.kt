package com.example.movies.data.model

import com.google.gson.annotations.SerializedName

data class AnimeResponse(
    @SerializedName("data")
    val data: List<AnimeData>,
    @SerializedName("pagination")
    val pagination: Pagination
)

data class AnimeData(
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("url")
    val url: String?,
    @SerializedName("images")
    val images: Images,
    @SerializedName("title")
    val title: String,
    @SerializedName("title_english")
    val titleEnglish: String?,
    @SerializedName("title_japanese")
    val titleJapanese: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("episodes")
    val episodes: Int?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("aired")
    val aired: Aired?,
    @SerializedName("duration")
    val duration: String?,
    @SerializedName("rating")
    val rating: String?,
    @SerializedName("score")
    val score: Double?,
    @SerializedName("scored_by")
    val scoredBy: Int?,
    @SerializedName("rank")
    val rank: Int?,
    @SerializedName("popularity")
    val popularity: Int?,
    @SerializedName("synopsis")
    val synopsis: String?,
    @SerializedName("background")
    val background: String?,
    @SerializedName("year")
    val year: Int?,
    @SerializedName("genres")
    val genres: List<Genre>?,
    @SerializedName("studios")
    val studios: List<Studio>?
)

data class Images(
    @SerializedName("jpg")
    val jpg: ImageUrl,
    @SerializedName("webp")
    val webp: ImageUrl
)

data class ImageUrl(
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("small_image_url")
    val smallImageUrl: String?,
    @SerializedName("large_image_url")
    val largeImageUrl: String?
)

data class Aired(
    @SerializedName("from")
    val from: String?,
    @SerializedName("to")
    val to: String?,
    @SerializedName("string")
    val string: String?
)

data class Genre(
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class Studio(
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

data class Pagination(
    @SerializedName("last_visible_page")
    val lastVisiblePage: Int,
    @SerializedName("has_next_page")
    val hasNextPage: Boolean
)
