package com.zedevstuds.price_equalizer_redesign.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.zedevstuds.price_equalizer_redesign.core.ui.MainActivityViewModel
import com.zedevstuds.price_equalizer_redesign.core.ui.theme.PriceCalculatorTheme
import com.zedevstuds.price_equalizer_redesign.price_calculation.ui.mainscreen.MainScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkTheme = viewModel.isDarkMode.collectAsState()

            PriceCalculatorTheme(darkTheme = darkTheme.value) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.background,
                                    MaterialTheme.colorScheme.primary
                                )
                            )
                        ),
//                    color = MaterialTheme.colorScheme.background,
                ) {
                    MainScreen(
                        isDarkMode = darkTheme.value,
                        onThemeUpdated = { isDark ->
                            viewModel.onDarkModeChanged(isDark)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_3)
@Composable
fun GreetingPreview() {
    PriceCalculatorTheme {
        Greeting("Android")
    }
}