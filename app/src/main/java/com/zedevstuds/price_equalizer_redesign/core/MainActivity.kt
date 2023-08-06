package com.zedevstuds.price_equalizer_redesign.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                    tonalElevation = 5.dp
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