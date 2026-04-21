package com.svb.fieldops.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.FlipCameraAndroid
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.svb.fieldops.ui.theme.SvbN1
import com.svb.fieldops.ui.theme.SvbN3
import androidx.compose.ui.graphics.Color
import com.svb.fieldops.ui.theme.SvbSelfieCard
import com.svb.fieldops.ui.theme.SvbWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelfieCaptureScreen(
    onBack: () -> Unit,
    onSimulateCapture: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = SvbN1,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = SvbN1,
                    titleContentColor = SvbWhite,
                    navigationIconContentColor = SvbWhite,
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Surface(
                            color = SvbSelfieCard,
                            shape = CircleShape,
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Back",
                                tint = SvbWhite,
                                modifier = Modifier.padding(10.dp),
                            )
                        }
                    }
                },
                title = {
                    Text(
                        text = "Shift Start Selfie",
                        style = MaterialTheme.typography.titleLarge,
                        color = SvbWhite,
                    )
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.72f)
                    .background(SvbSelfieCard, RoundedCornerShape(28.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val cx = w / 2f
                    val cy = h * 0.42f
                    val rw = w * 0.34f
                    val rh = h * 0.22f
                    drawOval(
                        color = Color.White.copy(alpha = 0.55f),
                        topLeft = Offset(cx - rw, cy - rh),
                        size = Size(rw * 2, rh * 2),
                        style = Stroke(
                            width = 3f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 12f), 0f),
                        ),
                    )
                }
                Text(
                    text = "Position your face here",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SvbN3,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 20.dp, start = 16.dp, end = 16.dp),
                )
            }
            Spacer(Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp),
            ) {
                IconButton(
                    onClick = { /* Phase 3: switch lens */ },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 12.dp),
                ) {
                    Surface(
                        color = SvbSelfieCard,
                        shape = CircleShape,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FlipCameraAndroid,
                            contentDescription = "Switch camera",
                            tint = SvbWhite,
                            modifier = Modifier.padding(7.dp),
                        )
                    }
                }
                val interaction = remember { MutableInteractionSource() }
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(84.dp)
                        .border(5.dp, SvbWhite, CircleShape)
                        .clickable(
                            interactionSource = interaction,
                            indication = null,
                            onClick = onSimulateCapture,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(SvbWhite, CircleShape),
                    )
                }
            }
        }
    }
}
