package com.svb.fieldops.presentation.screens.approvals

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PrecisionManufacturing
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.svb.fieldops.ui.theme.SvbDanger
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN2
import com.svb.fieldops.ui.theme.SvbN5
import com.svb.fieldops.ui.theme.SvbN7
import com.svb.fieldops.ui.theme.SvbPrimary1
import com.svb.fieldops.ui.theme.SvbPrimary2
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite

private val VerifiedSupBadgeBg = Color(0xFFE8F5E9)
private val PendingSupBadgeBg = Color(0xFFFFF6E5)
private val PendingSupBadgeText = SvbPrimary1
private val SwapCardBorder = Color(0xFFFFC107)
private val SwapCardBg = Color(0xFFFFF4D9)
/** Trailing chevron on swap row (mockup ~#FFB800 on cream card). */
private val SwapChevronYellow = Color(0xFFFFB800)
/** OTHER card info strip: white surface, tan “i”, blue-grey body (mockup). */
private val OtherInfoBannerIconTint = Color(0xFFC68642)
private val OtherInfoBannerTextColor = Color(0xFF455A64)

/** Whole-card fade after approve/decline (matches processed mockups). */
private const val ProcessedApprovalCardAlpha = 0.52f

private enum class SupBadgeKind { VerifiedBySup, PendingSup }

private enum class LineDecision { Pending, Approved, Declined }

private data class HsdRequestLine(
    val id: String,
    val title: String,
    val subtitle: String,
    val supBadge: SupBadgeKind,
    val footer: String,
    val machineIcon: ImageVector,
)

