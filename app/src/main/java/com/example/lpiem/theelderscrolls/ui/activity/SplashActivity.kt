package com.example.lpiem.theelderscrolls.ui.activity

import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.manager.GoogleConnectionManager
import com.example.lpiem.theelderscrolls.utils.RxLifecycleDelegate
import com.example.lpiem.theelderscrolls.viewmodel.ConnectionActivityViewModel
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import org.kodein.di.generic.instance
import timber.log.Timber

class SplashActivity : BaseActivity() {

    private val viewModel: ConnectionActivityViewModel by instance(arg = this)
    private val googleManager: GoogleConnectionManager by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Hiding title bar of SplashActivity
        window.requestFeature(Window.FEATURE_NO_TITLE)
        //Making the activity full screen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({

            testUserConnected()

            viewModel.signInState
                    .takeUntil(lifecycle(RxLifecycleDelegate.ActivityEvent.DESTROY))
                    .subscribe(
                            {
                                when (it) {
                                    NetworkEvent.None -> {
                                        // Nothing
                                    }
                                    NetworkEvent.InProgress -> {
                                        // Nothing
                                    }
                                    is NetworkEvent.Error -> {
                                        onSignInStateError()
                                    }
                                    is NetworkEvent.Success -> {
                                        onSignInStateSuccess()
                                    }
                                }
                            }, { Timber.e(it) }
                    )
        }, 3000)
    }

    //region Facebook & Google

    private fun onSignInStateSuccess() {
        startHome()
    }

    private fun onSignInStateError() {
        AccessToken.getCurrentAccessToken()?.let {
            LoginManager.getInstance().logOut()
        }
        googleManager.getGoogleSignInClient().signOut()
        Toast.makeText(this, getString(R.string.tv_error_login), Toast.LENGTH_SHORT).show()
        ConnectionActivity.start(this@SplashActivity)
    }

    private fun testUserConnected() {
        val accessToken = AccessToken.getCurrentAccessToken()
        val googleAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (accessToken != null && !accessToken.isExpired || googleAccount != null) {
            viewModel.loadUser()
        } else {
            ConnectionActivity.start(this@SplashActivity)
        }
    }

    private fun startHome() {
        MainActivity.start(this@SplashActivity)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}