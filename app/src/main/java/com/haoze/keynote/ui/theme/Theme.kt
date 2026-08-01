package com.haoze.keynote.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import android.os.Build

// ─── Dark Mode ────────────────────────────────────────────────────────────────

enum class DarkModePreference { SYSTEM, LIGHT, DARK }

@Immutable
data class DarkModeManager(val preference: DarkModePreference = DarkModePreference.SYSTEM) {
    @Composable
    fun isDarkMode(): Boolean = when (preference) {
        DarkModePreference.SYSTEM -> isSystemInDarkTheme()
        DarkModePreference.LIGHT -> false
        DarkModePreference.DARK -> true
    }
}

val LocalDarkModeManager = staticCompositionLocalOf<DarkModeManager> { DarkModeManager() }

fun Int.toDarkModePreference(): DarkModePreference = when (this) {
    1 -> DarkModePreference.LIGHT
    2 -> DarkModePreference.DARK
    else -> DarkModePreference.SYSTEM
}

fun DarkModePreference.toInt(): Int = when (this) {
    DarkModePreference.SYSTEM -> 0
    DarkModePreference.LIGHT -> 1
    DarkModePreference.DARK -> 2
}

// ─── Colors ───────────────────────────────────────────────────────────────────

@Immutable
data class AppColors(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,
    val outline: Color,
    val outlineVariant: Color,
    val background: Color,
    val onBackground: Color,
    val transparent: Color,
    val shadow: Color,
    val dialogContainer: Color,
    val unspecified: Color,
    val chartColors: List<Color>,
    val priorityLow: Color,
    val priorityMedium: Color,
    val priorityHigh: Color,
    val statTotalSpending: Color,
    val statBillCount: Color,
    val statDailyAvg: Color,
    val statTopCategory: Color
)

val LocalAppColors = staticCompositionLocalOf<AppColors> { error("No AppColors provided") }

private val FallbackLightScheme = lightColorScheme(
    primary = Color(0xFF1769AA), onPrimary = Color.White, primaryContainer = Color(0xFFD2E4FF), onPrimaryContainer = Color(0xFF001D36),
    secondary = Color(0xFF535F70), onSecondary = Color.White, secondaryContainer = Color(0xFFD7E3F7), onSecondaryContainer = Color(0xFF101C2B),
    tertiary = Color(0xFF6C5B00), onTertiary = Color.White, tertiaryContainer = Color(0xFFFFE978), onTertiaryContainer = Color(0xFF211B00),
    background = Color(0xFFF9F9FC), onBackground = Color(0xFF1A1C1E), surface = Color(0xFFF9F9FC), onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFDEE3EB), onSurfaceVariant = Color(0xFF42474E), outline = Color(0xFF72777F), outlineVariant = Color(0xFFC2C7CF),
    surfaceContainerLowest = Color(0xFFFFFFFF), surfaceContainerLow = Color(0xFFF3F4F7), surfaceContainer = Color(0xFFEEEFF3), surfaceContainerHigh = Color(0xFFE8EAF0), surfaceContainerHighest = Color(0xFFE2E5EA)
)

private val FallbackDarkScheme = darkColorScheme(
    primary = Color(0xFFA6CDFF), onPrimary = Color(0xFF003258), primaryContainer = Color(0xFF004A79), onPrimaryContainer = Color(0xFFD2E4FF),
    secondary = Color(0xFFBBC7DB), onSecondary = Color(0xFF253141), secondaryContainer = Color(0xFF3B4758), onSecondaryContainer = Color(0xFFD7E3F7),
    tertiary = Color(0xFFE8D971), onTertiary = Color(0xFF363000), tertiaryContainer = Color(0xFF504900), onTertiaryContainer = Color(0xFFFFE978),
    background = Color(0xFF1A1B1F), onBackground = Color(0xFFE3E2E6), surface = Color(0xFF1A1B1F), onSurface = Color(0xFFE3E2E6),
    surfaceVariant = Color(0xFF41474F), onSurfaceVariant = Color(0xFFC2C7CF), outline = Color(0xFF8C9199), outlineVariant = Color(0xFF41474F),
    surfaceContainerLowest = Color(0xFF121316), surfaceContainerLow = Color(0xFF1E1F23), surfaceContainer = Color(0xFF222328), surfaceContainerHigh = Color(0xFF2C2D32), surfaceContainerHighest = Color(0xFF37383E)
)

