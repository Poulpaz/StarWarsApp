package com.example.lpiem.starwars.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.lpiem.starwars.R
import com.facebook.*
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.connection_activity.*
import java.util.*

class ConnectionActivity : BaseActivity() {

    private val RC_SIGN_IN = 0
    private var TAG = "ConectionActivity"
    private var callbackManager: CallbackManager? = null
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.connection_activity)
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)

        loginWithFacebook()
        loginWithGoogle()

    }

        fun loginWithFacebook(){

            b_login_facebook.setOnClickListener {

                callbackManager = CallbackManager.Factory.create()
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
                LoginManager.getInstance().registerCallback(callbackManager,
                        object : FacebookCallback<LoginResult> {
                            override fun onSuccess(loginResult: LoginResult) {
                                Log.d(TAG, "Facebook token: " + loginResult.accessToken.token)
                            }

                            override fun onCancel() {
                                Log.d(TAG, "Facebook onCancel.")

                            }

                            override fun onError(error: FacebookException) {
                                Log.d(TAG, "Facebook onError.")

                            }
                        })
            }

        }

        fun loginWithGoogle(){
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build()

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

            b_login_google.setOnClickListener{
                signIn()
            }

        }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            //updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            //updateUI(null)
        }

    }

}