package com.example.waifubrowserv2.models

import java.io.Serializable
/**
 * {
    "images": [
    {
    "signature": "4e6e8aaf6b5c8dcf",
    "extension": ".jpg",
    "image_id": 7778,
    "favorites": 0,
    "dominant_color": "#b7adb0",
    "source": "https://www.pixiv.net/en/artworks/99526468",
    "uploaded_at": "2022-07-10T14:05:08.995696+02:00",
    "liked_at": null,
    "is_nsfw": false,
    "width": 1364,
    "height": 2048,
    "byte_size": 1415007,
    "url": "https://cdn.waifu.im/7778.jpg",
    "preview_url": "https://www.waifu.im/preview/7778/",
    "tags": [
    {
    "tag_id": 12,
    "name": "waifu",
    "description": "A female anime/manga character.",
    "is_nsfw": false
    }
    ]
 *}
 */

data class WaifuIm (
    val images : List<ImageObj>
) : Serializable

data class ImageObj (
    val extension : String,
    val image_id : String,
    val is_nsfw: Boolean,
    val byte_size: Long,
    val url : String

) : Serializable