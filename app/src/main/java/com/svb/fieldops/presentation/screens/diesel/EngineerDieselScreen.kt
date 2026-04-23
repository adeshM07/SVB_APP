package com.svb.fieldops.presentation.screens.diesel

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.PrecisionManufacturing
import androidx.compose.material.icons.outlined.Warning
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.svb.fieldops.domain.model.UserRole
import com.svb.fieldops.presentation.navigation.MainRoutes
import com.svb.fieldops.presentation.navigation.approvalsTabIndex
import com.svb.fieldops.presentation.navigation.dprTabIndex
import com.svb.fieldops.presentation.navigation.bottomNavItemsForRole
import com.svb.fieldops.presentation.navigation.dieselTabIndex
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
import com.svb.fieldops.ui.theme.SvbN7
import com.svb.fieldops.ui.theme.SvbPrimary1
import com.svb.fieldops.ui.theme.SvbPrimary2
import com.svb.fieldops.ui.theme.SvbPrimary5
import com.svb.fieldops.ui.theme.SvbRoseTint
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite

private data class LedgerDayRow(
    val dateLabel: String,
    val refId: String,
    val purchase: String,
    val purchaseHighlight: Boolean,
    val stock: String,
    val issued: String?,
    val issuedNavigable: Boolean,
    val balance: String,
    val balancePositive: Boolean,
)

private data class PendingDieselMachine(
    val id: String,
    val typeLabel: String,
    val volumeLitres: String,
    val volumeColor: Color,
    val badgeLabel: String,
    val badgeBackground: Color,
    val badgeTextColor: Color,
    val iconBackground: Color,
    val icon: ImageVector,
)

