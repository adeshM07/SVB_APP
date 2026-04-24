package com.svb.fieldops.presentation.screens.sitestart

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material.icons.outlined.PrecisionManufacturing
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.HowToReg
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
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
import com.svb.fieldops.presentation.screens.home.HomeIconTileShape
import com.svb.fieldops.presentation.screens.home.HomeRoleNavigationBar
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbCardMuted
import com.svb.fieldops.ui.theme.SvbDanger
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN2
import com.svb.fieldops.ui.theme.SvbN5
import com.svb.fieldops.ui.theme.SvbPrimary1
import com.svb.fieldops.ui.theme.SvbPrimary2
import com.svb.fieldops.ui.theme.SvbPrimary5
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite

private val SummaryCardBorder = SvbPrimary1
/** Pending row icon tile — light yellow (matches Zone A / WAITING chip family). */
private val PendingIconTileBg = SvbPrimary5
/** “Pending start” — bright brand yellow. */
private val PendingStartText = SvbPrimary2

/** Pill shape (capsule) for chips and status badges. */
private val PillShape = RoundedCornerShape(percent = 50)

/** Gap 1 — VERIFIED chip (pale mint + teal). */
private val VerifiedChipBg = Color(0xFFE8F5F0)
private val VerifiedChipFg = Color(0xFF00897B)

/** Gap 2 — Zone / pit location chips. */
private data class ZoneLocationStyle(val background: Color, val content: Color)

/** Zone A (yellow): pale primary wash + vivid yellow pin + label. */
private val ZoneAmberChipStyle = ZoneLocationStyle(
    background = SvbPrimary5,
    content = SvbPrimary2,
)
private val ZoneTealChipStyle = ZoneLocationStyle(
    background = Color(0xFFE0F2F1),
    content = Color(0xFF00897B),
)
private val ZoneNeutralChipStyle = ZoneLocationStyle(
    background = Color(0xFFEFEFEF),
    content = SvbN2,
)

/** Gap 3 — WAITING (filled) + Notify (outlined): same yellow system as Zone A. */
private val WaitingFilledBg = SvbPrimary5
private val WaitingNotifyYellow = SvbPrimary2

private val ActionChipMinHeight = 44.dp
private val ActionChipHPad = 14.dp
private val ActionChipVPad = 10.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EngineerVerifySiteStartScreen(
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

    BackHandler { navController.popRoleHomeWithHomeTabSelected() }

    Scaffold(
        containerColor = SvbLoginBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Site Start Verification",
                        style = MaterialTheme.typography.titleLarge,
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
                        Box(Modifier.fillMaxSize()) {
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
                                    .padding(top = 8.dp, end = 6.dp)
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
                        index == profileIdx ->
                            navController.navigate(MainRoutes.profile(role)) { launchSingleTop = true }
                        index == approvalsIdx ->
                            navController.navigate(MainRoutes.approvals(role)) { launchSingleTop = true }
                        index == dieselIdx ->
                            navController.navigate(MainRoutes.diesel(role)) { launchSingleTop = true }
                        index == dprIdx ->
                            navController.navigate(MainRoutes.dpr(role)) { launchSingleTop = true }
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
            SiteStartSummaryCard()
            Spacer(Modifier.height(20.dp))

            VerifySectionTitle("SUPERVISORS")
            Spacer(Modifier.height(10.dp))
            SupervisorCard()
            Spacer(Modifier.height(20.dp))

            VerifySectionTitle("OPERATORS")
            Spacer(Modifier.height(10.dp))
            OperatorsGroupedCard(
                onWaitingClick = { /* stub */ },
                onNotifyClick = { /* stub */ },
            )
            Spacer(Modifier.height(20.dp))

            VerifySectionTitle("DRIVERS")
            Spacer(Modifier.height(10.dp))
            DriversGroupedCard()
            Spacer(Modifier.height(20.dp))

            VerifySectionTitle("QUICK ZONE ASSIGNMENT")
            Spacer(Modifier.height(10.dp))
            QuickZoneAssignmentCard(
                onManageZones = {
                    navController.navigate(MainRoutes.zoneWorkPlan(role)) { launchSingleTop = true }
                },
            )
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { /* stub: confirm day */ },
                modifier = Modifier.fillMaxWidth(),
                shape = HomeCardShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SvbPrimary2,
                    contentColor = SvbBlack,
                ),
                contentPadding = PaddingValues(vertical = 14.dp),
            ) {
                Icon(
                    Icons.Outlined.VerifiedUser,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Confirm All Start Details & Begin Day",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = { /* stub: send reminder */ },
                modifier = Modifier.fillMaxWidth(),
                shape = HomeCardShape,
                border = BorderStroke(1.dp, SvbBlack),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SvbBlack),
                contentPadding = PaddingValues(vertical = 14.dp),
            ) {
                Icon(
                    Icons.Outlined.Notifications,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Send reminder to pending personnel",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
private fun VerifySectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = SvbN2,
    )
}

@Composable
private fun SiteStartSummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbPrimary5),
        border = BorderStroke(1.dp, SummaryCardBorder),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Today: 22 April 2026",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SvbBlack,
                )
                Icon(
                    Icons.Outlined.CalendarToday,
                    contentDescription = "Date",
                    tint = SvbPrimary2,
                    modifier = Modifier.size(28.dp),
                )
            }
            Spacer(Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                SummaryMetric(Icons.Outlined.Person, "1", "SUP")
                SummaryMetric(Icons.Outlined.PrecisionManufacturing, "3", "OPP")
                SummaryMetric(Icons.Outlined.LocalShipping, "2", "DVR")
                Spacer(Modifier.weight(1f))
                Text(
                    text = "active",
                    style = MaterialTheme.typography.bodySmall,
                    color = SvbN2,
                )
            }
            Spacer(Modifier.height(14.dp))
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = SvbWhite,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Outlined.HowToReg,
                        contentDescription = null,
                        tint = SvbSuccess,
                        modifier = Modifier.size(28.dp),
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "5 of 6 personnel started shift",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = SvbBlack,
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryMetric(icon: ImageVector, count: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = SvbN2, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(4.dp))
        Text(
            text = "$count $label",
            style = MaterialTheme.typography.bodySmall,
            color = SvbN2,
        )
    }
}

