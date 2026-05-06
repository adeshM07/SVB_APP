package com.svb.fieldops.presentation.screens.fuel

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PrecisionManufacturing
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.svb.fieldops.domain.model.UserRole
import com.svb.fieldops.presentation.navigation.MainRoutes
import com.svb.fieldops.presentation.navigation.NavStateKeys
import com.svb.fieldops.presentation.navigation.bottomNavItemsForRole
import com.svb.fieldops.presentation.navigation.fuelTabIndex
import com.svb.fieldops.presentation.navigation.popRoleHomeWithHomeTabSelected
import com.svb.fieldops.presentation.navigation.profileTabIndex
import com.svb.fieldops.presentation.navigation.reportsTabIndex
import com.svb.fieldops.presentation.navigation.verifyTabIndex
import com.svb.fieldops.presentation.screens.home.HomeCardShape
import com.svb.fieldops.presentation.screens.home.HomeIconTileShape
import com.svb.fieldops.presentation.screens.home.HomeRoleNavigationBar
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbCardMuted
import com.svb.fieldops.ui.theme.SvbDanger
import com.svb.fieldops.ui.theme.SvbDivider
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN2
import com.svb.fieldops.ui.theme.SvbN5
import com.svb.fieldops.ui.theme.SvbPrimary1
import com.svb.fieldops.ui.theme.SvbPrimary2
import com.svb.fieldops.ui.theme.SvbPrimary5
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite

private enum class HsdRequestFilter { All, Pending, Verified }

private data class HsdRequestLine(
    val id: String,
    val title: String,
    val subtitle: String,
    val machineIcon: ImageVector,
    val timeLabel: String,
    val verifiedDetail: String?,
    val isVerified: Boolean,
)

