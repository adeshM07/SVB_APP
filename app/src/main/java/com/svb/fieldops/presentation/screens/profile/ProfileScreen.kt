package com.svb.fieldops.presentation.screens.profile

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.svb.fieldops.domain.model.UserRole
import com.svb.fieldops.presentation.navigation.MainRoutes
import com.svb.fieldops.presentation.navigation.approvalsTabIndex
import com.svb.fieldops.presentation.navigation.bottomNavItemsForRole
import com.svb.fieldops.presentation.navigation.dieselTabIndex
import com.svb.fieldops.presentation.navigation.dprTabIndex
import com.svb.fieldops.presentation.navigation.fuelTabIndex
import com.svb.fieldops.presentation.navigation.loadingsTabIndex
import com.svb.fieldops.presentation.navigation.tripsTabIndex
import com.svb.fieldops.presentation.navigation.verifyTabIndex
import com.svb.fieldops.presentation.navigation.popRoleHomeWithHomeTabSelected
import com.svb.fieldops.presentation.navigation.profileTabIndex
import com.svb.fieldops.presentation.navigation.reportsTabIndex
import com.svb.fieldops.presentation.screens.home.HomeCardShape
import com.svb.fieldops.presentation.screens.home.HomeRoleNavigationBar
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbCardMuted
import com.svb.fieldops.ui.theme.SvbDanger
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN2
import com.svb.fieldops.ui.theme.SvbN3
import com.svb.fieldops.ui.theme.SvbN5
import com.svb.fieldops.ui.theme.SvbPrimary2
import com.svb.fieldops.ui.theme.SvbRoseTint
import com.svb.fieldops.ui.theme.SvbWhite

/** White chip behind M3 icons on profile menu rows (mockup ~8–10dp radius). */
private val ProfileMenuIconTileShape = RoundedCornerShape(10.dp)

private data class ProfileDemo(
    val initials: String,
    val fullName: String,
    val roleLine: String,
    val shifts: String,
    val attendance: String,
    val rating: String,
)

private fun profileDemoFor(role: UserRole): ProfileDemo = when (role) {
    UserRole.Driver -> ProfileDemo(
        initials = "SY",
        fullName = "Suresh Yadav",
        roleLine = "Driver • DVR001",
        shifts = "142",
        attendance = "97%",
        rating = "4.7",
    )
    UserRole.Operator -> ProfileDemo(
        initials = "RK",
        fullName = "Rajesh Kumar",
        roleLine = "Operator • OPR001",
        shifts = "128",
        attendance = "99%",
        rating = "4.6",
    )
    UserRole.Supervisor -> ProfileDemo(
        initials = "SK",
        fullName = "Sandeep Kumar",
        roleLine = "Supervisor • SUP001",
        shifts = "164",
        attendance = "98%",
        rating = "4.8",
    )
    UserRole.Engineer -> ProfileDemo(
        initials = "AV",
        fullName = "Anil Verma",
        roleLine = "Site Engineer • ENG001",
        shifts = "156",
        attendance = "98%",
        rating = "4.8",
    )
}

