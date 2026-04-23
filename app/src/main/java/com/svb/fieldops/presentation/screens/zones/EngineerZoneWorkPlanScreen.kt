package com.svb.fieldops.presentation.screens.zones

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material.icons.outlined.Warning
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
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
import com.svb.fieldops.ui.theme.SvbPrimary1
import com.svb.fieldops.ui.theme.SvbPrimary2
import com.svb.fieldops.ui.theme.SvbWhite

private val DropdownSelectedBlue = Color(0xFF1976D2)

private val WorkTypeOptions = listOf(
    "Earthwork",
    "Concrete",
    "Machine Hire",
    "Plumbing",
    "Electrical",
    "Shuttering",
    "Steel Fixing",
    "Others",
)

private val FilterZoneOptions = listOf("All Zones", "Zone A", "Zone B", "Pit-1")

private data class ZonePlanRow(
    val id: String,
    val title: String,
    val configured: Boolean,
    val summaryCollapsed: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EngineerZoneWorkPlanScreen(
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

    val zones = remember {
        listOf(
            ZonePlanRow("zone_a", "Zone A", true, "Earthwork • 2 machines • Target: 200 CUM"),
            ZonePlanRow("zone_b", "Zone B", true, "Concrete • 1 machine • Target: 50 RTM"),
            ZonePlanRow("pit_1", "Pit-1", true, "Earthwork • 3 machines • Target: 300 CUM"),
            ZonePlanRow("pit_2", "Pit-2", false, "Not configured"),
            ZonePlanRow("dump_yard", "Dump Yard", false, "Not configured"),
        )
    }

    var expandedZoneId by remember { mutableStateOf<String?>(null) }

    var workTypeIndexByZone by remember {
        mutableStateOf(
            mapOf(
                "zone_a" to 0,
                "zone_b" to 1,
                "pit_1" to 0,
                "pit_2" to -1,
                "dump_yard" to -1,
            ),
        )
    }
    var targetByZone by remember {
        mutableStateOf(
            mapOf(
                "zone_a" to "200",
                "zone_b" to "50",
                "pit_1" to "300",
                "pit_2" to "0",
                "dump_yard" to "0",
            ),
        )
    }
    var scopeByZone by remember {
        mutableStateOf(
            mapOf(
                "zone_a" to "",
                "zone_b" to "RCC Columns — Level 2",
                "pit_1" to "",
                "pit_2" to "",
                "dump_yard" to "",
            ),
        )
    }
    val machinesByZone = remember {
        mutableStateMapOf(
            "zone_b" to mutableListOf("TATA-024 Drv: Vikram"),
        )
    }

    fun machinesFor(id: String): MutableList<String> =
        machinesByZone.getOrPut(id) { mutableListOf() }

    BackHandler { navController.popRoleHomeWithHomeTabSelected() }

    Scaffold(
        containerColor = SvbLoginBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Zone & Work Plan",
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "17 April 2026",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SvbN2,
                )
                Text(
                    text = "3/5 zones configured • 4/5 machines assigned",
                    style = MaterialTheme.typography.bodySmall,
                    color = SvbN2,
                )
            }
            Spacer(Modifier.height(18.dp))
            zones.forEach { zone ->
                val expanded = expandedZoneId == zone.id
                ZoneWorkPlanCard(
                    zone = zone,
                    expanded = expanded,
                    onHeaderClick = {
                        expandedZoneId = if (expanded) null else zone.id
                    },
                    workTypeIndex = workTypeIndexByZone[zone.id] ?: -1,
                    onWorkTypeIndex = { idx ->
                        workTypeIndexByZone = workTypeIndexByZone + (zone.id to idx)
                    },
                    target = targetByZone[zone.id] ?: "",
                    onTargetChange = { t -> targetByZone = targetByZone + (zone.id to t) },
                    scope = scopeByZone[zone.id] ?: "",
                    onScopeChange = { s -> scopeByZone = scopeByZone + (zone.id to s) },
                    machines = machinesFor(zone.id),
                    onRemoveMachine = { line ->
                        machinesFor(zone.id).remove(line)
                    },
                    onAddMachine = {
                        machinesFor(zone.id).add("NEW-001 Drv: Demo")
                    },
                )
                Spacer(Modifier.height(10.dp))
            }
            Spacer(Modifier.height(6.dp))
            UnassignedMachineBanner()
            Spacer(Modifier.height(10.dp))
            SupervisorFilterCard()
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = { /* later: persist plan */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SvbPrimary2,
                    contentColor = SvbBlack,
                ),
                shape = RoundedCornerShape(18.dp),
                contentPadding = PaddingValues(vertical = 18.dp, horizontal = 20.dp),
            ) {
                Text(
                    text = "Save Zone & Work Plan",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ZoneWorkPlanCard(
    zone: ZonePlanRow,
    expanded: Boolean,
    onHeaderClick: () -> Unit,
    workTypeIndex: Int,
    onWorkTypeIndex: (Int) -> Unit,
    target: String,
    onTargetChange: (String) -> Unit,
    scope: String,
    onScopeChange: (String) -> Unit,
    machines: MutableList<String>,
    onRemoveMachine: (String) -> Unit,
    onAddMachine: () -> Unit,
) {
    if (zone.configured) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = HomeCardShape,
            colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            Column(Modifier.fillMaxWidth()) {
                ZoneCardHeaderRow(
                    title = zone.title,
                    subtitle = zone.summaryCollapsed,
                    pinMuted = false,
                    expanded = expanded,
                    onClick = onHeaderClick,
                )
                if (expanded) {
                    HorizontalDivider(color = SvbN5, modifier = Modifier.padding(horizontal = 14.dp))
                    ConfiguredZoneExpandedBody(
                        workTypeIndex = workTypeIndex,
                        onWorkTypeIndex = onWorkTypeIndex,
                        target = target,
                        onTargetChange = onTargetChange,
                        scope = scope,
                        onScopeChange = onScopeChange,
                        machines = machines,
                        onRemoveMachine = onRemoveMachine,
                        onAddMachine = onAddMachine,
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(HomeCardShape)
                .background(SvbWhite)
                .zoneDashedBorder(),
        ) {
            ZoneCardHeaderRow(
                title = zone.title,
                subtitle = zone.summaryCollapsed,
                pinMuted = true,
                expanded = expanded,
                onClick = onHeaderClick,
            )
            if (expanded) {
                HorizontalDivider(color = SvbN5, modifier = Modifier.padding(horizontal = 14.dp))
                UnconfiguredZoneExpandedBody(
                    workTypeIndex = workTypeIndex,
                    onWorkTypeIndex = onWorkTypeIndex,
                    target = target,
                    onTargetChange = onTargetChange,
                    scope = scope,
                    onScopeChange = onScopeChange,
                    machines = machines,
                    onRemoveMachine = onRemoveMachine,
                    onAddMachine = onAddMachine,
                )
            }
        }
    }
}

@Composable
private fun ZoneCardHeaderRow(
    title: String,
    subtitle: String,
    pinMuted: Boolean,
    expanded: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            shape = HomeIconTileShape,
            color = SvbN7,
            modifier = Modifier.size(44.dp),
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Outlined.PinDrop,
                    contentDescription = null,
                    tint = if (pinMuted) SvbN3 else SvbN2,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SvbBlack,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = SvbN2,
            )
        }
        Icon(
            if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
            contentDescription = null,
            tint = SvbN3,
            modifier = Modifier.size(24.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConfiguredZoneExpandedBody(
    workTypeIndex: Int,
    onWorkTypeIndex: (Int) -> Unit,
    target: String,
    onTargetChange: (String) -> Unit,
    scope: String,
    onScopeChange: (String) -> Unit,
    machines: MutableList<String>,
    onRemoveMachine: (String) -> Unit,
    onAddMachine: () -> Unit,
) {
    Column(Modifier.padding(start = 14.dp, end = 14.dp, bottom = 14.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Column(Modifier.weight(1.4f)) {
                Text(
                    text = "Type of Work",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = SvbN2,
                )
                Spacer(Modifier.height(6.dp))
                WorkTypeExposedDropdown(
                    selectedIndex = workTypeIndex,
                    onSelect = onWorkTypeIndex,
                    allowPlaceholder = false,
                )
            }
            Column(Modifier.weight(0.6f)) {
                Text(
                    text = "Target",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = SvbN2,
                )
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = target,
                    onValueChange = onTargetChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SvbN7,
                        unfocusedContainerColor = SvbN7,
                    ),
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Scope",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = SvbN2,
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = scope,
            onValueChange = onScopeChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            minLines = 2,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SvbWhite,
                unfocusedContainerColor = SvbWhite,
            ),
        )
        Spacer(Modifier.height(14.dp))
        Text(
            text = "MACHINES",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            color = SvbN2,
        )
        Spacer(Modifier.height(8.dp))
        machines.forEach { line ->
            MachineChipRow(line = line, onRemove = { onRemoveMachine(line) })
            Spacer(Modifier.height(8.dp))
        }
        DashedAddRow(label = "+ Add", onClick = onAddMachine)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UnconfiguredZoneExpandedBody(
    workTypeIndex: Int,
    onWorkTypeIndex: (Int) -> Unit,
    target: String,
    onTargetChange: (String) -> Unit,
    scope: String,
    onScopeChange: (String) -> Unit,
    machines: MutableList<String>,
    onRemoveMachine: (String) -> Unit,
    onAddMachine: () -> Unit,
) {
    Column(Modifier.padding(start = 14.dp, end = 14.dp, bottom = 14.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Column(Modifier.weight(1.4f)) {
                Text(
                    text = "Type of Work",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = SvbN2,
                )
                Spacer(Modifier.height(6.dp))
                WorkTypeExposedDropdown(
                    selectedIndex = workTypeIndex,
                    onSelect = onWorkTypeIndex,
                    allowPlaceholder = true,
                )
            }
            Column(Modifier.weight(0.6f)) {
                Text(
                    text = "Target",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = SvbN2,
                )
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = target,
                    onValueChange = onTargetChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("0", color = SvbN3) },
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SvbN7,
                        unfocusedContainerColor = SvbN7,
                    ),
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Scope",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = SvbN2,
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = scope,
            onValueChange = onScopeChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Describe the work...", color = SvbN3) },
            minLines = 2,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SvbWhite,
                unfocusedContainerColor = SvbWhite,
            ),
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "No machines assigned.",
            style = MaterialTheme.typography.bodySmall,
            color = SvbN3,
        )
        Spacer(Modifier.height(8.dp))
        machines.forEach { line ->
            MachineChipRow(line = line, onRemove = { onRemoveMachine(line) })
            Spacer(Modifier.height(8.dp))
        }
        DashedAddRow(label = "+ Add Machine", onClick = onAddMachine)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WorkTypeExposedDropdown(
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    allowPlaceholder: Boolean,
) {
    var expanded by remember { mutableStateOf(false) }
    val displayText = when {
        selectedIndex < 0 && allowPlaceholder -> ""
        selectedIndex < 0 -> WorkTypeOptions[0]
        else -> WorkTypeOptions.getOrElse(selectedIndex) { "" }
    }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            value = if (selectedIndex < 0 && allowPlaceholder) "" else displayText,
            onValueChange = {},
            readOnly = true,
            placeholder = if (allowPlaceholder && selectedIndex < 0) {
                { Text("Select...", color = SvbN3) }
            } else {
                null
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SvbWhite,
                unfocusedContainerColor = SvbWhite,
                focusedBorderColor = if (expanded) SvbPrimary2 else SvbN5,
                unfocusedBorderColor = SvbN5,
            ),
            singleLine = true,
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            WorkTypeOptions.forEachIndexed { index, label ->
                DropdownMenuItem(
                    text = {
                        Text(
                            label,
                            color = if (index == selectedIndex) SvbWhite else SvbBlack,
                        )
                    },
                    onClick = {
                        onSelect(index)
                        expanded = false
                    },
                    modifier = Modifier
                        .then(
                            if (index == selectedIndex) {
                                Modifier.background(DropdownSelectedBlue)
                            } else {
                                Modifier
                            },
                        ),
                )
            }
        }
    }
}

@Composable
private fun MachineChipRow(line: String, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SvbWhite)
            .borderThin()
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = line,
            style = MaterialTheme.typography.bodyMedium,
            color = SvbBlack,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Outlined.Close, contentDescription = "Remove", tint = SvbN2, modifier = Modifier.size(18.dp))
        }
    }
}

