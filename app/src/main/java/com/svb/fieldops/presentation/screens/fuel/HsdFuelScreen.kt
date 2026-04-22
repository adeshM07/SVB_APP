package com.svb.fieldops.presentation.screens.fuel

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.PrecisionManufacturing
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.svb.fieldops.domain.model.UserRole
import com.svb.fieldops.presentation.components.SvbPrimaryButton
import com.svb.fieldops.presentation.navigation.MainRoutes
import com.svb.fieldops.presentation.navigation.bottomNavItemsForRole
import com.svb.fieldops.presentation.navigation.fuelTabIndex
import com.svb.fieldops.presentation.navigation.popRoleHomeWithHomeTabSelected
import com.svb.fieldops.presentation.navigation.profileTabIndex
import com.svb.fieldops.presentation.screens.home.HomeCardShape
import com.svb.fieldops.presentation.screens.home.HomeRoleNavigationBar
import com.svb.fieldops.presentation.screens.home.SectionTitle
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbCardMuted
import com.svb.fieldops.ui.theme.SvbDanger
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN2
import com.svb.fieldops.ui.theme.SvbN5
import com.svb.fieldops.ui.theme.SvbN7
import com.svb.fieldops.ui.theme.SvbRoseTint
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite
import kotlin.math.min

/** Height reserved for the semicircular gauge + centred “12 / Litres” block (mock proportions). */
private val FuelGaugeTotalHeight = 176.dp

/** Nudge value text into the hollow of the arc (slightly above geometric box centre). */
private val FuelGaugeValueOffsetY = (-10).dp

