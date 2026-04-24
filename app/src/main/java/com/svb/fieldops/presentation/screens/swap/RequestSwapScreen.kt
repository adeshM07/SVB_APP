package com.svb.fieldops.presentation.screens.swap

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
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.PrecisionManufacturing
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.svb.fieldops.presentation.navigation.MainRoutes
import com.svb.fieldops.presentation.navigation.bottomNavItemsForRole
import com.svb.fieldops.presentation.navigation.fuelTabIndex
import com.svb.fieldops.presentation.navigation.loadingsTabIndex
import com.svb.fieldops.presentation.navigation.popRoleHomeWithHomeTabSelected
import com.svb.fieldops.presentation.navigation.profileTabIndex
import com.svb.fieldops.presentation.navigation.tripsTabIndex
import com.svb.fieldops.presentation.screens.driver.DriverJobOdometerPhotoPlaceholder
import com.svb.fieldops.presentation.screens.driver.DriverJobQrScanViewfinder
import com.svb.fieldops.presentation.screens.driver.DriverJobThreeStepStepper
import com.svb.fieldops.presentation.screens.driver.driverJobFormatKms
import com.svb.fieldops.presentation.screens.home.HomeCardShape
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
import com.svb.fieldops.ui.theme.SvbPrimary5
import com.svb.fieldops.ui.theme.SvbRoseTint
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite

private const val CurrentMachineTitle = "TATA 2518"
private const val CurrentMachineId = "#SVB-T019"
private const val NewMachineTitle = "JCB-11"
private const val NewMachineKind = "Excavator"
private const val NewMachineLastKms = 12_450

private val SwapReasonOptions = listOf(
    "Select reason...",
    "Machine Breakdown",
    "Hydraulic Leak",
    "Tyre Puncture",
    "Supervisor Reassignment",
    "Other",
)

private val MachineCardShape = RoundedCornerShape(14.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestSwapScreen(
    role: UserRole,
    navController: NavHostController,
) {
    val items = bottomNavItemsForRole(role)
    val homeIdx = 0
    val fuelIdx = requireNotNull(fuelTabIndex(role))
    val profileIdx = profileTabIndex(role)
    val scroll = rememberScrollState()

    var step by rememberSaveable { mutableIntStateOf(1) }
    var reasonIndex by rememberSaveable { mutableIntStateOf(0) }
    var swapNotes by rememberSaveable { mutableStateOf("") }
    var newOdometerDigits by rememberSaveable { mutableStateOf("12450") }
    var hsdLitresDigits by rememberSaveable { mutableStateOf("80") }
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
                        text = "Change Machine",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
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
                1 -> SwapStepScanAndReason(
                    reasonIndex = reasonIndex,
                    onReasonIndexChange = { reasonIndex = it },
                    swapNotes = swapNotes,
                    onSwapNotesChange = { swapNotes = it },
                    onNext = { step = 2 },
                )
                2 -> SwapStepNewMachineOdometer(
                    newOdometerDigits = newOdometerDigits,
                    onNewOdometerChange = { newOdometerDigits = it.filter { ch -> ch.isDigit() }.take(8) },
                    hsdLitresDigits = hsdLitresDigits,
                    onHsdLitresChange = { hsdLitresDigits = it.filter { ch -> ch.isDigit() }.take(4) },
                    odometerPhotoCaptured = odometerPhotoCaptured,
                    onCapturePhoto = { odometerPhotoCaptured = true },
                    onBack = { step = 1 },
                    onNext = { step = 3 },
                )
                3 -> SwapStepConfirm(
                    reasonLabel = SwapReasonOptions.getOrElse(reasonIndex) { SwapReasonOptions[1] },
                    newOdometerDigits = newOdometerDigits,
                    hsdLitresDigits = hsdLitresDigits,
                    onBack = { step = 2 },
                    onSubmit = { navController.popRoleHomeWithHomeTabSelected() },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwapStepScanAndReason(
    reasonIndex: Int,
    onReasonIndexChange: (Int) -> Unit,
    swapNotes: String,
    onSwapNotesChange: (String) -> Unit,
    onNext: () -> Unit,
) {
    Text(
        text = "Step 1: Scan New Machine",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = SvbBlack,
    )
    Spacer(Modifier.height(6.dp))
    Text(
        text = "Scan the QR code of the machine you want to switch to",
        style = MaterialTheme.typography.bodyMedium,
        color = SvbN2,
    )
    Spacer(Modifier.height(16.dp))
    CurrentMachineBanner()
    Spacer(Modifier.height(16.dp))
    DriverJobQrScanViewfinder(
        modifier = Modifier.fillMaxWidth(),
        hintBelowFrame = "Scan new machine QR",
    )
    Spacer(Modifier.height(20.dp))
    SwapReasonDropdown(
        selectedIndex = reasonIndex,
        onSelect = onReasonIndexChange,
    )
    Spacer(Modifier.height(18.dp))
    Text(
        text = "Additional Notes (optional)",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = SvbBlack,
    )
    Spacer(Modifier.height(8.dp))
    OutlinedTextField(
        value = swapNotes,
        onValueChange = onSwapNotesChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text("Describe the issue briefly...", color = SvbN2)
        },
        minLines = 3,
        shape = HomeCardShape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = SvbN7,
            unfocusedContainerColor = SvbN7,
            focusedBorderColor = SvbN5,
            unfocusedBorderColor = SvbN5,
        ),
    )
    Spacer(Modifier.height(22.dp))
    Button(
        onClick = onNext,
        modifier = Modifier.fillMaxWidth(),
        enabled = reasonIndex > 0,
        shape = HomeCardShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = SvbPrimary2,
            contentColor = SvbBlack,
            disabledContainerColor = SvbN5,
            disabledContentColor = SvbN3,
        ),
        contentPadding = PaddingValues(vertical = 16.dp),
    ) {
        Text("Next: Odometer", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(8.dp))
        Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
    }
}

