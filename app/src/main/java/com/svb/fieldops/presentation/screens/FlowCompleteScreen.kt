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
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        ) {
            Text(
                text = "Clock-in complete",
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "This is a placeholder after the five shared screens. Driver / Supervisor / Engineer homes will plug in here later.",
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
