package com.svb.fieldops.presentation.screens.driver

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PlayArrow
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.svb.fieldops.domain.model.UserRole
import com.svb.fieldops.presentation.navigation.MainRoutes
import com.svb.fieldops.presentation.navigation.bottomNavItemsForRole
import com.svb.fieldops.presentation.navigation.fuelTabIndex
import com.svb.fieldops.presentation.navigation.loadingsTabIndex
import com.svb.fieldops.presentation.navigation.popRoleHomeWithHomeTabSelected
import com.svb.fieldops.presentation.navigation.profileTabIndex
import com.svb.fieldops.presentation.navigation.tripsTabIndex
import com.svb.fieldops.presentation.screens.home.HomeCardShape
import com.svb.fieldops.presentation.screens.home.HomeRoleNavigationBar
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbCardMuted
import com.svb.fieldops.ui.theme.SvbDivider
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN2
import com.svb.fieldops.ui.theme.SvbN3
import com.svb.fieldops.ui.theme.SvbN5
import com.svb.fieldops.ui.theme.SvbN7
import com.svb.fieldops.ui.theme.SvbPrimary2
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite

private const val MachineTitle = "TATA 2518"
private const val MachineIdLabel = "#SVB-T019"
private const val LastReadingKms = 45_180

/** Teal for outlined verify badge + “Machine verified” (mock ~#26A69A). */
private val MachineVerifiedTeal = Color(0xFF26A69A)
private val MachineVerifiedBannerBg = Color(0xFFE0F2F1)
private val InfoBannerBg = Color(0xFFFFF8E1)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverStartJobScreen(
    role: UserRole,
    navController: NavHostController,
) {
    val items = bottomNavItemsForRole(role)
    val homeIdx = 0
    val fuelIdx = requireNotNull(fuelTabIndex(role))
    val profileIdx = profileTabIndex(role)
    val scroll = rememberScrollState()

    var step by rememberSaveable { mutableStateOf(1) }
    var odometerDigits by rememberSaveable { mutableStateOf("45230") }
    var odometerPhotoCaptured by rememberSaveable { mutableStateOf(false) }

    fun goBack() {
        when (step) {
            1 -> navController.popRoleHomeWithHomeTabSelected()
            else -> step--
        }
    }

    BackHandler(onBack = { goBack() })

    Scaffold(
        containerColor = SvbLoginBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Start Job",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = SvbBlack,
                        )
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
                    when (role) {
                        UserRole.Driver -> {
                            val tripsIdx = requireNotNull(tripsTabIndex(role))
                            when {
                                index == homeIdx -> Unit
                                index == tripsIdx ->
                                    navController.navigate(MainRoutes.trips(role)) { launchSingleTop = true }
                                index == fuelIdx ->
                                    navController.navigate(MainRoutes.fuel(role)) { launchSingleTop = true }
                                index == profileIdx ->
                                    navController.navigate(MainRoutes.profile(role)) { launchSingleTop = true }
                                else -> navController.popRoleHomeWithHomeTabSelected()
                            }
                        }
                        UserRole.Operator -> {
                            val loadingsIdx = requireNotNull(loadingsTabIndex(role))
                            when {
                                index == homeIdx -> Unit
                                index == loadingsIdx ->
                                    navController.navigate(MainRoutes.loadings(role)) { launchSingleTop = true }
                                index == fuelIdx ->
                                    navController.navigate(MainRoutes.fuel(role)) { launchSingleTop = true }
                                index == profileIdx ->
                                    navController.navigate(MainRoutes.profile(role)) { launchSingleTop = true }
                                else -> navController.popRoleHomeWithHomeTabSelected()
                            }
                        }
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
            DriverJobThreeStepStepper(currentStep = step)
            Spacer(Modifier.height(20.dp))
            when (step) {
                1 -> StepScanMachineQr(onNext = { step = 2 })
                2 -> StepOdometerReading(
                    odometerDigits = odometerDigits,
                    onOdometerChange = { odometerDigits = it.filter { ch -> ch.isDigit() }.take(8) },
                    odometerPhotoCaptured = odometerPhotoCaptured,
                    onCapturePhoto = { odometerPhotoCaptured = true },
                    onBack = { step = 1 },
                    onNext = { step = 3 },
                )
                3 -> StepConfirmStartDuty(
                    odometerDigits = odometerDigits,
                    odometerPhotoCaptured = odometerPhotoCaptured,
                    onBack = { step = 2 },
                    onStartDuty = { navController.popRoleHomeWithHomeTabSelected() },
                )
            }
        }
    }
}

@Composable
private fun StepScanMachineQr(onNext: () -> Unit) {
    Text(
        text = "Scan Machine QR",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = SvbBlack,
    )
    Spacer(Modifier.height(6.dp))
    Text(
        text = "Scan the QR code on your assigned machine.",
        style = MaterialTheme.typography.bodyMedium,
        color = SvbN2,
    )
    Spacer(Modifier.height(18.dp))
    DriverJobQrScanViewfinder(Modifier.fillMaxWidth())
    Spacer(Modifier.height(16.dp))
    Surface(
        shape = HomeCardShape,
        color = MachineVerifiedBannerBg,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DriverJobOutlinedCheckBadge(
                modifier = Modifier.size(20.dp),
                tint = MachineVerifiedTeal,
            )
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = "$MachineTitle — $MachineIdLabel",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SvbBlack,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Machine verified",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MachineVerifiedTeal,
                )
            }
        }
    }
    Spacer(Modifier.height(20.dp))
    Button(
        onClick = onNext,
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = SvbPrimary2,
            contentColor = SvbBlack,
        ),
        contentPadding = PaddingValues(vertical = 16.dp),
    ) {
        Text(
            text = "Next: Odometer Reading",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.width(8.dp))
        Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
    }
}

