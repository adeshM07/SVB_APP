package com.svb.fieldops.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.ReportProblem
import androidx.compose.material.icons.outlined.StopCircle
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.svb.fieldops.ui.theme.SvbPrimary2

@Composable
fun OperatorHomeScreen(navController: NavHostController) {
    val role = UserRole.Operator
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
                initials = "RK",
                greeting = "Good morning",
                userName = "Rajesh Kumar",
                notificationDot = true,
            )
            Spacer(Modifier.height(20.dp))
            DutyStatusHeroCard(
                badgeLabel = "Tipper #1",
                timerText = "04:23:17",
                footerText = "Clocked in 09:41 AM • Zone A",
                timerAndFooterAlignStart = true,
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                MetricMiniCard("24", "Loadings", Modifier.weight(1f))
                MetricMiniCard("68%", "HSD", Modifier.weight(1f))
                MetricMiniCard("6:20", "Hours", Modifier.weight(1f))
            }
            Spacer(Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = HomeCardShape,
                colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Column(Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "CURRENT LOADING",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = SvbN2,
                        )
                        Text(
                            text = "02:14",
                            style = MaterialTheme.typography.headlineMedium,
                            color = SvbBlack,
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "#24 • 2:15 PM",
                        style = MaterialTheme.typography.bodySmall,
                        color = SvbN2,
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        shape = HomeCardShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SvbPrimary2,
                            contentColor = SvbBlack,
                        ),
                        contentPadding = PaddingValues(vertical = 14.dp, horizontal = 16.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Icon(
                                Icons.Outlined.LocalShipping,
                                contentDescription = null,
                                modifier = Modifier.size(22.dp),
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = "Tipper Left — Log Loading",
                                style = MaterialTheme.typography.labelLarge,
                            )
                        }
                    }
                }
            }
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
            HomeFuelStatusCard(circularIconBackground = false)
            Spacer(Modifier.height(24.dp))
        }
    }
}
