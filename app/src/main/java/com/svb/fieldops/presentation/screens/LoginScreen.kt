package com.svb.fieldops.presentation.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.outlined.Engineering
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.svb.fieldops.presentation.components.SvbPrimaryButton
import com.svb.fieldops.presentation.viewmodel.ClockInUiState
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN3
import com.svb.fieldops.ui.theme.SvbN5
import com.svb.fieldops.ui.theme.SvbN7
private val FieldShape = RoundedCornerShape(12.dp)

private val LoginHeroIconSize = 164.dp
private val LoginHeroInnerIconSize = 68.dp

private val LoginHeroPeachOuterPadding = 10.dp
/** Ring inset from hero outer edge; lower value widens the band between peach fill and dashed ring. */
private val LoginHeroRingInsetFromEdge = 1.5.dp

/** Reference mockup: soft yellow disk + deeper gold dashed ring (small capsule dashes). */
private val LoginHeroPeachFill = Color(0xFFFFE9C9)
private val LoginHeroRingDash = Color(0xFFF5C75D)

/** Engineering icon on soft circle with a slowly rotating dashed ring (rounded dash caps). */
@Composable
private fun LoginEngineeringHeroIcon(modifier: Modifier = Modifier) {
    val ringTransition = rememberInfiniteTransition(label = "loginHeroRing")
    val ringRotation by ringTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "loginHeroRingRotation",
    )
    Box(
        modifier = modifier.size(LoginHeroIconSize),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(LoginHeroPeachOuterPadding)
                .background(LoginHeroPeachFill, CircleShape),
        )
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .rotate(ringRotation),
        ) {
            val strokeWidth = 1.6.dp.toPx()
            val inset = LoginHeroRingInsetFromEdge.toPx() + strokeWidth * 0.5f
            drawArc(
                color = LoginHeroRingDash,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = Size(
                    width = size.width - 2f * inset,
                    height = size.height - 2f * inset,
                ),
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(
                        intervals = floatArrayOf(4.dp.toPx(), 6.dp.toPx()),
                    ),
                ),
            )
        }
        Icon(
            imageVector = Icons.Outlined.Engineering,
            contentDescription = null,
            tint = SvbBlack,
            modifier = Modifier.size(LoginHeroInnerIconSize),
        )
    }
}

@Composable
fun LoginScreen(
    state: ClockInUiState,
    onEmployeeIdChange: (String) -> Unit,
    onUniqueCodeChange: (String) -> Unit,
    onLogin: () -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize(), color = SvbLoginBackground) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp),
        ) {
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineLarge,
                color = SvbBlack,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Enter your Employee ID and Unique Code to log in.",
                style = MaterialTheme.typography.bodyMedium,
                color = SvbN3,
            )
            Spacer(Modifier.height(28.dp))
            LoginEngineeringHeroIcon(Modifier.align(Alignment.CenterHorizontally))
            Spacer(Modifier.height(28.dp))
            Text(
                text = "Employee ID",
                style = MaterialTheme.typography.titleMedium,
                color = SvbBlack,
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = state.employeeId,
                onValueChange = onEmployeeIdChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("SVB20302023", color = SvbN3) },
                singleLine = true,
                shape = FieldShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SvbN7,
                    unfocusedContainerColor = SvbN7,
                    focusedBorderColor = SvbN5,
                    unfocusedBorderColor = SvbN5,
                    cursorColor = SvbBlack,
                    focusedTextColor = SvbBlack,
                    unfocusedTextColor = SvbBlack,
                ),
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Unique Code",
                style = MaterialTheme.typography.titleMedium,
                color = SvbBlack,
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = state.uniqueCode,
                onValueChange = onUniqueCodeChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter unique code", color = SvbN3) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = FieldShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SvbN7,
                    unfocusedContainerColor = SvbN7,
                    focusedBorderColor = SvbN5,
                    unfocusedBorderColor = SvbN5,
                    cursorColor = SvbBlack,
                    focusedTextColor = SvbBlack,
                    unfocusedTextColor = SvbBlack,
                ),
            )
            Spacer(Modifier.height(28.dp))
            SvbPrimaryButton(
                text = "Login",
                onClick = onLogin,
                enabled = state.employeeId.isNotBlank() && state.uniqueCode.isNotBlank(),
                leadingIcon = Icons.AutoMirrored.Rounded.Login,
            )
            val loginError = state.loginErrorMessage
            if (loginError != null) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = loginError,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}
