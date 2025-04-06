package com.example.authenticatorapp.presentation.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.utils.QRCodeAnalyzer

@Composable
fun QRcodeScreen(navController: NavController, onQRCodeScanned: (String) -> Unit){
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember{PreviewView(context)}
    var hasCameraPermission by remember { mutableStateOf(false) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            hasCameraPermission = true
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (hasCameraPermission) {
        Box(modifier = Modifier.fillMaxSize()){
            AndroidView(
                factory = { previewView },
                modifier = Modifier.fillMaxSize()
            ) { view ->
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(view.surfaceProvider)
                    }
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    val imageAnalyzer = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also {
                            it.setAnalyzer(
                                ContextCompat.getMainExecutor(context),
                                QRCodeAnalyzer { qrCode ->
                                    onQRCodeScanned(qrCode)
                                })
                        }

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner, cameraSelector, preview, imageAnalyzer
                        )
                    } catch (exc: Exception) {
                        Log.e("QRcodeScreen", "Camera binding failed", exc)
                    }
                }, ContextCompat.getMainExecutor(context))

            }

            QRScannerLayout()

            TopAppBar(
                backgroundColor = Color.Transparent,
                elevation = 0.dp,
                modifier = Modifier.padding(top = 52.dp),
                contentColor = Color.White,
                title = { Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) { Text(stringResource(R.string.scan_qr_code)) }},
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(painterResource(R.drawable.back),
                            contentDescription = "Back",
                            tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {navController.navigate("Main")}) {
                        Icon(painterResource(R.drawable.close),
                            contentDescription = "Close",
                            tint = Color.White)
                    }
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 80.dp)
                    .align(Alignment.BottomCenter),
            ) {
                Spacer(Modifier.weight(1f))
                FlashToggleButton(context)
                Spacer(Modifier.width(60.dp))
                GalleryButton()
                Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun QRScannerLayout() {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        val scannerSize = screenWidth * 0.8f
        val overlayColor = Color.Black.copy(alpha = 0.6f)

        // Позиція сканера
        val scannerLeft = (screenWidth - scannerSize) / 2
        val scannerTop = (screenHeight - scannerSize) / 2
        val scannerRight = scannerLeft + scannerSize
        val scannerBottom = scannerTop + scannerSize

        // Верхня частина
        Box(
            modifier = Modifier
                .offset(x = 0.dp, y = 0.dp)
                .width(screenWidth)
                .height(scannerTop)
                .background(overlayColor)
        )

        // Нижня частина
        Box(
            modifier = Modifier
                .offset(x = 0.dp, y = scannerBottom)
                .width(screenWidth)
                .height(screenHeight - scannerBottom)
                .background(overlayColor)
        )

        // Ліва частина
        Box(
            modifier = Modifier
                .offset(x = 0.dp, y = scannerTop)
                .width(scannerLeft)
                .height(scannerSize)
                .background(overlayColor)
        )

        // Права частина
        Box(
            modifier = Modifier
                .offset(x = scannerRight, y = scannerTop)
                .width(screenWidth - scannerRight)
                .height(scannerSize)
                .background(overlayColor)
        )

        ScannerCorners(
            modifier = Modifier
                .size(scannerSize)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun ScannerCorners(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val cornerLength = 50.dp.toPx()
        val strokeWidth = 4.dp.toPx()

        val color = Color.White

        // Верхній лівий кут
        drawLine(
            color = color,
            start = Offset(0f, cornerLength),
            end = Offset(0f, 0f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(cornerLength, 0f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // Верхній правий кут
        drawLine(
            color = color,
            start = Offset(size.width, cornerLength),
            end = Offset(size.width, 0f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width, 0f),
            end = Offset(size.width - cornerLength, 0f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // Нижній лівий кут
        drawLine(
            color = color,
            start = Offset(0f, size.height - cornerLength),
            end = Offset(0f, size.height),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(0f, size.height),
            end = Offset(cornerLength, size.height),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // Нижній правий кут
        drawLine(
            color = color,
            start = Offset(size.width, size.height - cornerLength),
            end = Offset(size.width, size.height),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width, size.height),
            end = Offset(size.width - cornerLength, size.height),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun FlashToggleButton(context: Context) {
    var isFlashOn by remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            isFlashOn = !isFlashOn
            // toggleFlashlight(context, isFlashOn)
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
            contentDescription = "Flash",
            tint = Color.White,
        )
    }
}

@Composable
fun GalleryButton() {
    IconButton(
        onClick = {},
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.3f))
    ) {
        Icon(
            painter = painterResource(R.drawable.gallery),
            contentDescription = "Gallery",
            tint = Color.White,
        )
    }
}