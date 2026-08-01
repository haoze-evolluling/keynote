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

object AppColorPalette {
    val Light = AppColors(
        primary = Color(0xFF0593FF), onPrimary = Color.White, primaryContainer = Color(0xFFD1E4FF), onPrimaryContainer = Color(0xFF001D36),
        secondary = Color(0xFF535F70), onSecondary = Color.White, secondaryContainer = Color(0xFFD7E3F7), onSecondaryContainer = Color(0xFF101C2B),
        surface = Color(0xFFFDFCFF), onSurface = Color(0xFF1A1C1E), surfaceVariant = Color(0xFFDAE4EF), onSurfaceVariant = Color(0xFF49454F),
        error = Color(0xFFBA1A1A), onError = Color.White, errorContainer = Color(0xFFFFDAD6), onErrorContainer = Color(0xFF410002),
        tertiary = Color(0xFF7C5800), onTertiary = Color.White, tertiaryContainer = Color(0xFFFFDEA1), onTertiaryContainer = Color(0xFF261A00),
        outline = Color(0xFF79747E), outlineVariant = Color(0xFFCAC4D0), background = Color(0xFFFDFCFF), onBackground = Color(0xFF1A1C1E),
        transparent = Color.Transparent, shadow = Color.Black.copy(alpha = 0.1f), dialogContainer = Color(0xFFFDFCFF), unspecified = Color.Unspecified,
        chartColors = listOf(Color(0xFF0593FF), Color(0xFF5BC0FF), Color(0xFF0066CC), Color(0xFFD1E4FF), Color(0xFF49454F)),
        priorityLow = Color(0xFF4CAF50), priorityMedium = Color(0xFFFF9800), priorityHigh = Color(0xFFF44336),
        statTotalSpending = Color(0xFFFF5252), statBillCount = Color(0xFFFFAB40), statDailyAvg = Color(0xFF69F0AE), statTopCategory = Color(0xFF40C4FF)
    )

    val Dark = AppColors(
        primary = Color(0xFF75C7FF), onPrimary = Color(0xFF00344F), primaryContainer = Color(0xFF004B72), onPrimaryContainer = Color(0xFFD1E4FF),
        secondary = Color(0xFFBBC7DB), onSecondary = Color(0xFF253141), secondaryContainer = Color(0xFF3B4758), onSecondaryContainer = Color(0xFFD7E3F7),
        surface = Color(0xFF101418), onSurface = Color(0xFFE2E2E8), surfaceVariant = Color(0xFF202A33), onSurfaceVariant = Color(0xFFC2C7CF),
        error = Color(0xFFFFB4AB), onError = Color(0xFF690005), errorContainer = Color(0xFF93000A), onErrorContainer = Color(0xFFFFDAD6),
        tertiary = Color(0xFFE9C16D), onTertiary = Color(0xFF3F2E00), tertiaryContainer = Color(0xFF5B4300), onTertiaryContainer = Color(0xFFFFDEA1),
        outline = Color(0xFF8E949D), outlineVariant = Color(0xFF4E5A67), background = Color(0xFF0D1117), onBackground = Color(0xFFE2E2E8),
        transparent = Color.Transparent, shadow = Color.Black.copy(alpha = 0.45f), dialogContainer = Color(0xFF182028), unspecified = Color.Unspecified,
        chartColors = listOf(Color(0xFF75C7FF), Color(0xFF89D5FF), Color(0xFF4EA3E5), Color(0xFFB7DCFF), Color(0xFFC2C7CF)),
        priorityLow = Color(0xFF81C995), priorityMedium = Color(0xFFFFB74D), priorityHigh = Color(0xFFFF8A80),
        statTotalSpending = Color(0xFFFF8A80), statBillCount = Color(0xFFFFB74D), statDailyAvg = Color(0xFF81C995), statTopCategory = Color(0xFF75C7FF)
    )
}

fun AppColors.toMaterialColorScheme() = if (this == AppColorPalette.Light) {
    androidx.compose.material3.lightColorScheme(
        primary = primary, onPrimary = onPrimary, primaryContainer = primaryContainer, onPrimaryContainer = onPrimaryContainer,
        secondary = secondary, onSecondary = onSecondary, secondaryContainer = secondaryContainer, onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary, onTertiary = onTertiary, tertiaryContainer = tertiaryContainer, onTertiaryContainer = onTertiaryContainer,
        error = error, onError = onError, errorContainer = errorContainer, onErrorContainer = onErrorContainer,
        background = background, onBackground = onBackground, surface = surface, onSurface = onSurface,
        surfaceVariant = surfaceVariant, onSurfaceVariant = onSurfaceVariant, outline = outline, outlineVariant = outlineVariant,
        inverseSurface = onSurface, inverseOnSurface = surface, inversePrimary = primaryContainer,
        surfaceDim = Color(0xFFE0E6EE), surfaceBright = surface, surfaceContainerLowest = surface,
        surfaceContainerLow = Color(0xFFF7F9FC), surfaceContainer = Color(0xFFF1F5F9),
        surfaceContainerHigh = Color(0xFFEAF0F7), surfaceContainerHighest = Color(0xFFE3EAF2), scrim = Color.Black
    )
} else {
    androidx.compose.material3.darkColorScheme(
        primary = primary, onPrimary = onPrimary, primaryContainer = primaryContainer, onPrimaryContainer = onPrimaryContainer,
        secondary = secondary, onSecondary = onSecondary, secondaryContainer = secondaryContainer, onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary, onTertiary = onTertiary, tertiaryContainer = tertiaryContainer, onTertiaryContainer = onTertiaryContainer,
        error = error, onError = onError, errorContainer = errorContainer, onErrorContainer = onErrorContainer,
        background = background, onBackground = onBackground, surface = surface, onSurface = onSurface,
        surfaceVariant = surfaceVariant, onSurfaceVariant = onSurfaceVariant, outline = outline, outlineVariant = outlineVariant,
        inverseSurface = onSurface, inverseOnSurface = surface, inversePrimary = primaryContainer,
        surfaceDim = Color(0xFF0B0F12), surfaceBright = Color(0xFF303841), surfaceContainerLowest = Color(0xFF0B0F12),
        surfaceContainerLow = Color(0xFF151B21), surfaceContainer = Color(0xFF192129), surfaceContainerHigh = Color(0xFF202A33),
        surfaceContainerHighest = Color(0xFF2A343E), scrim = Color.Black
    )
}

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
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

// ─── Theme Composable ─────────────────────────────────────────────────────────

@Composable
fun KeyNoteTheme(
    darkModeManager: DarkModeManager = DarkModeManager(),
    content: @Composable () -> Unit
) {
    val darkTheme = darkModeManager.isDarkMode()
    val colors = if (darkTheme) AppColorPalette.Dark else AppColorPalette.Light

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalDarkModeManager provides darkModeManager
    ) {
        MaterialTheme(
            colorScheme = colors.toMaterialColorScheme(),
            typography = Typography,
            shapes = AppShapes,
            content = content
        )
    }
}
