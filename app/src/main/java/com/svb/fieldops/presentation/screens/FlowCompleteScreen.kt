package com.svb.fieldops.presentation.screens

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.svb.fieldops.presentation.components.SvbPrimaryButton
import com.svb.fieldops.ui.theme.SvbN3

@Composable
fun FlowCompleteScreen() {
    val context = LocalContext.current
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
        ) {
            Text(
                text = "Clock-in complete",
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "This is a fallback when no role is in session. Normally you land on Driver, Operator, Supervisor, or Engineer home after clock-in.",
                style = MaterialTheme.typography.bodyMedium,
                color = SvbN3,
            )
            Spacer(Modifier.weight(1f))
            SvbPrimaryButton(
                text = "Close app",
                onClick = { (context as Activity).finishAffinity() },
            )
        }
    }
}