@Composable
private fun SupervisorCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
    ) {
        VerifiedPersonRow(
            leadingIcon = Icons.Outlined.Person,
            iconBackground = SvbWhite,
            name = "Sandeep Kumar",
            idCode = "(SUP001)",
            detailLine = "Started: 8:30 AM • Zone: All Zones",
            zoneTag = null,
            zoneLocationStyle = null,
            verified = true,
            pendingMachineLine = null,
            showActions = false,
            onWaitingClick = {},
            onNotifyClick = {},
        )
    }
}

@Composable
private fun OperatorsGroupedCard(
    onWaitingClick: () -> Unit,
    onNotifyClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
    ) {
        Column {
            VerifiedPersonRow(
                leadingIcon = Icons.Outlined.PrecisionManufacturing,
                iconBackground = SvbWhite,
                name = "Rajesh Kumar",
                idCode = "(OPP001)",
                detailLine = "JCB-07 • 9:42 AM",
                zoneTag = "Zone A",
                zoneLocationStyle = ZoneAmberChipStyle,
                verified = true,
                pendingMachineLine = null,
                showActions = false,
                onWaitingClick = {},
                onNotifyClick = {},
            )
            HorizontalDivider(color = SvbN5, thickness = 1.dp)
            VerifiedPersonRow(
                leadingIcon = Icons.Outlined.PrecisionManufacturing,
                iconBackground = SvbWhite,
                name = "Mohan Lal",
                idCode = "(OPP002)",
                detailLine = "EXC-12 • 9:50 AM",
                zoneTag = "Pit-1",
                zoneLocationStyle = ZoneTealChipStyle,
                verified = true,
                pendingMachineLine = null,
                showActions = false,
                onWaitingClick = {},
                onNotifyClick = {},
            )
            HorizontalDivider(color = SvbN5, thickness = 1.dp)
            VerifiedPersonRow(
                leadingIcon = Icons.Outlined.PrecisionManufacturing,
                iconBackground = PendingIconTileBg,
                name = "Anil Verma",
                idCode = "(OPP003)",
                detailLine = null,
                zoneTag = null,
                zoneLocationStyle = null,
                verified = false,
                pendingMachineLine = "JCB-03",
                showActions = true,
                onWaitingClick = onWaitingClick,
                onNotifyClick = onNotifyClick,
            )
        }
    }
}

@Composable
private fun DriversGroupedCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
    ) {
        Column {
            VerifiedPersonRow(
                leadingIcon = Icons.Outlined.LocalShipping,
                iconBackground = SvbWhite,
                name = "Suresh Yadav",
                idCode = "(DVR001)",
                detailLine = "TATA-019 • 9:35 AM",
                zoneTag = "Zone A",
                zoneLocationStyle = ZoneAmberChipStyle,
                verified = true,
                pendingMachineLine = null,
                showActions = false,
                onWaitingClick = {},
                onNotifyClick = {},
            )
            HorizontalDivider(color = SvbN5, thickness = 1.dp)
            VerifiedPersonRow(
                leadingIcon = Icons.Outlined.LocalShipping,
                iconBackground = SvbWhite,
                name = "Vikram Singh",
                idCode = "(DVR002)",
                detailLine = "TATA-024 • 9:45 AM",
                zoneTag = "Zone B",
                zoneLocationStyle = ZoneNeutralChipStyle,
                verified = true,
                pendingMachineLine = null,
                showActions = false,
                onWaitingClick = {},
                onNotifyClick = {},
            )
        }
    }
}

