package com.svb.fieldops.presentation.screens.verify

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.svb.fieldops.domain.model.UserRole
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
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbCardMuted
import com.svb.fieldops.ui.theme.SvbDanger
import com.svb.fieldops.ui.theme.SvbDivider
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN2
import com.svb.fieldops.ui.theme.SvbN3
import com.svb.fieldops.ui.theme.SvbN5
import com.svb.fieldops.ui.theme.SvbN7
import com.svb.fieldops.ui.theme.SvbPrimary2
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite

private const val TotalMachines = 5

private sealed class MachineVerifyState {
    data object Verified : MachineVerifyState()
    data object Scan : MachineVerifyState()
    data object Pending : MachineVerifyState()
}

private data class VerifyMachineLine(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupervisorVerifyStartDutyScreen(
    role: UserRole,
    navController: NavHostController,
) {
    val items = bottomNavItemsForRole(role)
    val verifyIdx = requireNotNull(verifyTabIndex(role)) { "SupervisorVerifyStartDutyScreen is Supervisor-only." }
    val reportsIdx = requireNotNull(reportsTabIndex(role)) { "Supervisor has Reports tab." }
    val fuelIdx = requireNotNull(fuelTabIndex(role)) { "Supervisor has Fuel tab." }
    val profileIdx = profileTabIndex(role)
    val scroll = rememberScrollState()

    val machineLines = remember {
        listOf(
            VerifyMachineLine(
                title = "JCB-07 — Excavator",
                subtitle = "Op: Rajesh Kumar",
                icon = Icons.Outlined.PrecisionManufacturing,
            ),
            VerifyMachineLine(
                title = "TATA-019 — Tipper",
                subtitle = "Dr: Suresh Yadav",
                icon = Icons.Outlined.LocalShipping,
            ),
            VerifyMachineLine(
                title = "HYD-03 — Mixer",
                subtitle = "Op: Vikram Singh",
                icon = Icons.Outlined.PrecisionManufacturing,
            ),
            VerifyMachineLine(
                title = "CAT-412 — Excavator",
                subtitle = "Op: Amit Patel",
                icon = Icons.Outlined.PrecisionManufacturing,
            ),
            VerifyMachineLine(
                title = "CMD-09 — Crane",
                subtitle = "Dr: Ravi Nair",
                icon = Icons.Outlined.LocalShipping,
            ),
        )
    }

    var rowStates by remember {
        mutableStateOf(
            listOf(
                MachineVerifyState.Verified,
                MachineVerifyState.Verified,
                MachineVerifyState.Scan,
                MachineVerifyState.Pending,
                MachineVerifyState.Pending,
            ),
        )
    }

    val verifiedCount = rowStates.count { it is MachineVerifyState.Verified }
    val allVerified = verifiedCount >= TotalMachines
    val remaining = (TotalMachines - verifiedCount).coerceAtLeast(0)

    BackHandler {
        navController.popRoleHomeWithHomeTabSelected()
    }

    Scaffold(
        containerColor = SvbLoginBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Verify Start Duty",
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
                selectedIndex = verifyIdx,
                onSelect = { index ->
                    when {
                        index == verifyIdx -> Unit
                        index == profileIdx ->
                            navController.navigate(MainRoutes.profile(role)) { launchSingleTop = true }
                        index == fuelIdx ->
                            navController.navigate(MainRoutes.fuel(role)) { launchSingleTop = true }
                        index == reportsIdx ->
                            navController.navigate(MainRoutes.reports(role)) { launchSingleTop = true }
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
                .padding(top = 8.dp, bottom = 20.dp),
        ) {
            Text(
                text = "Verify $TotalMachines machines",
                style = MaterialTheme.typography.bodySmall,
                color = SvbN2,
            )
            Spacer(Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = HomeCardShape,
                colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text(
                        text = "VERIFICATION PROGRESS",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = SvbN2,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "$verifiedCount of $TotalMachines verified",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                    Spacer(Modifier.height(14.dp))
                    VerifyProgressBar(progress = verifiedCount.toFloat() / TotalMachines.toFloat())
                }
            }
            Spacer(Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = HomeCardShape,
                colors = CardDefaults.cardColors(containerColor = SvbN7),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Column(Modifier.fillMaxWidth()) {
                    machineLines.forEachIndexed { i, line ->
                        VerifyMachineListRow(
                            line = line,
                            state = rowStates[i],
                            onVerifyClick = {
                                rowStates = rowStates.mapIndexed { idx, s ->
                                    if (idx == i && (s is MachineVerifyState.Scan || s is MachineVerifyState.Pending)) {
                                        MachineVerifyState.Verified
                                    } else {
                                        s
                                    }
                                }
                            },
                        )
                        if (i < machineLines.lastIndex) {
                            HorizontalDivider(color = SvbDivider, thickness = 1.dp)
                        }
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = { /* later: continue */ },
                enabled = allVerified,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (allVerified) 1f else 0.48f),
                shape = HomeCardShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SvbPrimary2,
                    contentColor = SvbBlack,
                    disabledContainerColor = SvbN7,
                    disabledContentColor = SvbN3,
                ),
                contentPadding = PaddingValues(vertical = 16.dp),
            ) {
                Text(
                    text = "All $TotalMachines Verified — Continue",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = if (allVerified) "Ready to continue" else "$remaining more to verify",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodySmall,
                color = SvbN2,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun VerifyProgressBar(progress: Float) {
    val p = progress.coerceIn(0f, 1f)
    val barHeight = 10.dp
    val shape = RoundedCornerShape(5.dp)
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(barHeight)
            .clip(shape)
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
private fun VerifyMachineListRow(
    line: VerifyMachineLine,
    state: MachineVerifyState,
    onVerifyClick: () -> Unit,
) {
    val verified = state is MachineVerifyState.Verified
    val iconTint = if (verified) SvbSuccess else SvbBlack

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Verified: soft green tile. Not verified: no fill / no clip so list (#F5F5F5) shows through.
        val iconBoxModifier = if (verified) {
            Modifier
                .size(48.dp)
                .clip(HomeIconTileShape)
                .background(SvbSuccess.copy(alpha = 0.16f))
        } else {
            Modifier.size(48.dp)
        }
        Box(
            modifier = iconBoxModifier,
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = line.icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(26.dp),
            )
        }
        Spacer(Modifier.width(14.dp))
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
        when (state) {
            MachineVerifyState.Verified -> {
                Text(
                    text = "VERIFIED",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = SvbSuccess,
                )
            }
            MachineVerifyState.Scan -> {
                OutlinedButton(
                    onClick = onVerifyClick,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, SvbN5),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = SvbBlack,
                        containerColor = Color.Transparent,
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                ) {
                    Icon(
                        Icons.Outlined.QrCode2,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Scan",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            MachineVerifyState.Pending -> {
                Surface(
                    modifier = Modifier.clickable(onClick = onVerifyClick),
                    shape = RoundedCornerShape(999.dp),
                    color = SvbCardMuted,
                ) {
                    Text(
                        text = "PENDING",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = SvbN2,
                    )
                }
            }
        }
    }
}
