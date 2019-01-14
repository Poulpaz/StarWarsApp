package com.example.lpiem.theelderscrolls.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.datasource.request.SignUpData
import com.example.lpiem.theelderscrolls.manager.GoogleConnectionManager
import com.example.lpiem.theelderscrolls.model.User
import com.example.lpiem.theelderscrolls.viewmodel.ConnectionActivityViewModel
import com.facebook.*
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
import org.kodein.di.generic.instance


class ConnectionActivity : BaseActivity() {

    private val viewModel: ConnectionActivityViewModel by instance(arg = this)

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

        viewModel.signInState
                .subscribe(
                        {
                            when (it) {
                                NetworkEvent.None -> {
                                    // Nothing
                                }
                                NetworkEvent.InProgress -> {
                                    onSignInStateInProgress()
                                }
                                is NetworkEvent.Error -> {
                                    onSignInStateError(it)
                                }
                                is NetworkEvent.Success -> {
                                    onSignInStateSuccess()
                                }
                            }
                        }, { Timber.e(it) }
                )
    }

    private fun onSignInStateSuccess() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun onSignInStateError(network: NetworkEvent.Error) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(R.string.tv_title_dialog_logout)
                .setMessage(R.string.tv_message_dialog_logout)
                .setNegativeButton(R.string.b_cancel_dialog_logout, { dialoginterface, i ->
                    b_login_facebook.isEnabled = true
                    b_login_google.isEnabled = true
                })
                .setPositiveButton(R.string.b_validate_dialog_logout) { dialoginterface, i ->
                    signUpUser()
                }.show()
    }

    private fun signUpUser() {
        val token = AccessToken.getCurrentAccessToken().token
        val user = SignUpData("Carlos", "Chastagnier", 28, "carlos@gmail.com", 10, "google.com")
        viewModel.signUp(token, user)
    }

    private fun onSignInStateInProgress() {
        //b_login_facebook.isEnabled = false
        //b_login_google.isEnabled = false
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
            //startHome()
        }
    }

    fun loginWithFacebook() {

        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        val token = loginResult.accessToken.token
                        Log.d(TAG, "Facebook token: " + token)
                        //val user = SignUpData("Carlos", "Chastagnier", 28, "carlos@gmail.com", 10, "google.com")
                        //viewModel.signUp(token, user)
                        GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                "/{person-id}/",
                                null,
                                HttpMethod.GET,
                                GraphRequest.Callback {
                                    it.rawResponse
                                }
                        ).executeAsync()
                        viewModel.signIn(token)
                        //startHome()
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