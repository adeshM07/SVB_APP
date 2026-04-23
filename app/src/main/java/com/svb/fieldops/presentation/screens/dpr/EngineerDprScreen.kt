package com.svb.fieldops.presentation.screens.dpr

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.ViewInAr
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import com.svb.fieldops.presentation.screens.home.HomeIconTileShape
import com.svb.fieldops.presentation.screens.home.HomeRoleNavigationBar
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbCardMuted
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN2
import com.svb.fieldops.ui.theme.SvbN3
import com.svb.fieldops.ui.theme.SvbN5
import com.svb.fieldops.ui.theme.SvbN7
import com.svb.fieldops.ui.theme.SvbPrimary2
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite

private val DprSummaryCardBg = Color(0xFF262626)
private val DprStepperLinePending = SvbN5
private val DprAutofillBannerBg = Color(0xFFE8F5E9)
private val DprLabelBlueGrey = Color(0xFF78909C)

private val StepLabels = listOf("Vendor", "Details", "Type", "Qty", "Submit")

private data class DprVendorOption(
    val initials: String,
    val name: String,
    val trade: String,
    val workOrders: String,
    val initialsBg: Color,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EngineerDprScreen(
    role: UserRole,
    navController: NavHostController,
) {
    val items = bottomNavItemsForRole(role)
    val dprIdx = requireNotNull(dprTabIndex(role)) { "Engineer DPR tab." }
    val dieselIdx = requireNotNull(dieselTabIndex(role))
    val approvalsIdx = requireNotNull(approvalsTabIndex(role))
    val profileIdx = profileTabIndex(role)
    val scroll = rememberScrollState()

    var currentStep by rememberSaveable { mutableIntStateOf(0) }
    var selectedVendorIndex by rememberSaveable { mutableIntStateOf(1) }
    var measurementTypeIndex by rememberSaveable { mutableIntStateOf(0) }
    var lengthM by rememberSaveable { mutableStateOf("10.0") }
    var breadthM by rememberSaveable { mutableStateOf("5.0") }
    var depthM by rememberSaveable { mutableStateOf("2.0") }
    var trips by rememberSaveable { mutableStateOf("8") }
    var avgQtyTrip by rememberSaveable { mutableStateOf("10") }
    var requestsText by rememberSaveable { mutableStateOf("") }
    var remarksText by rememberSaveable { mutableStateOf("") }

    val vendors = remember {
        listOf(
            DprVendorOption("AC", "ABC Construction", "Earthwork", "5 work orders", SvbN7),
            DprVendorOption("XY", "XYZ Builders", "Concrete", "3 work orders", SvbPrimary2),
            DprVendorOption("KI", "Kumar Infra", "Machine Hire", "2 work orders", SvbPrimary2),
        )
    }
    val selectedVendor = vendors[selectedVendorIndex.coerceIn(0, vendors.lastIndex)]

    val l = lengthM.toDoubleOrNull() ?: 0.0
    val b = breadthM.toDoubleOrNull() ?: 0.0
    val d = depthM.toDoubleOrNull() ?: 0.0
    val volumeCum = (l * b * d).toInt().coerceAtLeast(0)
    val tripsN = trips.toIntOrNull() ?: 0
    val avgN = avgQtyTrip.toIntOrNull() ?: 0
    val tripCum = tripsN * avgN
    val totalSubmitted = volumeCum + tripCum

    BackHandler {
        if (currentStep > 0) {
            currentStep -= 1
        } else {
            navController.popRoleHomeWithHomeTabSelected()
        }
    }

    Scaffold(
        containerColor = SvbLoginBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Daily Progress Report",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentStep > 0) {
                            currentStep -= 1
                        } else {
                            navController.popRoleHomeWithHomeTabSelected()
                        }
                    }) {
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
                selectedIndex = dprIdx,
                onSelect = { index ->
                    when {
                        index == dprIdx -> Unit
                        index == profileIdx ->
                            navController.navigate(MainRoutes.profile(role)) { launchSingleTop = true }
                        index == dieselIdx ->
                            navController.navigate(MainRoutes.diesel(role)) { launchSingleTop = true }
                        index == approvalsIdx ->
                            navController.navigate(MainRoutes.approvals(role)) { launchSingleTop = true }
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
            DprProgressStepper(currentStep = currentStep)
            Spacer(Modifier.height(20.dp))

            when (currentStep) {
                0 -> {
                    DprTodaysBanner()
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = "Select Vendor",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                    Spacer(Modifier.height(12.dp))
                    vendors.forEachIndexed { index, v ->
                        DprVendorCard(
                            vendor = v,
                            selected = index == selectedVendorIndex,
                            onClick = { selectedVendorIndex = index },
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { currentStep = 1 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SvbPrimary2,
                            contentColor = SvbBlack,
                        ),
                        shape = RoundedCornerShape(14.dp),
                    ) {
                        Text("Next: Confirm Details", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
                1 -> {
                    Text(
                        text = "Confirm Work Order Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                    Spacer(Modifier.height(14.dp))
                    DprDetailsCard(dprNoHighlight = "DPR-2026-0417-001")
                    Spacer(Modifier.height(12.dp))
                    DprAutofillBanner()
                    Spacer(Modifier.height(20.dp))
                    DprBackNextRow(
                        onBack = { currentStep = 0 },
                        onNext = { currentStep = 2 },
                        nextLabel = "Next: Measurement",
                        nextWeight = 2f,
                    )
                }
                2 -> {
                    Text(
                        text = "How is work measured?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                    Spacer(Modifier.height(14.dp))
                    DprMeasurementTypeCard(
                        title = "Volume (CUM)",
                        subtitle = "L × B × D",
                        selected = measurementTypeIndex == 0,
                        leadingIcon = { Icon(Icons.Outlined.ViewInAr, null, tint = if (measurementTypeIndex == 0) SvbPrimary2 else SvbN2, modifier = Modifier.size(28.dp)) },
                        onClick = { measurementTypeIndex = 0 },
                    )
                    Spacer(Modifier.height(10.dp))
                    DprMeasurementTypeCard(
                        title = "Running FT / Meters",
                        subtitle = "No's × Length = Total RTM",
                        selected = measurementTypeIndex == 1,
                        leadingIcon = {
                            Surface(shape = CircleShape, color = SvbN7, modifier = Modifier.size(44.dp)) {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Outlined.Straighten, null, tint = SvbN2, modifier = Modifier.size(24.dp))
                                }
                            }
                        },
                        onClick = { measurementTypeIndex = 1 },
                    )
                    Spacer(Modifier.height(10.dp))
                    DprMeasurementTypeCard(
                        title = "Machine Hire (Hours)",
                        subtitle = "Verified log based",
                        selected = measurementTypeIndex == 2,
                        leadingIcon = {
                            Surface(shape = CircleShape, color = SvbN7, modifier = Modifier.size(44.dp)) {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Outlined.Schedule, null, tint = SvbN2, modifier = Modifier.size(24.dp))
                                }
                            }
                        },
                        onClick = { measurementTypeIndex = 2 },
                    )
                    Spacer(Modifier.height(20.dp))
                    DprBackNextRow(
                        onBack = { currentStep = 1 },
                        onNext = { currentStep = 3 },
                        nextLabel = "Next: Enter Values",
                        nextWeight = 2f,
                    )
                }
                3 -> {
                    Text(
                        text = "Enter Measurements",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Manual input — all values in meters",
                        style = MaterialTheme.typography.bodySmall,
                        color = SvbN2,
                    )
                    Spacer(Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        DprDimField("Length (m)", lengthM, { lengthM = it }, Modifier.weight(1f))
                        Text("×", style = MaterialTheme.typography.bodyLarge, color = SvbN2, modifier = Modifier.padding(horizontal = 6.dp))
                        DprDimField("Breadth (m)", breadthM, { breadthM = it }, Modifier.weight(1f))
                        Text("×", style = MaterialTheme.typography.bodyLarge, color = SvbN2, modifier = Modifier.padding(horizontal = 6.dp))
                        DprDimField("Depth (m)", depthM, { depthM = it }, Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(16.dp))
                    DprVolumeResultCard(volumeCum = volumeCum)
                    Spacer(Modifier.height(14.dp))
                    DprPhotoPlaceholder()
                    Spacer(Modifier.height(20.dp))
                    DprBackNextRow(
                        onBack = { currentStep = 2 },
                        onNext = { currentStep = 4 },
                        nextLabel = "Next: Trips & Submit",
                        nextWeight = 2f,
                        nextFullWidth = true,
                    )
                }
                else -> {
                    Text(
                        text = "Trips, Requests & Submit",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                    Spacer(Modifier.height(14.dp))
                    DprTripQuantityCard(
                        trips = trips,
                        avgQty = avgQtyTrip,
                        resultCum = tripCum,
                        onTripsChange = { trips = it },
                        onAvgChange = { avgQtyTrip = it },
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Requests (material, equipment, etc.)",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = SvbBlack,
                    )
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = requestsText,
                        onValueChange = { requestsText = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("E.g. Need 2 dumpers tomorrow...", color = SvbN3) },
                        minLines = 3,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SvbWhite,
                            unfocusedContainerColor = SvbWhite,
                        ),
                    )
                    Spacer(Modifier.height(14.dp))
                    Text(
                        text = "Remarks",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = SvbBlack,
                    )
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = remarksText,
                        onValueChange = { remarksText = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Weather delays, observations...", color = SvbN3) },
                        minLines = 3,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SvbWhite,
                            unfocusedContainerColor = SvbWhite,
                        ),
                    )
                    Spacer(Modifier.height(16.dp))
                    DprFinalSummaryCard(
                        vendorName = selectedVendor.name,
                        workType = selectedVendor.trade,
                        qty1Cum = volumeCum,
                        qty2Cum = tripCum,
                        totalCum = totalSubmitted,
                    )
                    Spacer(Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        OutlinedButton(
                            onClick = { currentStep = 3 },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, SvbN5),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = SvbBlack),
                        ) {
                            Icon(Icons.AutoMirrored.Outlined.ArrowBack, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Back", fontWeight = FontWeight.SemiBold)
                        }
                        Button(
                            onClick = { /* later: submit to QS */ },
                            modifier = Modifier.weight(2f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SvbSuccess,
                                contentColor = SvbWhite,
                            ),
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Text("Submit to QS", fontWeight = FontWeight.Bold)
                            Spacer(Modifier.width(8.dp))
                            Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = null, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DprProgressStepper(currentStep: Int) {
    Column(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(5) { stepIndex ->
                if (stepIndex > 0) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.dp)
                            .background(if (currentStep > stepIndex - 1) SvbSuccess else DprStepperLinePending),
                    )
                }
                DprStepCircle(
                    stepNumber = stepIndex + 1,
                    state = when {
                        stepIndex < currentStep -> DprStepState.Done
                        stepIndex == currentStep -> DprStepState.Active
                        else -> DprStepState.Upcoming
                    },
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            StepLabels.forEachIndexed { i, label ->
                val activeStrongYellow = currentStep >= 2 && i == currentStep
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (i == currentStep) FontWeight.Bold else FontWeight.Medium,
                    color = when {
                        i < currentStep -> SvbN2
                        i == currentStep && activeStrongYellow -> SvbPrimary2
                        i == currentStep -> SvbN2
                        else -> SvbN3
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

private enum class DprStepState { Done, Active, Upcoming }

@Composable
private fun DprStepCircle(stepNumber: Int, state: DprStepState) {
    val bg = when (state) {
        DprStepState.Done -> SvbSuccess
        DprStepState.Active -> SvbPrimary2
        DprStepState.Upcoming -> SvbN7
    }
    val fg = when (state) {
        DprStepState.Done, DprStepState.Active -> SvbBlack
        DprStepState.Upcoming -> SvbN2
    }
    Surface(
        shape = CircleShape,
        color = bg,
        modifier = Modifier
            .size(32.dp)
            .then(
                if (state == DprStepState.Active) {
                    Modifier.border(1.dp, SvbBlack.copy(alpha = 0.08f), CircleShape)
                } else {
                    Modifier
                },
            ),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (state == DprStepState.Done) {
                Icon(
                    Icons.Outlined.Check,
                    contentDescription = null,
                    tint = SvbWhite,
                    modifier = Modifier.size(18.dp),
                )
            } else {
                Text(
                    text = "$stepNumber",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = fg,
                )
            }
        }
    }
}

@Composable
private fun DprTodaysBanner() {
    Surface(
        shape = HomeCardShape,
        color = SvbBlack,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = "TODAY'S DPR",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = SvbN3,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "17 April 2026",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = SvbWhite,
                )
            }
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = SvbPrimary2,
            ) {
                Text(
                    text = "DPR-001",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = SvbBlack,
                )
            }
        }
    }
}

@Composable
private fun DprVendorCard(
    vendor: DprVendorOption,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val border = if (selected) BorderStroke(1.5.dp, SvbPrimary2) else null
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) SvbPrimary2.copy(alpha = 0.18f) else SvbCardMuted,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = border,
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = HomeIconTileShape,
                color = vendor.initialsBg,
                modifier = Modifier.size(48.dp),
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = vendor.initials,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = vendor.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SvbBlack,
                )
                Spacer(Modifier.height(6.dp))
                Surface(shape = RoundedCornerShape(8.dp), color = SvbBlack) {
                    Text(
                        text = vendor.trade,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = SvbWhite,
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = vendor.workOrders,
                    style = MaterialTheme.typography.bodySmall,
                    color = SvbN2,
                )
            }
            if (selected) {
                Surface(shape = CircleShape, color = SvbPrimary2, modifier = Modifier.size(26.dp)) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(Icons.Outlined.Check, null, tint = SvbWhite, modifier = Modifier.size(16.dp))
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .border(2.dp, SvbN5, CircleShape),
                )
            }
        }
    }
}

