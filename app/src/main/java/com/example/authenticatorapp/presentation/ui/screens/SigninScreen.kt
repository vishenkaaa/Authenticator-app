package com.example.authenticatorapp.presentation.ui.screens

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.authenticatorapp.R
import com.example.authenticatorapp.presentation.ui.theme.AppTypography
import com.example.authenticatorapp.presentation.ui.theme.Blue
import com.example.authenticatorapp.presentation.ui.theme.Gray4
import com.example.authenticatorapp.presentation.ui.theme.Gray5
import com.example.authenticatorapp.presentation.ui.theme.Gray6
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.auth
import org.checkerframework.common.subtyping.qual.Bottom

@Composable
fun SigninScreen(navController: NavController) {

    val colors = MaterialTheme.colorScheme
    val auth = Firebase.auth
    val context = LocalContext.current

    var agreeToTermsAndPrivacy by remember { mutableStateOf(false)}
    var loginIsPressed by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
        .fillMaxSize()
        .background(colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 50.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable {
                            navController.popBackStack()
                        }
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Sign in",
                    color = colors.onPrimary,
                    style = AppTypography.bodyLarge,
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Wellcome to\nAuthenticator",
                    color = colors.primary,
                    style = AppTypography.titleLarge,
                    modifier = Modifier
                        .clickable {
                            navController.popBackStack()
                            navController.navigate("Onboarding")
                        }
                )

                Spacer(modifier = Modifier.height(60.dp))
                GoogleSignInButton(auth, context, navController, agreeToTermsAndPrivacy) {
                    loginIsPressed = true
                }

                Spacer(modifier = Modifier.height(24.dp))
                AppleSignInButton(auth, context, navController, agreeToTermsAndPrivacy){
                    loginIsPressed = true
                }

                Spacer(modifier = Modifier.height(170.dp))

                val annotatedText = buildAnnotatedString {
                    append("I agree to Authenticator App ")

                    pushStringAnnotation(tag = "TERMS", annotation = "TERMS")
                    withStyle(
                        style = SpanStyle(
                            color = Blue,
                            fontSize = AppTypography.labelSmall.fontSize
                        )
                    ) {
                        append("Terms of Service")
                    }
                    pop()

                    append(" and ")

                    pushStringAnnotation(tag = "PRIVACY", annotation = "PRIVACY")
                    withStyle(
                        style = SpanStyle(
                            color = Blue,
                            fontSize = AppTypography.labelSmall.fontSize
                        )
                    ) {
                        append("Privacy Policy")
                    }
                    pop()
                }

                var isChecked by remember { mutableStateOf(false) }

                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = {
                            isChecked = it
                            agreeToTermsAndPrivacy = it
                        },
                        modifier = Modifier.size(18.dp),
                        colors = CheckboxDefaults.colors(
                            checkedColor = colors.primary,
                            uncheckedColor = colors.onPrimary
                        ),
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    ClickableText(
                        text = annotatedText,
                        style = AppTypography.labelSmall.copy(color = colors.onPrimary),
                        onClick = { offset ->
                            val annotation =
                                annotatedText.getStringAnnotations(start = offset, end = offset)
                                    .firstOrNull()
                            annotation?.let {
                                when (it.tag) {
                                    "TERMS" -> {

                                    }

                                    "PRIVACY" -> {

                                    }
                                }
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(70.dp))
            }
        }

        if (!agreeToTermsAndPrivacy && loginIsPressed){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 146.dp)
                    .align(Alignment.BottomCenter)
                    .background(colors.onPrimaryContainer, shape = RoundedCornerShape(8.dp))
                    .border(width = 1.dp, color = Blue, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = stringResource(R.string.you_must_agree_to_the_terms_of_service_and_privacy_policy_to_proceed_with_login),
                        style = AppTypography.labelSmall,
                        color = colors.onPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = "Close",
                        modifier = Modifier
                            .size(18.dp)
                            .padding(3.dp)
                            .clickable {
                                loginIsPressed = false
                            },
                        colorFilter = ColorFilter.tint(Gray5),
                    )
                }
            }
        }
    }
}