@Composable
private fun StepOdometerReading(
    odometerDigits: String,
    onOdometerChange: (String) -> Unit,
    odometerPhotoCaptured: Boolean,
    onCapturePhoto: () -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit,
) {
    Text(
        text = "Odometer Reading",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = SvbBlack,
    )
    Spacer(Modifier.height(6.dp))
    Text(
        text = "Take a photo of the odometer and enter the reading",
        style = MaterialTheme.typography.bodyMedium,
        color = SvbN2,
    )
    Spacer(Modifier.height(18.dp))
    DriverJobOdometerPhotoPlaceholder(
        captured = odometerPhotoCaptured,
        onClick = onCapturePhoto,
    )
    Spacer(Modifier.height(18.dp))
    Text(
        text = "Odometer Reading (KMS)",
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        color = SvbN2,
    )
    Spacer(Modifier.height(8.dp))
    OutlinedTextField(
        value = odometerDigits,
        onValueChange = onOdometerChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = SvbN7,
            unfocusedContainerColor = SvbN7,
            focusedBorderColor = SvbN5,
            unfocusedBorderColor = SvbN5,
        ),
        textStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
    )
    Spacer(Modifier.height(14.dp))
    Surface(
        shape = HomeCardShape,
        color = InfoBannerBg,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Outlined.Info,
                contentDescription = null,
                tint = SvbPrimary2,
                modifier = Modifier.size(22.dp),
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "Last reading: ${driverJobFormatKms(LastReadingKms.toString())} KMS",
                style = MaterialTheme.typography.bodyMedium,
                color = SvbBlack,
            )
        }
    }
    Spacer(Modifier.height(22.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.weight(0.38f),
            shape = HomeCardShape,
            border = BorderStroke(1.dp, SvbN5),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = SvbBlack),
        ) {
            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text("Back", fontWeight = FontWeight.Bold)
        }
        Button(
            onClick = onNext,
            modifier = Modifier.weight(0.62f),
            enabled = odometerDigits.isNotEmpty() && odometerPhotoCaptured,
            shape = HomeCardShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = SvbPrimary2,
                contentColor = SvbBlack,
                disabledContainerColor = SvbN5,
                disabledContentColor = SvbN3,
            ),
        ) {
            Text("Next: Confirm", fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(6.dp))
            Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
        }
    }
}

@Composable
private fun StepConfirmStartDuty(
    odometerDigits: String,
    odometerPhotoCaptured: Boolean,
    onBack: () -> Unit,
    onStartDuty: () -> Unit,
) {
    Text(
        text = "Confirm & Start Duty",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = SvbBlack,
    )
    Spacer(Modifier.height(6.dp))
    Text(
        text = "Review your details before starting",
        style = MaterialTheme.typography.bodyMedium,
        color = SvbN2,
    )
    Spacer(Modifier.height(16.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            SummaryRow(label = "Machine", value = MachineTitle)
            HorizontalDivider(color = SvbDivider)
            SummaryRow(label = "Machine ID", value = MachineIdLabel)
            HorizontalDivider(color = SvbDivider)
            SummaryRow(label = "Start Odometer", value = "${driverJobFormatKms(odometerDigits)} KMS")
            HorizontalDivider(color = SvbDivider)
            OdometerPhotoSummaryRow(captured = odometerPhotoCaptured)
            HorizontalDivider(color = SvbDivider)
            SummaryRow(label = "Time", value = "09:41 AM")
            HorizontalDivider(color = SvbDivider)
            SummaryRow(label = "Location", value = "Zone A, SVB Site")
        }
    }
    Spacer(Modifier.height(22.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.weight(0.35f),
            shape = HomeCardShape,
            border = BorderStroke(1.dp, SvbN5),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = SvbBlack),
        ) {
            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text("Back", fontWeight = FontWeight.Bold)
        }
        Button(
            onClick = onStartDuty,
            modifier = Modifier.weight(0.65f),
            enabled = odometerDigits.isNotEmpty() && odometerPhotoCaptured,
            shape = HomeCardShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = SvbSuccess,
                contentColor = SvbWhite,
                disabledContainerColor = SvbN5,
                disabledContentColor = SvbN3,
            ),
        ) {
            Icon(Icons.Outlined.PlayArrow, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Start Duty", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        Modifier
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
private fun OdometerPhotoSummaryRow(captured: Boolean) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Odometer Photo",
            style = MaterialTheme.typography.bodyMedium,
            color = SvbN2,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (captured) {
                DriverJobOutlinedCheckBadge(Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
            }
            Text(
                text = if (captured) "Captured" else "Not captured",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = if (captured) SvbSuccess else SvbN2,
            )
        }
    }
}

