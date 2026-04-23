package com.svb.fieldops.presentation.screens.reports

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PrecisionManufacturing
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.svb.fieldops.domain.model.UserRole
import com.svb.fieldops.presentation.components.SvbOutlinedButton
import com.svb.fieldops.presentation.components.SvbPrimaryButton
import com.svb.fieldops.presentation.navigation.MainRoutes
import com.svb.fieldops.presentation.navigation.bottomNavItemsForRole
import com.svb.fieldops.presentation.navigation.fuelTabIndex
import com.svb.fieldops.presentation.navigation.popRoleHomeWithHomeTabSelected
import com.svb.fieldops.presentation.navigation.profileTabIndex
import com.svb.fieldops.presentation.navigation.reportsTabIndex
import com.svb.fieldops.presentation.navigation.verifyTabIndex
import com.svb.fieldops.presentation.screens.home.HomeCardShape
import com.svb.fieldops.presentation.screens.home.HomeIconTileShape
import com.svb.fieldops.presentation.screens.home.HomeRoleNavigationBar
import com.svb.fieldops.presentation.screens.home.SectionTitle
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbCardMuted
import com.svb.fieldops.ui.theme.SvbDanger
import com.svb.fieldops.ui.theme.SvbDivider
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN2
import com.svb.fieldops.ui.theme.SvbN7
import com.svb.fieldops.ui.theme.SvbPrimary1
import com.svb.fieldops.ui.theme.SvbPrimary5
import com.svb.fieldops.ui.theme.SvbRoseTint
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite

// --- Local Colors & Backgrounds ---
private val ReportGoodIconBg = SvbSuccess.copy(alpha = 0.15f)
private val ReportLowPerfIconBg = SvbPrimary1.copy(alpha = 0.15f)
private val ReportIssueIconBg = SvbDanger.copy(alpha = 0.15f)
private val MajorIssueBadgeBg = SvbDanger.copy(alpha = 0.15f)

private enum class StatusType { GOOD, LOW_PERF, ISSUE }

private data class MachineReportEntry(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconTint: Color,
    val iconBg: Color,
    val statusText: String,
    val statusType: StatusType,
)

