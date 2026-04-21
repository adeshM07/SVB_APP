package com.svb.fieldops.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val SvbLightColorScheme = lightColorScheme(
    primary = SvbPrimary2,
    onPrimary = SvbBlack,
    primaryContainer = SvbPrimary5,
    onPrimaryContainer = SvbN1,
    secondary = SvbPrimary1,
    onSecondary = SvbBlack,
    tertiary = SvbPrimary3,
    onTertiary = SvbBlack,
    background = SvbWhite,
    onBackground = SvbBlack,
    surface = SvbWhite,
    onSurface = SvbBlack,
    surfaceVariant = SvbN7,
    onSurfaceVariant = SvbN2,
    outline = SvbN5,
    error = SvbDanger,
    onError = SvbWhite,
)

@Composable
fun SvbFieldOpsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SvbLightColorScheme,
        typography = SvbTypography,
        shapes = SvbShapes,
        content = content,
    )
}