@Composable
private fun DprDetailsCard(dprNoHighlight: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(Modifier.weight(1f)) {
                    Text("VENDOR", style = detailLabelStyle(), color = DprLabelBlueGrey)
                    Text("XYZ Builders", style = detailValueStyle(), color = SvbBlack)
                }
                Column(Modifier.weight(1f)) {
                    Text("DATE & TIME", style = detailLabelStyle(), color = DprLabelBlueGrey)
                    Text("23 Apr 2026, 12:46 pm", style = detailValueStyle(), color = SvbBlack)
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(Modifier.weight(1f)) {
                    Text("DPR NO", style = detailLabelStyle(), color = DprLabelBlueGrey)
                    Text(dprNoHighlight, style = detailValueStyle(), color = SvbPrimary2)
                }
                Column(Modifier.weight(1f)) {
                    Text("TYPE OF WORK", style = detailLabelStyle(), color = DprLabelBlueGrey)
                    Text("Concrete", style = detailValueStyle(), color = SvbBlack)
                }
            }
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = SvbN5)
            Spacer(Modifier.height(12.dp))
            Text("SCOPE OF WORK", style = detailLabelStyle(), color = DprLabelBlueGrey)
            Text("RCC Columns", style = detailValueStyle(), color = SvbBlack)
        }
    }
}

