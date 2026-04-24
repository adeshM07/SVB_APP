package com.svb.fieldops.presentation.screens.driver

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.svb.fieldops.presentation.screens.home.HomeCardShape
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbN2
import com.svb.fieldops.ui.theme.SvbN3
import com.svb.fieldops.ui.theme.SvbN5
import com.svb.fieldops.ui.theme.SvbN7
import com.svb.fieldops.ui.theme.SvbPrimary2
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite

private val ScannerBg = Color(0xFF3A3A3A)
private val ScannerCornerBracketLength = 36.dp
private val ScannerFramePaddingH = 20.dp
private val ScannerFramePaddingV = 22.dp
private val ScannerLaserInsetFromFrame = 14.dp
private val ScannerLaserThickness = 3.dp

@Composable
fun DriverJobQrScanViewfinder(
    modifier: Modifier = Modifier,
    hintBelowFrame: String = "Align QR code within frame",
) {
    val infinite = rememberInfiniteTransition(label = "qrLaser")
    val sweep by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "laserSweep",
    )
    val density = LocalDensity.current
    Column(
        modifier = modifier
            .height(312.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(ScannerBg)
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(
                    horizontal = ScannerFramePaddingH,
                    vertical = ScannerFramePaddingV,
                )
                .drawBehind {
                    val lineW = 3.dp.toPx()
                    val len = ScannerCornerBracketLength.toPx()
                    val yellow = SvbPrimary2
                    drawLine(yellow, Offset(0f, 0f), Offset(len, 0f), lineW)
                    drawLine(yellow, Offset(0f, 0f), Offset(0f, len), lineW)
                    drawLine(yellow, Offset(size.width, 0f), Offset(size.width - len, 0f), lineW)
                    drawLine(yellow, Offset(size.width, 0f), Offset(size.width, len), lineW)
                    drawLine(yellow, Offset(0f, size.height), Offset(len, size.height), lineW)
                    drawLine(yellow, Offset(0f, size.height), Offset(0f, size.height - len), lineW)
                    drawLine(yellow, Offset(size.width, size.height), Offset(size.width - len, size.height), lineW)
                    drawLine(yellow, Offset(size.width, size.height), Offset(size.width, size.height - len), lineW)
                },
        ) {
            val bracketLenPx = with(density) { ScannerCornerBracketLength.toPx() }
            val gapPx = with(density) { 10.dp.toPx() }
            val topStop = bracketLenPx + gapPx
            val bottomStop = bracketLenPx + gapPx
            val maxHpx = with(density) { maxHeight.toPx() }
            val laserHPx = with(density) { ScannerLaserThickness.toPx() }
            val travelPx = (maxHpx - laserHPx - topStop - bottomStop).coerceAtLeast(8f)
            val yPx = topStop + sweep * travelPx
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ScannerLaserInsetFromFrame)
                    .height(ScannerLaserThickness)
                    .align(Alignment.TopStart)
                    .offset { IntOffset(0, yPx.toInt()) }
                    .background(SvbPrimary2.copy(alpha = 0.95f)),
            )
        }
        Text(
            text = hintBelowFrame,
            style = MaterialTheme.typography.bodySmall,
            color = SvbN3,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 2.dp),
        )
    }
}

@Composable
fun DriverJobThreeStepStepper(currentStep: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        DriverJobStepDot(number = 1, done = currentStep > 1, active = currentStep == 1)
        Box(
            Modifier
                .weight(1f)
                .height(3.dp)
                .padding(horizontal = 6.dp)
                .background(if (currentStep > 1) SvbSuccess else SvbN5),
        )
        DriverJobStepDot(number = 2, done = currentStep > 2, active = currentStep == 2)
        Box(
            Modifier
                .weight(1f)
                .height(3.dp)
                .padding(horizontal = 6.dp)
                .background(if (currentStep > 2) SvbSuccess else SvbN5),
        )
        DriverJobStepDot(number = 3, done = false, active = currentStep == 3)
    }
}

@Composable
private fun DriverJobStepDot(number: Int, done: Boolean, active: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    when {
                        done -> SvbSuccess
                        active -> SvbPrimary2
                        else -> SvbN7
                    },
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (done) {
                Icon(
                    Icons.Outlined.Check,
                    contentDescription = null,
                    tint = SvbWhite,
                    modifier = Modifier.size(18.dp),
                )
            } else {
                Text(
                    text = number.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (active) SvbBlack else SvbN2,
                )
            }
        }
    }
}

@Composable
fun DriverJobOutlinedCheckBadge(
    modifier: Modifier = Modifier,
    tint: Color = SvbSuccess,
) {
    Canvas(modifier) {
        val cx = size.width * 0.5f
        val cy = size.height * 0.5f
        val minR = size.minDimension * 0.5f
        val stroke = (minR * 0.12f).coerceIn(1.35f.dp.toPx(), 2f.dp.toPx())
        val ringRadius = minR - stroke * 0.55f
        drawCircle(
            color = tint,
            radius = ringRadius,
            center = Offset(cx, cy),
            style = Stroke(width = stroke),
        )
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.30f, h * 0.50f)
            lineTo(w * 0.44f, h * 0.64f)
            lineTo(w * 0.72f, h * 0.36f)
        }
        drawPath(
            path = path,
            color = tint,
            style = Stroke(
                width = stroke,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
            ),
        )
    }
}

fun driverJobFormatKms(digits: String): String {
    val n = digits.filter { it.isDigit() }
    if (n.isEmpty()) return "0"
    return n.reversed().chunked(3).joinToString(",").reversed()
}

@Composable
fun DriverJobOdometerPhotoPlaceholder(
    captured: Boolean,
    onClick: () -> Unit,
    proofSubtitle: String = "This serves as proof of reading",
) {
    val dash = 10.dp
    val gap = 8.dp
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(HomeCardShape)
            .clickable(onClick = onClick)
            .drawBehind {
                val w = 2.dp.toPx()
                val dashPx = dash.toPx()
                val gapPx = gap.toPx()
                val inset = w * 0.5f
                drawRoundRect(
                    color = SvbN5,
                    topLeft = Offset(inset, inset),
                    size = Size(size.width - inset * 2, size.height - inset * 2),
                    style = Stroke(
                        width = w,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashPx, gapPx), 0f),
                    ),
                    cornerRadius = CornerRadius(20.dp.toPx(), 20.dp.toPx()),
                )
            },
        color = SvbN7,
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                Icons.Outlined.CameraAlt,
                contentDescription = null,
                tint = SvbN3,
                modifier = Modifier.size(48.dp),
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = if (captured) "Photo captured — tap to retake" else "Tap to capture odometer photo",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SvbBlack,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = proofSubtitle,
                style = MaterialTheme.typography.bodySmall,
                color = SvbN2,
            )
        }
    }
}
