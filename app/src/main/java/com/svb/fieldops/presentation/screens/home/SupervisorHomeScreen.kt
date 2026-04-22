package com.svb.fieldops.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FactCheck
import androidx.compose.material.icons.outlined.InsertChart
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PrecisionManufacturing
import androidx.compose.material.icons.outlined.ReportProblem
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.StopCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.svb.fieldops.domain.model.UserRole
import com.svb.fieldops.presentation.navigation.MainRoutes
import com.svb.fieldops.presentation.navigation.bottomNavItemsForRole
import com.svb.fieldops.presentation.navigation.fuelTabIndex
import com.svb.fieldops.presentation.navigation.profileTabIndex
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbCardMuted
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN2
import com.svb.fieldops.ui.theme.SvbWhite

@Composable
fun SupervisorHomeScreen(navController: NavHostController) {
    val role = UserRole.Supervisor
    val navItems = bottomNavItemsForRole(role)
    val profileIdx = profileTabIndex(role)
    val fuelIdx = fuelTabIndex(role)
    var selectedTab by remember { mutableIntStateOf(0) }
    HomeBottomTabResetFromProfileEffect(navController) { selectedTab = 0 }

    val scroll = rememberScrollState()
    Scaffold(
        containerColor = SvbLoginBackground,
        bottomBar = {
            HomeRoleNavigationBar(
                items = navItems,
                selectedIndex = selectedTab,
                onSelect = { index ->
                    selectedTab = index
                    when {
                        index == profileIdx ->
                            navController.navigate(MainRoutes.profile(role)) { launchSingleTop = true }
                        fuelIdx != null && index == fuelIdx ->
                            navController.navigate(MainRoutes.fuel(role)) { launchSingleTop = true }
                    }
                },
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
                initials = "SK",
                greeting = "Good morning",
                userName = "Sandeep Kumar",
                notificationDot = true,
            )
            Spacer(Modifier.height(20.dp))
            DutyStatusHeroCard(
                badgeLabel = "Team A",
                timerText = "04:23:17",
                footerText = "Clocked in 09:41 AM • SVB Site",
                timerAndFooterAlignStart = true,
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                MetricMiniCard("4 / 5", "Active", Modifier.weight(1f))
                MetricMiniCard("3", "Pending Verify", Modifier.weight(1f))
                MetricMiniCard("1", "Issues", Modifier.weight(1f))
            }
            Spacer(Modifier.height(22.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = HomeCardShape,
                colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    SectionHeaderRow(
                        title = "PENDING VERIFICATIONS",
                        trailing = {
                            Surface(
                                shape = RoundedCornerShape(999.dp),
                                color = SvbWhite,
                            ) {
                                Text(
                                    text = "3 pending",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SvbBlack,
                                )
                            }
                        },
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "Verify your team",
                        style = MaterialTheme.typography.bodySmall,
                        color = SvbN2,
                    )
                    Spacer(Modifier.height(12.dp))
                    GroupedActionCard(containerColor = SvbWhite) {
                        ActionTileRow(
                            icon = Icons.Outlined.PrecisionManufacturing,
                            title = "JCB-07 Start Duty",
                            subtitle = "09:42 AM • Operator Rajesh",
                            onClick = {},
                            iconContainerColor = SvbCardMuted,
                        )
                        ActionDividerTextAligned()
                        ActionTileRow(
                            icon = Icons.Outlined.LocalShipping,
                            title = "TATA-019 Trip #5",
                            subtitle = "11:18 AM • Driver Suresh",
                            onClick = {},
                            iconContainerColor = SvbCardMuted,
                        )
                        ActionDividerTextAligned()
                        ActionTileRow(
                            icon = Icons.Outlined.LocalShipping,
                            title = "TATA-024 Trip #3",
                            subtitle = "12:05 PM • Driver Vikram",
                            onClick = {},
                            iconContainerColor = SvbCardMuted,
                        )
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            SectionTitle("ACTIONS")
            GroupedActionCard(containerColor = SvbCardMuted) {
                ActionTileRow(
                    icon = Icons.AutoMirrored.Outlined.FactCheck,
                    title = "Verify Start Duty",
                    subtitle = "Approve operator start requests",
                    onClick = {},
                    iconContainerColor = SvbWhite,
                )
                ActionDividerTextAligned()
                ActionTileRow(
                    icon = Icons.Outlined.Route,
                    title = "Verify Trips",
                    subtitle = "Cross-check tipper loadings",
                    onClick = {},
                    iconContainerColor = SvbWhite,
                )
                ActionDividerTextAligned()
                ActionTileRow(
                    icon = Icons.Outlined.LocalGasStation,
                    title = "Verify HSD Requests",
                    subtitle = "Check operator fuel requests",
                    onClick = {},
                    iconContainerColor = SvbWhite,
                )
                ActionDividerTextAligned()
                ActionTileRow(
                    icon = Icons.Outlined.Build,
                    title = "Close Breakdowns",
                    subtitle = "Mark resolved issues",
                    onClick = {},
                    iconContainerColor = SvbWhite,
                )
                ActionDividerTextAligned()
                ActionTileRow(
                    icon = Icons.Outlined.ReportProblem,
                    title = "Report Breakdown",
                    subtitle = "Log a site machine issue",
                    onClick = {},
                    iconContainerColor = SvbWhite,
                )
                ActionDividerTextAligned()
                ActionTileRow(
                    icon = Icons.Outlined.LocalGasStation,
                    title = "Request HSD (Self)",
                    subtitle = "Raise own diesel request",
                    onClick = {},
                    iconContainerColor = SvbWhite,
                )
                ActionDividerTextAligned()
                ActionTileRow(
                    icon = Icons.Outlined.StopCircle,
                    title = "End Job",
                    subtitle = "Close shift and submit log",
                    onClick = {},
                    iconContainerColor = SvbWhite,
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
