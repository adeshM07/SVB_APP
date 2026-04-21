package com.svb.fieldops.presentation.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.svb.fieldops.presentation.components.SvbPrimaryButton
import com.svb.fieldops.presentation.viewmodel.ClockInUiState
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbLoginBackground
import com.svb.fieldops.ui.theme.SvbN3
import com.svb.fieldops.ui.theme.SvbN5
import com.svb.fieldops.ui.theme.SvbN7
import com.svb.fieldops.ui.theme.SvbPrimary1
import com.svb.fieldops.ui.theme.SvbPrimary4

private val FieldShape = RoundedCornerShape(12.dp)

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
            Box(
                modifier = Modifier
                    .size(132.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(SvbPrimary4.copy(alpha = 0.55f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Engineering,
                    contentDescription = null,
                    tint = SvbPrimary1,
                    modifier = Modifier.size(72.dp),
                )
            }
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
            Spacer(Modifier.height(12.dp))
        }
    }
}
