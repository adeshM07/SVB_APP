package com.svb.fieldops.presentation.screens.fuel

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.ForwardToInbox
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.svb.fieldops.domain.model.UserRole
import com.svb.fieldops.presentation.navigation.NavStateKeys
import com.svb.fieldops.presentation.navigation.MainRoutes
import com.svb.fieldops.presentation.navigation.bottomNavItemsForRole
import com.svb.fieldops.presentation.navigation.fuelTabIndex
import com.svb.fieldops.presentation.navigation.popRoleHomeWithHomeTabSelected
import com.svb.fieldops.presentation.navigation.profileTabIndex
import com.svb.fieldops.presentation.navigation.reportsTabIndex
import com.svb.fieldops.presentation.navigation.verifyTabIndex
import com.svb.fieldops.presentation.screens.home.HomeCardShape
import com.svb.fieldops.presentation.screens.home.HomeRoleNavigationBar
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbCardMuted
import com.svb.fieldops.ui.theme.SvbDanger
import com.svb.fieldops.ui.theme.SvbDivider
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN2
import com.svb.fieldops.ui.theme.SvbN3
import com.svb.fieldops.ui.theme.SvbN5
import com.svb.fieldops.ui.theme.SvbN7
import com.svb.fieldops.ui.theme.SvbPrimary1
import com.svb.fieldops.ui.theme.SvbPrimary2
import com.svb.fieldops.ui.theme.SvbPrimary5
import com.svb.fieldops.ui.theme.SvbRoseTint
import com.svb.fieldops.ui.theme.SvbSuccess
import com.svb.fieldops.ui.theme.SvbWhite

private data class FlowContext(
    val machineLabel: String,
    val machineType: String,
    val operatorLiters: Int,
)

private fun flowContextForRequestId(requestId: String): FlowContext? = when (requestId) {
    "r1" -> FlowContext("JCB-07", "Excavator", 50)
    "r2" -> FlowContext("TATA-024", "Tipper", 80)
    "r3" -> FlowContext("EXC-12", "Excavator", 120)
    else -> null
}

private val ScannerBg = Color(0xFF3A3A3A)
private val PillShape = RoundedCornerShape(percent = 50)

/** Step 3 verification summary (mockup-aligned). */
private val HsdSummaryRecommendedYellow = Color(0xFFF1C40F)
private val HsdSummaryForwardGreen = Color(0xFF4CAF50)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupervisorVerifyHsdRequestFlowScreen(
    role: UserRole,
    requestId: String,
    navController: NavHostController,
) {
    val ctx = flowContextForRequestId(requestId)
    if (ctx == null) {
        LaunchedEffect(Unit) { navController.popBackStack() }
        return
    }

    val items = bottomNavItemsForRole(role)
    val homeIdx = 0
    val verifyIdx = requireNotNull(verifyTabIndex(role))
    val reportsIdx = requireNotNull(reportsTabIndex(role))
    val fuelIdx = requireNotNull(fuelTabIndex(role))
    val profileIdx = profileTabIndex(role)
    val scroll = rememberScrollState()

    var step by rememberSaveable { mutableIntStateOf(1) }
    var verifiedLitersText by rememberSaveable { mutableStateOf("140") }
    var hourMeterText by rememberSaveable { mutableStateOf("1245") }
    var remarks by rememberSaveable { mutableStateOf("") }

    fun goBackWithinFlow() {
        if (step > 1) step-- else navController.popBackStack()
    }

    BackHandler(onBack = { goBackWithinFlow() })

    Scaffold(
        containerColor = SvbLoginBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Verify HSD Request",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SvbBlack,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { goBackWithinFlow() }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = SvbBlack,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* later */ }) {
                        Box(Modifier.size(48.dp)) {
                            Icon(
                                Icons.Outlined.Notifications,
                                contentDescription = null,
                                tint = SvbBlack,
                                modifier = Modifier.align(Alignment.Center),
                            )
                            Box(
                                Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 10.dp, end = 8.dp)
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
            HsdFlowStepper(currentStep = step)
            Spacer(Modifier.height(24.dp))
            when (step) {
                1 -> StepScanQr(ctx = ctx, onNext = { step = 2 })
                2 -> StepVerifyLevel(
                    ctx = ctx,
                    verifiedLitersText = verifiedLitersText,
                    onVerifiedLitersChange = { verifiedLitersText = it },
                    hourMeterText = hourMeterText,
                    onHourMeterChange = { hourMeterText = it },
                    onBack = { step = 1 },
                    onNext = { step = 3 },
                )
                3 -> StepForwardEngineer(
                    ctx = ctx,
                    verifiedLitersText = verifiedLitersText,
                    remarks = remarks,
                    onRemarksChange = { remarks = it },
                    onBack = { step = 2 },
                    onForward = {
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            NavStateKeys.HSD_REQUEST_FLOW_COMPLETED_ID,
                            requestId,
                        )
                        navController.popBackStack()
                    },
                )
            }
        }
    }
}

