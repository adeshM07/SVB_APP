package com.svb.fieldops.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.ReportProblem
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.StopCircle
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.svb.fieldops.ui.theme.SvbCardMuted
import com.svb.fieldops.ui.theme.SvbLoginBackground

@Composable
fun DriverHomeScreen() {
    val scroll = rememberScrollState()
    Scaffold(
        containerColor = SvbLoginBackground,
        bottomBar = {
            HomeRoleNavigationBar(
                items = listOf(
                    HomeNavEntry("Home", Icons.Outlined.Home),
                    HomeNavEntry("Trips", Icons.Outlined.Route),
                    HomeNavEntry("Fuel", Icons.Outlined.LocalGasStation),
                    HomeNavEntry("Profile", Icons.Outlined.Person),
                ),
                selectedIndex = 0,
            )
        },
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scroll)
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp, bottom = 8.dp),
        ) {
            HomeHeaderBar(
                initials = "SY",
                greeting = "Good morning",
                userName = "Suresh Yadav",
                notificationDot = true,
            )
            Spacer(Modifier.height(20.dp))
            DutyStatusHeroCard(
                badgeLabel = "TATA 2518",
                timerText = "04:23:17",
                footerText = "Clocked in 09:41 AM   •   Zone A, SVB Site",
                timerAndFooterAlignStart = true,
            )
            Spacer(Modifier.height(24.dp))
            SectionTitle("ACTIONS")
            GroupedActionCard(containerColor = SvbCardMuted) {
                ActionTileRow(
                    icon = Icons.Outlined.PlayCircle,
                    title = "Start Job",
                    subtitle = "Scan machine QR & begin duty",
                    onClick = {},
                )
                ActionDividerTextAligned()
                ActionTileRow(
                    icon = Icons.Outlined.StopCircle,
                    title = "End Job",
                    subtitle = "Close your current duty",
                    onClick = {},
                )
                ActionDividerTextAligned()
                ActionTileRow(
                    icon = Icons.Outlined.ReportProblem,
                    title = "Report Breakdown",
                    subtitle = "Log a machine issue",
                    onClick = {},
                )
                ActionDividerTextAligned()
                ActionTileRow(
                    icon = Icons.Outlined.SwapHoriz,
                    title = "Request Swap",
                    subtitle = "Requires engineer approval",
                    onClick = {},
                )
            }
            Spacer(Modifier.height(12.dp))
            HomeFuelStatusCard(circularIconBackground = true)
            Spacer(Modifier.height(24.dp))
        }
    }
}