private data class SwapRequestLine(
    val id: String,
    val routeTitle: String,
    val subtitle: String,
    val fromId: String,
    val toId: String,
    val fromIcon: ImageVector,
    val toIcon: ImageVector,
    val fromTint: Color,
    val toTint: Color,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EngineerApprovalsScreen(
    role: UserRole,
    navController: NavHostController,
) {
    val items = bottomNavItemsForRole(role)
    val approvalsIdx = requireNotNull(approvalsTabIndex(role)) { "EngineerApprovalsScreen is Engineer-only." }
    val dieselIdx = requireNotNull(dieselTabIndex(role)) { "Engineer has Diesel tab." }
    val dprIdx = requireNotNull(dprTabIndex(role)) { "Engineer has DPR tab." }
    val profileIdx = profileTabIndex(role)
    val scroll = rememberScrollState()

    var hsdDecisions by remember {
        mutableStateOf(
            mapOf(
                "hsd1" to LineDecision.Pending,
                "hsd2" to LineDecision.Pending,
                "hsd3" to LineDecision.Pending,
                "hsd4" to LineDecision.Pending,
                "hsd5" to LineDecision.Pending,
            ),
        )
    }

    var swapDecisions by remember {
        mutableStateOf(
            mapOf(
                "swap1" to LineDecision.Pending,
                "swap2" to LineDecision.Pending,
            ),
        )
    }

    var expandedSwapId by rememberSaveable { mutableStateOf<String?>(null) }

    val hsdLines = remember {
        listOf(
            HsdRequestLine(
                id = "hsd1",
                title = "JCB-07 — #SVB-M042",
                subtitle = "by OPP — Rajesh Kumar • 50 L",
                supBadge = SupBadgeKind.VerifiedBySup,
                footer = "10 min ago • Tap for details",
                machineIcon = Icons.Outlined.PrecisionManufacturing,
            ),
            HsdRequestLine(
                id = "hsd2",
                title = "TATA-019 — #SVB-M018",
                subtitle = "by OPP — Suresh Yadav • 100 L",
                supBadge = SupBadgeKind.PendingSup,
                footer = "25 min ago • Tap for details",
                machineIcon = Icons.Outlined.LocalShipping,
            ),
            HsdRequestLine(
                id = "hsd3",
                title = "HYD-03 — #SVB-M031",
                subtitle = "by OPP — Vikram Singh • 30 L",
                supBadge = SupBadgeKind.VerifiedBySup,
                footer = "32 min ago • Tap for details",
                machineIcon = Icons.Outlined.PrecisionManufacturing,
            ),
            HsdRequestLine(
                id = "hsd4",
                title = "CAT-412 — #SVB-M055",
                subtitle = "by OPP — Amit Patel • 80 L",
                supBadge = SupBadgeKind.PendingSup,
                footer = "48 min ago • Tap for details",
                machineIcon = Icons.Outlined.PrecisionManufacturing,
            ),
            HsdRequestLine(
                id = "hsd5",
                title = "CMD-09 — #SVB-M060",
                subtitle = "by OPP — Ravi Nair • 45 L",
                supBadge = SupBadgeKind.VerifiedBySup,
                footer = "1 hr ago • Tap for details",
                machineIcon = Icons.Outlined.LocalShipping,
            ),
        )
    }

    val swapLines = remember {
        listOf(
            SwapRequestLine(
                id = "swap1",
                routeTitle = "JCB-07 → JCB-11",
                subtitle = "by OPP Rajesh • Hydraulic leak • 15 min ago",
                fromId = "JCB-07",
                toId = "JCB-11",
                fromIcon = Icons.Outlined.PrecisionManufacturing,
                toIcon = Icons.Outlined.PrecisionManufacturing,
                fromTint = SvbDanger,
                toTint = SvbSuccess,
            ),
            SwapRequestLine(
                id = "swap2",
                routeTitle = "TATA-019 → TATA-031",
                subtitle = "by SUP Suresh • Tyre puncture • 42 min ago",
                fromId = "TATA-019",
                toId = "TATA-031",
                fromIcon = Icons.Outlined.LocalShipping,
                toIcon = Icons.Outlined.LocalShipping,
                fromTint = SvbDanger,
                toTint = SvbSuccess,
            ),
        )
    }

    val pendingHsdCount = hsdLines.count { hsdDecisions[it.id] == LineDecision.Pending }
    val pendingSwapCount = swapLines.count { swapDecisions[it.id] == LineDecision.Pending }
    val pendingTotal = pendingHsdCount + pendingSwapCount

    BackHandler {
        if (expandedSwapId != null) {
            expandedSwapId = null
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
                        text = "Approvals",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (expandedSwapId != null) {
                                expandedSwapId = null
                            } else {
                                navController.popRoleHomeWithHomeTabSelected()
                            }
                        },
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = SvbBlack,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(MainRoutes.notifications(role)) { launchSingleTop = true } }) {
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
                selectedIndex = approvalsIdx,
                onSelect = { index ->
                    when {
                        index == approvalsIdx -> Unit
                        index == profileIdx ->
                            navController.navigate(MainRoutes.profile(role)) { launchSingleTop = true }
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
            Text(
                text = "$pendingTotal pending requests",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = SvbBlack,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Waiting for your approval",
                style = MaterialTheme.typography.bodyMedium,
                color = SvbN2,
            )
            Spacer(Modifier.height(20.dp))

            ApprovalsSectionHeaderRow(
                title = "HSD REQUESTS (${hsdLines.size})",
            )
            Spacer(Modifier.height(10.dp))
            hsdLines.forEach { line ->
                val decision = hsdDecisions[line.id] ?: LineDecision.Pending
                HsdRequestCard(
                    line = line,
                    decision = decision,
                    onApprove = {
                        hsdDecisions = hsdDecisions + (line.id to LineDecision.Approved)
                    },
                    onDecline = {
                        hsdDecisions = hsdDecisions + (line.id to LineDecision.Declined)
                    },
                )
                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(8.dp))
            ApprovalsSectionHeaderRow(
                title = "SWAP REQUESTS (${swapLines.size})",
                trailing = {
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = SvbPrimary1,
                    ) {
                        Text(
                            text = "TAP CARD FOR DETAILS",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = SvbWhite,
                        )
                    }
                },
            )
            Spacer(Modifier.height(10.dp))
            swapLines.forEach { line ->
                val decision = swapDecisions[line.id] ?: LineDecision.Pending
                val expanded = expandedSwapId == line.id
                SwapRequestCard(
                    line = line,
                    decision = decision,
                    expanded = expanded,
                    onHeaderClick = {
                        expandedSwapId = if (expanded) null else line.id
                    },
                    onApprove = {
                        swapDecisions = swapDecisions + (line.id to LineDecision.Approved)
                    },
                    onDecline = {
                        swapDecisions = swapDecisions + (line.id to LineDecision.Declined)
                    },
                )
                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(8.dp))
            ApprovalsSectionHeaderRow(title = "OTHER (1)")
            Spacer(Modifier.height(10.dp))
            OtherRequestCard()
        }
    }
}

@Composable
private fun ApprovalsSectionHeaderRow(
    title: String,
    trailing: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            color = SvbN2,
        )
        trailing?.invoke()
    }
}

