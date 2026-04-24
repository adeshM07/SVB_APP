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
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.svb.fieldops.presentation.navigation.bottomNavItemsForRole
import com.svb.fieldops.presentation.navigation.fuelTabIndex
import com.svb.fieldops.presentation.navigation.popRoleHomeWithHomeTabSelected
import com.svb.fieldops.presentation.navigation.profileTabIndex
import com.svb.fieldops.presentation.navigation.reportsTabIndex
import com.svb.fieldops.presentation.navigation.tripsTabIndex
import com.svb.fieldops.presentation.navigation.verifyTabIndex
import com.svb.fieldops.presentation.screens.home.HomeCardShape
import com.svb.fieldops.presentation.screens.home.HomeRoleNavigationBar
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbDanger
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN2
import com.svb.fieldops.ui.theme.SvbN5
import com.svb.fieldops.ui.theme.SvbN7
import com.svb.fieldops.ui.theme.SvbPrimary2
import com.svb.fieldops.ui.theme.SvbPrimary5
import com.svb.fieldops.ui.theme.SvbWhite

private enum class ReportSeverity { Minor, Major }

private val SubmitCoral = Color(0xFFFF5A5F)
private val SeverityCardShape = RoundedCornerShape(14.dp)
private val TypeRowShape = RoundedCornerShape(12.dp)

private val BreakdownTypes = listOf(
    "Tyre Puncture",
    "Low HSD",
    "Hose Leakage",
    "Battery Issue",
    "Others",
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupervisorReportBreakdownScreen(
    role: UserRole,
    navController: NavHostController,
) {
    val items = bottomNavItemsForRole(role)
    val homeIdx = 0
    val fuelIdx = requireNotNull(fuelTabIndex(role))
    val profileIdx = profileTabIndex(role)
    val scroll = rememberScrollState()

    var isMajorSeverity by rememberSaveable { mutableStateOf(false) }
    val severity = if (isMajorSeverity) ReportSeverity.Major else ReportSeverity.Minor
    var typeIndex by rememberSaveable { mutableIntStateOf(0) }
    var notes by rememberSaveable { mutableStateOf("") }

    BackHandler { navController.popRoleHomeWithHomeTabSelected() }

    Scaffold(
        containerColor = SvbLoginBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Report Breakdown",
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
                    if (role == UserRole.Supervisor) {
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
                        UserRole.Supervisor -> {
                            val verifyIdx = requireNotNull(verifyTabIndex(role))
                            val reportsIdx = requireNotNull(reportsTabIndex(role))
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
            Text(
                text = "Select severity and type of breakdown",
                style = MaterialTheme.typography.bodyMedium,
                color = SvbN2,
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SeverityChoiceCard(
                    title = "Minor",
                    subtitle = "Wait for repair",
                    selected = severity == ReportSeverity.Minor,
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Warning,
                            contentDescription = null,
                            tint = SvbPrimary2,
                            modifier = Modifier.size(28.dp),
                        )
                    },
                    onClick = { isMajorSeverity = false },
                    modifier = Modifier.weight(1f),
                )
                SeverityChoiceCard(
                    title = "Major",
                    subtitle = "Vehicle swap needed",
                    selected = severity == ReportSeverity.Major,
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Error,
                            contentDescription = null,
                            tint = SvbDanger,
                            modifier = Modifier.size(28.dp),
                        )
                    },
                    onClick = { isMajorSeverity = true },
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Type of Breakdown",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SvbBlack,
            )
            Spacer(Modifier.height(10.dp))
            BreakdownTypes.forEachIndexed { index, label ->
                if (index > 0) Spacer(Modifier.height(8.dp))
                BreakdownTypeRow(
                    label = label,
                    selected = typeIndex == index,
                    onClick = { typeIndex = index },
                )
            }
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Additional Notes",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SvbBlack,
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "Describe the issue...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = SvbN2,
                    )
                },
                minLines = 4,
                shape = HomeCardShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SvbN7,
                    unfocusedContainerColor = SvbN7,
                    focusedBorderColor = SvbN5,
                    unfocusedBorderColor = SvbN5,
                ),
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { navController.popRoleHomeWithHomeTabSelected() },
                modifier = Modifier.fillMaxWidth(),
                shape = HomeCardShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SubmitCoral,
                    contentColor = SvbWhite,
                ),
                contentPadding = PaddingValues(vertical = 16.dp),
            ) {
                Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = null, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Submit Breakdown Report",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun SeverityChoiceCard(
    title: String,
    subtitle: String,
    selected: Boolean,
    leadingIcon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val border = if (selected) {
        BorderStroke(2.dp, SvbPrimary2)
    } else {
        BorderStroke(1.dp, SvbN5)
    }
    val bg = if (selected) SvbPrimary5 else SvbWhite
    Surface(
        modifier = modifier
            .clip(SeverityCardShape)
            .clickable(onClick = onClick),
        shape = SeverityCardShape,
        color = bg,
        border = border,
    ) {
        Column(
            Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            leadingIcon()
            Spacer(Modifier.height(8.dp))
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
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun BreakdownTypeRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val border = if (selected) BorderStroke(2.dp, SvbPrimary2) else BorderStroke(1.dp, SvbN5)
    val bg = if (selected) SvbPrimary5 else SvbN7
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(TypeRowShape)
            .clickable(onClick = onClick),
        shape = TypeRowShape,
        color = bg,
        border = border,
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = SvbPrimary2,
                    unselectedColor = SvbN5,
                ),
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                color = SvbBlack,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
