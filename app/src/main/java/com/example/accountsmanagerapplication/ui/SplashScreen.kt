package com.example.accountsmanagerapplication.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.accountsmanagerapplication.R
import androidx.compose.ui.res.stringResource

@Composable
fun SplashScreen(
    onLoadingComplete: () -> Unit
) {
    var isLoadingComplete by remember { mutableStateOf(false) }
    
    // Ultra-quick splash for low-end devices
    LaunchedEffect(Unit) {
        delay(500) // Reduced to 500ms for faster launch
        isLoadingComplete = true
    }
    
    // Animate the fade out when loading is complete
    val alpha by animateFloatAsState(
        targetValue = if (isLoadingComplete) 0f else 1f,
        animationSpec = tween(200), // Very fast fade
        label = "splash_fade"
    )

    LaunchedEffect(isLoadingComplete) {
        if (isLoadingComplete) {
            delay(200) // Very quick fade
            onLoadingComplete()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .alpha(alpha),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Icon - Using a reliable vector icon
            androidx.compose.material3.Icon(
                imageVector = Icons.Default.AccountBalance,
                contentDescription = "App Icon",
                modifier = Modifier.size(120.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            // App Title and Developer Credit
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
            
            Text(
                text = stringResource(id = R.string.developer_credit),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
            
            Text(
                text = stringResource(id = R.string.developer_location),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp)
            )
            
            // Simple loading indicator
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .size(24.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 2.dp
            )
        }
    }
}
