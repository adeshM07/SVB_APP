package com.svb.fieldops.presentation.screens.endjob

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.outlined.QrCode2
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN2
import com.svb.fieldops.ui.theme.SvbN3
import com.svb.fieldops.ui.theme.SvbN5
import com.svb.fieldops.ui.theme.SvbN7
import com.svb.fieldops.ui.theme.SvbPrimary1
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite

private val PendingQrTileBg = Color(0xFFFFF3E0)
private val EndJobPrimaryGreen = Color(0xFF4CAF50)

private data class EndJobMachineLine(
    val id: String,
    val title: String,
    val subtitle: String,
    val verifiedIcon: ImageVector,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupervisorEndJobScreen(
    role: UserRole,
    navController: NavHostController,
) {
    val items = bottomNavItemsForRole(role)
    val homeIdx = 0
    val verifyIdx = requireNotNull(verifyTabIndex(role)) { "Supervisor only." }
    val reportsIdx = requireNotNull(reportsTabIndex(role))
    val fuelIdx = requireNotNull(fuelTabIndex(role))
    val profileIdx = profileTabIndex(role)
    val scroll = rememberScrollState()

    val machines = remember {
        listOf(
            EndJobMachineLine("m1", "JCB-07", "Op: Rajesh", Icons.Outlined.PrecisionManufacturing),
            EndJobMachineLine("m2", "TATA-019", "Dr: Suresh", Icons.Outlined.LocalShipping),
            EndJobMachineLine("m3", "EXC-12", "Op: Mohan", Icons.Outlined.PrecisionManufacturing),
            EndJobMachineLine("m4", "TATA-024", "Dr: Vikram • Pending", Icons.Outlined.LocalShipping),
            EndJobMachineLine("m5", "JCB-03", "Op: Anil • Pending", Icons.Outlined.PrecisionManufacturing),
        )
    }
    var verifiedIds by remember {
        mutableStateOf(setOf("m1", "m2", "m3"))
    }

    val endJobEntry = checkNotNull(navController.currentBackStackEntry)
    val tripVerifiedMachineId by endJobEntry.savedStateHandle
        .getStateFlow<String?>(NavStateKeys.END_JOB_TRIP_VERIFIED_MACHINE_ID, null)
        .collectAsStateWithLifecycle()

    LaunchedEffect(tripVerifiedMachineId) {
        val id = tripVerifiedMachineId ?: return@LaunchedEffect
        verifiedIds = verifiedIds + id
        endJobEntry.savedStateHandle.remove<String>(NavStateKeys.END_JOB_TRIP_VERIFIED_MACHINE_ID)
    }

    val total = machines.size
    val verifiedCount = machines.count { it.id in verifiedIds }
    val progress = verifiedCount.toFloat() / total.toFloat()

    BackHandler { navController.popRoleHomeWithHomeTabSelected() }

    Scaffold(
        containerColor = SvbLoginBackground,
        topBar = {
            TopAppBar(
                title = {
                    Column(Modifier.fillMaxWidth()) {
                        Text(
                            text = "End Job — Supervisor",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SvbBlack,
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "Verify all team machines",
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = HomeCardShape,
                colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "VERIFICATION REQUIRED",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = SvbN3,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "$verifiedCount of $total verified",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                    Spacer(Modifier.height(12.dp))
                    EndJobProgressBar(progress = progress)
                }
            }
            Spacer(Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = HomeCardShape,
                colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
            ) {
                Column(Modifier.fillMaxWidth()) {
                    machines.forEachIndexed { index, line ->
                        if (index > 0) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 14.dp),
                                color = SvbN5,
                            )
                        }
                        EndJobMachineRow(
                            line = line,
                            verified = line.id in verifiedIds,
                            onScan = {
                                navController.navigate(MainRoutes.verifyTripEndJob(role, line.id)) {
                                    launchSingleTop = true
                                }
                            },
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = HomeCardShape,
                colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "TEAM SUMMARY",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = SvbN3,
                    )
                    Spacer(Modifier.height(12.dp))
                    TeamSummaryRow("Total trips today", "32")
                    Spacer(Modifier.height(10.dp))
                    TeamSummaryRow("Total loadings", "87")
                    Spacer(Modifier.height(10.dp))
                    TeamSummaryRow("Active hours", "38h")
                    Spacer(Modifier.height(10.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Issues",
                            style = MaterialTheme.typography.bodySmall,
                            color = SvbN2,
                            modifier = Modifier.weight(1f),
                        )
                        Text(
                            text = "1 open",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = SvbPrimary1,
                        )
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
            val allVerified = verifiedCount >= total
            Button(
                onClick = { navController.popRoleHomeWithHomeTabSelected() },
                modifier = Modifier.fillMaxWidth(),
                enabled = allVerified,
                shape = HomeCardShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = EndJobPrimaryGreen,
                    contentColor = SvbWhite,
                    disabledContainerColor = SvbN5,
                    disabledContentColor = SvbN2,
                ),
                contentPadding = PaddingValues(vertical = 16.dp),
            ) {
                Icon(Icons.Outlined.CheckCircle, contentDescription = null, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Complete End Job & Clock Out",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Proceeds to shift end selfie",
                style = MaterialTheme.typography.bodySmall,
                color = SvbN2,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun EndJobProgressBar(progress: Float) {
    val p = progress.coerceIn(0f, 1f)
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(SvbN7),
    ) {
        if (p > 0f) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(p)
                    .background(SvbSuccess),
            )
        }
    }
}

@Composable
private fun EndJobMachineRow(
    line: EndJobMachineLine,
    verified: Boolean,
    onScan: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (verified) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(HomeIconTileShape)
                    .background(SvbSuccess.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = line.verifiedIcon,
                    contentDescription = null,
                    tint = SvbSuccess,
                    modifier = Modifier.size(26.dp),
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(HomeIconTileShape)
                    .background(PendingQrTileBg),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.QrCode2,
                    contentDescription = null,
                    tint = SvbPrimary1,
                    modifier = Modifier.size(26.dp),
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
            Spacer(Modifier.height(2.dp))
            Text(
                text = line.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = SvbN2,
            )
        }
        if (verified) {
            Text(
                text = "VERIFIED",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = SvbSuccess,
            )
        } else {
            OutlinedButton(
                onClick = onScan,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, SvbN5),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            ) {
                Text("Scan", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun TeamSummaryRow(label: String, value: String) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = SvbN2,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = SvbBlack,
        )
    }
}
