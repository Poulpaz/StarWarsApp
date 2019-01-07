package com.example.lpiem.starwars.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.lpiem.starwars.R
import com.example.lpiem.starwars.manager.GoogleConnectionManager
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.jakewharton.rxbinding2.view.clicks
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.connection_activity.*
import timber.log.Timber
import com.facebook.AccessToken
import org.kodein.di.generic.instance


class ConnectionActivity : BaseActivity() {

    private val RC_SIGN_IN = 0
    private var TAG = "ConectionActivity"
    private var callbackManager: CallbackManager? = null
    private val googleManager: GoogleConnectionManager by instance()

    companion object {
        fun start(fromActivity: AppCompatActivity) {
            fromActivity.startActivity(
                    Intent(fromActivity, ConnectionActivity::class.java)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.connection_activity)
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)

        testUserConnected()

        b_login_google.clicks()
                .subscribe(
                        { loginWithGoogle() },
                        { Timber.e(it) }
                )

        b_login_facebook.clicks()
                .subscribe(
                        { loginWithFacebook() },
                        { Timber.e(it) }
                )

    }

    private fun testUserConnected() {
        val accessToken = AccessToken.getCurrentAccessToken()
        val googleAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (googleAccount != null || accessToken != null && !accessToken.isExpired) {
            val personName = googleAccount?.displayName
            val personGivenName = googleAccount?.givenName
            val personFamilyName = googleAccount?.familyName
            val personEmail = googleAccount?.email
            val personId = googleAccount?.id
            val personPhoto = googleAccount?.photoUrl
            val token = googleAccount?.idToken
            startHome()
        }
    }

    fun loginWithFacebook() {

        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        Log.d(TAG, "Facebook token: " + loginResult.accessToken.token)
                        startHome()
                    }

                    override fun onCancel() {
                        Log.d(TAG, "Facebook onCancel.")
                    }

                    override fun onError(error: FacebookException) {
                        Log.d(TAG, "Facebook onError.")
                    }
                })
    }

    fun loginWithGoogle() {
        val signInIntent = googleManager.getGoogleSignInClient().getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            updateUI(account)
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            Log.d(TAG, "Google token: " + account.email)
            MainActivity.start(this)
        }
    }

    private fun startHome() {
        MainActivity.start(this@ConnectionActivity)
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

}