private fun Modifier.borderThin(): Modifier =
    this.then(Modifier.border(1.dp, SvbN5, RoundedCornerShape(10.dp)))

@Composable
private fun DashedAddRow(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .drawBehind {
                val w = 1.5.dp.toPx()
                val dash = 8.dp.toPx()
                val gap = 6.dp.toPx()
                val r = 10.dp.toPx()
                drawRoundRect(
                    color = SvbN3,
                    topLeft = Offset(w / 2f, w / 2f),
                    size = Size(size.width - w, size.height - w),
                    cornerRadius = CornerRadius(r, r),
                    style = Stroke(width = w, pathEffect = PathEffect.dashPathEffect(floatArrayOf(dash, gap), 0f)),
                )
            }
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = SvbN2)
    }
}

private fun Modifier.zoneDashedBorder(): Modifier = drawBehind {
    val strokeWidth = 1.5.dp.toPx()
    val dash = 8.dp.toPx()
    val gap = 6.dp.toPx()
    val corner = 16.dp.toPx()
    drawRoundRect(
        color = SvbN3,
        topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f),
        size = Size(size.width - strokeWidth, size.height - strokeWidth),
        cornerRadius = CornerRadius(corner, corner),
        style = Stroke(
            width = strokeWidth,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(dash, gap), 0f),
        ),
    )
}

