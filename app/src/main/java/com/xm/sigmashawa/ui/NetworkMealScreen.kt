package com.xm.sigmashawa.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.xm.sigmashawa.data.network.MealDbApiClient
import com.xm.sigmashawa.data.repository.MealIdeaRepositoryImpl
import com.xm.sigmashawa.domain.model.MealIdea
import kotlinx.coroutines.launch

private val SigmaGreen = Color(0xFF1E8E3E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkMealScreen(
    modifier: Modifier = Modifier,
    onBackToOrder: () -> Unit = {},
    mealIdeaRepository: MealIdeaRepositoryImpl = remember {
        MealIdeaRepositoryImpl(MealDbApiClient())
    }
) {
    var labelText by rememberSaveable {
        mutableStateOf("Нажми кнопку, чтобы загрузить идею блюда из API.")
    }
    var imageUrl by rememberSaveable { mutableStateOf<String?>(null) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun loadMealIdea() {
        scope.launch {
            isLoading = true
            val result = mealIdeaRepository.getRandomMealIdea()
            result
                .onSuccess { idea: MealIdea ->
                    labelText = "Идея из сети: ${idea.name}\nКатегория: ${idea.category}\nКухня: ${idea.area}"
                    imageUrl = idea.thumbnailUrl
                }
                .onFailure { error ->
                    labelText = "Не удалось загрузить данные: ${error.message ?: "unknown error"}"
                    imageUrl = null
                }
            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        loadMealIdea()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "SigmaShawa API",
                        fontWeight = FontWeight.Black,
                        color = SigmaGreen
                    )
                },
                actions = {
                    TextButton(onClick = onBackToOrder) {
                        Text("Заказ")
                    }
                }
            )
        },
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Идеи для блюд",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Источник данных: themealdb.com",
                style = MaterialTheme.typography.bodyMedium
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F3EE)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Лейбл с данными из сети",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(text = labelText)
                    imageUrl?.let { mealImageUrl ->
                        AsyncImage(
                            model = mealImageUrl,
                            contentDescription = "Фото блюда",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        )
                    }
                }
            }

            Button(
                onClick = ::loadMealIdea,
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SigmaGreen,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Другое блюдо", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
