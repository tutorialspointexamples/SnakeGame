package com.w3stech.easysnake.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.w3stech.easysnake.R

val SkyBlue = Color(0xFF5BB8D4)
val SoftCream = Color(0xFFF7F1E6)
val LeafGreen = Color(0xFF1F9A6A)
val MintGreen = Color(0xFF4FCF9A)
val DeepTeal = Color(0xFF0E6B5C)
val FruitCoral = Color(0xFFFF6B5A)
val SunnyYellow = Color(0xFFFFC857)
val DeepLeaf = Color(0xFF0F7A52)
val BoardGrass = Color(0xFFEFF8F1)
val BoardLine = Color(0xFFD0E6D8)
val TextInk = Color(0xFF17362C)
val GlassWhite = Color(0xE6FFFFFF)
val SlowBlue = Color(0xFF4A9FD8)
val GhostViolet = Color(0xFF9B7EDC)
val WarmSand = Color(0xFFFFE8C8)

val NunitoFamily = FontFamily(
    Font(R.font.nunito_regular, FontWeight.Normal),
    Font(R.font.nunito_regular, FontWeight.Medium),
    Font(R.font.nunito_regular, FontWeight.SemiBold),
    Font(R.font.nunito_regular, FontWeight.Bold),
    Font(R.font.nunito_regular, FontWeight.ExtraBold)
)

private val ArcadeColorScheme = lightColorScheme(
    primary = LeafGreen,
    onPrimary = Color.White,
    secondary = SunnyYellow,
    onSecondary = TextInk,
    tertiary = FruitCoral,
    background = SoftCream,
    onBackground = TextInk,
    surface = GlassWhite,
    onSurface = TextInk,
    surfaceVariant = Color(0xFFFFF6E8),
    onSurfaceVariant = TextInk
)

val ArcadeTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 48.sp,
        lineHeight = 52.sp
    ),
    displayMedium = TextStyle(
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 36.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),
    titleLarge = TextStyle(
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,
        lineHeight = 24.sp
    ),
    labelLarge = TextStyle(
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        letterSpacing = 0.4.sp
    ),
    labelMedium = TextStyle(
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        letterSpacing = 0.8.sp
    )
)

val PlaygroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF7ED6EF),
        Color(0xFFB8F0D8),
        SoftCream,
        WarmSand
    )
)

val PrimaryButtonGradient = Brush.horizontalGradient(
    colors = listOf(DeepTeal, LeafGreen, MintGreen)
)

@Composable
fun EasySnakeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ArcadeColorScheme,
        typography = ArcadeTypography,
        content = content
    )
}