@Composable
private fun detailLabelStyle() = MaterialTheme.typography.labelSmall.copy(
    fontWeight = FontWeight.ExtraBold,
)

@Composable
private fun detailValueStyle() = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)

@Composable
private fun DprAutofillBanner() {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DprAutofillBannerBg,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = SvbSuccess,
                modifier = Modifier.size(22.dp),
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "Auto-filled from Work Order",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = SvbSuccess,
            )
        }
    }
}

@Composable
private fun DprBackNextRow(
    onBack: () -> Unit,
    onNext: () -> Unit,
    nextLabel: String,
    nextWeight: Float,
    nextFullWidth: Boolean = false,
) {
    if (nextFullWidth) {
        Column(Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, SvbN5),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SvbBlack),
            ) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Back", fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = SvbPrimary2, contentColor = SvbBlack),
                shape = RoundedCornerShape(14.dp),
            ) {
                Text(nextLabel, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, null, modifier = Modifier.size(20.dp))
            }
        }
    } else {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, SvbN5),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SvbBlack),
            ) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Back", fontWeight = FontWeight.SemiBold)
            }
            Button(
                onClick = onNext,
                modifier = Modifier.weight(nextWeight),
                colors = ButtonDefaults.buttonColors(containerColor = SvbPrimary2, contentColor = SvbBlack),
                shape = RoundedCornerShape(14.dp),
            ) {
                Text(nextLabel, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, null, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
private fun DprMeasurementTypeCard(
    title: String,
    subtitle: String,
    selected: Boolean,
    leadingIcon: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    val border = if (selected) BorderStroke(1.5.dp, SvbPrimary2) else BorderStroke(1.dp, SvbN5)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) SvbPrimary2.copy(alpha = 0.15f) else SvbWhite,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = border,
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (title.startsWith("Volume")) {
                Box(Modifier.size(44.dp), contentAlignment = Alignment.Center) {
                    leadingIcon()
                }
            } else {
                leadingIcon()
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = SvbBlack)
                Spacer(Modifier.height(4.dp))
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = SvbN2)
            }
        }
    }
}