private data class MileageMachine(
    val title: String,
    val subtitle: String,
    val mileageLabel: String,
    val mileageColor: Color,
    val iconTint: Color,
    val start: String,
    val stop: String,
    val filled: String,
    val bal: String,
    val warning: String?,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EngineerDieselScreen(
    role: UserRole,
    navController: NavHostController,
) {
    val items = bottomNavItemsForRole(role)
    val dieselIdx = requireNotNull(dieselTabIndex(role)) { "Diesel inventory is Engineer-only." }
    val approvalsIdx = requireNotNull(approvalsTabIndex(role)) { "Engineer has Approvals tab." }
    val dprIdx = requireNotNull(dprTabIndex(role)) { "Engineer has DPR tab." }
    val profileIdx = profileTabIndex(role)
    val scroll = rememberScrollState()
    var ledgerFilter by remember { mutableIntStateOf(0) }

    val ledgerDemo = remember {
        listOf(
            LedgerDayRow(
                dateLabel = "19 Apr 2026",
                refId = "ABC123",
                purchase = "+1,800",
                purchaseHighlight = true,
                stock = "2,000",
                issued = "200",
                issuedNavigable = true,
                balance = "2,000",
                balancePositive = true,
            ),
            LedgerDayRow(
                dateLabel = "17 Apr 2026",
                refId = "ABC123",
                purchase = "0",
                purchaseHighlight = false,
                stock = "500",
                issued = "100",
                issuedNavigable = true,
                balance = "400",
                balancePositive = false,
            ),
            LedgerDayRow(
                dateLabel = "16 Apr 2026",
                refId = "ABC123",
                purchase = "+2,000",
                purchaseHighlight = true,
                stock = "2,000",
                issued = "1,500",
                issuedNavigable = true,
                balance = "500",
                balancePositive = true,
            ),
        )
    }

    val mileageDemo = remember {
        listOf(
            MileageMachine(
                title = "Excavator",
                subtitle = "Avg: 16 L/hr",
                mileageLabel = "16.00 mileage",
                mileageColor = SvbSuccess,
                iconTint = SvbSuccess,
                start = "4,320",
                stop = "4,330",
                filled = "200 L",
                bal = "40 L",
                warning = null,
            ),
            MileageMachine(
                title = "Dumper",
                subtitle = "Avg: 2 L/km",
                mileageLabel = "2.00 mileage",
                mileageColor = SvbDanger,
                iconTint = SvbDanger,
                start = "4,320",
                stop = "4,520",
                filled = "200 L",
                bal = "100 L",
                warning = "Low mileage — check for leaks or excess idling",
            ),
        )
    }

    BackHandler { navController.popRoleHomeWithHomeTabSelected() }

    Scaffold(
        containerColor = SvbLoginBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "HSD Inventory",
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
                    IconButton(onClick = { /* later: add entry */ }) {
                        Icon(Icons.Outlined.Add, contentDescription = "Add", tint = SvbBlack)
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
                selectedIndex = dieselIdx,
                onSelect = { index ->
                    when {
                        index == dieselIdx -> Unit
                        index == profileIdx ->
                            navController.navigate(MainRoutes.profile(role)) { launchSingleTop = true }
                        index == approvalsIdx ->
                            navController.navigate(MainRoutes.approvals(role)) { launchSingleTop = true }
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
            DieselAlertBanner()
            Spacer(Modifier.height(14.dp))
            CurrentStockCard(stockLitres = 2_000, capacityLitres = 3_000, burnPerDay = 450)
            Spacer(Modifier.height(16.dp))
            IssueAndPurchaseRow()
            Spacer(Modifier.height(24.dp))
            DailyLedgerHeader(
                selectedFilter = ledgerFilter,
                onFilterSelected = { ledgerFilter = it },
            )
            Spacer(Modifier.height(12.dp))
            ledgerDemo.forEach { row ->
                LedgerDayCard(row)
                Spacer(Modifier.height(12.dp))
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Mileage Tracker",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SvbBlack,
            )
            Spacer(Modifier.height(12.dp))
            mileageDemo.forEach { m ->
                MileageMachineCard(m)
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun DieselAlertBanner() {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val pendingMachines = remember {
        listOf(
            PendingDieselMachine(
                id = "JCB-07",
                typeLabel = "Excavator",
                volumeLitres = "200 L",
                volumeColor = SvbDanger,
                badgeLabel = "URGENT",
                badgeBackground = SvbDanger,
                badgeTextColor = SvbWhite,
                iconBackground = SvbDanger,
                icon = Icons.Outlined.PrecisionManufacturing,
            ),
            PendingDieselMachine(
                id = "TATA-019",
                typeLabel = "Dumper",
                volumeLitres = "100 L",
                volumeColor = SvbPrimary1,
                badgeLabel = "LOW",
                badgeBackground = SvbPrimary2,
                badgeTextColor = SvbBlack,
                iconBackground = SvbPrimary2,
                icon = Icons.Outlined.LocalShipping,
            ),
            PendingDieselMachine(
                id = "HYD-03",
                typeLabel = "Hydra",
                volumeLitres = "50 L",
                volumeColor = SvbPrimary1,
                badgeLabel = "LOW",
                badgeBackground = SvbPrimary2,
                badgeTextColor = SvbBlack,
                iconBackground = SvbPrimary2,
                icon = Icons.Outlined.Build,
            ),
        )
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        color = SvbRoseTint,
        border = BorderStroke(1.dp, SvbDanger.copy(alpha = 0.45f)),
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Outlined.LocalGasStation,
                    contentDescription = null,
                    tint = SvbDanger,
                    modifier = Modifier.size(28.dp),
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        text = "3 machines need diesel",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SvbDanger,
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "Total pending: 350 L",
                        style = MaterialTheme.typography.bodySmall,
                        color = SvbN2,
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = SvbDanger,
                    modifier = Modifier.size(24.dp),
                )
            }
            if (expanded) {
                HorizontalDivider(color = SvbN5, thickness = 1.dp)
                pendingMachines.forEachIndexed { index, machine ->
                    PendingDieselMachineRow(machine)
                    if (index < pendingMachines.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 14.dp),
                            color = SvbN5,
                            thickness = 1.dp,
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = { /* later: issue diesel flow */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                        .padding(bottom = 14.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SvbDanger,
                        contentColor = SvbWhite,
                    ),
                ) {
                    Icon(
                        Icons.Outlined.LocalGasStation,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                        tint = SvbWhite,
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "Issue Diesel Now",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Composable
private fun PendingDieselMachineRow(machine: PendingDieselMachine) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(machine.iconBackground),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                machine.icon,
                contentDescription = null,
                tint = SvbWhite,
                modifier = Modifier.size(24.dp),
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = machine.id,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SvbBlack,
            )
            Text(
                text = machine.typeLabel,
                style = MaterialTheme.typography.bodySmall,
                color = SvbN2,
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = machine.volumeLitres,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = machine.volumeColor,
            )
            Spacer(Modifier.height(6.dp))
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = machine.badgeBackground,
            ) {
                Text(
                    text = machine.badgeLabel,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = machine.badgeTextColor,
                )
            }
        }
    }
}

@Composable
private fun CurrentStockCard(
    stockLitres: Int,
    capacityLitres: Int,
    burnPerDay: Int,
) {
    val progress = (stockLitres.toFloat() / capacityLitres.toFloat()).coerceIn(0f, 1f)
    val daysLeft = (stockLitres / burnPerDay.toFloat()).toInt().coerceAtLeast(1)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "CURRENT STOCK",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.ExtraBold,
                color = SvbN2,
            )
            Spacer(Modifier.height(10.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    text = "${stockLitres.formatThousands()} L",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = SvbBlack,
                )
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "~${burnPerDay} L/day",
                        style = MaterialTheme.typography.bodySmall,
                        color = SvbN2,
                    )
                    Text(
                        text = "~$daysLeft days left",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SvbPrimary1,
                    )
                }
            }
            Spacer(Modifier.height(14.dp))
            StockLevelProgressBar(progress = progress)
            Spacer(Modifier.height(6.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("0", style = MaterialTheme.typography.bodySmall, color = SvbN2)
                Text(
                    text = "${capacityLitres.formatThousands()} L capacity",
                    style = MaterialTheme.typography.bodySmall,
                    color = SvbN2,
                )
            }
        }
    }
}

private fun Int.formatThousands(): String =
    toString().reversed().chunked(3).joinToString(",").reversed()

/** Pill-shaped track (light grey) + amber fill — matches HSD Current Stock mock. */
@Composable
private fun StockLevelProgressBar(progress: Float) {
    val p = progress.coerceIn(0f, 1f)
    val barHeight = 12.dp
    val capsule = RoundedCornerShape(6.dp)
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(barHeight)
            .clip(capsule)
            .background(SvbN7),
    ) {
        if (p > 0f) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(p)
                    .background(SvbPrimary1),
            )
        }
    }
}

