package com.svb.fieldops.presentation.screens.verify

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Notifications
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.svb.fieldops.domain.model.UserRole
import com.svb.fieldops.presentation.navigation.MainRoutes
import com.svb.fieldops.presentation.navigation.NavStateKeys
import com.svb.fieldops.presentation.navigation.bottomNavItemsForRole
import com.svb.fieldops.presentation.navigation.fuelTabIndex
import com.svb.fieldops.presentation.navigation.popRoleHomeWithHomeTabSelected
import com.svb.fieldops.presentation.navigation.popToRoleHomeWithHomeTab
import com.svb.fieldops.presentation.navigation.profileTabIndex
import com.svb.fieldops.presentation.navigation.reportsTabIndex
import com.svb.fieldops.presentation.navigation.verifyTabIndex
import com.svb.fieldops.presentation.screens.home.HomeCardShape
import com.svb.fieldops.presentation.screens.home.HomeRoleNavigationBar
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbDanger
import com.svb.fieldops.ui.theme.SvbDivider
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN2
import com.svb.fieldops.ui.theme.SvbN5
import com.svb.fieldops.ui.theme.SvbN7
import com.svb.fieldops.ui.theme.SvbPrimary2
import com.svb.fieldops.ui.theme.SvbPrimary5
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite

private val TripCardBg = Color(0xFFF1F8F4)
private val TripCardBorderGreen = SvbSuccess.copy(alpha = 0.45f)
private val WithinChipBg = Color(0xFFE8F5E9)
private val PitChipBg = Color(0xFFE0F2F1)
private val PitChipFg = Color(0xFF00897B)
private val PillShape = RoundedCornerShape(percent = 50)

private fun goToVerifyStart(navController: NavHostController, role: UserRole) {
    navController.navigate(MainRoutes.verifyStartDuty(role)) {
        popUpTo(MainRoutes.supervisor) { inclusive = false }
        launchSingleTop = true
    }
}

private fun NavHostController.leaveVerifyTripScreen(endJobMachineId: String?) {
    if (!endJobMachineId.isNullOrBlank()) {
        popBackStack()
    } else {
        popRoleHomeWithHomeTabSelected()
    }
}

