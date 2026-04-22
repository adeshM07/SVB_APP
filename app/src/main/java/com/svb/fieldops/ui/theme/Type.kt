package com.svb.fieldops.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.svb.fieldops.R

/**
 * Inter variable font ([R.font.inter_variable], SIL OFL — rsms/inter) for the whole app.
 * Starts from Material 3 defaults and applies Inter to every slot, then Field Ops sizes/weights.
 */
private val SvbFontFamily = FontFamily(
    Font(R.font.inter_variable),
)

private fun TextStyle.withInter(): TextStyle = copy(fontFamily = SvbFontFamily)

private val defaultTypography = Typography()

val SvbTypography = Typography(
    displayLarge = defaultTypography.displayLarge.withInter().copy(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 36.sp,
        lineHeight = 42.sp,
    ),
    displayMedium = defaultTypography.displayMedium.withInter(),
    displaySmall = defaultTypography.displaySmall.withInter(),
    headlineLarge = defaultTypography.headlineLarge.withInter().copy(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
    ),
    headlineMedium = defaultTypography.headlineMedium.withInter().copy(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
    ),
    headlineSmall = defaultTypography.headlineSmall.withInter(),
    titleLarge = defaultTypography.titleLarge.withInter().copy(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
    ),
    titleMedium = defaultTypography.titleMedium.withInter().copy(
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
    ),
    titleSmall = defaultTypography.titleSmall.withInter(),
    bodyLarge = defaultTypography.bodyLarge.withInter(),
    bodyMedium = defaultTypography.bodyMedium.withInter().copy(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp,
    ),
    bodySmall = defaultTypography.bodySmall.withInter().copy(
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 14.sp,
    ),
    labelLarge = defaultTypography.labelLarge.withInter().copy(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 14.sp,
        lineHeight = 18.sp,
    ),
    labelMedium = defaultTypography.labelMedium.withInter(),
    labelSmall = defaultTypography.labelSmall.withInter().copy(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
)
