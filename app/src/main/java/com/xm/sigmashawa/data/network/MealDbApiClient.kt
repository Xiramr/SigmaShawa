package com.xm.sigmashawa.data.network

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MealDbApiClient(
    private val endpoint: String = "https://www.themealdb.com/api/json/v1/1/random.php"
) {
    fun fetchRandomMeal(): MealDbDto {
        val connection = (URL(endpoint).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 5_000
            readTimeout = 5_000
            setRequestProperty("Accept", "application/json")
        }

        return try {
            val responseCode = connection.responseCode
            val responseText = (if (responseCode in 200..299) connection.inputStream else connection.errorStream)
                ?.bufferedReader()
                ?.use { it.readText() }
                .orEmpty()

            if (responseCode !in 200..299) {
                throw IllegalStateException("HTTP $responseCode")
            }

            parseMeal(responseText)
        } finally {
            connection.disconnect()
        }
    }

    private fun parseMeal(body: String): MealDbDto {
        val meals = JSONObject(body).optJSONArray("meals")
            ?: throw IllegalStateException("Response has no meals array")

        if (meals.length() == 0) {
            throw IllegalStateException("Meals array is empty")
        }

        val firstMeal = meals.getJSONObject(0)
        val name = firstMeal.optString("strMeal").takeIf { it.isNotBlank() } ?: "Unknown meal"
        val category = firstMeal.optString("strCategory").takeIf { it.isNotBlank() } ?: "Unknown category"
        val area = firstMeal.optString("strArea").takeIf { it.isNotBlank() } ?: "Unknown origin"
        val thumbnailUrl = firstMeal.optString("strMealThumb").takeIf { it.isNotBlank() }

        return MealDbDto(
            name = name,
            category = category,
            area = area,
            thumbnailUrl = thumbnailUrl
        )
    }
}
