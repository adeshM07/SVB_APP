package com.svb.fieldops.presentation.screens.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.svb.fieldops.presentation.navigation.NavStateKeys
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbCardMuted
import com.svb.fieldops.ui.theme.SvbDanger
import com.svb.fieldops.ui.theme.SvbN2
import com.svb.fieldops.ui.theme.SvbN5
import com.svb.fieldops.ui.theme.SvbN7
import com.svb.fieldops.ui.theme.SvbPrimary4
import com.svb.fieldops.ui.theme.SvbPrimary5
import com.svb.fieldops.ui.theme.SvbRoseTint
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite

internal val HomeCardShape = RoundedCornerShape(20.dp)
internal val HomeIconTileShape = RoundedCornerShape(12.dp)

/**
 * When [NavStateKeys.RESET_HOME_BOTTOM_TAB] is set on this screen’s back stack entry (e.g. before
 * popping Profile), selects the Home tab again.
 */
@Composable
internal fun HomeBottomTabResetFromProfileEffect(
    navController: NavHostController,
    onResetToHomeTab: () -> Unit,
) {
    val handle = navController.currentBackStackEntry?.savedStateHandle ?: return
    val reset by handle.getStateFlow(NavStateKeys.RESET_HOME_BOTTOM_TAB, false)
        .collectAsStateWithLifecycle()
    LaunchedEffect(reset) {
        if (reset) {
            onResetToHomeTab()
            handle[NavStateKeys.RESET_HOME_BOTTOM_TAB] = false
        }
    }
}

/** Start inset for [ActionDividerTextAligned] — matches [ActionTileRow] padding + icon + gap. */
private val ActionTileRowTextStartInset = 16.dp + 44.dp + 14.dp

@Composable
internal fun HomeHeaderBar(
    initials: String,
    greeting: String,
    userName: String,
    notificationDot: Boolean,
    modifier: Modifier = Modifier,
    onNotificationClick: () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(SvbPrimary4.copy(alpha = 0.65f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = initials,
                    style = MaterialTheme.typography.titleLarge,
                    color = SvbBlack,
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = greeting,
                    style = MaterialTheme.typography.bodySmall,
                    color = SvbN2,
                )
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = SvbBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SvbN7,
            modifier = Modifier.size(44.dp),
        ) {
            Box(Modifier.fillMaxSize()) {
                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(44.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = SvbBlack,
                        modifier = Modifier.size(22.dp),
                    )
                }
                if (notificationDot) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF64C4C)),
                    )
                }
            }
        }
    }
}

@Composable
internal fun DutyStatusHeroCard(
    badgeLabel: String,
    timerText: String,
    footerText: String,
    modifier: Modifier = Modifier,
    /**
     * When true: clock-in line under ON DUTY row, then large timer below; both start-aligned if [timerAndFooterAlignStart].
     * When false: timer then footer; centered unless [timerAndFooterAlignStart].
     */
    clockInLineAboveTimer: Boolean = false,
    /** When true, timer and clock-in/zone line use start alignment (Driver-style). */
    timerAndFooterAlignStart: Boolean = false,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(Modifier.padding(20.dp)) {
            val dutyDotPulse = rememberInfiniteTransition(label = "onDutyDot")
            val dutyDotAlpha by dutyDotPulse.animateFloat(
                initialValue = 0.42f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1100, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse,
                ),
                label = "onDutyDotAlpha",
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(SvbSuccess.copy(alpha = dutyDotAlpha)),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "ON DUTY",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = SvbSuccess,
                    )
                }
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = SvbWhite,
                ) {
                    Text(
                        text = badgeLabel,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = SvbBlack,
                    )
                }
            }
            val timerAlign = if (timerAndFooterAlignStart) TextAlign.Start else TextAlign.Center
            val footerAlign = if (timerAndFooterAlignStart) TextAlign.Start else TextAlign.Center
            if (clockInLineAboveTimer) {
                Spacer(Modifier.height(10.dp))
                Text(
                    text = footerText,
                    style = MaterialTheme.typography.bodySmall,
                    color = SvbN2,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = footerAlign,
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = timerText,
                    style = MaterialTheme.typography.displayLarge,
                    color = SvbBlack,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = timerAlign,
                )
            } else {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = timerText,
                    style = MaterialTheme.typography.displayLarge,
                    color = SvbBlack,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = timerAlign,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = footerText,
                    style = MaterialTheme.typography.bodySmall,
                    color = SvbN2,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = footerAlign,
                )
            }
        }
    }
}

