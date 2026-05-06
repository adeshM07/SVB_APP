package com.svb.fieldops.presentation.screens.notifications

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.PrecisionManufacturing
import androidx.compose.material.icons.outlined.PriorityHigh
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.svb.fieldops.domain.model.UserRole
import com.svb.fieldops.presentation.navigation.MainRoutes
import com.svb.fieldops.presentation.navigation.approvalsTabIndex
import com.svb.fieldops.presentation.navigation.bottomNavItemsForRole
import com.svb.fieldops.presentation.navigation.dieselTabIndex
import com.svb.fieldops.presentation.navigation.dprTabIndex
import com.svb.fieldops.presentation.navigation.popRoleHomeWithHomeTabSelected
import com.svb.fieldops.presentation.navigation.profileTabIndex
import com.svb.fieldops.presentation.screens.home.HomeCardShape
import com.svb.fieldops.presentation.screens.home.HomeRoleNavigationBar
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbCardMuted
import com.svb.fieldops.ui.theme.SvbDanger
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN2
import com.svb.fieldops.ui.theme.SvbN5
import com.svb.fieldops.ui.theme.SvbN7
import com.svb.fieldops.ui.theme.SvbPrimary1
import com.svb.fieldops.ui.theme.SvbPrimary2
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite

private enum class NotificationFilter { All, Pending, Done }
private enum class ActionDecision { None, Approved, Declined, Acknowledged, Rejected }

private data class NotificationItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val time: String,
    val icon: ImageVector,
    val iconTint: Color,
    val iconBg: Color,
    val primaryAction: String? = null,
    val secondaryAction: String? = null,
    val resolvedText: String? = null,
    val isToday: Boolean = true,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EngineerNotificationsScreen(
    role: UserRole,
    navController: NavHostController,
) {
    val items = bottomNavItemsForRole(role)
    val homeIdx = 0
    val approvalsIdx = requireNotNull(approvalsTabIndex(role))
    val dieselIdx = requireNotNull(dieselTabIndex(role))
    val dprIdx = requireNotNull(dprTabIndex(role))
    val profileIdx = profileTabIndex(role)
    val scroll = rememberScrollState()

    var filter by remember { mutableStateOf(NotificationFilter.All) }
    val decisions = remember {
        mutableStateMapOf(
            "n1" to ActionDecision.None,
            "n2" to ActionDecision.None,
            "n3" to ActionDecision.None,
        )
    }

    val rows = remember {
        listOf(
            NotificationItem(
                id = "n1",
                title = "HSD Request from JCB-07",
                subtitle = "By Rajesh • 50 L • Verified by SUP",
                time = "10 min",
                icon = Icons.Outlined.LocalGasStation,
                iconTint = SvbPrimary1,
                iconBg = SvbPrimary1.copy(alpha = 0.18f),
                primaryAction = "Approve",
                secondaryAction = "Decline",
            ),
            NotificationItem(
                id = "n2",
                title = "Swap request for TATA-019",
                subtitle = "By SUP Suresh • Tyre puncture",
                time = "25 min",
                icon = Icons.Outlined.SwapHoriz,
                iconTint = SvbPrimary1,
                iconBg = SvbPrimary1.copy(alpha = 0.18f),
                primaryAction = "Approve",
                secondaryAction = "Decline",
            ),
            NotificationItem(
                id = "n3",
                title = "Breakdown reported on JCB-05",
                subtitle = "Battery dead, mechanic needed • Tap to handle",
                time = "1 hr",
                icon = Icons.Outlined.PriorityHigh,
                iconTint = SvbDanger,
                iconBg = SvbDanger.copy(alpha = 0.12f),
                primaryAction = "Acknowledge",
                secondaryAction = "Reject",
            ),
            NotificationItem(
                id = "n4",
                title = "Your DPR submitted to QS",
                subtitle = "QS team approval pending",
                time = "2 hr",
                icon = Icons.Outlined.CheckCircle,
                iconTint = SvbSuccess,
                iconBg = SvbSuccess.copy(alpha = 0.12f),
            ),
            NotificationItem(
                id = "n5",
                title = "HSD issued: 100 L to JCB-07",
                subtitle = "Diesel was issued",
                time = "Yesterday 14:30",
                icon = Icons.Outlined.LocalGasStation,
                iconTint = SvbSuccess,
                iconBg = SvbSuccess.copy(alpha = 0.12f),
                isToday = false,
            ),
            NotificationItem(
                id = "n6",
                title = "Breakdown closed: TATA-055",
                subtitle = "Tyre replaced, vehicle back in service",
                time = "Yesterday 16:00",
                icon = Icons.Outlined.Build,
                iconTint = SvbSuccess,
                iconBg = SvbSuccess.copy(alpha = 0.12f),
                isToday = false,
            ),
        )
    }

    val visible = rows.filter { row ->
        val decision = decisions[row.id] ?: ActionDecision.None
        when (filter) {
            NotificationFilter.All -> true
            NotificationFilter.Pending -> row.primaryAction != null && decision == ActionDecision.None
            NotificationFilter.Done -> row.primaryAction == null || decision != ActionDecision.None
        }
    }

    val allCount = rows.size
    val pendingCount = rows.count { (decisions[it.id] ?: ActionDecision.None) == ActionDecision.None && it.primaryAction != null }
    val doneCount = allCount - pendingCount

    BackHandler { navController.popBackStack() }

    Scaffold(
        containerColor = SvbLoginBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notifications",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back", tint = SvbBlack)
                    }
                },
                actions = {
                    IconButton(onClick = { /* later: filter/sort */ }) {
                        Icon(Icons.Outlined.Tune, contentDescription = "Filters", tint = SvbBlack)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SvbLoginBackground,
                    scrolledContainerColor = SvbLoginBackground,
                ),
            )
        },
        bottomBar = {
            HomeRoleNavigationBar(
                items = items,
                selectedIndex = homeIdx,
                onSelect = { index ->
                    when {
                        index == homeIdx -> navController.popRoleHomeWithHomeTabSelected()
                        index == approvalsIdx ->
                            navController.navigate(MainRoutes.approvals(role)) { launchSingleTop = true }
                        index == dieselIdx ->
                            navController.navigate(MainRoutes.diesel(role)) { launchSingleTop = true }
                        index == dprIdx ->
                            navController.navigate(MainRoutes.dpr(role)) { launchSingleTop = true }
                        index == profileIdx ->
                            navController.navigate(MainRoutes.profile(role)) { launchSingleTop = true }
                        else -> navController.popRoleHomeWithHomeTabSelected()
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
                .padding(top = 8.dp, bottom = 20.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                NotificationsFilterChip("All ($allCount)", selected = filter == NotificationFilter.All) {
                    filter = NotificationFilter.All
                }
                NotificationsFilterChip("Pending ($pendingCount)", selected = filter == NotificationFilter.Pending) {
                    filter = NotificationFilter.Pending
                }
                NotificationsFilterChip("Done ($doneCount)", selected = filter == NotificationFilter.Done) {
                    filter = NotificationFilter.Done
                }
            }

            Spacer(Modifier.padding(top = 14.dp))
            val todayRows = visible.filter { it.isToday }
            if (todayRows.isNotEmpty()) {
                NotificationsSectionLabel("TODAY")
                Spacer(Modifier.padding(top = 8.dp))
                todayRows.forEach {
                    NotificationRowCard(
                        item = it,
                        decision = decisions[it.id] ?: ActionDecision.None,
                        onPrimary = {
                            decisions[it.id] = when (it.primaryAction) {
                                "Approve" -> ActionDecision.Approved
                                "Acknowledge" -> ActionDecision.Acknowledged
                                else -> ActionDecision.None
                            }
                        },
                        onSecondary = {
                            decisions[it.id] = when (it.secondaryAction) {
                                "Decline" -> ActionDecision.Declined
                                "Reject" -> ActionDecision.Rejected
                                else -> ActionDecision.None
                            }
                        },
                    )
                    Spacer(Modifier.padding(top = 10.dp))
                }
            }

            val yesterdayRows = visible.filter { !it.isToday }
            if (yesterdayRows.isNotEmpty()) {
                NotificationsSectionLabel("YESTERDAY")
                Spacer(Modifier.padding(top = 8.dp))
                yesterdayRows.forEach {
                    NotificationRowCard(
                        item = it,
                        decision = decisions[it.id] ?: ActionDecision.None,
                        onPrimary = {},
                        onSecondary = {},
                    )
                    Spacer(Modifier.padding(top = 10.dp))
                }
            }
        }
    }
}

@Composable
private fun NotificationsSectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = SvbN2,
    )
}

