package com.svb.fieldops.presentation.screens.breakdowns

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Schedule
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
import com.svb.fieldops.presentation.navigation.NavStateKeys
import com.svb.fieldops.presentation.navigation.bottomNavItemsForRole
import com.svb.fieldops.presentation.navigation.onBreakdownFlowBottomNavSelect
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
import com.svb.fieldops.ui.theme.SvbPrimary2
import com.svb.fieldops.ui.theme.SvbPrimary5
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite

private val MajorBadgeBg = Color(0xFFFFEBEE)
private val FieldShape = RoundedCornerShape(10.dp)

private data class CloseBreakdownDetail(
    val assetTitle: String,
    val breakdownCode: String,
    val typeLabel: String,
    val isMajor: Boolean,
    val reported: String,
    val downtime: String,
    val serviceEngineer: String,
    val resolutionDate: String,
    val resolutionTime: String,
    val fixedOptions: List<String>,
    val fixedDefaultIndex: Int,
    val costDefault: String,
    val sparesDefault: String,
)

private fun closeDetailFor(breakdownId: String): CloseBreakdownDetail = when (breakdownId) {
    "b1" -> CloseBreakdownDetail(
        assetTitle = "JCB-03 — Excavator",
        breakdownCode = "Breakdown #BD-2026-0415-03",
        typeLabel = "Battery Issue",
        isMajor = true,
        reported = "2h ago by Op Anil",
        downtime = "2h 15m",
        serviceEngineer = "Booked 4:00 PM",
        resolutionDate = "15 Apr 2026",
        resolutionTime = "4:15 PM",
        fixedOptions = listOf("Battery replaced", "Alternator serviced", "Electrical wiring", "Other"),
        fixedDefaultIndex = 0,
        costDefault = "4500",
        sparesDefault = "1x Exide 12V battery, terminal clamps",
    )
    "b2" -> CloseBreakdownDetail(
        assetTitle = "TATA-019 — Dumper",
        breakdownCode = "Breakdown #BD-2026-0415-19",
        typeLabel = "Tyre Puncture",
        isMajor = false,
        reported = "30 min ago by Dr Suresh",
        downtime = "45m",
        serviceEngineer = "Tyre bay — in progress",
        resolutionDate = "15 Apr 2026",
        resolutionTime = "4:30 PM",
        fixedOptions = listOf("Tyre replaced", "Tube patched", "Wheel balanced", "Other"),
        fixedDefaultIndex = 0,
        costDefault = "3200",
        sparesDefault = "1x radial tyre 12.00-20",
    )
    else -> CloseBreakdownDetail(
        assetTitle = "EXC-12 — Excavator",
        breakdownCode = "Breakdown #BD-2026-0415-12",
        typeLabel = "Hose Leakage",
        isMajor = false,
        reported = "1h ago by Op Mohan",
        downtime = "1h 10m",
        serviceEngineer = "Hose kit dispatched",
        resolutionDate = "15 Apr 2026",
        resolutionTime = "4:20 PM",
        fixedOptions = listOf("Hose replaced", "Clamp tightened", "Seal replaced", "Other"),
        fixedDefaultIndex = 0,
        costDefault = "1800",
        sparesDefault = "Hydraulic hose assembly",
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EngineerCloseBreakdownScreen(
    role: UserRole,
    breakdownId: String,
    navController: NavHostController,
) {
    val items = bottomNavItemsForRole(role)
    val homeIdx = 0
    val scroll = rememberScrollState()
    val detail = remember(breakdownId) { closeDetailFor(breakdownId) }

    var fixedIndex by remember(breakdownId) { mutableIntStateOf(detail.fixedDefaultIndex) }
    var costText by remember(breakdownId) { mutableStateOf(detail.costDefault) }
    var sparesText by remember(breakdownId) { mutableStateOf(detail.sparesDefault) }
    var remarks by remember(breakdownId) { mutableStateOf("") }

    fun onResolvedAndClose() {
        navController.previousBackStackEntry?.savedStateHandle?.set(
            NavStateKeys.REMOVE_OPEN_BREAKDOWN_ID,
            breakdownId,
        )
        navController.popBackStack()
    }

    BackHandler { navController.popBackStack() }

    Scaffold(
        containerColor = SvbLoginBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Close Breakdown",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                    navController.onBreakdownFlowBottomNavSelect(role, index)
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
            SummaryCard(detail = detail)
            Spacer(Modifier.height(20.dp))
            Text(
                text = "RESOLUTION DETAILS",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.ExtraBold,
                color = SvbN2,
            )
            Spacer(Modifier.height(10.dp))
            ReadOnlyLabeledField(
                label = "Resolution Date",
                value = detail.resolutionDate,
                leadingIcon = {
                    Icon(Icons.Outlined.CalendarMonth, null, tint = SvbN2, modifier = Modifier.size(20.dp))
                },
            )
            Spacer(Modifier.height(12.dp))
            ReadOnlyLabeledField(
                label = "Resolution Time",
                value = detail.resolutionTime,
                leadingIcon = {
                    Icon(Icons.Outlined.Schedule, null, tint = SvbN2, modifier = Modifier.size(20.dp))
                },
            )
            Spacer(Modifier.height(12.dp))
            WhatFixedDropdown(
                label = "What was fixed?",
                options = detail.fixedOptions,
                selectedIndex = fixedIndex,
                onSelect = { fixedIndex = it },
            )
            Spacer(Modifier.height(12.dp))
            LabeledOutlinedField(
                label = "Cost incurred (₹)",
                value = costText,
                onValueChange = { costText = it },
                singleLine = true,
            )
            Spacer(Modifier.height(16.dp))
            LabeledOutlinedField(
                label = "Spares used",
                value = sparesText,
                onValueChange = { sparesText = it },
                singleLine = false,
                minLines = 2,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Photo of repaired part (mandatory)",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = SvbN2,
            )
            Spacer(Modifier.height(6.dp))
            DashedPhotoCaptureSlot(onClick = { /* later: camera */ })
            Spacer(Modifier.height(12.dp))
            LabeledOutlinedField(
                label = "Remarks",
                value = remarks,
                onValueChange = { remarks = it },
                placeholder = "Any additional notes...",
                singleLine = false,
                minLines = 2,
            )
            Spacer(Modifier.height(16.dp))
            DowntimeBanner(downtime = detail.downtime)
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { onResolvedAndClose() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SvbSuccess,
                    contentColor = SvbWhite,
                ),
                contentPadding = PaddingValues(vertical = 16.dp),
            ) {
                Icon(Icons.Outlined.CheckCircle, contentDescription = null, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Mark as Resolved & Close",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = { /* later: re-assign flow */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, SvbN5),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SvbBlack),
                contentPadding = PaddingValues(vertical = 14.dp),
            ) {
                Icon(Icons.Outlined.Build, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Re-assign to another engineer",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
private fun SummaryCard(detail: CloseBreakdownDetail) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = HomeIconTileShape,
                    color = SvbWhite,
                    modifier = Modifier.size(44.dp),
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Outlined.Build,
                            contentDescription = null,
                            tint = SvbN2,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        text = detail.assetTitle,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = detail.breakdownCode,
                        style = MaterialTheme.typography.bodySmall,
                        color = SvbN2,
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = SvbN5)
            SummaryRow(
                label = "Type",
                valueContent = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = detail.typeLabel,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = SvbBlack,
                        )
                        if (detail.isMajor) {
                            Surface(shape = RoundedCornerShape(6.dp), color = MajorBadgeBg) {
                                Text(
                                    text = "MAJOR",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SvbDanger,
                                )
                            }
                        } else {
                            Surface(shape = RoundedCornerShape(6.dp), color = SvbPrimary5) {
                                Text(
                                    text = "MINOR",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SvbPrimary1,
                                )
                            }
                        }
                    }
                },
            )
            HorizontalDivider(color = SvbN5)
            SummaryRow(
                label = "Reported",
                valueContent = {
                    Text(
                        text = detail.reported,
                        style = MaterialTheme.typography.bodyMedium,
                        color = SvbBlack,
                    )
                },
            )
            HorizontalDivider(color = SvbN5)
            SummaryRow(
                label = "Downtime so far",
                valueContent = {
                    Text(
                        text = detail.downtime,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = SvbDanger,
                    )
                },
            )
            HorizontalDivider(color = SvbN5)
            SummaryRow(
                label = "Service engineer",
                valueContent = {
                    Text(
                        text = detail.serviceEngineer,
                        style = MaterialTheme.typography.bodyMedium,
                        color = SvbBlack,
                    )
                },
            )
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    valueContent: @Composable () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = SvbN2,
            modifier = Modifier.width(120.dp),
        )
        Box(Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
            valueContent()
        }
    }
}

