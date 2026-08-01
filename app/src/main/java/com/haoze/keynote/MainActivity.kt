package com.haoze.keynote

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.haoze.keynote.ui.navigation.AppNavigation
import com.haoze.keynote.ui.theme.DarkModeManager
import com.haoze.keynote.ui.theme.DarkModePreference
import com.haoze.keynote.ui.theme.KeyNoteTheme
import com.haoze.keynote.ui.theme.toDarkModePreference
import com.haoze.keynote.util.PreferencesManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.setBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(this, R.color.app_startup_background))
        )
        setContent {
            val preferencesManager = remember { PreferencesManager(applicationContext) }
            val darkModePrefValue by preferencesManager.darkModePreference.collectAsState(initial = null)
            val systemInDarkTheme = isSystemInDarkTheme()

            val isDarkMode = darkModePrefValue
                ?.toDarkModePreference()
                ?.let { preference ->
                    when (preference) {
                        DarkModePreference.SYSTEM -> systemInDarkTheme
                        DarkModePreference.LIGHT -> false
                        DarkModePreference.DARK -> true
                    }
                }
                ?: systemInDarkTheme

            val darkModeManager = remember(darkModePrefValue, isDarkMode) {
                DarkModeManager(preference = darkModePrefValue?.toDarkModePreference() ?: if (isDarkMode) DarkModePreference.DARK else DarkModePreference.LIGHT)
            }

            KeyNoteTheme(darkModeManager = darkModeManager) {
                val colors = androidx.compose.material3.MaterialTheme.colorScheme
                SideEffect {
                    window.setBackgroundDrawable(ColorDrawable(colors.background.toArgb()))
                    val controller = WindowInsetsControllerCompat(window, window.decorView)
                    controller.isAppearanceLightStatusBars = !isDarkMode
                    controller.isAppearanceLightNavigationBars = !isDarkMode
                }
                Surface(modifier = Modifier.fillMaxSize(), color = colors.background) {
                    if (darkModePrefValue != null) AppNavigation()
                }
            }
        }
    }
}