@Composable
private fun CurrentMachineBanner() {
    Surface(
        shape = HomeCardShape,
        color = SvbRoseTint,
        border = BorderStroke(1.dp, SvbDanger.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
            Text(
                text = "CURRENT MACHINE",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = SvbN2,
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.LocalShipping,
                    contentDescription = null,
                    tint = SvbDanger,
                    modifier = Modifier.size(28.dp),
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "$CurrentMachineTitle — $CurrentMachineId",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SvbDanger,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwapReasonDropdown(
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Text(
        text = "Reason for Swap",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = SvbBlack,
    )
    Spacer(Modifier.height(8.dp))
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = SwapReasonOptions.getOrElse(selectedIndex) { SwapReasonOptions[0] },
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            placeholder = { Text("Select reason...", color = SvbN2) },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SvbN7,
                unfocusedContainerColor = SvbN7,
                focusedBorderColor = SvbPrimary2,
                unfocusedBorderColor = SvbN5,
            ),
            singleLine = true,
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            SwapReasonOptions.forEachIndexed { index, label ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        onSelect(index)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun SwapStepNewMachineOdometer(
    newOdometerDigits: String,
    onNewOdometerChange: (String) -> Unit,
    hsdLitresDigits: String,
    onHsdLitresChange: (String) -> Unit,
    odometerPhotoCaptured: Boolean,
    onCapturePhoto: () -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit,
) {
    Text(
        text = "Step 2: New Machine Odometer",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = SvbBlack,
    )
    Spacer(Modifier.height(6.dp))
    Text(
        text = "Capture the odometer reading of the new machine.",
        style = MaterialTheme.typography.bodyMedium,
        color = SvbN2,
    )
    Spacer(Modifier.height(16.dp))
    Surface(
        shape = HomeCardShape,
        color = Color(0xFFE8F5E9),
        border = BorderStroke(1.dp, SvbSuccess.copy(alpha = 0.45f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Outlined.PrecisionManufacturing,
                contentDescription = null,
                tint = SvbSuccess,
                modifier = Modifier.size(32.dp),
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = "New Machine: $NewMachineTitle",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SvbSuccess,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "$NewMachineKind • Last KMS: ${driverJobFormatKms(NewMachineLastKms.toString())}",
                    style = MaterialTheme.typography.bodySmall,
                    color = SvbN2,
                )
            }
        }
    }
    Spacer(Modifier.height(16.dp))
    DriverJobOdometerPhotoPlaceholder(
        captured = odometerPhotoCaptured,
        onClick = onCapturePhoto,
    )
    Spacer(Modifier.height(16.dp))
    Text(
        text = "New Machine Odometer (KMS)",
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        color = SvbN2,
    )
    Spacer(Modifier.height(8.dp))
    OutlinedTextField(
        value = newOdometerDigits,
        onValueChange = onNewOdometerChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("e.g. 12,450", color = SvbN2) },
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
    Text(
        text = "Current HSD Level (Litres)",
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        color = SvbN2,
    )
    Spacer(Modifier.height(8.dp))
    OutlinedTextField(
        value = hsdLitresDigits,
        onValueChange = onHsdLitresChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("e.g. 80", color = SvbN2) },
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
            Icon(
                Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(6.dp))
            Text("Back", fontWeight = FontWeight.Bold)
        }
        Button(
            onClick = onNext,
            modifier = Modifier.weight(0.62f),
            enabled = newOdometerDigits.isNotEmpty() && hsdLitresDigits.isNotEmpty() && odometerPhotoCaptured,
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
private fun SwapStepConfirm(
    reasonLabel: String,
    newOdometerDigits: String,
    hsdLitresDigits: String,
    onBack: () -> Unit,
    onSubmit: () -> Unit,
) {
    Text(
        text = "Step 3: Confirm Swap",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = SvbBlack,
    )
    Spacer(Modifier.height(6.dp))
    Text(
        text = "Review and submit your machine change request",
        style = MaterialTheme.typography.bodyMedium,
        color = SvbN2,
    )
    Spacer(Modifier.height(16.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MachineMiniCard(
            title = CurrentMachineTitle,
            foot = "current",
            borderColor = SvbDanger.copy(alpha = 0.5f),
            bg = SvbRoseTint,
            iconTint = SvbDanger,
            icon = Icons.Outlined.LocalShipping,
            modifier = Modifier.weight(1f),
        )
        Icon(
            Icons.AutoMirrored.Outlined.ArrowForward,
            contentDescription = null,
            tint = SvbPrimary2,
            modifier = Modifier.size(28.dp),
        )
        MachineMiniCard(
            title = NewMachineTitle,
            foot = "new",
            borderColor = SvbSuccess.copy(alpha = 0.55f),
            bg = Color(0xFFE8F5E9),
            iconTint = SvbSuccess,
            icon = Icons.Outlined.PrecisionManufacturing,
            modifier = Modifier.weight(1f),
        )
    }
    Spacer(Modifier.height(18.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            SwapSummaryRow("Reason", reasonLabel)
            HorizontalDivider(color = SvbDivider)
            SwapSummaryRow("New Machine", "$NewMachineTitle — $NewMachineKind")
            HorizontalDivider(color = SvbDivider)
            SwapSummaryRow("Odometer", "${driverJobFormatKms(newOdometerDigits)} KMS")
            HorizontalDivider(color = SvbDivider)
            SwapSummaryRow(
                "HSD Level",
                "${hsdLitresDigits.filter { it.isDigit() }.let { if (it.isEmpty()) "0" else it }} L",
            )
            HorizontalDivider(color = SvbDivider)
            SwapSummaryRow("Operator", "Continues on new machine")
        }
    }
    Spacer(Modifier.height(14.dp))
    Surface(
        shape = HomeCardShape,
        color = SvbPrimary5,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                Icons.Outlined.Info,
                contentDescription = null,
                tint = SvbPrimary2,
                modifier = Modifier.size(22.dp),
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "This swap request will be sent to the Site Engineer for approval. You will be notified once approved.",
                style = MaterialTheme.typography.bodySmall,
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
            modifier = Modifier.weight(0.35f),
            shape = HomeCardShape,
            border = BorderStroke(1.dp, SvbN5),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = SvbBlack),
        ) {
            Icon(
                Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(6.dp))
            Text("Back", fontWeight = FontWeight.Bold)
        }
        Button(
            onClick = onSubmit,
            modifier = Modifier.weight(0.65f),
            shape = HomeCardShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = SvbSuccess,
                contentColor = SvbWhite,
            ),
        ) {
            Icon(Icons.Outlined.SwapHoriz, contentDescription = null, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(8.dp))
            Text("Submit Swap Request", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun MachineMiniCard(
    title: String,
    foot: String,
    borderColor: Color,
    bg: Color,
    iconTint: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.clip(MachineCardShape),
        shape = MachineCardShape,
        color = bg,
        border = BorderStroke(1.dp, borderColor),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(28.dp))
            Spacer(Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = iconTint,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = foot,
                style = MaterialTheme.typography.labelSmall,
                color = SvbN2,
            )
        }
    }
}

@Composable
private fun SwapSummaryRow(label: String, value: String) {
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