@Composable
private fun UnassignedMachineBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Outlined.Warning,
                contentDescription = null,
                tint = SvbPrimary1,
                modifier = Modifier.size(22.dp),
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "JCB-03 (Op: Anil Verma) — not assigned to any zone",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = SvbN2,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SupervisorFilterCard() {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    val label = FilterZoneOptions[selectedIndex]

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Person,
                    contentDescription = null,
                    tint = SvbN2,
                    modifier = Modifier.size(22.dp),
                )
                Spacer(Modifier.width(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = "Sandeep Kumar",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                    Text(
                        text = "SUP001",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Normal,
                        color = SvbN2,
                    )
                }
            }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.wrapContentWidth(),
            ) {
                Surface(
                    onClick = { expanded = !expanded },
                    modifier = Modifier
                        .menuAnchor()
                        .widthIn(max = 112.dp),
                    shape = RoundedCornerShape(6.dp),
                    color = SvbWhite,
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (expanded) SvbPrimary2 else SvbN5,
                    ),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodySmall,
                            color = SvbN2,
                            maxLines = 1,
                        )
                        Icon(
                            imageVector = Icons.Outlined.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = SvbBlack,
                        )
                    }
                }
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    FilterZoneOptions.forEachIndexed { index, option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    option,
                                    color = if (index == selectedIndex) SvbWhite else SvbBlack,
                                )
                            },
                            onClick = {
                                selectedIndex = index
                                expanded = false
                            },
                            modifier = Modifier
                                .then(
                                    if (index == selectedIndex) {
                                        Modifier.background(DropdownSelectedBlue)
                                    } else {
                                        Modifier
                                    },
                                ),
                        )
                    }
                }
            }
        }
    }
}
