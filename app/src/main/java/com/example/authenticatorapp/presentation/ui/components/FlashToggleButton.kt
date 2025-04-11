package com.example.authenticatorapp.presentation.ui.components

import androidx.camera.core.Camera
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.authenticatorapp.R

@Composable
fun FlashToggleButton(camera: MutableState<Camera?>) {
    var isFlashOn by remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            isFlashOn = !isFlashOn
            camera.value?.cameraControl?.enableTorch(isFlashOn)
        },
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.3f))
    ) {
        Icon(
            painter = painterResource(
                if (isFlashOn) R.drawable.flash else R.drawable.flash_off
            ),
            contentDescription = "Ліхтарик" ,
            tint = Color.White,
        )
    }
}