@Composable
private fun HsdFlowStepper(currentStep: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        HsdStepDot(
            number = 1,
            done = currentStep > 1,
            active = currentStep == 1,
        )
        Box(
            Modifier
                .weight(1f)
                .height(3.dp)
                .padding(horizontal = 6.dp)
                .background(if (currentStep > 1) SvbSuccess else SvbN5),
        )
        HsdStepDot(
            number = 2,
            done = currentStep > 2,
            active = currentStep == 2,
        )
        Box(
            Modifier
                .weight(1f)
                .height(3.dp)
                .padding(horizontal = 6.dp)
                .background(if (currentStep > 2) SvbSuccess else SvbN5),
        )
        HsdStepDot(
            number = 3,
            done = false,
            active = currentStep == 3,
        )
    }
}

@Composable
private fun HsdStepDot(number: Int, done: Boolean, active: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    when {
                        done -> SvbSuccess
                        active -> SvbPrimary2
                        else -> SvbN7
                    },
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (done) {
                Icon(
                    Icons.Outlined.Check,
                    contentDescription = null,
                    tint = SvbWhite,
                    modifier = Modifier.size(18.dp),
                )
            } else {
                Text(
                    text = number.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (active) SvbBlack else SvbN2,
                )
            }
        }
    }
}

private val ScannerCornerBracketLength = 36.dp
/** Inset of yellow L-bracket frame from the inner scan area (inside dark panel). */
private val ScannerFramePaddingH = 20.dp
private val ScannerFramePaddingV = 22.dp
/** Extra gap so the laser line does not touch the yellow corner arms. */
private val ScannerLaserInsetFromFrame = 14.dp
private val ScannerLaserThickness = 3.dp

/** Green ring + green tick; center is transparent (card shows through). */
@Composable
private fun HsdOutlinedSuccessCheckBadge(
    modifier: Modifier = Modifier,
    tint: Color = SvbSuccess,
) {
    Canvas(modifier) {
        val cx = size.width * 0.5f
        val cy = size.height * 0.5f
        val minR = size.minDimension * 0.5f
        val stroke = (minR * 0.12f).coerceIn(1.35f.dp.toPx(), 2f.dp.toPx())
        val ringRadius = minR - stroke * 0.55f
        drawCircle(
            color = tint,
            radius = ringRadius,
            center = Offset(cx, cy),
            style = Stroke(width = stroke),
        )
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.30f, h * 0.50f)
            lineTo(w * 0.44f, h * 0.64f)
            lineTo(w * 0.72f, h * 0.36f)
        }
        drawPath(
            path = path,
            color = tint,
            style = Stroke(
                width = stroke,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
            ),
        )
    }
}