private data class RefillRow(
    val amountLabel: String,
    val dateTime: String,
    val status: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HsdFuelScreen(
    role: UserRole,
    navController: NavHostController,
) {
    val items = bottomNavItemsForRole(role)
    val fuelIdx = requireNotNull(fuelTabIndex(role)) { "HsdFuelScreen is only for Driver, Operator, and Supervisor." }
    val profileIdx = profileTabIndex(role)
    val scroll = rememberScrollState()

    val capacityL = 60
    val consumedL = 48
    val remainingL = 12
    val remainingFraction = remainingL.toFloat() / capacityL.toFloat()

    var quantityText by remember { mutableStateOf("50") }
    var meterText by remember { mutableStateOf("12") }

    val recentRefills = remember {
        listOf(
            RefillRow("+50 Litres", "14 Apr • 3:30 PM", "Filled"),
            RefillRow("+40 Litres", "10 Apr • 11:15 AM", "Filled"),
            RefillRow("+35 Litres", "02 Apr • 9:00 AM", "Filled"),
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
                        text = "HSD / Fuel",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SvbLoginBackground,
                    scrolledContainerColor = SvbLoginBackground,
                ),
            )
        },
        bottomBar = {
            HomeRoleNavigationBar(
                items = items,
                selectedIndex = fuelIdx,
                onSelect = { index ->
                    when {
                        index == fuelIdx -> Unit
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
                .padding(top = 8.dp, bottom = 20.dp),
        ) {
            FuelStatusDashboardCard(
                remainingLitres = remainingL,
                capacityLitres = capacityL,
                consumedLitres = consumedL,
                remainingFraction = remainingFraction,
            )
            Spacer(Modifier.height(14.dp))
            LowFuelAlertBanner()
            Spacer(Modifier.height(22.dp))
            SectionTitle("REQUEST FUEL")
            MachineAssignmentCard()
            Spacer(Modifier.height(12.dp))
            FuelLabeledTextField(
                label = "Quantity Required (Litres)",
                value = quantityText,
                onValueChange = { quantityText = it },
            )
            Spacer(Modifier.height(16.dp))
            FuelLabeledTextField(
                label = "Current HSD Meter Reading",
                value = meterText,
                onValueChange = { meterText = it },
            )
            Spacer(Modifier.height(18.dp))
            SvbPrimaryButton(
                text = "Submit Request",
                onClick = { /* later: API */ },
                leadingIcon = Icons.AutoMirrored.Outlined.Send,
            )
            Spacer(Modifier.height(28.dp))
            SectionTitle("RECENT REFILLS")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = HomeCardShape,
                colors = CardDefaults.cardColors(containerColor = SvbWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Column(Modifier.fillMaxWidth()) {
                    recentRefills.forEachIndexed { i, row ->
                        RecentRefillRow(row)
                        if (i < recentRefills.lastIndex) {
                            HorizontalDivider(color = SvbN7, thickness = 1.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FuelLabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = SvbBlack,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = null,
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = SvbBlack),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SvbN7,
                unfocusedContainerColor = SvbN7,
                focusedBorderColor = SvbN5,
                unfocusedBorderColor = SvbN5,
                cursorColor = SvbBlack,
                focusedTextColor = SvbBlack,
                unfocusedTextColor = SvbBlack,
            ),
        )
    }
}

@Composable
private fun FuelStatusDashboardCard(
    remainingLitres: Int,
    capacityLitres: Int,
    consumedLitres: Int,
    remainingFraction: Float,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(FuelGaugeTotalHeight),
            ) {
                FuelGaugeArc(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 6.dp),
                    remainingFraction = remainingFraction,
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = FuelGaugeValueOffsetY),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = remainingLitres.toString(),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = SvbDanger,
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "Litres",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SvbN2,
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FuelStatColumn(
                    label = "Capacity",
                    value = "${capacityLitres} L",
                    valueColor = SvbBlack,
                    modifier = Modifier.weight(1f),
                )
                Box(
                    Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(SvbN5),
                )
                FuelStatColumn(
                    label = "Consumed",
                    value = "${consumedLitres} L",
                    valueColor = SvbBlack,
                    modifier = Modifier.weight(1f),
                )
                Box(
                    Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(SvbN5),
                )
                FuelStatColumn(
                    label = "Remaining",
                    value = "${remainingLitres} L",
                    valueColor = SvbDanger,
                    labelColor = SvbDanger,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun FuelGaugeArc(
    modifier: Modifier = Modifier,
    remainingFraction: Float,
) {
    Canvas(modifier = modifier) {
        val stroke = 14.dp.toPx()
        val cx = size.width / 2f
        val cy = size.height
        // Upper semicircle: ellipse 2r × 2r with bottom on canvas bottom; 2r ≤ min(width, height).
        val r = min(size.width / 2f, size.height / 2f) - stroke / 2f
        val topLeft = Offset(cx - r, cy - 2 * r)
        val arcSize = Size(2 * r, 2 * r)
        // Track first, then progress — same geometry, Round caps on both (mock: thick rounded arcs).
        drawArc(
            color = SvbN5,
            startAngle = 180f,
            sweepAngle = -180f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round),
        )
        drawArc(
            color = SvbDanger,
            startAngle = 180f,
            sweepAngle = -180f * remainingFraction.coerceIn(0f, 1f),
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round),
        )
    }
}

@Composable
private fun FuelStatColumn(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier,
    labelColor: Color = SvbN2,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = labelColor,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = valueColor,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun LowFuelAlertBanner() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        color = SvbRoseTint,
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Outlined.Warning,
                contentDescription = null,
                tint = SvbDanger,
                modifier = Modifier.size(28.dp),
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = "Low fuel — request refill",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SvbDanger,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "Below minimum operating level",
                    style = MaterialTheme.typography.bodySmall,
                    color = SvbDanger.copy(alpha = 0.88f),
                )
            }
        }
    }
}

@Composable
private fun MachineAssignmentCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SvbWhite,
                modifier = Modifier.size(48.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Outlined.PrecisionManufacturing,
                        contentDescription = null,
                        tint = SvbBlack,
                        modifier = Modifier.size(26.dp),
                    )
                }
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = "JCB 3DX — #SVB-M042",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SvbBlack,
                )
                Text(
                    text = "Machine assigned",
                    style = MaterialTheme.typography.bodySmall,
                    color = SvbN2,
                )
            }
        }
    }
}

@Composable
private fun RecentRefillRow(row: RefillRow) {
    val mintBg = SvbSuccess.copy(alpha = 0.18f)
    val statusBg = SvbSuccess.copy(alpha = 0.14f)
    val statusText = Color(0xFF145A40)

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(mintBg, shape = RoundedCornerShape(999.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Outlined.LocalGasStation,
                contentDescription = null,
                tint = SvbSuccess,
                modifier = Modifier.size(22.dp),
            )
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = row.amountLabel,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SvbBlack,
            )
            Text(
                text = row.dateTime,
                style = MaterialTheme.typography.bodySmall,
                color = SvbN2,
            )
        }
        Surface(
            shape = RoundedCornerShape(999.dp),
            color = statusBg,
        ) {
            Text(
                text = row.status,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = statusText,
            )
        }
    }
}
