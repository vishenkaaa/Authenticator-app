package com.example.authenticatorapp.presentation.ui.components

import android.graphics.ImageDecoder
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.navigation.Main
import com.example.authenticatorapp.presentation.utils.OtpAuthParser
import com.example.authenticatorapp.presentation.viewmodel.AddAccountViewModel
import com.example.authenticatorapp.presentation.viewmodel.MainActivityViewModel
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

@Composable
fun GalleryButton(
    viewModel: AddAccountViewModel = hiltViewModel(),
    navController: NavController,
    mainViewModel: MainActivityViewModel
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            try {
                val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))

                val detector = BarcodeScanning.getClient()
                val inputImage = InputImage.fromBitmap(bitmap, 0)

                detector.process(inputImage)
                    .addOnSuccessListener { barcodes ->
                        if (barcodes.isNotEmpty() && barcodes[0].rawValue != null) {
                            val qrCode = barcodes[0].rawValue!!
                            val otpData = OtpAuthParser.parseOtpAuthUrl(qrCode)

                            if (otpData != null) {
                                viewModel.addAccount(
                                    service = otpData.serviceName.displayName,
                                    email = otpData.email,
                                    secret = otpData.secret,
                                    type = otpData.type,
                                    algorithm = otpData.algorithm,
                                    digits = otpData.digits,
                                    counter = otpData.counter
                                )

                                Toast.makeText(context,
                                    context.getString(R.string.account_successfully_added), Toast.LENGTH_SHORT).show()
                                navController.navigate(Main) {
                                    popUpTo(Main) { inclusive = true }
                                }
                            } else {
                                Toast.makeText(context,
                                    context.getString(R.string.invalid_qr_code_format), Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(context,
                                context.getString(R.string.qr_code_not_found_in_the_image), Toast.LENGTH_LONG).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context,
                            context.getString(R.string.scanning_error, it.message), Toast.LENGTH_LONG).show()
                    }
            } catch (e: Exception) {
                Log.e("GalleryButton", "Error processing image", e)
            }
        }
    }

    IconButton(
        onClick = {
            mainViewModel.setIntentionalExternalAction()
            launcher.launch("image/*") },
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.3f))
    ) {
        Icon(
            painterResource(R.drawable.gallery),
            contentDescription = "Галерея",
            tint = Color.White
        )
    }
}