@Composable
fun AppleSignInButton(auth: FirebaseAuth, context: Context, navController: NavController, agreeToTermsAndPrivacy: Boolean, onLoginAttempt: () -> Unit) {

    Button(
        onClick = {
            if(agreeToTermsAndPrivacy){
                val provider = OAuthProvider.newBuilder("apple.com")
                val pending = auth.pendingAuthResult
                if (pending != null) {
                    pending.addOnSuccessListener { result ->
                        Log.d("AppleSignIn", "Success: ${result.user?.email}")
                        navController.navigate("Main")
                    }
                        .addOnFailureListener { e ->
                            Log.e("AppleSignIn", "Error: ${e.localizedMessage}")
                        }
                } else {
                    auth.startActivityForSignInWithProvider(context as Activity, provider.build())
                        .addOnSuccessListener { result ->
                            Log.d("AppleSignIn", "Success: ${result.user?.email}")
                            navController.navigate("Main")
                        }
                        .addOnFailureListener { e ->
                            Log.e("AppleSignIn", "Error: ${e.localizedMessage}")
                        }
                }
            }
            else onLoginAttempt()
        },
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(27.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Black
        )
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .zIndex(1f)
                .align(Alignment.Bottom)
        ) {
            Image(
                painter = painterResource(R.drawable.apple),
                contentDescription = "Apple",
            )
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = "Sign In with Apple",
                color = White,
                style = AppTypography.bodyLarge,
            )
        }
    }
}

@Composable
fun GoogleSignInButton(auth: FirebaseAuth, context: Context, navController: NavController, agreeToTermsAndPrivacy: Boolean, onLoginAttempt: () -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!, auth, navController)
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Error: ${e.localizedMessage}")
        }
    }

    val googleSignInClient = remember {
        GoogleSignIn.getClient(
            context.applicationContext, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("659133148753-gdeg9eu8k0mffj8nbtptirv2920glm3v.apps.googleusercontent.com")
                .requestEmail()
                .build()
        )
    }

    val colors = MaterialTheme.colorScheme
    Button(
        onClick = {
            if(agreeToTermsAndPrivacy){ launcher.launch(googleSignInClient.signInIntent) }
            else onLoginAttempt()},
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .shadow(4.dp, shape = RoundedCornerShape(27.dp))
            .border(
                1.dp,
                color = if (isSystemInDarkTheme()) Gray6 else White,
                RoundedCornerShape(30.dp)
            ),
        shape = RoundedCornerShape(27.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.onPrimaryContainer
        )
    ) {
        Image(
            painter = painterResource(R.drawable.google),
            contentDescription = "Google",
        )
        Spacer(modifier = Modifier.width(15.dp))
        Text(
            text = "Sign In with Google",
            color = colors.inversePrimary,
            style = AppTypography.bodyLarge
        )
    }
}

fun firebaseAuthWithGoogle(idToken: String, auth: FirebaseAuth, navController: NavController) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    auth.signOut()
    Log.d("GoogleSignIn", "Success: ${auth.currentUser?.displayName}")
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("GoogleSignIn", "Success: ${auth.currentUser?.displayName}")

                Handler(Looper.getMainLooper()).post {
                    navController.navigate("Main")
                }
            } else {
                Log.e("GoogleSignIn", "Error: ${task.exception?.message}")
            }
        }
}

//fun signOutGoogle(auth: FirebaseAuth, context: Context) {
//    val googleSignInClient = GoogleSignIn.getClient(
//        context.applicationContext,
//        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken("659133148753-gdeg9eu8k0mffj8nbtptirv2920glm3v.apps.googleusercontent.com")
//            .requestEmail()
//            .build()
//    )
//
//    auth.signOut()
//    googleSignInClient.revokeAccess().addOnCompleteListener {
//        Log.d("GoogleSignIn", "User fully signed out")
//    }
//}