private data class TripLogReportEntry(
    val tripId: String,
    val timeAndDistance: String,
    val icon: ImageVector,
    val iconTint: Color,
    val statusText: String,
    val isVerified: Boolean,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupervisorReportsScreen(
    role: UserRole,
    navController: NavHostController,
) {
    val items = bottomNavItemsForRole(role)
    val reportsIdx = requireNotNull(reportsTabIndex(role)) { "SupervisorReportsScreen is Supervisor-only." }
    val profileIdx = profileTabIndex(role)
    val fuelIdx = fuelTabIndex(role)
    val verifyIdx = verifyTabIndex(role)

    val scroll = rememberScrollState()
    var selectedTimeFilter by remember { mutableIntStateOf(0) }

    val machines = remember {
        listOf(
            MachineReportEntry("JCB-07 — Excavator", "Op: Rajesh • 24 loadings • 8h 20m", Icons.Outlined.PrecisionManufacturing, SvbSuccess, ReportGoodIconBg, "GOOD", StatusType.GOOD),
            MachineReportEntry("TATA-019 — Tipper", "Dr: Suresh • 12 trips • 7h 45m", Icons.Outlined.LocalShipping, SvbSuccess, ReportGoodIconBg, "GOOD", StatusType.GOOD),
            MachineReportEntry("EXC-12 — Excavator", "Op: Mohan • 18 loadings • 6h 50m", Icons.Outlined.PrecisionManufacturing, SvbPrimary1, ReportLowPerfIconBg, "LOW PERF", StatusType.LOW_PERF),
            MachineReportEntry("TATA-024 — Tipper", "Dr: Vikram • 10 trips • 7h 20m", Icons.Outlined.LocalShipping, SvbSuccess, ReportGoodIconBg, "GOOD", StatusType.GOOD),
            MachineReportEntry("JCB-03 — Excavator", "Op: Anil • 13 loadings • Battery Issue", Icons.Outlined.PrecisionManufacturing, SvbDanger, ReportIssueIconBg, "ISSUE", StatusType.ISSUE),
        )
    }

    val tripLogs = remember {
        listOf(
            TripLogReportEntry("Trip #5 — TATA-019", "2:15 PM • 10 KMS • Dr Suresh", Icons.Outlined.LocalShipping, SvbSuccess, "VERIFIED", true),
            TripLogReportEntry("Trip #4 — TATA-019", "1:30 PM • 10 KMS • Dr Suresh", Icons.Outlined.LocalShipping, SvbSuccess, "VERIFIED", true),
            TripLogReportEntry("Trip #3 — TATA-024", "12:00 PM • 12 KMS • Dr Vikram", Icons.Outlined.LocalShipping, SvbSuccess, "VERIFIED", true),
            TripLogReportEntry("Trip #2 — TATA-024", "11:00 AM • 10 KMS • Dr Vikram", Icons.Outlined.LocalShipping, SvbPrimary1, "FLAGGED", false),
            TripLogReportEntry("Trip #1 — TATA-019", "10:00 AM • 8 KMS • Dr Suresh", Icons.Outlined.LocalShipping, SvbSuccess, "VERIFIED", true),
        )
    }

    BackHandler {
        navController.popRoleHomeWithHomeTabSelected()
    }

    Scaffold(
        containerColor = SvbLoginBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Team Reports",
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
                    IconButton(onClick = { /* Handle Notifications */ }) {
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
                selectedIndex = reportsIdx,
                onSelect = { index ->
                    when {
                        index == reportsIdx -> Unit
                        index == profileIdx ->
                            navController.navigate(MainRoutes.profile(role)) { launchSingleTop = true }
                        index == fuelIdx ->
                            navController.navigate(MainRoutes.fuel(role)) { launchSingleTop = true }
                        index == verifyIdx ->
                            navController.navigate(MainRoutes.verifyStartDuty(role)) { launchSingleTop = true }
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ReportFilterChip("Today", selectedTimeFilter == 0) { selectedTimeFilter = 0 }
                ReportFilterChip("This Week", selectedTimeFilter == 1) { selectedTimeFilter = 1 }
                ReportFilterChip("This Month", selectedTimeFilter == 2) { selectedTimeFilter = 2 }
            }
            
            Spacer(Modifier.height(20.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ReportStatCard(
                        title = "TOTAL TRIPS",
                        value = "32",
                        subtitle = "+12% vs yesterday",
                        isPositiveIndicator = true,
                        modifier = Modifier.weight(1f),
                    )
                    ReportStatCard(
                        title = "TOTAL LOADINGS",
                        value = "87",
                        subtitle = "+8% vs yesterday",
                        isPositiveIndicator = true,
                        modifier = Modifier.weight(1f),
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ReportStatCard(
                        title = "ACTIVE HOURS",
                        value = "38h",
                        subtitle = "Across 5 machines",
                        isPositiveIndicator = false,
                        modifier = Modifier.weight(1f),
                    )
                    ReportStatCard(
                        title = "OPEN ISSUES",
                        value = "1",
                        subtitle = "Needs attention",
                        isPositiveIndicator = false,
                        isWarning = true,
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            SectionTitle("PER MACHINE")
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = HomeCardShape,
                colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Column(Modifier.fillMaxWidth()) {
                    machines.forEachIndexed { i, entry ->
                        MachineReportRow(entry)
                        if (i < machines.lastIndex) {
                            HorizontalDivider(color = SvbDivider, thickness = 1.dp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            SectionTitle("OPEN ISSUES")
            
            CriticalIssueCard()

            Spacer(Modifier.height(24.dp))
            SectionTitle("TRIP LOG")

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = HomeCardShape,
                colors = CardDefaults.cardColors(containerColor = SvbWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Column(Modifier.fillMaxWidth()) {
                    tripLogs.forEachIndexed { i, entry ->
                        TripLogReportRow(entry)
                        if (i < tripLogs.lastIndex) {
                            HorizontalDivider(color = SvbN7, thickness = 1.dp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            SvbPrimaryButton(
                text = "Export Report",
                onClick = { /* Trigger Download */ },
                leadingIcon = Icons.Outlined.FileDownload,
            )
        }
    }
}

@Composable
private fun ReportFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(999.dp),
        color = if (selected) SvbPrimary5 else SvbN7,
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) SvbPrimary1 else SvbN2,
        )
    }
}

@Composable
private fun ReportStatCard(
    title: String,
    value: String,
    subtitle: String,
    isPositiveIndicator: Boolean,
    modifier: Modifier = Modifier,
    isWarning: Boolean = false,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.ExtraBold,
                color = SvbN2,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = if (isWarning) SvbPrimary1 else SvbBlack,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = if (isPositiveIndicator) SvbSuccess else if (isWarning) SvbPrimary1 else SvbN2,
            )
        }
    }
}

@Composable
private fun MachineReportRow(entry: MachineReportEntry) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = { /* later: machine detail */ })
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(HomeIconTileShape)
                .background(entry.iconBg),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = entry.icon,
                contentDescription = null,
                tint = entry.iconTint,
                modifier = Modifier.size(24.dp),
            )
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = entry.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SvbBlack,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = entry.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = SvbN2,
            )
        }
        
        if (entry.statusType == StatusType.GOOD) {
            Text(
                text = entry.statusText,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = SvbSuccess,
            )
        } else {
            val (bgColor, textColor) = if (entry.statusType == StatusType.LOW_PERF) {
                SvbPrimary5 to SvbPrimary1
            } else {
                SvbRoseTint to SvbDanger
            }
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = bgColor,
            ) {
                Text(
                    text = entry.statusText,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                )
            }
        }
    }
}

@Composable
private fun CriticalIssueCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbRoseTint),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = null,
                    tint = SvbDanger,
                    modifier = Modifier.size(24.dp),
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        text = "JCB-03 — Battery dead",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "Reported 1h ago by Op Anil",
                        style = MaterialTheme.typography.bodySmall,
                        color = SvbN2,
                    )
                }
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = MajorIssueBadgeBg,
                ) {
                    Text(
                        text = "MAJOR",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = SvbDanger,
                    )
                }
            }
            Spacer(Modifier.height(14.dp))
            SvbOutlinedButton(
                text = "View Details",
                onClick = {},
            )
        }
    }
}

@Composable
private fun TripLogReportRow(entry: TripLogReportEntry) {
    val bgAlpha = if (entry.isVerified) 0.15f else 0.25f
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = { /* later: trip log detail */ })
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(HomeIconTileShape)
                .background(entry.iconTint.copy(alpha = bgAlpha)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = entry.icon,
                contentDescription = null,
                tint = entry.iconTint,
                modifier = Modifier.size(24.dp),
            )
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = entry.tripId,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SvbBlack,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = entry.timeAndDistance,
                style = MaterialTheme.typography.bodySmall,
                color = SvbN2,
            )
        }
        
        if (entry.isVerified) {
            Text(
                text = entry.statusText,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = SvbSuccess,
            )
        } else {
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = SvbPrimary5,
            ) {
                Text(
                    text = entry.statusText,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = SvbPrimary1,
                )
            }
        }
    }
}