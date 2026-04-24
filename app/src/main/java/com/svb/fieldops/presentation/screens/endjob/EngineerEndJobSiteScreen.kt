package com.svb.fieldops.presentation.screens.endjob

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PrecisionManufacturing
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite

private val SiteOutputSectionBg = Color(0xFFFFF4E5)
private val SuccessChipBg = Color(0xFFE8F5E9)
private val SuccessIconTileBg = Color(0xFFE8F5E9)
private val MiniStatCardShape = RoundedCornerShape(14.dp)
private val StatusPillShape = RoundedCornerShape(percent = 50)

private data class SiteMachineLine(
    val id: String,
    val subtitle: String,
    val machineIcon: ImageVector,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EngineerEndJobSiteScreen(
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

    val machines = remember {
        listOf(
            SiteMachineLine("JCB-07", "Op Rajesh • HM 4330", Icons.Outlined.PrecisionManufacturing),
            SiteMachineLine("TATA-019", "Dr Suresh • ODO 4520", Icons.Outlined.LocalShipping),
            SiteMachineLine("EXC-12", "Op Mohan • HM 5120", Icons.Outlined.PrecisionManufacturing),
            SiteMachineLine("JCB-03", "Op Anil • HM 4012", Icons.Outlined.PrecisionManufacturing),
            SiteMachineLine("HYD-01", "Op Ravi • HM 3890", Icons.Outlined.PrecisionManufacturing),
        )
    }

    BackHandler { navController.popRoleHomeWithHomeTabSelected() }

    Scaffold(
        containerColor = SvbLoginBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column(Modifier.fillMaxWidth()) {
                        Text(
                            text = "End Job — Site Engineer",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SvbBlack,
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "Site-wide verification — close the entire site",
                            style = MaterialTheme.typography.bodySmall,
                            color = SvbN2,
                        )
                    }
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
            TodaysSiteOutputCard()
            Spacer(Modifier.height(20.dp))

            EndJobSectionTitle("PENDING CHECKS")
            Spacer(Modifier.height(10.dp))
            PendingChecksCard()
            Spacer(Modifier.height(20.dp))

            EndJobSectionTitle("SITE MACHINES")
            Spacer(Modifier.height(10.dp))
            SiteMachinesCard(machines)
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { /* later: submit DPR & end site */ },
                modifier = Modifier.fillMaxWidth(),
                shape = HomeCardShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SvbSuccess,
                    contentColor = SvbWhite,
                ),
                contentPadding = PaddingValues(vertical = 16.dp),
            ) {
                Icon(
                    Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "All Verified — Submit DPR & End Site Day",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun EndJobSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = SvbN2,
    )
}

@Composable
private fun TodaysSiteOutputCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SiteOutputSectionBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "TODAY'S SITE OUTPUT",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = SvbN2,
            )
            Spacer(Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                MiniStatCell("5", "Total Machines", Modifier.weight(1f))
                MiniStatCellVerifiedStops(Modifier.weight(1f))
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                MiniStatCell("38h", "Site Hours", Modifier.weight(1f))
                MiniStatCell("180", "Output (CUM)", Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun MiniStatCell(value: String, label: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MiniStatCardShape,
        color = SvbWhite,
        shadowElevation = 0.dp,
    ) {
        Column(
            Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = SvbBlack,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = SvbN2,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun MiniStatCellVerifiedStops(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MiniStatCardShape,
        color = SvbWhite,
        shadowElevation = 0.dp,
    ) {
        Column(
            Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "5/5",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = SvbSuccess,
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = SvbSuccess,
                    modifier = Modifier.size(22.dp),
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Verified Stops",
                style = MaterialTheme.typography.bodySmall,
                color = SvbN2,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun PendingChecksCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column {
            PendingCheckRow(
                icon = Icons.Outlined.Description,
                title = "DPR Status",
                subtitle = "Submitted to QS",
                chipLabel = "DONE",
            )
            HorizontalDivider(color = SvbN5, thickness = 1.dp)
            PendingCheckRow(
                icon = Icons.Outlined.CheckCircle,
                title = "Open Approvals",
                subtitle = "All clear — nothing pending",
                chipLabel = "0 OPEN",
            )
            HorizontalDivider(color = SvbN5, thickness = 1.dp)
            PendingCheckRow(
                icon = Icons.Outlined.Build,
                title = "Open Breakdowns",
                subtitle = "No machines down",
                chipLabel = "0 OPEN",
            )
        }
    }
}

@Composable
private fun PendingCheckRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    chipLabel: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SuccessIconTileBg,
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = SvbSuccess,
                modifier = Modifier.padding(10.dp).size(22.dp),
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SvbBlack,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = SvbN2,
            )
        }
        Surface(
            shape = StatusPillShape,
            color = SuccessChipBg,
        ) {
            Text(
                text = chipLabel,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = SvbSuccess,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            )
        }
    }
}

@Composable
private fun SiteMachinesCard(lines: List<SiteMachineLine>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column {
            lines.forEachIndexed { index, line ->
                if (index > 0) {
                    HorizontalDivider(color = SvbN5, thickness = 1.dp)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(
                        shape = CircleShape,
                        color = SuccessIconTileBg,
                    ) {
                        Icon(
                            line.machineIcon,
                            contentDescription = null,
                            tint = SvbSuccess,
                            modifier = Modifier.padding(10.dp).size(22.dp),
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = line.id,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = SvbBlack,
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = line.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = SvbN2,
                        )
                    }
                    Icon(
                        Icons.Outlined.CheckCircle,
                        contentDescription = "Verified",
                        tint = SvbSuccess,
                        modifier = Modifier.size(26.dp),
                    )
                }
            }
        }
    }
}
