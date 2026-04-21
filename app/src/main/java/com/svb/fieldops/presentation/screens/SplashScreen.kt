package com.svb.fieldops.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.svb.fieldops.R
import com.svb.fieldops.presentation.components.SvbPrimaryButton
import com.svb.fieldops.ui.theme.SvbBlack
import com.svb.fieldops.ui.theme.SvbN1
import com.svb.fieldops.ui.theme.SvbN7

/**
 * Splash: Field Ops — top-right **profile** control is a **48×48 dp** tile with
 * [RoundedCornerShape] (12 dp) on [SvbN7], with [Icons.Outlined.Person] (stroke, not filled).
 *
 * Center **logo** is [R.drawable.svb_logo] at **197×74 dp** per layout spec (transparent PNG
 * recommended so the wordmark sits on [SvbN1] without a baked-in plate).
 */
@Composable
fun SplashScreen(
    onProfileClick: () -> Unit,
    onGetStarted: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = SvbN1,
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .statusBarsPadding(),
        ) {
            val profileShape = RoundedCornerShape(12.dp)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 12.dp, end = 16.dp)
                    .size(48.dp)
                    .clip(profileShape)
                    .background(SvbN7)
                    .clickable(onClick = onProfileClick),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Profile",
                    tint = SvbBlack,
                    modifier = Modifier.size(24.dp),
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.svb_logo),
                    contentDescription = "SVB Infra Projects",
                    modifier = Modifier
                        .width(197.dp)
                        .height(74.dp),
                    contentScale = ContentScale.Fit,
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .width(325.dp)
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 28.dp),
            ) {
                SvbPrimaryButton(
                    text = "Get Started",
                    onClick = onGetStarted,
                    leadingIcon = Icons.AutoMirrored.Rounded.ArrowForward,
                )
            }
        }
    }
}