fun ColorScheme.toAppColors(darkTheme: Boolean) = AppColors(
    primary, onPrimary, primaryContainer, onPrimaryContainer, secondary, onSecondary, secondaryContainer, onSecondaryContainer,
    surface, onSurface, surfaceVariant, onSurfaceVariant, error, onError, errorContainer, onErrorContainer, tertiary, onTertiary,
    tertiaryContainer, onTertiaryContainer, outline, outlineVariant, background, onBackground, Color.Transparent,
    Color.Black.copy(alpha = if (darkTheme) 0.45f else 0.12f), surfaceContainerHigh, Color.Unspecified,
    chartColors = listOf(primary, tertiary, secondary, primaryContainer, onSurfaceVariant),
    priorityLow = if (darkTheme) Color(0xFF81C995) else Color(0xFF2E7D32),
    priorityMedium = if (darkTheme) Color(0xFFFFB74D) else Color(0xFFB06000),
    priorityHigh = if (darkTheme) Color(0xFFFF8A80) else Color(0xFFBA1A1A),
    statTotalSpending = error, statBillCount = tertiary, statDailyAvg = if (darkTheme) Color(0xFF81C995) else Color(0xFF2E7D32), statTopCategory = primary
)

// ─── Spacing ──────────────────────────────────────────────────────────────────

object SpacingTokens {
    val screenPadding = 16.dp
    val contentSpacing = 12.dp
    val smallSpacing = 8.dp
    val tinySpacing = 4.dp
    val iconSmall = 18.dp
    val iconMedium = 22.dp
    val iconLarge = 24.dp
    val actionRowHeight = 36.dp
    val chipHeight = 24.dp
    val cardElevation = 0.dp
    val shadowElevation = 8.dp
    val borderWidth = 1.dp
    val borderWidthThick = 2.dp
    val chartStrokeWidth = 2.dp
    val chartGridStrokeWidth = 1.dp
    val chartPointRadius = 4.dp
    val chartInnerPointRadius = 2.dp
    val chartBarWidth = 24.dp
    val chartBarSpacing = 12.dp
    val chartPaddingLeft = 60.dp
    val chartPaddingRight = 16.dp
    val chartPaddingTop = 16.dp
    val chartPaddingBottom = 30.dp
    val chartPointSpacing = 48.dp
    val chartMinWidth = 300.dp
    val chartHeight = 200.dp
    val donutChartSize = 200.dp
    val donutChartStrokeWidth = 40.dp
}

// ─── Modal / Typography ───────────────────────────────────────────────────────

object ModalTokens {
    val titleTextStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold, lineHeight = 24.sp)
    val bodyTextStyle = TextStyle(fontSize = 14.sp, lineHeight = 20.sp)
    val buttonTextStyle = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.5.sp)
    val labelTextStyle = TextStyle(fontSize = 12.sp, lineHeight = 16.sp)
    val menuItemPaddingVertical = 8.dp
    val menuDividerPaddingVertical = 4.dp
}

val Typography = Typography(
    bodyLarge = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.5.sp)
)

@Composable
fun DialogContent(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState()), verticalArrangement = verticalArrangement, horizontalAlignment = horizontalAlignment, content = content)
}

// ─── Shapes ───────────────────────────────────────────────────────────────────

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(10.dp),
    large = RoundedCornerShape(14.dp),
    extraLarge = RoundedCornerShape(20.dp)
)

// ─── Theme Composable ─────────────────────────────────────────────────────────

@Composable
fun KeyNoteTheme(
    darkModeManager: DarkModeManager = DarkModeManager(),
    content: @Composable () -> Unit
) {
    val darkTheme = darkModeManager.isDarkMode()
    val context = LocalContext.current
    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && darkTheme -> dynamicDarkColorScheme(context)
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> dynamicLightColorScheme(context)
        darkTheme -> FallbackDarkScheme
        else -> FallbackLightScheme
    }
    val colors = colorScheme.toAppColors(darkTheme)

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalDarkModeManager provides darkModeManager
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = AppShapes,
            content = content
        )
    }
}