@Composable
private fun HsdRequestCard(
    line: HsdRequestLine,
    decision: LineDecision,
    onApprove: () -> Unit,
    onDecline: () -> Unit,
) {
    val processed = decision != LineDecision.Pending
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (processed) ProcessedApprovalCardAlpha else 1f),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
            ) {
                Surface(
                    shape = CircleShape,
                    color = SvbN7,
                    modifier = Modifier.size(44.dp),
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            line.machineIcon,
                            contentDescription = null,
                            tint = SvbBlack,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = line.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = line.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = SvbN2,
                    )
                }
                SupVerificationBadge(line.supBadge)
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = line.footer,
                style = MaterialTheme.typography.bodySmall,
                color = SvbN2,
            )
            when (decision) {
                LineDecision.Pending -> {
                    Spacer(Modifier.height(14.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Button(
                            onClick = onApprove,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SvbSuccess,
                                contentColor = SvbWhite,
                            ),
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Icon(Icons.Outlined.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Approve", fontWeight = FontWeight.SemiBold)
                        }
                        OutlinedButton(
                            onClick = onDecline,
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, SvbDanger),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = SvbDanger),
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Icon(Icons.Outlined.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Decline", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
                LineDecision.Approved -> {
                    Spacer(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Check,
                            contentDescription = null,
                            tint = SvbSuccess,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Approved",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = SvbSuccess,
                        )
                    }
                }
                LineDecision.Declined -> {
                    Spacer(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Close,
                            contentDescription = null,
                            tint = SvbDanger,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Declined",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = SvbDanger,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SupVerificationBadge(kind: SupBadgeKind) {
    val (label, bg, fg) = when (kind) {
        SupBadgeKind.VerifiedBySup -> Triple(
            "VERIFIED BY SUP",
            VerifiedSupBadgeBg,
            SvbSuccess,
        )
        SupBadgeKind.PendingSup -> Triple(
            "PENDING SUP",
            PendingSupBadgeBg,
            PendingSupBadgeText,
        )
    }
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bg,
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            color = fg,
        )
    }
}

@Composable
private fun SwapRequestCard(
    line: SwapRequestLine,
    decision: LineDecision,
    expanded: Boolean,
    onHeaderClick: () -> Unit,
    onApprove: () -> Unit,
    onDecline: () -> Unit,
) {
    val processed = decision != LineDecision.Pending
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (processed) ProcessedApprovalCardAlpha else 1f),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SwapCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, SwapCardBorder),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onHeaderClick),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    shape = CircleShape,
                    color = SvbWhite,
                    modifier = Modifier.size(44.dp),
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Outlined.SwapHoriz,
                            contentDescription = null,
                            tint = SvbPrimary2,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = line.routeTitle,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = line.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = SvbN2,
                    )
                }
                Icon(
                    Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    tint = SwapChevronYellow,
                    modifier = Modifier.size(22.dp),
                )
            }
            if (expanded) {
                Spacer(Modifier.height(14.dp))
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = SvbWhite,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                line.fromIcon,
                                contentDescription = null,
                                tint = line.fromTint,
                                modifier = Modifier.size(36.dp),
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = line.fromId,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = line.fromTint,
                            )
                        }
                        Icon(
                            Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                            contentDescription = null,
                            tint = SwapChevronYellow,
                            modifier = Modifier.size(24.dp),
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                line.toIcon,
                                contentDescription = null,
                                tint = line.toTint,
                                modifier = Modifier.size(36.dp),
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = line.toId,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = line.toTint,
                            )
                        }
                    }
                }
                when (decision) {
                    LineDecision.Pending -> {
                        Spacer(Modifier.height(14.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Button(
                                onClick = onApprove,
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SvbSuccess,
                                    contentColor = SvbWhite,
                                ),
                                shape = RoundedCornerShape(12.dp),
                            ) {
                                Icon(Icons.Outlined.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Approve", fontWeight = FontWeight.SemiBold)
                            }
                            OutlinedButton(
                                onClick = onDecline,
                                modifier = Modifier.weight(1f),
                                border = BorderStroke(1.dp, SvbDanger),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = SvbDanger),
                                shape = RoundedCornerShape(12.dp),
                            ) {
                                Icon(Icons.Outlined.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Decline", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                    LineDecision.Approved -> {
                        Spacer(Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Check,
                                contentDescription = null,
                                tint = SvbSuccess,
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Approved",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = SvbSuccess,
                            )
                        }
                    }
                    LineDecision.Declined -> {
                        Spacer(Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Close,
                                contentDescription = null,
                                tint = SvbDanger,
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Declined",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = SvbDanger,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OtherRequestCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
            ) {
                Surface(
                    shape = HomeIconTileShape,
                    color = SvbWhite,
                    modifier = Modifier.size(44.dp),
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Outlined.Description,
                            contentDescription = null,
                            tint = SvbN2,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "PR-2104 — Bulk HSD 2000 L",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "to GM • Submitted by you",
                        style = MaterialTheme.typography.bodySmall,
                        color = SvbN2,
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SvbN5,
                ) {
                    Text(
                        text = "WAITING GM",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = SvbN2,
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SvbWhite,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = null,
                        tint = OtherInfoBannerIconTint,
                        modifier = Modifier.size(22.dp),
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "You can submit. Only GM can approve.",
                        style = MaterialTheme.typography.bodySmall,
                        color = OtherInfoBannerTextColor,
                    )
                }
            }
        }
    }
}