@Composable
private fun QrScanViewfinder(modifier: Modifier = Modifier) {
    val infinite = rememberInfiniteTransition(label = "qrLaser")
    val sweep by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "laserSweep",
    )
    val density = LocalDensity.current
    Column(
        modifier = modifier
            .height(312.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(ScannerBg)
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(
                    horizontal = ScannerFramePaddingH,
                    vertical = ScannerFramePaddingV,
                )
                .drawBehind {
                    val lineW = 3.dp.toPx()
                    val len = ScannerCornerBracketLength.toPx()
                    val yellow = SvbPrimary2
                    drawLine(yellow, Offset(0f, 0f), Offset(len, 0f), lineW)
                    drawLine(yellow, Offset(0f, 0f), Offset(0f, len), lineW)
                    drawLine(yellow, Offset(size.width, 0f), Offset(size.width - len, 0f), lineW)
                    drawLine(yellow, Offset(size.width, 0f), Offset(size.width, len), lineW)
                    drawLine(yellow, Offset(0f, size.height), Offset(len, size.height), lineW)
                    drawLine(yellow, Offset(0f, size.height), Offset(0f, size.height - len), lineW)
                    drawLine(yellow, Offset(size.width, size.height), Offset(size.width - len, size.height), lineW)
                    drawLine(yellow, Offset(size.width, size.height), Offset(size.width, size.height - len), lineW)
                },
        ) {
            val bracketLenPx = with(density) { ScannerCornerBracketLength.toPx() }
            val gapPx = with(density) { 10.dp.toPx() }
            val topStop = bracketLenPx + gapPx
            val bottomStop = bracketLenPx + gapPx
            val maxHpx = with(density) { maxHeight.toPx() }
            val laserHPx = with(density) { ScannerLaserThickness.toPx() }
            val travelPx = (maxHpx - laserHPx - topStop - bottomStop).coerceAtLeast(8f)
            val yPx = topStop + sweep * travelPx
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ScannerLaserInsetFromFrame)
                    .height(ScannerLaserThickness)
                    .align(Alignment.TopStart)
                    .offset { IntOffset(0, yPx.toInt()) }
                    .background(SvbPrimary2.copy(alpha = 0.95f)),
            )
        }
        Text(
            text = "Align QR code within frame",
            style = MaterialTheme.typography.bodySmall,
            color = SvbN3,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 2.dp),
        )
    }
}

@Composable
private fun StepScanQr(ctx: FlowContext, onNext: () -> Unit) {
    Text(
        text = "Step 1 of 3: Scan Machine QR",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = SvbBlack,
    )
    Spacer(Modifier.height(8.dp))
    Text(
        text = "Scan QR on the machine to verify identity",
        style = MaterialTheme.typography.bodyMedium,
        color = SvbN2,
    )
    Spacer(Modifier.height(20.dp))
    QrScanViewfinder(
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(Modifier.height(18.dp))
    Surface(
        shape = HomeCardShape,
        color = Color(0xFFE8F5E9),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HsdOutlinedSuccessCheckBadge(Modifier.size(20.dp))
            Spacer(Modifier.width(14.dp))
            Column {
                Text(
                    text = "${ctx.machineLabel} — ${ctx.machineType} detected",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SvbBlack,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Machine identity matches request",
                    style = MaterialTheme.typography.bodySmall,
                    color = SvbSuccess,
                )
            }
        }
    }
    Spacer(Modifier.height(24.dp))
    Button(
        onClick = onNext,
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = SvbPrimary2,
            contentColor = SvbBlack,
        ),
        contentPadding = PaddingValues(vertical = 14.dp),
    ) {
        Text("Next: Check HSD Level", fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(8.dp))
        Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
    }
}