private fun NavHostController.completeVerifyTripFromEndJob(machineId: String) {
    previousBackStackEntry?.savedStateHandle?.set(NavStateKeys.END_JOB_TRIP_VERIFIED_MACHINE_ID, machineId)
    popBackStack()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupervisorVerifyTripScreen(
    role: UserRole,
    navController: NavHostController,
    endJobMachineId: String? = null,
) {
    val fromEndJob = !endJobMachineId.isNullOrBlank()
    val items = bottomNavItemsForRole(role)
    val homeIdx = 0
    val verifyIdx = requireNotNull(verifyTabIndex(role)) { "Supervisor only." }
    val reportsIdx = requireNotNull(reportsTabIndex(role))
    val fuelIdx = requireNotNull(fuelTabIndex(role))
    val profileIdx = profileTabIndex(role)
    val scroll = rememberScrollState()
    var remarks by remember { mutableStateOf("") }

    BackHandler { navController.leaveVerifyTripScreen(endJobMachineId) }

    Scaffold(
        containerColor = SvbLoginBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Verify Trip",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.leaveVerifyTripScreen(endJobMachineId) }) {
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
                            if (fromEndJob) {
                                navController.popToRoleHomeWithHomeTab(role)
                            } else {
                                navController.popRoleHomeWithHomeTabSelected()
                            }
                        index == verifyIdx ->
                            navController.navigate(MainRoutes.verifyStartDuty(role)) { launchSingleTop = true }
                        index == reportsIdx ->
                            navController.navigate(MainRoutes.reports(role)) { launchSingleTop = true }
                        index == fuelIdx ->
                            navController.navigate(MainRoutes.fuel(role)) { launchSingleTop = true }
                        index == profileIdx ->
                            navController.navigate(MainRoutes.profile(role)) { launchSingleTop = true }
                        else ->
                            if (fromEndJob) {
                                navController.popToRoleHomeWithHomeTab(role)
                            } else {
                                navController.popRoleHomeWithHomeTabSelected()
                            }
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
            TripVerifyStepper()
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Confirm Trip Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SvbBlack,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Review and verify the scanned trip",
                style = MaterialTheme.typography.bodyMedium,
                color = SvbN2,
            )
            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = HomeCardShape,
                colors = CardDefaults.cardColors(containerColor = TripCardBg),
                border = BorderStroke(1.dp, TripCardBorderGreen),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = SvbSuccess,
                            modifier = Modifier.size(28.dp),
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "TATA-019 — Trip #5",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = SvbBlack,
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "Driver: Suresh Kumar",
                                style = MaterialTheme.typography.bodySmall,
                                color = SvbN2,
                            )
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                    HorizontalDivider(color = SvbDivider)
                    TripDetailValueRow(label = "Driver logged", value = "14:25 PM")
                    HorizontalDivider(color = SvbDivider)
                    TripDetailValueRow(label = "Your scan", value = "14:27 PM")
                    HorizontalDivider(color = SvbDivider)
                    TripDetailWindowRow()
                    HorizontalDivider(color = SvbDivider)
                    TripDetailLoadingRow()
                    HorizontalDivider(color = SvbDivider)
                    TripDetailValueRow(label = "Distance", value = "10 KMS")
                    Spacer(Modifier.height(14.dp))
                    Text(
                        text = "Trip is valid for verification",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = SvbSuccess,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            Text(
                text = "Remarks (optional)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SvbBlack,
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = remarks,
                onValueChange = { remarks = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Any notes...", color = SvbN2) },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SvbN7,
                    unfocusedContainerColor = SvbN7,
                    focusedBorderColor = SvbN5,
                    unfocusedBorderColor = SvbN5,
                ),
                minLines = 3,
            )
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = {
                        if (fromEndJob) {
                            navController.popBackStack()
                        } else {
                            goToVerifyStart(navController, role)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = HomeCardShape,
                    border = BorderStroke(1.dp, SvbDanger),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SvbDanger),
                ) {
                    Icon(Icons.Outlined.Close, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Reject", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = {
                        if (fromEndJob) {
                            navController.completeVerifyTripFromEndJob(requireNotNull(endJobMachineId))
                        } else {
                            goToVerifyStart(navController, role)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = HomeCardShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SvbSuccess,
                        contentColor = SvbWhite,
                    ),
                ) {
                    Icon(Icons.Outlined.Check, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Verify Trip", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun TripVerifyStepper() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(SvbSuccess),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Outlined.Check,
                    contentDescription = null,
                    tint = SvbWhite,
                    modifier = Modifier.size(18.dp),
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Scanned",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = SvbSuccess,
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .defaultMinSize(minHeight = 32.dp)
                .align(Alignment.CenterVertically),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .height(3.dp)
                    .background(SvbSuccess),
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(SvbPrimary2),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "2",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = SvbBlack,
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Confirm",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = SvbN2,
            )
        }
    }
}

@Composable
private fun TripDetailValueRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = SvbN2,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = SvbBlack,
        )
    }
}

@Composable
private fun TripDetailWindowRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "±3 min window",
            style = MaterialTheme.typography.bodyMedium,
            color = SvbN2,
        )
        Surface(shape = PillShape, color = WithinChipBg) {
            Row(
                Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Outlined.Check,
                    contentDescription = null,
                    tint = SvbSuccess,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Within",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = SvbSuccess,
                )
            }
        }
    }
}

@Composable
private fun TripDetailLoadingRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Loading point",
            style = MaterialTheme.typography.bodyMedium,
            color = SvbN2,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(shape = PillShape, color = SvbPrimary5) {
                Text(
                    text = "Zone A",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = SvbPrimary2,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                )
            }
            Text(
                text = "—",
                style = MaterialTheme.typography.bodyMedium,
                color = SvbN2,
            )
            Surface(shape = PillShape, color = PitChipBg) {
                Text(
                    text = "Pit 3",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = PitChipFg,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                )
            }
        }
    }
}
