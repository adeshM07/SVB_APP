package com.svb.fieldops.presentation.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(168.dp)
                        .background(SvbN5.copy(alpha = 0.55f), CircleShape),
                )
                Box(
                    modifier = Modifier
                        .size(97.dp)
                        .background(SvbPrimary4.copy(alpha = 0.65f), CircleShape),
                )
                Box(
                    modifier = Modifier
                        .size(72.dp)
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
            }
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