@Composable
private fun StepVerifyLevel(
    ctx: FlowContext,
    verifiedLitersText: String,
    onVerifiedLitersChange: (String) -> Unit,
    hourMeterText: String,
    onHourMeterChange: (String) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit,
) {
    Text(
        text = "Step 2 of 3: Verify HSD Level",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = SvbBlack,
    )
    Spacer(Modifier.height(4.dp))
    Text(
        text = "Check the actual fuel gauge reading",
        style = MaterialTheme.typography.bodyMedium,
        color = SvbN2,
    )
    Spacer(Modifier.height(16.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Outlined.CheckCircle, null, tint = SvbN2, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = "Operator's reported HSD",
                    style = MaterialTheme.typography.bodySmall,
                    color = SvbN2,
                )
                Text(
                    text = "${ctx.operatorLiters} L",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SvbBlack,
                )
            }
            Surface(shape = PillShape, color = SvbN7, border = BorderStroke(1.dp, SvbN5)) {
                Row(
                    Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.Lock, null, modifier = Modifier.size(14.dp), tint = SvbN2)
                    Spacer(Modifier.width(4.dp))
                    Text("Read-only", style = MaterialTheme.typography.labelSmall, color = SvbN2)
                }
            }
        }
    }
    Spacer(Modifier.height(14.dp))
    Text(
        text = "Your verified HSD reading (L)",
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold,
        color = SvbBlack,
    )
    Spacer(Modifier.height(6.dp))
    OutlinedTextField(
        value = verifiedLitersText,
        onValueChange = onVerifiedLitersChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = SvbN7,
            unfocusedContainerColor = SvbN7,
        ),
    )
    Spacer(Modifier.height(12.dp))
    Text(
        text = "Hour meter / KMS reading",
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold,
        color = SvbBlack,
    )
    Spacer(Modifier.height(6.dp))
    OutlinedTextField(
        value = hourMeterText,
        onValueChange = onHourMeterChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = SvbN7,
            unfocusedContainerColor = SvbN7,
        ),
    )
    Spacer(Modifier.height(16.dp))
    Text(
        text = "Photo of HSD gauge *",
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold,
        color = SvbBlack,
    )
    Spacer(Modifier.height(8.dp))
    HsdGaugePhotoSlot(onClick = { /* later: camera */ })
    val verifiedVal = verifiedLitersText.toIntOrNull()
    val diff = if (verifiedVal != null) kotlin.math.abs(verifiedVal - ctx.operatorLiters) else null
    if (diff != null && diff > 0) {
        Spacer(Modifier.height(14.dp))
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SvbRoseTint,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Warning, null, tint = SvbDanger, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Discrepancy detected",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SvbDanger,
                    )
                    Text(
                        text = "Difference of $diff L from operator's reading",
                        style = MaterialTheme.typography.bodySmall,
                        color = SvbBlack,
                    )
                }
            }
        }
    }
    Spacer(Modifier.height(20.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.weight(1f),
            shape = HomeCardShape,
            border = BorderStroke(1.dp, SvbN5),
        ) {
            Icon(Icons.AutoMirrored.Outlined.ArrowBack, null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text("Back", fontWeight = FontWeight.SemiBold)
        }
        Button(
            onClick = onNext,
            modifier = Modifier.weight(1f),
            shape = HomeCardShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = SvbPrimary2,
                contentColor = SvbBlack,
            ),
        ) {
            Text("Next: Forward", fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(6.dp))
            Icon(Icons.AutoMirrored.Outlined.ArrowForward, null, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun HsdGaugePhotoSlot(onClick: () -> Unit) {
    val dashColor = SvbN3
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(HomeCardShape)
            .background(SvbWhite)
            .drawBehind {
                val w = 1.5.dp.toPx()
                val dash = 8.dp.toPx()
                val gap = 6.dp.toPx()
                val r = 12.dp.toPx()
                drawRoundRect(
                    color = dashColor,
                    topLeft = Offset(w / 2f, w / 2f),
                    size = Size(size.width - w, size.height - w),
                    cornerRadius = CornerRadius(r, r),
                    style = Stroke(width = w, pathEffect = PathEffect.dashPathEffect(floatArrayOf(dash, gap), 0f)),
                )
            }
            .clickable(onClick = onClick)
            .padding(vertical = 22.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Outlined.CameraAlt, null, tint = SvbN2, modifier = Modifier.size(36.dp))
            Spacer(Modifier.height(8.dp))
            Text("Tap to capture HSD gauge photo", style = MaterialTheme.typography.bodyMedium, color = SvbBlack)
            Text(
                "Mandatory proof of reading",
                style = MaterialTheme.typography.bodySmall,
                color = SvbN2,
            )
        }
    }
}

@Composable
private fun StepForwardEngineer(
    ctx: FlowContext,
    verifiedLitersText: String,
    remarks: String,
    onRemarksChange: (String) -> Unit,
    onBack: () -> Unit,
    onForward: () -> Unit,
) {
    Text(
        text = "Step 3 of 3: Forward to Engineer",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = SvbBlack,
    )
    Spacer(Modifier.height(4.dp))
    Text(
        text = "Review verification details before forwarding",
        style = MaterialTheme.typography.bodyMedium,
        color = SvbN2,
    )
    Spacer(Modifier.height(16.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = HomeCardShape,
        colors = CardDefaults.cardColors(containerColor = SvbCardMuted),
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
            Text(
                text = "VERIFICATION SUMMARY",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = SvbN3,
                letterSpacing = 0.6.sp,
            )
            Spacer(Modifier.height(10.dp))
            VerificationSummaryDivider()
            Spacer(Modifier.height(12.dp))
            SummaryLine("Machine", ctx.machineLabel)
            Spacer(Modifier.height(10.dp))
            VerificationSummaryDivider()
            Spacer(Modifier.height(10.dp))
            SummaryLine("Operator's request", "${ctx.operatorLiters} L")
            Spacer(Modifier.height(10.dp))
            VerificationSummaryDivider()
            Spacer(Modifier.height(10.dp))
            SummaryLine(
                label = "Your verification",
                value = "${verifiedLitersText.ifBlank { "—" }} L (current level)",
            )
            Spacer(Modifier.height(10.dp))
            VerificationSummaryDivider()
            Spacer(Modifier.height(10.dp))
            SummaryLine(
                label = "Recommended issue",
                value = "${ctx.operatorLiters} L (room in tank)",
                valueColor = HsdSummaryRecommendedYellow,
            )
            Spacer(Modifier.height(10.dp))
            VerificationSummaryDivider()
            Spacer(Modifier.height(12.dp))
            SummaryStatusRow()
        }
    }
    Spacer(Modifier.height(16.dp))
    Text(
        text = "Remarks (optional)",
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.Bold,
        color = SvbN2,
    )
    Spacer(Modifier.height(6.dp))
    OutlinedTextField(
        value = remarks,
        onValueChange = onRemarksChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = "Add any notes for the Engineer...",
                style = MaterialTheme.typography.bodyLarge,
                color = SvbN2,
            )
        },
        minLines = 3,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = SvbN7,
            unfocusedContainerColor = SvbN7,
            focusedPlaceholderColor = SvbN2,
            unfocusedPlaceholderColor = SvbN2,
            focusedBorderColor = SvbN5,
            unfocusedBorderColor = SvbN5,
        ),
    )
    Spacer(Modifier.height(20.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.weight(0.32f),
            shape = HomeCardShape,
            border = BorderStroke(1.dp, SvbN5),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = SvbWhite,
                contentColor = SvbBlack,
            ),
        ) {
            Icon(Icons.AutoMirrored.Outlined.ArrowBack, null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text("Back", fontWeight = FontWeight.SemiBold)
        }
        Button(
            onClick = onForward,
            modifier = Modifier.weight(0.68f),
            shape = HomeCardShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = HsdSummaryForwardGreen,
                contentColor = SvbWhite,
            ),
        ) {
            Icon(Icons.AutoMirrored.Outlined.ForwardToInbox, null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Forward to Engineer", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun VerificationSummaryDivider() {
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = SvbDivider,
    )
}

@Composable
private fun SummaryLine(
    label: String,
    value: String,
    valueColor: Color = SvbBlack,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = SvbN2,
            modifier = Modifier.widthIn(max = 180.dp),
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = valueColor,
            textAlign = TextAlign.End,
            maxLines = 2,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Composable
private fun SummaryStatusRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Status",
            style = MaterialTheme.typography.bodySmall,
            color = SvbN2,
            modifier = Modifier.widthIn(max = 180.dp),
        )
        Spacer(Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            HsdOutlinedSuccessCheckBadge(
                modifier = Modifier.size(18.dp),
                tint = HsdSummaryForwardGreen,
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = "Verified, ready for engineer",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = HsdSummaryForwardGreen,
                textAlign = TextAlign.End,
                maxLines = 2,
            )
        }
    }
}