@Composable
private fun IssueAndPurchaseRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Surface(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(14.dp))
                .clickable { /* Issue HSD */ },
            shape = RoundedCornerShape(14.dp),
            color = SvbPrimary1,
        ) {
            Row(
                Modifier.padding(vertical = 16.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    Icons.Outlined.ArrowUpward,
                    contentDescription = null,
                    tint = SvbBlack,
                    modifier = Modifier.size(22.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Issue HSD",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SvbBlack,
                )
            }
        }
        Surface(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(14.dp))
                .clickable { /* Add purchase */ },
            shape = RoundedCornerShape(14.dp),
            color = SvbN7,
        ) {
            Row(
                Modifier.padding(vertical = 16.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    Icons.Outlined.Add,
                    contentDescription = null,
                    tint = SvbBlack,
                    modifier = Modifier.size(22.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Add Purchase",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SvbBlack,
                )
            }
        }
    }
}

@Composable
private fun DailyLedgerHeader(
    selectedFilter: Int,
    onFilterSelected: (Int) -> Unit,
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Daily Ledger",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = SvbBlack,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            LedgerFilterChip("All", selected = selectedFilter == 0) { onFilterSelected(0) }
            LedgerFilterChip("Week", selected = selectedFilter == 1) { onFilterSelected(1) }
            LedgerFilterChip("Month", selected = selectedFilter == 2) { onFilterSelected(2) }
        }
    }
}

@Composable
private fun LedgerFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(999.dp),
        color = if (selected) SvbPrimary1 else SvbN7,
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) SvbWhite else SvbN2,
        )
    }
}

private val LedgerPositiveGreenBg = SvbSuccess.copy(alpha = 0.14f)