@Composable
private fun NotificationsFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(999.dp),
        color = if (selected) SvbPrimary1.copy(alpha = 0.25f) else SvbN7,
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = if (selected) SvbPrimary1 else SvbBlack,
        )
    }
}

@Composable
private fun NotificationRowCard(
    item: NotificationItem,
    decision: ActionDecision,
    onPrimary: () -> Unit,
    onSecondary: () -> Unit,
) {
    val resolvedText = when (decision) {
        ActionDecision.Approved -> "Approved"
        ActionDecision.Declined -> "Declined"
        ActionDecision.Acknowledged -> "Acknowledged"
        ActionDecision.Rejected -> "Rejected"
        ActionDecision.None -> null
    }
    val isDone = resolvedText != null

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(14.dp),
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Surface(
                    shape = CircleShape,
                    color = item.iconBg,
                    modifier = Modifier.size(38.dp),
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(item.icon, contentDescription = null, tint = item.iconTint, modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(Modifier.size(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                    Spacer(Modifier.size(4.dp))
                    Text(
                        text = item.subtitle,
                        style = MaterialTheme.typography.bodyLarge,
                        color = SvbN2,
                    )
                }
                Text(text = item.time, style = MaterialTheme.typography.bodySmall, color = SvbN2)
            }

            if (!item.primaryAction.isNullOrBlank() && !item.secondaryAction.isNullOrBlank()) {
                Spacer(Modifier.size(10.dp))
                if (!isDone) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Button(
                            onClick = onPrimary,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SvbSuccess, contentColor = SvbWhite),
                        ) {
                            Icon(Icons.Outlined.Check, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.size(6.dp))
                            Text(item.primaryAction, fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = onSecondary,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, SvbDanger),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = SvbDanger),
                        ) {
                            Icon(Icons.Outlined.Close, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.size(6.dp))
                            Text(item.secondaryAction, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Text(
                        text = if (resolvedText == "Approved" || resolvedText == "Acknowledged") "✓ $resolvedText" else "✗ $resolvedText",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (resolvedText == "Approved" || resolvedText == "Acknowledged") SvbSuccess else SvbDanger,
                    )
                }
            }
        }
    }
}
