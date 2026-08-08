package com.example.ui.theme

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.R

// 1. Dark Default
val DarkColorScheme = darkColorScheme(
    primary = CyanPrimary,
    onPrimary = DarkBackground,
    primaryContainer = CyanPrimaryContainer,
    onPrimaryContainer = OnCyanPrimaryContainer,
    secondary = BlueSecondary,
    onSecondary = DarkBackground,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
    error = CrimsonDanger,
    errorContainer = CrimsonDangerContainer
)

// 2. Light Default
val LightColorScheme = lightColorScheme(
    primary = LightCyanPrimary,
    onPrimary = LightSurface,
    primaryContainer = LightCyanPrimaryContainer,
    onPrimaryContainer = LightOnCyanPrimaryContainer,
    secondary = LightCyanPrimary,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorder,
    error = CrimsonDanger,
    errorContainer = CrimsonDangerContainer
)

// 3. Ocean Blue
val OceanColorScheme = darkColorScheme(
    primary = OceanPrimary,
    onPrimary = OceanBackground,
    primaryContainer = OceanPrimaryContainer,
    onPrimaryContainer = OceanOnPrimaryContainer,
    secondary = BlueSecondary,
    onSecondary = OceanBackground,
    background = OceanBackground,
    onBackground = DarkTextPrimary,
    surface = OceanSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = OceanSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = Color(0x3338BDF8),
    error = CrimsonDanger,
    errorContainer = CrimsonDangerContainer
)

// 4. Emerald
val EmeraldColorScheme = darkColorScheme(
    primary = EmeraldPrimary,
    onPrimary = EmeraldBackground,
    primaryContainer = EmeraldPrimaryContainer,
    onPrimaryContainer = EmeraldOnPrimaryContainer,
    secondary = Color(0xFF34D399),
    onSecondary = EmeraldBackground,
    background = EmeraldBackground,
    onBackground = Color(0xFFF0FDF4),
    surface = EmeraldSurface,
    onSurface = Color(0xFFF0FDF4),
    surfaceVariant = EmeraldSurfaceVariant,
    onSurfaceVariant = Color(0xFFA7F3D0),
    outline = Color(0x3310B981),
    error = CrimsonDanger,
    errorContainer = CrimsonDangerContainer
)

// 5. Royal Purple
val PurpleColorScheme = darkColorScheme(
    primary = PurplePrimary,
    onPrimary = PurpleBackground,
    primaryContainer = PurplePrimaryContainer,
    onPrimaryContainer = PurpleOnPrimaryContainer,
    secondary = Color(0xFFC084FC),
    onSecondary = PurpleBackground,
    background = PurpleBackground,
    onBackground = Color(0xFFFAF5FF),
    surface = PurpleSurface,
    onSurface = Color(0xFFFAF5FF),
    surfaceVariant = PurpleSurfaceVariant,
    onSurfaceVariant = Color(0xFFE9D5FF),
    outline = Color(0x33A855F7),
    error = CrimsonDanger,
    errorContainer = CrimsonDangerContainer
)

// 6. Sunset
val SunsetColorScheme = darkColorScheme(
    primary = SunsetPrimary,
    onPrimary = SunsetBackground,
    primaryContainer = SunsetPrimaryContainer,
    onPrimaryContainer = SunsetOnPrimaryContainer,
    secondary = Color(0xFFFB923C),
    onSecondary = SunsetBackground,
    background = SunsetBackground,
    onBackground = Color(0xFFFFF7ED),
    surface = SunsetSurface,
    onSurface = Color(0xFFFFF7ED),
    surfaceVariant = SunsetSurfaceVariant,
    onSurfaceVariant = Color(0xFFFDBA74),
    outline = Color(0x33F97316),
    error = CrimsonDanger,
    errorContainer = CrimsonDangerContainer
)

