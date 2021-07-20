package com.angelote.testimagenes.Clases

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ImagenesServicio(
    @SerializedName("id") var id: String? = null,
    @SerializedName("created_at") var created_at: String? = null,
    @SerializedName("updated_at") var updated_at: String? = null,
    @SerializedName("promoted_at") var promoted_at: String? = null,
    @SerializedName("width") var width: Int? = null,
    @SerializedName("height") var height: Int? = null,
    @SerializedName("color") var color: String? = null,
    @SerializedName("blur_hash") var blur_hash: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("alt_description") var alt_description: String? = null,
    @SerializedName("urls") var urls: Urls? = null,
    @SerializedName("links") var links: Links? = null,
    @SerializedName("likes") var likes: Int? = null,
    @SerializedName("liked_by_user") var liked_by_user: Boolean? = null,
    @SerializedName("sponsorship") var sponsorship: Sponsorship? = null,
    @SerializedName("user") var user: User? = null,
    @SerializedName("favorito") var favorito: Boolean? = false
) : Serializable

data class Urls(
    @SerializedName("raw") var raw: String? = null,
    @SerializedName("full") var full: String? = null,
    @SerializedName("regular") var regular: String? = null,
    @SerializedName("small") var small: String? = null,
    @SerializedName("thumb") var thumb: String? = null
) : Serializable


data class Links(
    @SerializedName("self") var self: String? = null,
    @SerializedName("html") var html: String? = null,
    @SerializedName("download") var download: String? = null,
    @SerializedName("download_location") var download_location: String? = null
) : Serializable

data class Sponsorship(@SerializedName("sponsor") var sponsor: Sponsor? = null) : Serializable

data class Sponsor(
    @SerializedName("total_collections") var total_collections: Int? = null,
    @SerializedName("total_likes") var total_likes: Int? = null,
    @SerializedName("total_photos") var total_photos: Int? = null
) : Serializable

data class User(
    @SerializedName("id") var id: String? = null,
    @SerializedName("updated_at") var updated_at: String? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("first_name") var first_name: String? = null,
    @SerializedName("last_name") var last_name: String? = null,
    @SerializedName("twitter_username") var twitter_username: String? = null,
    @SerializedName("portfolio_url") var portfolio_url: String? = null,
    @SerializedName("bio") var bio: String? = null,
    @SerializedName("profile_image") var profile_image: ProfileImage? = null,
    @SerializedName("instagram_username") var instagram_username: String? = null,
    @SerializedName("total_collections") var total_collections: Int? = null,
    @SerializedName("total_likes") var total_likes: Int? = null,
    @SerializedName("total_photos") var total_photos: Int? = null,
    @SerializedName("photos") var photos: ArrayList<Photos>? = null
) : Serializable

data class Photos(
    @SerializedName("id") var id: String? = null,
    @SerializedName("created_at") var created_at: String? = null,
    @SerializedName("updated_at") var updated_at: String? = null,
    @SerializedName("blur_hash") var blur_hash: String? = null,
    @SerializedName("urls") var urls: Urls? = null,
    @SerializedName("user") var user: User? = null
) : Serializable

data class ProfileImage(
    @SerializedName("small") var small: String? = null,
    @SerializedName("medium") var medium: String? = null,
    @SerializedName("large") var large: String? = null
) : Serializable