@Composable
private fun VerifiedPersonRow(
    leadingIcon: ImageVector,
    iconBackground: Color,
    name: String,
    idCode: String,
    detailLine: String?,
    zoneTag: String?,
    zoneLocationStyle: ZoneLocationStyle?,
    verified: Boolean,
    pendingMachineLine: String?,
    showActions: Boolean,
    onWaitingClick: () -> Unit,
    onNotifyClick: () -> Unit,
) {
    Column(Modifier.padding(14.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = HomeIconTileShape,
                color = iconBackground,
            ) {
                Icon(
                    leadingIcon,
                    contentDescription = null,
                    tint = SvbBlack,
                    modifier = Modifier.padding(12.dp).size(24.dp),
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = buildAnnotatedString {
                        append(name)
                        withStyle(SpanStyle(color = SvbN2, fontWeight = FontWeight.Normal)) {
                            append(" $idCode")
                        }
                    },
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SvbBlack,
                )
                if (pendingMachineLine != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = buildAnnotatedString {
                            append("$pendingMachineLine • ")
                            withStyle(SpanStyle(color = PendingStartText, fontWeight = FontWeight.SemiBold)) {
                                append("Pending start")
                            }
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = SvbN2,
                    )
                } else if (detailLine != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = detailLine,
                        style = MaterialTheme.typography.bodySmall,
                        color = SvbN2,
                    )
                    if (zoneTag != null && zoneLocationStyle != null) {
                        Spacer(Modifier.height(8.dp))
                        ZoneLocationPill(
                            label = zoneTag,
                            style = zoneLocationStyle,
                        )
                    }
                }
            }
            if (verified) {
                VerifiedPillChip()
            }
        }
        if (showActions) {
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    shape = PillShape,
                    color = WaitingFilledBg,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = ActionChipMinHeight)
                        .clip(PillShape)
                        .clickable(onClick = onWaitingClick),
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = ActionChipHPad, vertical = ActionChipVPad),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = WaitingNotifyYellow,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "WAITING",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = WaitingNotifyYellow,
                        )
                    }
                }
                Surface(
                    shape = PillShape,
                    color = Color.Transparent,
                    border = BorderStroke(1.dp, WaitingNotifyYellow),
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = ActionChipMinHeight)
                        .clip(PillShape)
                        .clickable(onClick = onNotifyClick),
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = ActionChipHPad, vertical = ActionChipVPad),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Outlined.NotificationsActive,
                            contentDescription = null,
                            tint = WaitingNotifyYellow,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Notify",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = WaitingNotifyYellow,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VerifiedPillChip() {
    Surface(
        shape = PillShape,
        color = VerifiedChipBg,
        modifier = Modifier.padding(start = 8.dp),
    ) {
        Row(
            Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Outlined.CheckCircleOutline,
                contentDescription = null,
                tint = VerifiedChipFg,
                modifier = Modifier.size(17.dp),
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = "VERIFIED",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = VerifiedChipFg,
            )
        }
    }
}

@Composable
private fun ZoneLocationPill(
    label: String,
    style: ZoneLocationStyle,
) {
    Surface(
        shape = PillShape,
        color = style.background,
    ) {
        Row(
            Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Outlined.LocationOn,
                contentDescription = null,
                tint = style.content,
                modifier = Modifier.size(14.dp),
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = style.content,
            )
        }
    }
}

@Composable
private fun QuickZoneAssignmentCard(onManageZones: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = HomeIconTileShape,
                color = SvbWhite,
            ) {
                Icon(
                    Icons.Outlined.PinDrop,
                    contentDescription = null,
                    tint = SvbPrimary1,
                    modifier = Modifier.padding(12.dp).size(24.dp),
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = "Manage today's zones",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SvbBlack,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Assign or reassign zones for today's operations",
                    style = MaterialTheme.typography.bodySmall,
                    color = SvbN2,
                )
            }
            OutlinedButton(
                onClick = onManageZones,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, SvbBlack),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SvbBlack),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            ) {
                Icon(Icons.Outlined.LocationOn, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text("Manage Zones", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