// 7. Midnight
val MidnightColorScheme = darkColorScheme(
    primary = MidnightPrimary,
    onPrimary = MidnightBackground,
    primaryContainer = MidnightPrimaryContainer,
    onPrimaryContainer = MidnightOnPrimaryContainer,
    secondary = Color(0xFFA5B4FC),
    onSecondary = MidnightBackground,
    background = MidnightBackground,
    onBackground = Color(0xFFF9FAFB),
    surface = MidnightSurface,
    onSurface = Color(0xFFF9FAFB),
    surfaceVariant = MidnightSurfaceVariant,
    onSurfaceVariant = Color(0xFF9CA3AF),
    outline = Color(0x33818CF8),
    error = CrimsonDanger,
    errorContainer = CrimsonDangerContainer
)

// 8. Slate (Light/Minimal Slate)
val SlateColorScheme = lightColorScheme(
    primary = SlatePrimary,
    onPrimary = Color.White,
    primaryContainer = SlatePrimaryContainer,
    onPrimaryContainer = SlateOnPrimaryContainer,
    secondary = Color(0xFF64748B),
    onSecondary = Color.White,
    background = SlateBackground,
    onBackground = Color(0xFF0F172A),
    surface = SlateSurface,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = SlateSurfaceVariant,
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1),
    error = CrimsonDanger,
    errorContainer = CrimsonDangerContainer
)

// 9. Aqua
val AquaColorScheme = darkColorScheme(
    primary = AquaPrimary,
    onPrimary = AquaBackground,
    primaryContainer = AquaPrimaryContainer,
    onPrimaryContainer = AquaOnPrimaryContainer,
    secondary = Color(0xFF22D3EE),
    onSecondary = AquaBackground,
    background = AquaBackground,
    onBackground = Color(0xFFECFEFF),
    surface = AquaSurface,
    onSurface = Color(0xFFECFEFF),
    surfaceVariant = AquaSurfaceVariant,
    onSurfaceVariant = Color(0xFF67E8F9),
    outline = Color(0x3306B6D4),
    error = CrimsonDanger,
    errorContainer = CrimsonDangerContainer
)

data class AppThemeItem(
    val key: String,
    val nameRes: Int,
    val defaultName: String,
    val descRes: Int,
    val defaultDesc: String,
    val previewPrimary: Color,
    val previewBackground: Color,
    val previewSurface: Color,
    val isDarkScheme: Boolean
) {
    fun getLocalizedName(context: Context): String {
        return try {
            context.getString(nameRes)
        } catch (e: Exception) {
            defaultName
        }
    }

    fun getDescription(context: Context): String {
        return try {
            context.getString(descRes)
        } catch (e: Exception) {
            defaultDesc
        }
    }
}

