package hr.ferit.lorenacicek.flashcarder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import hr.ferit.lorenacicek.flashcarder.ui.theme.AppNavigation

import hr.ferit.lorenacicek.flashcarder.ui.theme.FlashCarderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashCarderTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}

