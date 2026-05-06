package com.svb.fieldops.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.svb.fieldops.presentation.components.SvbOutlinedButton
import com.svb.fieldops.presentation.viewmodel.ClockInUiState
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbClockInHeader
import com.svb.fieldops.ui.theme.SvbClockInSheet
import com.svb.fieldops.ui.theme.SvbN3
import com.svb.fieldops.ui.theme.SvbN5
import com.svb.fieldops.ui.theme.SvbN7
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite

@Composable
fun ClockInPreviewScreen(
    state: ClockInUiState,
    onRetake: () -> Unit,
    onClockIn: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.38f)
                .background(SvbClockInHeader),
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 28.dp)
                    .size(128.dp)
                    .clip(CircleShape)
                    .background(SvbN7),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = null,
                    tint = SvbN3,
                    modifier = Modifier.size(56.dp),
                )
            }
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 20.dp, bottom = 20.dp),
                shape = RoundedCornerShape(9999.dp),
                color = SvbSuccess,
            ) {
                Row(
                    Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .border(width = 1.5.dp, color = SvbWhite, shape = CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = SvbWhite,
                            modifier = Modifier.size(15.dp),
                        )
                    }
                    Text(
                        text = "Photo Captured",
                        style = MaterialTheme.typography.labelSmall,
                        color = SvbWhite,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.62f)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(SvbClockInSheet)
                .padding(horizontal = 20.dp, vertical = 20.dp),
        ) {
            PreviewInfoRow(
                icon = Icons.Rounded.CalendarToday,
                label = "Date",
                value = state.previewDate,
            )
            HorizontalDivider(color = SvbN5.copy(alpha = 0.6f), thickness = 1.dp)
            PreviewInfoRow(
                icon = Icons.Rounded.Schedule,
                label = "Time",
                value = state.previewTime,
            )
            HorizontalDivider(color = SvbN5.copy(alpha = 0.6f), thickness = 1.dp)
            PreviewInfoRow(
                icon = Icons.Outlined.LocationOn,
                label = "Location",
                value = state.previewLocation,
            )
            HorizontalDivider(color = SvbN5.copy(alpha = 0.6f), thickness = 1.dp)
            PreviewInfoRow(
                icon = Icons.Outlined.Badge,
                label = "Employee ID",
                value = state.employeeId,
            )
            HorizontalDivider(color = SvbN5.copy(alpha = 0.6f), thickness = 1.dp)
            PreviewInfoRow(
                icon = Icons.Outlined.LocalShipping,
                label = "Machine",
                value = state.previewMachine,
            )
            Spacer(Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SvbOutlinedButton(
                    text = "Retake",
                    onClick = onRetake,
                    modifier = Modifier.weight(1f),
                    leadingIcon = Icons.Rounded.Refresh,
                )
                Button(
                    onClick = onClockIn,
                    modifier = Modifier
                        .weight(1.35f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SvbSuccess,
                        contentColor = SvbWhite,
                    ),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Clock In",
                            style = MaterialTheme.typography.labelLarge,
                            color = SvbWhite,
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun PreviewInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Surface(
            color = SvbN7,
            shape = RoundedCornerShape(12.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SvbBlack,
                modifier = Modifier
                    .padding(12.dp)
                    .size(24.dp),
            )
        }
        Column(Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = SvbN3,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = SvbBlack,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
