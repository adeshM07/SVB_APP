package com.svb.fieldops.presentation.screens.loadings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.QrCode2
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.svb.fieldops.presentation.screens.home.HomeCardShape
import com.svb.fieldops.presentation.screens.home.HomeRoleNavigationBar
import com.svb.fieldops.presentation.screens.home.MetricMiniCard
import com.svb.fieldops.presentation.screens.home.SectionTitle
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbCardMuted
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN2
import com.svb.fieldops.ui.theme.SvbN7
import com.svb.fieldops.ui.theme.SvbPrimary2
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite

private data class TodayLogRow(
    val idLabel: String,
    val idColor: Color,
    val startTime: String,
    val duration: String,
    val status: String,
    val statusColor: Color,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperatorLoadingsScreen(
    role: UserRole,
    navController: NavHostController,
) {
    val items = bottomNavItemsForRole(role)
    val loadingsIdx = requireNotNull(loadingsTabIndex(role)) { "OperatorLoadingsScreen is Operator-only." }
    val fuelIdx = requireNotNull(fuelTabIndex(role)) { "Operator has Fuel tab." }
    val profileIdx = profileTabIndex(role)
    val scroll = rememberScrollState()

    val todayLog = remember {
        listOf(
            TodayLogRow("#24", SvbPrimary2, "2:15 PM", "2m 14s", "Active", SvbPrimary2),
            TodayLogRow("#23", SvbSuccess, "1:45 PM", "1m 52s", "Done", SvbSuccess),
            TodayLogRow("#22", SvbSuccess, "1:30 PM", "2m 05s", "Done", SvbSuccess),
            TodayLogRow("#21", SvbSuccess, "1:12 PM", "2m 20s", "Done", SvbSuccess),
            TodayLogRow("#20", SvbSuccess, "12:55 PM", "1m 48s", "Done", SvbSuccess),
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
                        text = "Loadings",
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
                selectedIndex = loadingsIdx,
                onSelect = { index ->
                    when {
                        index == loadingsIdx -> Unit
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = HomeCardShape,
                colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Column(Modifier.padding(18.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top,
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = "CURRENT LOADING",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = SvbN2,
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "#24 • Started 2:15 PM",
                                style = MaterialTheme.typography.bodySmall,
                                color = SvbN2,
                            )
                        }
                        Text(
                            text = "02:14",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = SvbBlack,
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { /* later: log loading */ },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SvbPrimary2,
                            contentColor = SvbBlack,
                        ),
                        contentPadding = PaddingValues(vertical = 14.dp, horizontal = 16.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Icon(
                                Icons.Outlined.LocalShipping,
                                contentDescription = null,
                                modifier = Modifier.size(22.dp),
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = "Tipper Left — Log Loading",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = { /* later: associate QR */ }),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            Icons.Outlined.QrCode2,
                            contentDescription = null,
                            tint = SvbN2,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Associate tipper QR",
                            style = MaterialTheme.typography.bodySmall,
                            color = SvbN2,
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                MetricMiniCard("24", "Loadings", Modifier.weight(1f))
                MetricMiniCard("2:10", "Avg (min)", Modifier.weight(1f))
                MetricMiniCard("6:20", "Hours", Modifier.weight(1f))
            }
            Spacer(Modifier.height(22.dp))
            SectionTitle("TODAY'S LOG")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = HomeCardShape,
                colors = CardDefaults.cardColors(containerColor = SvbWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Column(Modifier.fillMaxWidth()) {
                    todayLog.forEachIndexed { i, row ->
                        TodayLogListRow(row)
                        if (i < todayLog.lastIndex) {
                            HorizontalDivider(color = SvbN7, thickness = 1.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TodayLogListRow(row: TodayLogRow) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = { /* later: row detail */ })
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = row.idLabel,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = row.idColor,
            modifier = Modifier.width(48.dp),
        )
        Text(
            text = row.startTime,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = SvbBlack,
            modifier = Modifier.width(72.dp),
        )
        Text(
            text = row.duration,
            style = MaterialTheme.typography.bodySmall,
            color = SvbN2,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = row.status,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = row.statusColor,
        )
    }
}
