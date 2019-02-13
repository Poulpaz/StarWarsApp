package com.example.lpiem.theelderscrolls.ui.activity

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.Toast
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.manager.GoogleConnectionManager
import com.example.lpiem.theelderscrolls.utils.RxLifecycleDelegate
import com.example.lpiem.theelderscrolls.viewmodel.ConnectionActivityViewModel
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.activity_splash.*
import org.kodein.di.generic.instance
import timber.log.Timber

class SplashActivity : BaseActivity() {

    private val viewModel: ConnectionActivityViewModel by instance(arg = this)
    private val googleManager: GoogleConnectionManager by instance()

    companion object {
        val DEFAULT_ANIMATION_DURATION = 50000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Hiding title bar of SplashActivity
        window.requestFeature(Window.FEATURE_NO_TITLE)
        //Making the activity full screen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        onStartRotation();
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
                                        onSignInStateError(it)
                                    }
                                    is NetworkEvent.Success -> {
                                        onSignInStateSuccess()
                                    }
                                }
                            }, { Timber.e(it) }
                    )
        }, 2000)
    }

    //region Facebook & Google

    private fun onSignInStateSuccess() {
        startHome()
    }

    private fun onSignInStateError(error: NetworkEvent.Error) {
        AccessToken.getCurrentAccessToken()?.let {
            LoginManager.getInstance().logOut()
        }
        googleManager.getGoogleSignInClient().signOut()
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

    private fun onStartRotation() {
        val valueAnimator = ValueAnimator.ofFloat(0f, 360f)
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            iv_logo_activity_splash.rotation = value
        }
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = DEFAULT_ANIMATION_DURATION
        valueAnimator.start()
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}