@Composable
private fun LedgerDayCard(row: LedgerDayRow) {
    val purchasePositive = row.purchaseHighlight && row.purchase != "0"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, SvbN5), HomeCardShape),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = row.dateLabel,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SvbBlack,
                )
                Text(
                    text = row.refId,
                    style = MaterialTheme.typography.bodySmall,
                    color = SvbN2,
                )
            }
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = SvbN7, thickness = 1.dp)
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth()) {
                LedgerMetricCell(
                    label = "PURCHASE",
                    value = row.purchase,
                    valueColor = if (purchasePositive) SvbSuccess else SvbN2,
                    valueBold = purchasePositive,
                    valueBackgroundColor = if (purchasePositive) LedgerPositiveGreenBg else null,
                    modifier = Modifier.weight(1f),
                )
                LedgerVerticalBar()
                LedgerMetricCell(
                    label = "STOCK",
                    value = row.stock,
                    valueColor = SvbBlack,
                    valueBold = true,
                    modifier = Modifier.weight(1f),
                )
                LedgerVerticalBar()
                LedgerIssuedCell(
                    value = row.issued,
                    showChevron = row.issuedNavigable,
                    modifier = Modifier.weight(1f),
                )
                LedgerVerticalBar()
                LedgerMetricCell(
                    label = "BALANCE",
                    value = row.balance,
                    valueColor = if (row.balancePositive) SvbSuccess else SvbBlack,
                    valueBold = true,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun LedgerVerticalBar() {
    Box(
        Modifier
            .width(1.dp)
            .height(48.dp)
            .padding(vertical = 2.dp)
            .background(SvbN5),
    )
}

@Composable
private fun LedgerMetricCell(
    label: String,
    value: String,
    valueColor: Color,
    valueBold: Boolean,
    modifier: Modifier = Modifier,
    valueBackgroundColor: Color? = null,
) {
    Column(
        modifier = modifier.padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = SvbN2,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(4.dp))
        if (valueBackgroundColor != null) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = valueBackgroundColor,
            ) {
                Text(
                    text = value,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = if (valueBold) FontWeight.Bold else FontWeight.Medium,
                    color = valueColor,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = if (valueBold) FontWeight.Bold else FontWeight.Medium,
                color = valueColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun LedgerIssuedCell(
    value: String?,
    showChevron: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "ISSUED",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = SvbN2,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(4.dp))
        val issuedEmpty = value == null || value == "0"
        if (!issuedEmpty) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = SvbRoseTint,
            ) {
                Row(
                    Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SvbDanger,
                    )
                    if (showChevron) {
                        Icon(
                            Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                            contentDescription = null,
                            tint = SvbDanger,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
            }
        } else {
            Text(
                text = "0",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                color = SvbN2,
            )
        }
    }
}

@Composable
private fun MileageMachineCard(m: MileageMachine) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(m.iconTint.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        if (m.title == "Dumper") Icons.Outlined.LocalShipping else Icons.Outlined.PrecisionManufacturing,
                        contentDescription = null,
                        tint = m.iconTint,
                        modifier = Modifier.size(26.dp),
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        text = m.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                    Text(
                        text = m.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = SvbN2,
                    )
                }
                Text(
                    text = m.mileageLabel,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = m.mileageColor,
                )
            }
            Spacer(Modifier.height(14.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MileageStatBox("START", m.start, SvbBlack, Modifier.weight(1f))
                MileageStatBox("STOP", m.stop, SvbBlack, Modifier.weight(1f))
                MileageStatBox("FILLED", m.filled, SvbPrimary1, Modifier.weight(1f))
                MileageStatBox("BAL", m.bal, SvbBlack, Modifier.weight(1f))
            }
            if (m.warning != null) {
                Spacer(Modifier.height(12.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(SvbRoseTint)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Outlined.Warning,
                        contentDescription = null,
                        tint = SvbDanger,
                        modifier = Modifier.size(22.dp),
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = m.warning,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = SvbDanger,
                    )
                }
            }
        }
    }
}

@Composable
private fun MileageStatBox(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = SvbWhite,
        border = BorderStroke(1.dp, SvbN5),
    ) {
        Column(
            Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = SvbN2,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = valueColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}
