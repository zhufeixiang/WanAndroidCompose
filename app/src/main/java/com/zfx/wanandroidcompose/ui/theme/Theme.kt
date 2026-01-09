package com.zfx.wanandroidcompose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat



enum class AppTheme(val themeName : String,val color : Color) {
    DARK("深色主题",ColorDark), //深色主题
    LIGHT("浅色主题",ColorLight),//浅色主题
    AUTO("跟随系统",Pink40),//跟随系统
    BLUE("蓝色主题",ColorBlue),//蓝色主题
    RED("红色主题",ColorRed),//红色主题
    YELLOW("黄色主题",ColorYellow),//黄色主题
    GREEN("绿色主题",ColorGreen),//绿色主题
    ORANGE("橘色主题",ColorOrange); //橘色主题
    
    companion object {
        /**
         * 获取所有主题
         */
        fun allThemes(): List<AppTheme> {
            return enumValues<AppTheme>().toList()
        }
    }
}

private val DarkColorScheme = darkColorScheme(
    primary = ColorDark,
    secondary = ColorDark80,
    tertiary = ColorFF8A8A8A,
    background = ColorDark80,
    onPrimary = Purple80,
    onBackground = Purple40,
    onTertiary = Pink80,
    surface = Pink40,
    error = ColorGreen

)

private val LightColorScheme = lightColorScheme(
    primary = ColorLight,
    secondary = ColorLight80,
    tertiary = ColorWhite,
    background = ColorFFF7F7F7,
    onPrimary = ColorWhite,
    onBackground = Color808A8A8A,
    onTertiary = ColorFF8A8A8A,
    surface = ColorBlack,
    error = ColorRed

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

private val BlueColorScheme = darkColorScheme(
    primary = ColorLight,
    secondary = ColorLight80,
    tertiary = ColorWhite,
    background = ColorFFF7F7F7,
    onPrimary = ColorWhite,
    onBackground = Color808A8A8A,
    onTertiary = ColorFF8A8A8A,
    surface = ColorBlack,
    error = ColorRed
)


private val RedColorScheme = darkColorScheme(
    primary = ColorRed,
    secondary = ColorRed80,
    tertiary = ColorWhite,
    background = ColorFFF7F7F7,
    onPrimary = ColorWhite,
    onBackground = Color808A8A8A,
    onTertiary = ColorFF8A8A8A,
    surface = ColorBlack,
    error = ColorGreen
)


private val YellowColorScheme = darkColorScheme(
    primary = ColorYellow,
    secondary = ColorYellow80,
    tertiary = ColorWhite,
    background = ColorFFF7F7F7,
    onPrimary = ColorWhite,
    onBackground = Color808A8A8A,
    onTertiary = ColorFF8A8A8A,
    surface = ColorBlack,
    error = ColorRed
)


private val GreenColorScheme = darkColorScheme(
    primary = ColorGreen,
    secondary = ColorGreen80,
    tertiary = ColorWhite,
    background = ColorFFF7F7F7,
    onPrimary = ColorWhite,
    onBackground = Color808A8A8A,
    onTertiary = ColorFF8A8A8A,
    surface = ColorBlack,
    error = ColorRed
)

private val OrangeColorScheme = darkColorScheme(
    primary = ColorOrange,
    secondary = ColorOrange80,
    tertiary = ColorWhite,
    background = ColorFFF7F7F7,
    onPrimary = ColorWhite,
    onBackground = Color808A8A8A,
    onTertiary = ColorFF8A8A8A,
    surface = ColorBlack,
    error = ColorRed
)




@Composable
fun WanAndroidComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    theme: AppTheme = AppTheme.AUTO,
    content: @Composable () -> Unit
) {
    val colorScheme = remember(theme,darkTheme) {
        when(theme){
            AppTheme.DARK -> {
                DarkColorScheme
            }
            AppTheme.LIGHT -> {
                LightColorScheme
            }
            AppTheme.BLUE -> {
                BlueColorScheme
            }

            AppTheme.RED -> {
                RedColorScheme
            }
            AppTheme.GREEN -> {
                GreenColorScheme
            }

            AppTheme.ORANGE -> {
                OrangeColorScheme
            }

            AppTheme.YELLOW -> {
                YellowColorScheme
            }
            else -> {
                if (darkTheme) DarkColorScheme else LightColorScheme
            }
        }
    }


//        when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }

    // 设置状态栏颜色和图标颜色，跟随主题变化
    // 注意：setStatusBarColor 已被标记为 @Deprecated，但在 Android 14 及以下仍可使用
    // Android 15+ 状态栏颜色将是透明的且无法更改
    val view = LocalView.current
    LaunchedEffect(colorScheme.primary, darkTheme) {
        val window = (view.context as? android.app.Activity)?.window
        window?.let {
            // 设置状态栏背景色（跟随主题变化）
            // 使用 @Suppress("DEPRECATION") 抑制废弃警告（在 Android 14 及以下仍可用）
            @Suppress("DEPRECATION")
            it.statusBarColor = colorScheme.primary.toArgb()
            
            // 设置状态栏图标颜色（这个 API 不会废弃）
            // 根据主题决定状态栏图标颜色（深色背景用浅色图标，浅色背景用深色图标）
            WindowCompat.getInsetsController(it, view).apply {
                isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}