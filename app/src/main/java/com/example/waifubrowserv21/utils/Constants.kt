package com.example.waifubrowserv21.utils

import android.os.Parcel
import android.os.Parcelable
import com.example.waifubrowserv2.models.ImageObj

object Constants {
    val IMAGE_ID : String = "image_id"
    val IMAGE_OBJECT : String = "image_obj"
    const val CHANNEL_ID: String = "channel_1"
    const val IMAGE_URL : String = ""
    const val WAIFU_CATEGORY: String = "category"
    const val IDEAL_IMAGE_SIZE : Long = 10000000

    const val MULTIPLE_WAIFU_SFW_LINK =
        "https://api.waifu.im/search?is_nsfw=false&orientation=PORTRAIT&gif=false&many=true&included_tags="

    const val MULTIPLE_WAIFU_NSFW_LINK =
        "https://api.waifu.im/search?is_nsfw=true&orientation=PORTRAIT&gif=false&many=true&included_tags="

    val SFW_LINK_CATEGORY = arrayOf(
        "waifu", "maid", "marin-kitagawa", "mori-calliope", "raiden-shogun",
        "oppai", "selfies", "uniform"
    )

    val NSFW_LINK_CATEGORY = arrayOf(
        "waifu",
        "maid",
        "marin-kitagawa",
        "mori-calliope",
        "raiden-shogun",
        "oppai",
        "selfies",
        "uniform",
        "ass",
        "hentai",
        "milf",
        "oral",
        "ecchi",
        "ero",
        "paizuri"
    )

}