@Composable
private fun ReadOnlyLabeledField(
    label: String,
    value: String,
    leadingIcon: @Composable () -> Unit,
) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = SvbN2,
        )
        Spacer(Modifier.height(6.dp))
        Surface(
            shape = FieldShape,
            color = SvbN7,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leadingIcon()
                Spacer(Modifier.width(10.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = SvbBlack,
                    modifier = Modifier.weight(1f),
                )
                Icon(Icons.Outlined.Lock, contentDescription = null, tint = SvbN2, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WhatFixedDropdown(
    label: String,
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = SvbN2,
        )
        Spacer(Modifier.height(6.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth(),
        ) {
            OutlinedTextField(
                value = options.getOrElse(selectedIndex) { "" },
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                shape = FieldShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SvbN7,
                    unfocusedContainerColor = SvbN7,
                    focusedBorderColor = SvbN5,
                    unfocusedBorderColor = SvbN5,
                ),
                singleLine = true,
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelect(index)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun LabeledOutlinedField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
) {
    Column(modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = SvbN2,
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = if (placeholder != null) {
                { Text(placeholder, color = SvbN2) }
            } else {
                null
            },
            shape = FieldShape,
            singleLine = singleLine,
            minLines = minLines,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SvbN7,
                unfocusedContainerColor = SvbN7,
                focusedBorderColor = SvbN5,
                unfocusedBorderColor = SvbN5,
            ),
        )
    }
}

@Composable
private fun DashedPhotoCaptureSlot(onClick: () -> Unit) {
    val dashColor = SvbN3
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(FieldShape)
            .drawBehind {
                val r = 10.dp.toPx()
                val strokeW = 1.5.dp.toPx()
                val dash = 8.dp.toPx()
                val gap = 6.dp.toPx()
                val innerR = (r - strokeW / 2f).coerceAtLeast(4f)
                drawRoundRect(
                    color = SvbWhite,
                    topLeft = Offset.Zero,
                    size = size,
                    cornerRadius = CornerRadius(r, r),
                )
                drawRoundRect(
                    color = dashColor,
                    topLeft = Offset(strokeW / 2f, strokeW / 2f),
                    size = Size(size.width - strokeW, size.height - strokeW),
                    cornerRadius = CornerRadius(innerR, innerR),
                    style = Stroke(
                        width = strokeW,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(dash, gap), 0f),
                    ),
                )
            }
            .clickable(onClick = onClick)
            .padding(vertical = 22.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Outlined.CameraAlt,
                contentDescription = null,
                tint = SvbN2,
                modifier = Modifier.size(28.dp),
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Tap to capture repaired part.",
                style = MaterialTheme.typography.bodyMedium,
                color = SvbN2,
            )
        }
    }
}

@Composable
private fun DowntimeBanner(downtime: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbPrimary5.copy(alpha = 0.55f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(SvbWhite),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Outlined.Warning,
                    contentDescription = null,
                    tint = SvbPrimary1,
                    modifier = Modifier.size(24.dp),
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = "TOTAL DOWNTIME WILL BE LOGGED",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = SvbN2,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = downtime,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = SvbPrimary1,
                )
            }
        }
    }
}
