package com.xm.sigmashawa.data.repository

import com.xm.sigmashawa.data.network.MealDbApiClient
import com.xm.sigmashawa.domain.model.MealIdea
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MealIdeaRepositoryImpl(
    private val apiClient: MealDbApiClient,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getRandomMealIdea(): Result<MealIdea> {
        return try {
            val remoteMeal = withContext(ioDispatcher) {
                apiClient.fetchRandomMeal()
            }
            Result.success(
                MealIdea(
                    name = remoteMeal.name,
                    category = remoteMeal.category,
                    area = remoteMeal.area,
                    thumbnailUrl = remoteMeal.thumbnailUrl
                )
            )
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}
