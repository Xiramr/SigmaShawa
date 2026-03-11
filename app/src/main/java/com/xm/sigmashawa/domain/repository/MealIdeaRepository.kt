package com.xm.sigmashawa.domain.repository

import com.xm.sigmashawa.domain.model.MealIdea

interface MealIdeaRepository {
    suspend fun getRandomMealIdea(): Result<MealIdea>
}