private val FilterChipShape = RoundedCornerShape(999.dp)
private val PendingRowChipShape = RoundedCornerShape(999.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupervisorVerifyHsdRequestsScreen(
    role: UserRole,
    navController: NavHostController,
    listSavedStateHandle: SavedStateHandle,
) {
    val items = bottomNavItemsForRole(role)
    val homeIdx = 0
    val verifyIdx = requireNotNull(verifyTabIndex(role))
    val reportsIdx = requireNotNull(reportsTabIndex(role))
    val fuelIdx = requireNotNull(fuelTabIndex(role))
    val profileIdx = profileTabIndex(role)
    val scroll = rememberScrollState()

    val completedFlowId by listSavedStateHandle
        .getStateFlow<String?>(NavStateKeys.HSD_REQUEST_FLOW_COMPLETED_ID, null)
        .collectAsStateWithLifecycle()

    var lines by remember {
        mutableStateOf(
            listOf(
                HsdRequestLine(
                    id = "r1",
                    title = "JCB-07 — 50 L",
                    subtitle = "Requested by OPP Rajesh",
                    machineIcon = Icons.Outlined.PrecisionManufacturing,
                    timeLabel = "10 min ago",
                    verifiedDetail = null,
                    isVerified = false,
                ),
                HsdRequestLine(
                    id = "r2",
                    title = "TATA-024 — 80 L",
                    subtitle = "Requested by OPP Vikram",
                    machineIcon = Icons.Outlined.LocalShipping,
                    timeLabel = "25 min ago",
                    verifiedDetail = null,
                    isVerified = false,
                ),
                HsdRequestLine(
                    id = "r3",
                    title = "EXC-12 — 120 L",
                    subtitle = "Requested by OPP Mohan",
                    machineIcon = Icons.Outlined.PrecisionManufacturing,
                    timeLabel = "",
                    verifiedDetail = "Waiting for ENG",
                    isVerified = true,
                ),
            ),
        )
    }

    LaunchedEffect(completedFlowId) {
        val id = completedFlowId ?: return@LaunchedEffect
        lines = lines.map { l ->
            if (l.id == id) {
                l.copy(isVerified = true, verifiedDetail = "Waiting for ENG", timeLabel = "")
            } else {
                l
            }
        }
        listSavedStateHandle.remove<String>(NavStateKeys.HSD_REQUEST_FLOW_COMPLETED_ID)
    }

    var filter by remember { mutableStateOf(HsdRequestFilter.All) }

    val total = lines.size
    val pendingCount = lines.count { !it.isVerified }
    val verifiedCount = lines.count { it.isVerified }

    val visible = when (filter) {
        HsdRequestFilter.All -> lines
        HsdRequestFilter.Pending -> lines.filter { !it.isVerified }
        HsdRequestFilter.Verified -> lines.filter { it.isVerified }
    }

    BackHandler { navController.popRoleHomeWithHomeTabSelected() }

    Scaffold(
        containerColor = SvbLoginBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "HSD Requests to Verify",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popRoleHomeWithHomeTabSelected() }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = SvbBlack,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* later: notifications */ }) {
                        Box(Modifier.size(48.dp)) {
                            Icon(
                                Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                tint = SvbBlack,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(end = 6.dp),
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 10.dp, end = 8.dp)
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(SvbDanger),
                            )
                        }
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
                        index == homeIdx ->
                            navController.popRoleHomeWithHomeTabSelected()
                        index == verifyIdx ->
                            navController.navigate(MainRoutes.verifyStartDuty(role)) { launchSingleTop = true }
                        index == reportsIdx ->
                            navController.navigate(MainRoutes.reports(role)) { launchSingleTop = true }
                        index == fuelIdx ->
                            navController.navigate(MainRoutes.fuel(role)) { launchSingleTop = true }
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
                .fillMaxWidth()
                .padding(innerPadding)
                .verticalScroll(scroll)
                .padding(horizontal = 20.dp)
                .padding(top = 8.dp, bottom = 24.dp),
        ) {
            Text(
                text = "Review and verify operator HSD requests before forwarding to Engineer",
                style = MaterialTheme.typography.bodyMedium,
                color = SvbN2,
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                HsdFilterChip(
                    label = "All ($total)",
                    selected = filter == HsdRequestFilter.All,
                    onClick = { filter = HsdRequestFilter.All },
                    modifier = Modifier.weight(1f),
                )
                HsdFilterChip(
                    label = "Pending ($pendingCount)",
                    selected = filter == HsdRequestFilter.Pending,
                    onClick = { filter = HsdRequestFilter.Pending },
                    modifier = Modifier.weight(1f),
                )
                HsdFilterChip(
                    label = "Verified ($verifiedCount)",
                    selected = filter == HsdRequestFilter.Verified,
                    onClick = { filter = HsdRequestFilter.Verified },
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(Modifier.height(20.dp))
            Text(
                text = "REQUESTS ($total)",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = SvbN2,
            )
            Spacer(Modifier.height(10.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = HomeCardShape,
                colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Column {
                    if (visible.isEmpty()) {
                        Text(
                            text = "No requests in this filter.",
                            modifier = Modifier.padding(20.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = SvbN2,
                        )
                    } else {
                        visible.forEachIndexed { index, line ->
                            if (index > 0) {
                                HorizontalDivider(color = SvbDivider, thickness = 1.dp)
                            }
                            HsdRequestRow(
                                line = line,
                                onVerify = {
                                    navController.navigate(MainRoutes.verifyHsdRequestFlow(role, line.id)) {
                                        launchSingleTop = true
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HsdFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bg = if (selected) SvbPrimary5 else SvbLoginBackground
    val border = if (selected) BorderStroke(1.dp, SvbPrimary2) else BorderStroke(1.dp, SvbN5)
    val textColor = if (selected) SvbPrimary1 else SvbBlack
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = FilterChipShape,
        color = bg,
        border = border,
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
        )
    }
}

@Composable
private fun HsdRequestRow(
    line: HsdRequestLine,
    onVerify: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val iconBg = if (line.isVerified) SvbSuccess.copy(alpha = 0.16f) else SvbWhite
        val iconTint = if (line.isVerified) SvbSuccess else SvbBlack
        if (line.isVerified) {
            Surface(
                shape = HomeIconTileShape,
                color = iconBg,
            ) {
                Icon(
                    line.machineIcon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.padding(10.dp).size(24.dp),
                )
            }
        } else {
            Surface(
                shape = HomeIconTileShape,
                color = iconBg,
                border = BorderStroke(1.dp, SvbN5),
            ) {
                Icon(
                    line.machineIcon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.padding(10.dp).size(24.dp),
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = line.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SvbBlack,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = line.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = SvbN2,
            )
            Spacer(Modifier.height(8.dp))
            if (!line.isVerified) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = PendingRowChipShape,
                        color = SvbPrimary5,
                    ) {
                        Row(
                            Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                Icons.Outlined.Schedule,
                                contentDescription = null,
                                tint = SvbPrimary1,
                                modifier = Modifier.size(14.dp),
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "Pending",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = SvbPrimary1,
                            )
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = line.timeLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = SvbN2,
                    )
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        tint = SvbSuccess,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Verified",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = SvbSuccess,
                    )
                    line.verifiedDetail?.let { detail ->
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = detail,
                            style = MaterialTheme.typography.bodySmall,
                            color = SvbN2,
                        )
                    }
                }
            }
        }
        if (!line.isVerified) {
            OutlinedButton(
                onClick = onVerify,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, SvbPrimary2),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = SvbPrimary1,
                    containerColor = SvbLoginBackground,
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            ) {
                Icon(
                    Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(6.dp))
                Text("Verify", fontWeight = FontWeight.Bold)
            }
        }
    }
}