@Composable
private fun DprDimField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
        singleLine = true,
        textStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = SvbN7,
            unfocusedContainerColor = SvbN7,
        ),
    )
}

@Composable
private fun DprVolumeResultCard(volumeCum: Int) {
    Surface(
        shape = HomeCardShape,
        color = DprSummaryCardBg,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "TOTAL VOLUME",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.ExtraBold,
                color = SvbN3,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "$volumeCum CUM",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = SvbPrimary2,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "L × B × D = Auto-calculated",
                style = MaterialTheme.typography.bodySmall,
                color = SvbN3,
            )
        }
    }
}

@Composable
private fun DprPhotoPlaceholder() {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = SvbN7,
        border = BorderStroke(1.dp, SvbN5),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(Icons.Outlined.PhotoCamera, null, tint = SvbN2, modifier = Modifier.size(26.dp))
            Spacer(Modifier.width(10.dp))
            Text(
                text = "Tap to capture measurement photo",
                style = MaterialTheme.typography.bodyMedium,
                color = SvbN2,
            )
        }
    }
}

@Composable
private fun DprTripQuantityCard(
    trips: String,
    avgQty: String,
    resultCum: Int,
    onTripsChange: (String) -> Unit,
    onAvgChange: (String) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "QUANTITY 2 — TRIP BASED",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.ExtraBold,
                color = SvbN2,
            )
            Spacer(Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                OutlinedTextField(
                    value = trips,
                    onValueChange = onTripsChange,
                    modifier = Modifier.weight(1f),
                    label = { Text("Trips") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SvbWhite,
                        unfocusedContainerColor = SvbWhite,
                    ),
                )
                Text("×", modifier = Modifier.padding(horizontal = 6.dp), color = SvbN2)
                OutlinedTextField(
                    value = avgQty,
                    onValueChange = onAvgChange,
                    modifier = Modifier.weight(1f),
                    label = { Text("Avg Qty/Trip") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SvbWhite,
                        unfocusedContainerColor = SvbWhite,
                    ),
                )
                Text("=", modifier = Modifier.padding(horizontal = 6.dp), color = SvbN2)
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SvbPrimary2,
                    modifier = Modifier.width(88.dp),
                ) {
                    Text(
                        text = "$resultCum CUM",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun DprFinalSummaryCard(
    vendorName: String,
    workType: String,
    qty1Cum: Int,
    qty2Cum: Int,
    totalCum: Int,
) {
    Surface(
        shape = HomeCardShape,
        color = DprSummaryCardBg,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "FINAL SUMMARY",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.ExtraBold,
                color = SvbN3,
            )
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f)) {
                    Text("Vendor", style = MaterialTheme.typography.labelSmall, color = SvbN3)
                    Text(vendorName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = SvbWhite)
                }
                Column(Modifier.weight(1f)) {
                    Text("Work Type", style = MaterialTheme.typography.labelSmall, color = SvbN3)
                    Text(workType, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = SvbWhite)
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f)) {
                    Text("Measurement (Qty 1)", style = MaterialTheme.typography.labelSmall, color = SvbN3)
                    Text("$qty1Cum CUM", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = SvbPrimary2)
                }
                Column(Modifier.weight(1f)) {
                    Text("Trip Qty (Qty 2)", style = MaterialTheme.typography.labelSmall, color = SvbN3)
                    Text("$qty2Cum CUM", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = SvbPrimary2)
                }
            }
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = SvbN2.copy(alpha = 0.25f))
            Spacer(Modifier.height(10.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "TOTAL SUBMITTED",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = SvbN3,
                )
                Text(
                    text = "$totalCum CUM",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = SvbPrimary2,
                )
            }
        }
    }
}
