package com.svb.fieldops.presentation.screens.trips

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.outlined.Check
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
import androidx.compose.runtime.remember
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
import com.svb.fieldops.presentation.navigation.popRoleHomeWithHomeTabSelected
import com.svb.fieldops.presentation.navigation.profileTabIndex
import com.svb.fieldops.presentation.navigation.tripsTabIndex
import com.svb.fieldops.presentation.screens.home.HomeCardShape
import com.svb.fieldops.presentation.screens.home.HomeRoleNavigationBar
import com.svb.fieldops.presentation.screens.home.MetricMiniCard
import com.svb.fieldops.presentation.screens.home.SectionTitle
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbCardMuted
import com.svb.fieldops.ui.theme.SvbDivider
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN2
import com.svb.fieldops.ui.theme.SvbN3
import com.svb.fieldops.ui.theme.SvbN7
import com.svb.fieldops.ui.theme.SvbPrimary1
import com.svb.fieldops.ui.theme.SvbPrimary5
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite

/** Trip log “Ltrs Used” summary card tint (~#FFF4D9). */
private val TripFuelSummaryCardBg = Color(0xFFFFF4D9)

private val TripDoneBadgeBg = SvbSuccess.copy(alpha = 0.16f)
private val TripDonePillBg = SvbSuccess.copy(alpha = 0.18f)

private data class TripLogEntry(
    val tripNumber: Int,
    val route: String,
    val timeAndDistance: String,
    val isActive: Boolean,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverTodaysTripsScreen(
    role: UserRole,
    navController: NavHostController,
) {
    val items = bottomNavItemsForRole(role)
    val tripsIdx = requireNotNull(tripsTabIndex(role)) { "DriverTodaysTripsScreen is Driver-only." }
    val fuelIdx = requireNotNull(fuelTabIndex(role)) { "Driver has Fuel tab." }
    val profileIdx = profileTabIndex(role)
    val scroll = rememberScrollState()

    val tripLog = remember {
        listOf(
            TripLogEntry(5, "Zone A → Dump Yard", "2:15 PM • 10 KMS", isActive = true),
            TripLogEntry(4, "Zone A → Dump Yard", "1:45 PM • 10 KMS", isActive = false),
            TripLogEntry(3, "Zone A → Dump Yard", "1:30 PM • 10 KMS", isActive = false),
            TripLogEntry(2, "Zone A → Dump Yard", "1:12 PM • 10 KMS", isActive = false),
            TripLogEntry(1, "Zone A → Dump Yard", "12:55 PM • 10 KMS", isActive = false),
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
                        text = "Today's Trips",
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
                selectedIndex = tripsIdx,
                onSelect = { index ->
                    when {
                        index == tripsIdx -> Unit
                        index == profileIdx ->
                            navController.navigate(MainRoutes.profile(role)) { launchSingleTop = true }
                        index == fuelIdx ->
                            navController.navigate(MainRoutes.fuel(role)) { launchSingleTop = true }
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                MetricMiniCard("5", "Trips", Modifier.weight(1f))
                MetricMiniCard("50", "KMS Total", Modifier.weight(1f))
                MetricMiniCard(
                    value = "6.25",
                    label = "Ltrs Used",
                    modifier = Modifier.weight(1f),
                    containerColor = TripFuelSummaryCardBg,
                    valueColor = SvbPrimary1,
                    labelColor = SvbBlack,
                    valueFontWeight = FontWeight.Bold,
                )
            }
            Spacer(Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = HomeCardShape,
                colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Column(Modifier.padding(18.dp)) {
                    TripLogDetailRow(label = "Machine", value = "TATA 2518")
                    TripLogDivider()
                    TripLogDetailRow(label = "Machine ID", value = "#SVB-T019")
                    TripLogDivider()
                    TripLogDetailRow(label = "Start Odometer", value = "45,230 KMS")
                    TripLogDivider()
                    TripLogDetailRow(label = "End Odometer", value = "45,280 KMS")
                    TripLogDivider()
                    TripLogOdometerPhotosRow()
                    TripLogDivider()
                    TripLogDetailRow(label = "Log out time", value = "09:41 AM")
                    TripLogDivider()
                    TripLogDetailRow(label = "Location", value = "Zone A, SVB Site")
                    TripLogDivider()
                    OdometerRow(label = "Distance Covered", value = "50 KMS", valueEmphasis = true)
                    TripLogDivider()
                    OdometerRow(
                        label = "Est. Fuel (8 km/L)",
                        value = "~6.25 Litres",
                        valueEmphasis = false,
                        valueMuted = true,
                    )
                }
            }
            Spacer(Modifier.height(22.dp))
            SectionTitle("TRIP LOG")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = HomeCardShape,
                colors = CardDefaults.cardColors(containerColor = SvbWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Column(Modifier.fillMaxWidth()) {
                    tripLog.forEachIndexed { i, entry ->
                        TripLogRow(entry)
                        if (i < tripLog.lastIndex) {
                            HorizontalDivider(color = SvbN7, thickness = 1.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TripLogDivider() {
    HorizontalDivider(color = SvbDivider, thickness = 1.dp)
}

@Composable
private fun TripLogDetailRow(
    label: String,
    value: String,
) {
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
            fontWeight = FontWeight.Normal,
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
private fun TripLogOdometerPhotosRow() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Odometer Photos",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal,
            color = SvbN2,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(SvbSuccess),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = SvbWhite,
                    modifier = Modifier.size(14.dp),
                )
            }
            Text(
                text = "Captured",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = SvbSuccess,
            )
        }
    }
}

@Composable
private fun OdometerRow(
    label: String,
    value: String,
    valueEmphasis: Boolean,
    valueMuted: Boolean = false,
) {
    val valueColor = when {
        valueEmphasis -> SvbPrimary1
        valueMuted -> SvbN2
        else -> SvbBlack
    }
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
            style = if (valueEmphasis) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = when {
                valueEmphasis -> FontWeight.Bold
                valueMuted -> FontWeight.Medium
                else -> FontWeight.Bold
            },
            color = valueColor,
        )
    }
}

@Composable
private fun TripLogRow(entry: TripLogEntry) {
    val badgeBg = if (entry.isActive) SvbPrimary5 else TripDoneBadgeBg
    val badgeNumberColor = if (entry.isActive) SvbPrimary1 else SvbSuccess
    val (pillBg, pillText, pillTextColor) = if (entry.isActive) {
        Triple(SvbPrimary5, "Active", SvbPrimary1)
    } else {
        Triple(TripDonePillBg, "Done", SvbSuccess)
    }

    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = { /* later: trip detail */ })
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(badgeBg),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = entry.tripNumber.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = badgeNumberColor,
            )
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = entry.route,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SvbBlack,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = entry.timeAndDistance,
                style = MaterialTheme.typography.bodySmall,
                color = SvbN2,
            )
        }
        Surface(
            shape = RoundedCornerShape(999.dp),
            color = pillBg,
        ) {
            Text(
                text = pillText,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = pillTextColor,
            )
        }
    }
}
