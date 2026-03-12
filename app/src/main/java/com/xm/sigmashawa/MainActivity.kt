package com.xm.sigmashawa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.xm.sigmashawa.ui.NetworkMealScreen
import com.xm.sigmashawa.ui.SigmaShawaScreen

class MainActivity : ComponentActivity() {
    private enum class Screen {
        ORDER,
        NETWORK
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                var currentScreen by rememberSaveable { mutableStateOf(Screen.ORDER) }

                when (currentScreen) {
                    Screen.ORDER -> SigmaShawaScreen(
                        onOpenNetworkScreen = { currentScreen = Screen.NETWORK }
                    )
                    Screen.NETWORK -> NetworkMealScreen(
                        onBackToOrder = { currentScreen = Screen.ORDER }
                    )
                }
            }
        }
    }
}
