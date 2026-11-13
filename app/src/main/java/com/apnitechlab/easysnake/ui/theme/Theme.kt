package com.apnitechlab.easysnake.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme

val SkyBlue = Color(0xFF7EC8E3)
val SoftCream = Color(0xFFFFF7E8)
val LeafGreen = Color(0xFF3FAF6C)
val MintGreen = Color(0xFF6FCF97)
val FruitCoral = Color(0xFFE85D4C)
val SunnyYellow = Color(0xFFF4C95F)
val DeepLeaf = Color(0xFF2E8B57)
val BoardGrass = Color(0xFFE8F6E8)
val BoardLine = Color(0xFFD5E8D5)
val TextInk = Color(0xFF2F4A3C)

private val KidsColorScheme = lightColorScheme(
    primary = LeafGreen,
    onPrimary = Color.White,
    secondary = SunnyYellow,
    onSecondary = TextInk,
    tertiary = FruitCoral,
    background = SoftCream,
    onBackground = TextInk,
    surface = Color.White,
    onSurface = TextInk,
    surfaceVariant = Color(0xFFFFF1D6),
    onSurfaceVariant = TextInk
)

val KidsTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 52.sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
)

val PlaygroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFB8E4F5),
        SoftCream,
        Color(0xFFFFE8B8)
    )
)

@Composable
fun EasySnakeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = KidsColorScheme,
        typography = KidsTypography,
        content = content
    )
}