@Composable
fun ProfileScreen(
    role: UserRole,
    navController: NavHostController,
) {
    val demo = profileDemoFor(role)
    val items = bottomNavItemsForRole(role)
    val profileIdx = profileTabIndex(role)
    val approvalsIdx = approvalsTabIndex(role)
    val dieselIdx = dieselTabIndex(role)
    val dprIdx = dprTabIndex(role)
    val fuelIdx = fuelTabIndex(role)
    val loadingsIdx = loadingsTabIndex(role)
    val tripsIdx = tripsTabIndex(role)
    val verifyIdx = verifyTabIndex(role)
    val reportsIdx = reportsTabIndex(role)
    val scroll = rememberScrollState()

    Scaffold(
        containerColor = SvbLoginBackground,
        bottomBar = {
            HomeRoleNavigationBar(
                items = items,
                selectedIndex = profileIdx,
                onSelect = { index ->
                    when {
                        index == profileIdx -> Unit
                        loadingsIdx != null && index == loadingsIdx ->
                            navController.navigate(MainRoutes.loadings(role)) { launchSingleTop = true }
                        tripsIdx != null && index == tripsIdx ->
                            navController.navigate(MainRoutes.trips(role)) { launchSingleTop = true }
                        verifyIdx != null && index == verifyIdx ->
                            navController.navigate(MainRoutes.verifyStartDuty(role)) { launchSingleTop = true }
                        reportsIdx != null && index == reportsIdx ->
                            navController.navigate(MainRoutes.reports(role)) { launchSingleTop = true }
                        fuelIdx != null && index == fuelIdx ->
                            navController.navigate(MainRoutes.fuel(role)) { launchSingleTop = true }
                        approvalsIdx != null && index == approvalsIdx ->
                            navController.navigate(MainRoutes.approvals(role)) { launchSingleTop = true }
                        dieselIdx != null && index == dieselIdx ->
                            navController.navigate(MainRoutes.diesel(role)) { launchSingleTop = true }
                        dprIdx != null && index == dprIdx ->
                            navController.navigate(MainRoutes.dpr(role)) { launchSingleTop = true }
                        else -> navController.popRoleHomeWithHomeTabSelected()
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scroll),
        ) {
            ProfileHeaderYellow(
                initials = demo.initials,
                fullName = demo.fullName,
                roleLine = demo.roleLine,
                shifts = demo.shifts,
                attendance = demo.attendance,
                rating = demo.rating,
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                ProfileMenuCard(
                    icon = Icons.Outlined.Person,
                    title = "My Details",
                    onClick = {},
                )
                ProfileMenuCard(
                    icon = Icons.Outlined.History,
                    title = "Shift History",
                    onClick = {},
                )
                ProfileMenuCard(
                    icon = Icons.Outlined.LocalShipping,
                    title = "Machine History",
                    onClick = {},
                )
                ProfileMenuCard(
                    icon = Icons.Outlined.LocalGasStation,
                    title = "Fuel Log",
                    onClick = {},
                )
                ProfileMenuCard(
                    icon = Icons.Outlined.Notifications,
                    title = "Notifications",
                    onClick = {},
                )
                ProfileMenuCard(
                    icon = Icons.Outlined.Settings,
                    title = "Settings",
                    onClick = {},
                )
                ProfileLogoutCard(
                    onClick = { /* later: sign out / clear session */ },
                )
            }
        }
    }
}

@Composable
private fun ProfileHeaderYellow(
    initials: String,
    fullName: String,
    roleLine: String,
    shifts: String,
    attendance: String,
    rating: String,
) {
    val headerShape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(headerShape)
            .background(SvbPrimary2)
            .padding(top = 28.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(SvbWhite),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = initials,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = SvbBlack,
            )
        }
        Spacer(Modifier.height(14.dp))
        Text(
            text = fullName,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = SvbBlack,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = roleLine,
            style = MaterialTheme.typography.bodyMedium,
            color = SvbN2,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(HomeCardShape)
                .background(SvbWhite.copy(alpha = 0.52f))
                .padding(vertical = 16.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProfileStatCell(shifts, "Shifts", Modifier.weight(1f))
            Box(
                Modifier
                    .width(1.dp)
                    .height(36.dp)
                    .background(SvbN5),
            )
            ProfileStatCell(attendance, "Attendance", Modifier.weight(1f))
            Box(
                Modifier
                    .width(1.dp)
                    .height(36.dp)
                    .background(SvbN5),
            )
            ProfileStatCell(rating, "Rating", Modifier.weight(1f))
        }
    }
}

@Composable
private fun ProfileStatCell(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = SvbBlack,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = SvbN2,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ProfileMenuCard(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = ProfileMenuIconTileShape,
                color = SvbWhite,
                modifier = Modifier.size(44.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = SvbBlack, modifier = Modifier.size(22.dp))
                }
            }
            Spacer(Modifier.width(14.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = SvbBlack,
                modifier = Modifier.weight(1f),
            )
            Icon(
                Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = SvbN3,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

@Composable
private fun ProfileLogoutCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = ProfileMenuIconTileShape,
                color = SvbRoseTint,
                modifier = Modifier.size(44.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.AutoMirrored.Outlined.Logout,
                        contentDescription = null,
                        tint = SvbDanger,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
            Spacer(Modifier.width(14.dp))
            Text(
                text = "Logout",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = SvbDanger,
                modifier = Modifier.weight(1f),
            )
            Icon(
                Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = SvbDanger.copy(alpha = 0.55f),
                modifier = Modifier.size(22.dp),
            )
        }
    }
}
