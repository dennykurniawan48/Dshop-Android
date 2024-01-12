package com.dennydev.dshop.component

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.dennydev.dshop.model.Constant
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.tasks.OnCompleteListener


@Composable
fun GoogleSignInHelper(context: Context, onGoogleSignInSeleced: Boolean, onDismiss: () -> Unit, onResultReceived:(String) -> Unit) {
    val googleSignInClient = remember { GoogleSignIn.getClient(context, getGoogleSignInOptions()) }
    val activityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ){ result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        try {
            val account = task.result
            val idToken = account.idToken

            if (idToken != null) {
                onResultReceived(idToken)
                onDismiss()
            } else {
                throw IllegalStateException("ID token is null")
            }
        } catch (e: ApiException) {
            when(e.statusCode) {
                CommonStatusCodes.CANCELED -> {
                    Log.d("Sign in", "On close clicked")
                    onDismiss()
                }
                CommonStatusCodes.NETWORK_ERROR -> {
                    Log.d("Sign in", "Network error.")
                    onDismiss()
                }
                else -> {
                    Log.d("Sign in", "Error ${e}")
                    onDismiss()
                }
            }
            // Display an error message
        } catch (e: Exception) {
            Log.w("Error sign in", "signInResult:failed message=${e.message}")
            // Display an error message
            onDismiss()
        }
    }

        if(onGoogleSignInSeleced) {
            activityLauncher.launch(googleSignInClient.signInIntent)
        }
}

private fun getGoogleSignInOptions(): GoogleSignInOptions {
    return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(Constant.GOOGLE_CLIENT_ID)
        .requestEmail()
        .build()
}

fun googleSignOut(context: Context, onLogout:() -> Unit){
    val googleSignInClient = GoogleSignIn.getClient(context, getGoogleSignInOptions())
    googleSignInClient.signOut()
        .addOnCompleteListener(context as Activity, OnCompleteListener<Void?> {
            onLogout()
        })
}