package com.svb.fieldops.presentation.screens.breakdowns

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.outlined.Notifications
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.svb.fieldops.domain.model.UserRole
import com.svb.fieldops.presentation.navigation.MainRoutes
import com.svb.fieldops.presentation.navigation.NavStateKeys
import com.svb.fieldops.presentation.navigation.bottomNavItemsForRole
import com.svb.fieldops.presentation.navigation.onBreakdownFlowBottomNavSelect
import com.svb.fieldops.presentation.navigation.popRoleHomeWithHomeTabSelected
import com.svb.fieldops.presentation.screens.home.HomeCardShape
import com.svb.fieldops.presentation.screens.home.HomeIconTileShape
import com.svb.fieldops.presentation.screens.home.HomeRoleNavigationBar
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbCardMuted
import com.svb.fieldops.ui.theme.SvbDanger
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN2
import com.svb.fieldops.ui.theme.SvbN5
import com.svb.fieldops.ui.theme.SvbPrimary1
import com.svb.fieldops.ui.theme.SvbPrimary5
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite

private enum class BreakdownSeverity { Major, Minor }

private enum class BreakdownFilter { All, Major, Minor }

private data class BreakdownLine(
    val id: String,
    val severity: BreakdownSeverity,
    val title: String,
    val meta: String,
    val statusMessage: String,
)

/** Filter chips — idle background (all same; mock cool grey). */
private val ChipIdleBg = Color(0xFFEEF2F5)

/** Filter chip — selected background (cream yellow; mock “All” active). */
private val ChipSelectedBg = SvbPrimary5

/** Minor row icon tile — warm cream (mock). */
private val MinorIconTileBg = Color(0xFFFFF3E0)

/** Minor wrench tint (mock). */
private val MinorWrenchTint = Color(0xFFFFA000)

/** List row MAJOR/MINOR badge — same for both (mock). */
private val SeverityBadgeBg = Color(0xFFFFF0E0)
private val SeverityBadgeText = MinorWrenchTint

/** Major row icon tile — light red (mock). */
private val MajorIconTileBg = Color(0xFFFFEBEE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EngineerOpenBreakdownsScreen(
    role: UserRole,
    navController: NavHostController,
) {
    val items = bottomNavItemsForRole(role)
    val homeIdx = 0
    val scroll = rememberScrollState()

    val seed = remember {
        listOf(
            BreakdownLine(
                id = "b1",
                severity = BreakdownSeverity.Major,
                title = "JCB-03 — Battery Issue",
                meta = "2h ago • Reported by Op Anil",
                statusMessage = "Service engineer arriving 4PM",
            ),
            BreakdownLine(
                id = "b2",
                severity = BreakdownSeverity.Minor,
                title = "TATA-019 — Tyre Puncture",
                meta = "30 min ago • Reported by Dr Suresh",
                statusMessage = "Tyre being replaced",
            ),
            BreakdownLine(
                id = "b3",
                severity = BreakdownSeverity.Minor,
                title = "EXC-12 — Hose Leakage",
                meta = "1h ago • Reported by Op Mohan",
                statusMessage = "Hose being replaced",
            ),
        )
    }
    var lines by remember { mutableStateOf(seed) }
    var filter by rememberSaveable { mutableStateOf(BreakdownFilter.All) }

    val openBreakdownsEntry = checkNotNull(navController.currentBackStackEntry) { "Open breakdowns route active." }
    val removeBreakdownId by openBreakdownsEntry.savedStateHandle
        .getStateFlow<String?>(NavStateKeys.REMOVE_OPEN_BREAKDOWN_ID, null)
        .collectAsStateWithLifecycle()
    LaunchedEffect(removeBreakdownId) {
        val id = removeBreakdownId ?: return@LaunchedEffect
        lines = lines.filter { it.id != id }
        openBreakdownsEntry.savedStateHandle.remove<String>(NavStateKeys.REMOVE_OPEN_BREAKDOWN_ID)
    }

    val majorCount = lines.count { it.severity == BreakdownSeverity.Major }
    val minorCount = lines.count { it.severity == BreakdownSeverity.Minor }
    val openCount = lines.size

    val visible = when (filter) {
        BreakdownFilter.All -> lines
        BreakdownFilter.Major -> lines.filter { it.severity == BreakdownSeverity.Major }
        BreakdownFilter.Minor -> lines.filter { it.severity == BreakdownSeverity.Minor }
    }

    BackHandler { navController.popRoleHomeWithHomeTabSelected() }

    Scaffold(
        containerColor = SvbLoginBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Open Breakdowns",
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
            Text(
                text = "$openCount open breakdowns",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = SvbBlack,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Tap any to close & resolve",
                style = MaterialTheme.typography.bodyMedium,
                color = SvbN2,
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                BreakdownFilterChip(
                    label = "All ($openCount)",
                    selected = filter == BreakdownFilter.All,
                    onClick = { filter = BreakdownFilter.All },
                    modifier = Modifier.weight(1f),
                )
                BreakdownFilterChip(
                    label = "Major ($majorCount)",
                    selected = filter == BreakdownFilter.Major,
                    onClick = { filter = BreakdownFilter.Major },
                    modifier = Modifier.weight(1f),
                )
                BreakdownFilterChip(
                    label = "Minor ($minorCount)",
                    selected = filter == BreakdownFilter.Minor,
                    onClick = { filter = BreakdownFilter.Minor },
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = HomeCardShape,
                colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Column(Modifier.fillMaxWidth()) {
                    if (visible.isEmpty()) {
                        Text(
                            text = "No breakdowns in this filter.",
                            modifier = Modifier.padding(20.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = SvbN2,
                        )
                    } else {
                        visible.forEachIndexed { index, line ->
                            if (index > 0) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 14.dp),
                                    color = SvbN5,
                                )
                            }
                            BreakdownListRow(
                                line = line,
                                onClose = {
                                    navController.navigate(MainRoutes.closeBreakdown(role, line.id))
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BreakdownFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(999.dp)
    val bg = if (selected) ChipSelectedBg else ChipIdleBg
    val textColor = SvbBlack
    Box(
        modifier = modifier
            .clip(shape)
            .background(bg, shape)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            color = textColor,
            textAlign = TextAlign.Center,
            maxLines = 2,
        )
    }
}

@Composable
private fun BreakdownListRow(
    line: BreakdownLine,
    onClose: () -> Unit,
) {
    val isMajor = line.severity == BreakdownSeverity.Major
    val iconBg = if (isMajor) MajorIconTileBg else MinorIconTileBg
    val iconTint = if (isMajor) SvbDanger else MinorWrenchTint

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            shape = HomeIconTileShape,
            color = iconBg,
            modifier = Modifier.size(44.dp),
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Outlined.Build,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = line.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SvbBlack,
                    modifier = Modifier.weight(1f),
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = SeverityBadgeBg,
                ) {
                    Text(
                        text = if (isMajor) "MAJOR" else "MINOR",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = SeverityBadgeText,
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = line.meta,
                style = MaterialTheme.typography.bodySmall,
                color = SvbN2,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = line.statusMessage,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = SvbPrimary1,
            )
        }
        Spacer(Modifier.width(8.dp))
        Button(
            onClick = onClose,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SvbSuccess,
                contentColor = SvbWhite,
            ),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
        ) {
            Text(
                text = "Close",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
