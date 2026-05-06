package com.svb.fieldops.presentation.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.outlined.GpsFixed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.svb.fieldops.presentation.components.SvbPrimaryButton
import com.svb.fieldops.presentation.viewmodel.ClockInUiState
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN3
import com.svb.fieldops.ui.theme.SvbN5
import com.svb.fieldops.ui.theme.SvbN7
import com.svb.fieldops.ui.theme.SvbPrimary2
import com.svb.fieldops.ui.theme.SvbPrimary4
import com.svb.fieldops.ui.theme.SvbSuccess

private val GeofenceHeroSize = 168.dp
/** Inset gray disk from hero edge so the dashed ring sits outside it with a clear gap. */
private val GeofenceHeroGrayOuterPadding = 12.dp
private val GeofenceRingInsetFromEdge = 1.5.dp
private val GeofenceRingStrokeWidth = 1.6.dp
private val GeofenceRingDashColor = Color(0xFFF5C75D)
private const val GeofenceRingRotationMs = 14_000
private const val GeofencePulseMs = 2_000

/** Concentric GPS hero with slow rotating dashed ring (aligned with login screen treatment). */
@Composable
private fun GeofenceVerifiedHeroGraphic() {
    val ringTransition = rememberInfiniteTransition(label = "geofenceHeroRing")
    val ringRotation by ringTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(GeofenceRingRotationMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "geofenceHeroRingRotation",
    )
    // CSS reference parity:
    // 0%/100% => 12px + 24px soft rings, 50% => 18px + 36px rings.
    val pulseProgress by ringTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(GeofencePulseMs, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "geofenceHeroPulse",
    )
    val pulseRing1Scale = 1.0f + (1.12f - 1.0f) * pulseProgress
    val pulseRing2Scale = 1.0f + (1.20f - 1.0f) * pulseProgress
    val pulseRing1Alpha = 0.15f + (0.10f - 0.15f) * pulseProgress
    val pulseRing2Alpha = 0.08f + (0.05f - 0.08f) * pulseProgress
    val innerYellowScale = 1.0f + (1.06f - 1.0f) * pulseProgress
    val innerYellowAlpha = 1.0f + (0.92f - 1.0f) * pulseProgress
    Box(
        modifier = Modifier.size(GeofenceHeroSize),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(GeofenceHeroGrayOuterPadding)
                .background(SvbN5.copy(alpha = 0.55f), CircleShape),
        )
        Box(
            modifier = Modifier
                .size(120.dp)
                .graphicsLayer(
                    scaleX = pulseRing2Scale,
                    scaleY = pulseRing2Scale,
                )
                .background(SvbPrimary2.copy(alpha = pulseRing2Alpha), CircleShape),
        )
        Box(
            modifier = Modifier
                .size(96.dp)
                .graphicsLayer(
                    scaleX = pulseRing1Scale,
                    scaleY = pulseRing1Scale,
                )
                .background(SvbPrimary2.copy(alpha = pulseRing1Alpha), CircleShape),
        )
        Box(
            modifier = Modifier
                .size(97.dp)
                .background(SvbPrimary4.copy(alpha = 0.65f), CircleShape),
        )
        Box(
            modifier = Modifier
                .size(72.dp)
                .graphicsLayer(
                    scaleX = innerYellowScale,
                    scaleY = innerYellowScale,
                    alpha = innerYellowAlpha,
                )
                .background(SvbPrimary2, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.GpsFixed,
                contentDescription = null,
                tint = SvbBlack,
                modifier = Modifier.size(36.dp),
            )
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .rotate(ringRotation),
        ) {
            val strokeWidth = GeofenceRingStrokeWidth.toPx()
            val inset = GeofenceRingInsetFromEdge.toPx() + strokeWidth * 0.5f
            drawArc(
                color = GeofenceRingDashColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = Size(
                    width = size.width - 2f * inset,
                    height = size.height - 2f * inset,
                ),
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(
                        intervals = floatArrayOf(4.dp.toPx(), 6.dp.toPx()),
                    ),
                ),
            )
        }
    }
}

@Composable
fun GeofenceVerifiedScreen(
    state: ClockInUiState,
    onContinue: () -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize(), color = SvbLoginBackground) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GeofenceVerifiedHeroGraphic()
            Spacer(Modifier.height(28.dp))
            Text(
                text = "Location Verified!",
                style = MaterialTheme.typography.titleLarge,
                color = SvbSuccess,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = "You are within the SVB 68-Acre Project site boundary.",
                style = MaterialTheme.typography.bodyMedium,
                color = SvbN3,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SvbN7),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Column(Modifier.padding(horizontal = 18.dp, vertical = 16.dp)) {
                    GeofenceDetailRow(label = "Site", value = state.siteName)
                    Spacer(Modifier.height(14.dp))
                    GeofenceDetailRow(label = "Your Location", value = state.previewLocation)
                    Spacer(Modifier.height(14.dp))
                    GeofenceDetailRow(label = "Distance", value = state.distanceText)
                    Spacer(Modifier.height(14.dp))
                    GeofenceDetailRow(
                        label = "Status",
                        value = state.statusLabel,
                        valueColor = SvbSuccess,
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            SvbPrimaryButton(
                text = "Continue to Clock In",
                onClick = onContinue,
                leadingIcon = Icons.AutoMirrored.Rounded.ArrowForward,
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun GeofenceDetailRow(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color = SvbBlack,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = SvbN3,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = valueColor,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End,
        )
    }
}
