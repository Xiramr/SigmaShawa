package com.xm.sigmashawa.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.xm.sigmashawa.domain.model.Ingredient
import com.xm.sigmashawa.domain.repository.InMemoryMenuRepository
import com.xm.sigmashawa.domain.service.OrderService
import kotlinx.coroutines.launch

private val MacGreen = Color(0xFF1E8E3E)
private val MacYellow = Color(0xFFF2B705)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SigmaShawaScreen(
    modifier: Modifier = Modifier,
    orderService: OrderService = remember { OrderService(InMemoryMenuRepository()) }
) {
    var productId by rememberSaveable { mutableStateOf("sh1") }
    var countText by rememberSaveable { mutableStateOf("1") }
    var cartCount by rememberSaveable { mutableIntStateOf(0) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "SigmaShawa",
                        fontWeight = FontWeight.Black,
                        color = MacGreen
                    )
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
                text = "Дорогой пользователь",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111111)
            )

            Text(
                text = "Самоео полезное приложение",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF444444)
            )

            OutlinedTextField(
                value = productId,
                onValueChange = { productId = it.trim() },
                label = { Text("ID товара (sh1, sh2, dr1)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            OutlinedTextField(
                value = countText,
                onValueChange = { countText = it.filter { ch -> ch.isDigit() }.take(2) },
                label = { Text("Количество") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Button(
                onClick = {
                    val count = countText.toIntOrNull() ?: 0
                    if (count <= 0) {
                        scope.launch { snackbarHostState.showSnackbar("Количество должно быть > 0") }
                        return@Button
                    }

                    runCatching {
                        orderService.addToCart(
                            productId = productId,
                            extras = emptyList<Ingredient>(),
                            count = count
                        )
                    }.onSuccess {
                        cartCount += count
                        scope.launch {
                            snackbarHostState.showSnackbar("Добавлено: +$count (всего $cartCount)")
                        }
                    }.onFailure { e ->
                        scope.launch { snackbarHostState.showSnackbar("Ошибка: ${e.message}") }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MacGreen,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Положить в корзину", fontWeight = FontWeight.Bold)
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Text("В корзине:", fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                    Text(

                        text = "$cartCount шт",
                        fontWeight = FontWeight.Black,
                        color = MacYellow
                    )
                }
            }
        }
    }
}