@Composable
internal fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier.padding(bottom = 10.dp),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.ExtraBold,
        color = SvbN2,
    )
}

@Composable
internal fun SectionHeaderRow(
    title: String,
    modifier: Modifier = Modifier,
    trailing: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
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
internal fun ActionTileRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    /** Background behind the leading icon (e.g. light chip on white list rows). */
    iconContainerColor: Color = SvbWhite,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            shape = HomeIconTileShape,
            color = iconContainerColor,
            modifier = Modifier.size(44.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = SvbBlack, modifier = Modifier.size(22.dp))
            }
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = SvbBlack)
            Spacer(Modifier.height(2.dp))
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = SvbN2)
        }
        Icon(
            Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            contentDescription = null,
            tint = SvbN5,
            modifier = Modifier.size(22.dp),
        )
    }
}

@Composable
internal fun GroupedActionCard(
    modifier: Modifier = Modifier,
    containerColor: Color = SvbWhite,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column { content() }
    }
}

@Composable
internal fun MetricMiniCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(value, style = MaterialTheme.typography.headlineMedium, color = SvbBlack)
            Spacer(Modifier.height(4.dp))
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                color = SvbN2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

data class HomeNavEntry(
    val label: String,
    val icon: ImageVector,
)

@Composable
internal fun HomeRoleNavigationBar(
    items: List<HomeNavEntry>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    onSelect: (Int) -> Unit = {},
) {
    NavigationBar(
        modifier = modifier.navigationBarsPadding(),
        containerColor = SvbWhite,
        tonalElevation = 0.dp,
    ) {
        items.forEachIndexed { index, item ->
            val selected = index == selectedIndex
            NavigationBarItem(
                selected = selected,
                onClick = { onSelect(index) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp),
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SvbBlack,
                    selectedTextColor = SvbBlack,
                    unselectedIconColor = SvbN2,
                    unselectedTextColor = SvbN2,
                    indicatorColor = SvbPrimary5,
                ),
            )
        }
    }
}

@Composable
internal fun ActionDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = SvbN7,
        thickness = 1.dp,
    )
}

/** Divider inset so the line starts with the action title column (not under the icon). */
@Composable
internal fun ActionDividerTextAligned() {
    HorizontalDivider(
        modifier = Modifier.padding(start = ActionTileRowTextStartInset, end = 16.dp),
        color = SvbN7,
        thickness = 1.dp,
    )
}

@Composable
internal fun HomeFuelStatusCard(
    circularIconBackground: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val trackShape = RoundedCornerShape(4.dp)
    val iconShape = if (circularIconBackground) CircleShape else HomeIconTileShape
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = {}),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = iconShape,
                color = SvbRoseTint,
                modifier = Modifier.size(44.dp),
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.LocalGasStation,
                        contentDescription = null,
                        tint = SvbDanger,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "HSD Low — 12 Ltrs",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SvbBlack,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "Tap to request fuel",
                    style = MaterialTheme.typography.bodySmall,
                    color = SvbN2,
                )
            }
            Spacer(Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .width(72.dp)
                    .height(8.dp)
                    .clip(trackShape)
                    .background(SvbN7),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.14f)
                        .align(Alignment.CenterStart)
                        .clip(trackShape)
                        .background(SvbDanger),
                )
            }
        }
    }
}
