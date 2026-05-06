package com.svb.fieldops.presentation.screens.diesel

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
import com.svb.fieldops.presentation.screens.home.HomeRoleNavigationBar
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN2
import com.svb.fieldops.ui.theme.SvbN5
import com.svb.fieldops.ui.theme.SvbN7
import com.svb.fieldops.ui.theme.SvbPrimary1
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite

private val AddPurchaseDateStripBg = SvbN7
private val AddPurchasePhotoBoxBg = SvbN7.copy(alpha = 0.35f)
private val AddPurchaseNewStockBg = SvbSuccess.copy(alpha = 0.10f)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPurchaseScreen(
    role: UserRole,
    navController: NavHostController,
) {
    val items = bottomNavItemsForRole(role)
    val dieselIdx = requireNotNull(dieselTabIndex(role))
    val approvalsIdx = requireNotNull(approvalsTabIndex(role))
    val dprIdx = requireNotNull(dprTabIndex(role))
    val profileIdx = profileTabIndex(role)
    val scroll = rememberScrollState()

    BackHandler { navController.popBackStack() }

    Scaffold(
        containerColor = SvbLoginBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Purchase",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back", tint = SvbBlack)
                    }
                },
                actions = {
                    IconButton(onClick = { /* later: quick add */ }) {
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .verticalScroll(scroll)
                .padding(horizontal = 20.dp)
                .padding(top = 10.dp, bottom = 24.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = AddPurchaseDateStripBg,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.CalendarToday, contentDescription = null, tint = SvbN2, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "6 May 2026 • Today",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = SvbBlack,
                        modifier = Modifier.weight(1f),
                    )
                    Icon(Icons.Outlined.Lock, contentDescription = null, tint = SvbN2, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(Modifier.height(18.dp))
            AddPurchaseField(
                label = "Quantity Purchased (Litres) — How much purchased?",
                value = "1800",
                boldValue = true,
            )
            Spacer(Modifier.height(14.dp))
            AddPurchaseField(
                label = "Vendor — Select supplier",
                value = "ABC Petroleum",
            )
            Spacer(Modifier.height(14.dp))
            AddPurchaseField(
                label = "Invoice Number",
                value = "INV-2026-0416-08",
            )

            Spacer(Modifier.height(14.dp))
            Text(
                text = "Photo of Invoice (mandatory)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SvbBlack,
            )
            Spacer(Modifier.height(8.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .drawBehind {
                        val stroke = 2.dp.toPx()
                        val inset = stroke * 0.5f
                        drawRoundRect(
                            color = SvbSuccess,
                            topLeft = Offset(inset, inset),
                            size = Size(size.width - inset * 2, size.height - inset * 2),
                            style = Stroke(
                                width = stroke,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10.dp.toPx(), 7.dp.toPx()), 0f),
                            ),
                            cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx()),
                        )
                    },
                shape = RoundedCornerShape(12.dp),
                color = AddPurchasePhotoBoxBg,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(Icons.Outlined.Description, contentDescription = null, tint = SvbSuccess, modifier = Modifier.size(30.dp))
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Invoice photo captured",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = SvbBlack,
                    )
                }
            }

            Spacer(Modifier.height(14.dp))
            Text(
                text = "Remarks (optional)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SvbBlack,
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Any notes...", color = SvbN2) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SvbN7,
                    unfocusedContainerColor = SvbN7,
                    focusedBorderColor = SvbN5,
                    unfocusedBorderColor = SvbN5,
                ),
            )

            Spacer(Modifier.height(14.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = AddPurchaseNewStockBg,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                    Text(
                        text = "NEW STOCK",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = SvbN2,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "2,000 + 1,800 = 3,800 L",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SvbSuccess,
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SvbPrimary1,
                    contentColor = SvbBlack,
                ),
            ) {
                Text(
                    text = "Save Purchase Entry",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun AddPurchaseField(
    label: String,
    value: String,
    boldValue: Boolean = false,
) {
    Text(
        text = label,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.SemiBold,
        color = SvbBlack,
    )
    Spacer(Modifier.height(8.dp))
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = SvbN7,
        border = BorderStroke(1.dp, SvbN5),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = value,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
            style = if (boldValue) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium,
            fontWeight = if (boldValue) FontWeight.ExtraBold else FontWeight.Medium,
            color = SvbBlack,
        )
    }
}