val ALL_THEME_ITEMS = listOf(
    AppThemeItem(
        key = "SYSTEM",
        nameRes = R.string.theme_system_default,
        defaultName = "System Default",
        descRes = R.string.theme_system_default_desc,
        defaultDesc = "Follows device appearance automatically",
        previewPrimary = CyanPrimary,
        previewBackground = DarkBackground,
        previewSurface = DarkSurface,
        isDarkScheme = true
    ),
    AppThemeItem(
        key = "LIGHT",
        nameRes = R.string.theme_light,
        defaultName = "Light",
        descRes = R.string.theme_light_desc,
        defaultDesc = "Clean, bright, professional appearance",
        previewPrimary = LightCyanPrimary,
        previewBackground = LightBackground,
        previewSurface = LightSurface,
        isDarkScheme = false
    ),
    AppThemeItem(
        key = "DARK",
        nameRes = R.string.theme_dark,
        defaultName = "Dark",
        descRes = R.string.theme_dark_desc,
        defaultDesc = "Modern dark interface with comfortable contrast",
        previewPrimary = CyanPrimary,
        previewBackground = DarkBackground,
        previewSurface = DarkSurface,
        isDarkScheme = true
    ),
    AppThemeItem(
        key = "OCEAN_BLUE",
        nameRes = R.string.theme_ocean_blue,
        defaultName = "Ocean Blue",
        descRes = R.string.theme_ocean_blue_desc,
        defaultDesc = "Professional blue/teal ISP management style",
        previewPrimary = OceanPrimary,
        previewBackground = OceanBackground,
        previewSurface = OceanSurface,
        isDarkScheme = true
    ),
    AppThemeItem(
        key = "EMERALD",
        nameRes = R.string.theme_emerald,
        defaultName = "Emerald",
        descRes = R.string.theme_emerald_desc,
        defaultDesc = "Elegant green-based professional theme",
        previewPrimary = EmeraldPrimary,
        previewBackground = EmeraldBackground,
        previewSurface = EmeraldSurface,
        isDarkScheme = true
    ),
    AppThemeItem(
        key = "ROYAL_PURPLE",
        nameRes = R.string.theme_royal_purple,
        defaultName = "Royal Purple",
        descRes = R.string.theme_royal_purple_desc,
        defaultDesc = "Premium purple-based theme",
        previewPrimary = PurplePrimary,
        previewBackground = PurpleBackground,
        previewSurface = PurpleSurface,
        isDarkScheme = true
    ),
    AppThemeItem(
        key = "SUNSET",
        nameRes = R.string.theme_sunset,
        defaultName = "Sunset",
        descRes = R.string.theme_sunset_desc,
        defaultDesc = "Warm orange/red accent style",
        previewPrimary = SunsetPrimary,
        previewBackground = SunsetBackground,
        previewSurface = SunsetSurface,
        isDarkScheme = true
    ),
    AppThemeItem(
        key = "MIDNIGHT",
        nameRes = R.string.theme_midnight,
        defaultName = "Midnight",
        descRes = R.string.theme_midnight_desc,
        defaultDesc = "Deep dark premium appearance with subtle blue accents",
        previewPrimary = MidnightPrimary,
        previewBackground = MidnightBackground,
        previewSurface = MidnightSurface,
        isDarkScheme = true
    ),
    AppThemeItem(
        key = "SLATE",
        nameRes = R.string.theme_slate,
        defaultName = "Slate",
        descRes = R.string.theme_slate_desc,
        defaultDesc = "Minimal gray/slate professional appearance",
        previewPrimary = SlatePrimary,
        previewBackground = SlateBackground,
        previewSurface = SlateSurface,
        isDarkScheme = false
    ),
    AppThemeItem(
        key = "AQUA",
        nameRes = R.string.theme_aqua,
        defaultName = "Aqua",
        descRes = R.string.theme_aqua_desc,
        defaultDesc = "Fresh cyan/blue appearance",
        previewPrimary = AquaPrimary,
        previewBackground = AquaBackground,
        previewSurface = AquaSurface,
        isDarkScheme = true
    )
)

fun getThemeItem(key: String): AppThemeItem {
    return ALL_THEME_ITEMS.find { it.key.equals(key, ignoreCase = true) }
        ?: ALL_THEME_ITEMS.first()
}

fun getThemeColorScheme(themeMode: String, isSystemDark: Boolean): Pair<ColorScheme, Boolean> {
    return when (themeMode.uppercase()) {
        "LIGHT" -> Pair(LightColorScheme, false)
        "DARK" -> Pair(DarkColorScheme, true)
        "OCEAN_BLUE" -> Pair(OceanColorScheme, true)
        "EMERALD" -> Pair(EmeraldColorScheme, true)
        "ROYAL_PURPLE" -> Pair(PurpleColorScheme, true)
        "SUNSET" -> Pair(SunsetColorScheme, true)
        "MIDNIGHT" -> Pair(MidnightColorScheme, true)
        "SLATE" -> Pair(SlateColorScheme, false)
        "AQUA" -> Pair(AquaColorScheme, true)
        else -> Pair(if (isSystemDark) DarkColorScheme else LightColorScheme, isSystemDark)
    }
}

@Composable
fun IspControlTheme(
    themeMode: String = "SYSTEM",
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val (colorScheme, isDarkStatusBar) = getThemeColorScheme(themeMode, isSystemDark)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkStatusBar
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !isDarkStatusBar
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
