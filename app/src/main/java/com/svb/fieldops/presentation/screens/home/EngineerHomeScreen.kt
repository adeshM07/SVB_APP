package com.svb.fieldops.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.automirrored.outlined.FactCheck
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.StopCircle
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.svb.fieldops.domain.model.UserRole
import com.svb.fieldops.presentation.navigation.MainRoutes
import com.svb.fieldops.presentation.navigation.bottomNavItemsForRole
import com.svb.fieldops.presentation.navigation.profileTabIndex
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbCardMuted
import com.svb.fieldops.ui.theme.SvbDanger
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN2
import com.svb.fieldops.ui.theme.SvbN5
import com.svb.fieldops.ui.theme.SvbN7
import com.svb.fieldops.ui.theme.SvbPrimary1
import com.svb.fieldops.ui.theme.SvbRoseTint
import com.svb.fieldops.ui.theme.SvbWhite

@Composable
fun EngineerHomeScreen(navController: NavHostController) {
    val role = UserRole.Engineer
    val navItems = bottomNavItemsForRole(role)
    val profileIdx = profileTabIndex(role)
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
                    if (index == profileIdx) {
                        navController.navigate(MainRoutes.profile(role)) {
                            launchSingleTop = true
                        }
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
                initials = "AV",
                greeting = "Good morning",
                userName = "Anil Verma",
                notificationDot = true,
            )
            Spacer(Modifier.height(20.dp))
            DutyStatusHeroCard(
                badgeLabel = "Site Lead",
                timerText = "04:23:17",
                footerText = "Clocked in 09:41 AM • SVB 68-Acre",
                timerAndFooterAlignStart = true,
            )
            Spacer(Modifier.height(16.dp))
            PendingApprovalsAlertCard()
            Spacer(Modifier.height(16.dp))
            HsdInventoryCard()
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                MetricMiniCard("5 / 5", "Active", Modifier.weight(1f))
                MetricMiniCard("2", "Breakdowns", Modifier.weight(1f))
                MetricMiniCard("180", "Today CUM", Modifier.weight(1f))
            }
            Spacer(Modifier.height(24.dp))
            SectionTitle("ACTIONS")
            GroupedActionCard(containerColor = SvbCardMuted) {
                ActionTileRow(
                    icon = Icons.Outlined.TaskAlt,
                    title = "Approvals",
                    subtitle = "7 requests pending",
                    onClick = {},
                    iconContainerColor = SvbWhite,
                )
                ActionDividerTextAligned()
                ActionTileRow(
                    icon = Icons.Outlined.LocalGasStation,
                    title = "HSD Inventory",
                    subtitle = "Stock: 2,000 L",
                    onClick = {},
                    iconContainerColor = SvbWhite,
                )
                ActionDividerTextAligned()
                ActionTileRow(
                    icon = Icons.AutoMirrored.Outlined.Assignment,
                    title = "Daily Project Report",
                    subtitle = "Submit today's site report",
                    onClick = {},
                    iconContainerColor = SvbWhite,
                )
                ActionDividerTextAligned()
                ActionTileRow(
                    icon = Icons.Outlined.PinDrop,
                    title = "Assign Zones",
                    subtitle = "Allocate machines to zones",
                    onClick = {},
                    iconContainerColor = SvbWhite,
                )
                ActionDividerTextAligned()
                ActionTileRow(
                    icon = Icons.Outlined.Build,
                    title = "Open Breakdowns",
                    subtitle = "Manage and resolve issues",
                    onClick = {},
                    iconContainerColor = SvbWhite,
                )
                ActionDividerTextAligned()
                ActionTileRow(
                    icon = Icons.AutoMirrored.Outlined.FactCheck,
                    title = "Verify Site Start",
                    subtitle = "Confirm all personnel started",
                    onClick = {},
                    iconContainerColor = SvbWhite,
                )
                ActionDividerTextAligned()
                ActionTileRow(
                    icon = Icons.Outlined.ShoppingCart,
                    title = "Add Purchase",
                    subtitle = "New HSD purchase entry",
                    onClick = {},
                    iconContainerColor = SvbWhite,
                )
                ActionDividerTextAligned()
                ActionTileRow(
                    icon = Icons.Outlined.StopCircle,
                    title = "End Job — Site-wide",
                    subtitle = "Close all operator duties",
                    onClick = {},
                    iconContainerColor = SvbWhite,
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PendingApprovalsAlertCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {}),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbRoseTint),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = HomeIconTileShape,
                color = SvbWhite,
                modifier = Modifier.size(44.dp),
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Outlined.TaskAlt,
                        contentDescription = null,
                        tint = SvbDanger,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "PENDING APPROVALS",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = SvbDanger,
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "7",
                        style = MaterialTheme.typography.displayLarge,
                        color = SvbBlack,
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "5 HSD, 2 swap",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SvbN2,
                    )
                }
                Text(
                    text = "7 requests pending",
                    style = MaterialTheme.typography.bodySmall,
                    color = SvbDanger,
                )
            }
            Icon(
                Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = SvbDanger,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

@Composable
private fun HsdInventoryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.LocalGasStation,
                        contentDescription = null,
                        tint = SvbBlack,
                        modifier = Modifier.size(22.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "HSD INVENTORY",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = SvbN2,
                    )
                }
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = SvbPrimary1.copy(alpha = 0.35f),
                ) {
                    Text(
                        text = "LOW STOCK",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = SvbBlack,
                    )
                }
            }
            Spacer(Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = "500 L",
                    style = MaterialTheme.typography.displayLarge,
                    color = SvbBlack,
                )
                Text(
                    text = "Closing balance",
                    style = MaterialTheme.typography.bodySmall,
                    color = SvbN2,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Running low — below 1000 L threshold",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = SvbPrimary1,
            )
            Spacer(Modifier.height(14.dp))
            HorizontalDivider(color = SvbN7)
            Spacer(Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HsdStatCell("1500 L", "Today Issued", Modifier.weight(1f))
                HsdStatColumnDividerBar()
                HsdStatCell("0 L", "Today Purchase", Modifier.weight(1f))
                HsdStatColumnDividerBar()
                HsdStatCell("1700 L", "Daily Avg", Modifier.weight(1f))
            }
        }
    }
}

/** Short vertical rule between HSD stat columns (visible on muted card). */
@Composable
private fun HsdStatColumnDividerBar() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(40.dp)
            .background(SvbN5),
    )
}

@Composable
private fun HsdStatCell(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(value, style = MaterialTheme.typography.titleLarge, color = SvbBlack)
        Spacer(Modifier.height(4.dp))
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = SvbN2,
            textAlign = TextAlign.Center,
        )
    }
}

