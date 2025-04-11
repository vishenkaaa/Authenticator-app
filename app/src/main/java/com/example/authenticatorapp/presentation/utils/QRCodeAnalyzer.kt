package com.example.authenticatorapp.presentation.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.viewmodel.AddAccountViewModel
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class QRCodeAnalyzer(
    private val viewModel: AddAccountViewModel,
    private val navController: NavController,
    private val context: Context,
    private val onDismiss: () -> Unit
) : ImageAnalysis.Analyzer {
    private val scanner = BarcodeScanning.getClient()
    private var isQrProcessed = false

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        if (isQrProcessed) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image ?: run {
            imageProxy.close()
            return
        }

        val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        scanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                if (!isQrProcessed && barcodes.isNotEmpty()) {
                    barcodes.firstOrNull()?.rawValue?.let { qrCode ->
                        isQrProcessed = true
                        processQrCode(qrCode)
                    }
                }
                imageProxy.close()
            }
            .addOnFailureListener {
                Log.e("QRCodeAnalyzer", "QR scan failed", it)
                imageProxy.close()
            }
    }

    private fun processQrCode(qrCode: String) {
        val otpData = OtpAuthParser.parseOtpAuthUrl(qrCode)
        if (otpData != null) {
            viewModel.addAccount(
                service = otpData.serviceName,
                email = otpData.email,
                secret = otpData.secret,
                type = otpData.type,
                algorithm = otpData.algorithm,
                digits = otpData.digits,
                counter = otpData.counter
            )

            Toast.makeText(context, context.getString(R.string.account_successfully_added), Toast.LENGTH_SHORT).show()

            navController.navigate("Main") {
                popUpTo("Main") { inclusive = true }
            }
        } else {
            onDismiss()
        }
    }
}