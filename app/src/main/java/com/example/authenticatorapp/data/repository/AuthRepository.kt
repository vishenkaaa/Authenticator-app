package com.example.authenticatorapp.data.repository

import com.example.authenticatorapp.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor() {
    private val auth: FirebaseAuth = Firebase.auth

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUser(): User? {
        return auth.currentUser?.let { firebaseUser ->
            User(
                id = firebaseUser.uid,
                name = firebaseUser.displayName,
                email = firebaseUser.email
            )
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun monitorAuthState(onAuthChanged: (Boolean) -> Unit) {
        auth.addAuthStateListener { firebaseAuth ->
            onAuthChanged(firebaseAuth.currentUser != null)
        }
    }
}