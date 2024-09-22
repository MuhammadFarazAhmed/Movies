package com.example.movies.common

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.example.domain.model.Media
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CustomNavType {

    val MediaType = object : NavType<Media>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): Media? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Media {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: Media): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: Media) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}