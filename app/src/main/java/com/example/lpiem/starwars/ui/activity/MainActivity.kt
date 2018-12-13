package com.example.lpiem.starwars.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.lpiem.starwars.R
import com.example.lpiem.starwars.utils.or
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import com.facebook.AccessToken
import com.facebook.login.LoginManager


class MainActivity : AppCompatActivity() {

    companion object {
        fun start(fromActivity: AppCompatActivity) {
            fromActivity.startActivity(
                    Intent(fromActivity, MainActivity::class.java)
            )
        }
    }

    private lateinit var currentController: NavController
    private lateinit var navControllerHome: NavController
    private lateinit var navControllerProfile: NavController
    private lateinit var navControllerExchange: NavController
    private lateinit var navControllerChat: NavController
    private lateinit var navControllerBattle: NavController

    private lateinit var homeWrapper: FrameLayout
    private lateinit var profileWrapper: FrameLayout
    private lateinit var exchangeWrapper: FrameLayout
    private lateinit var chatWrapper: FrameLayout
    private lateinit var battleWrapper: FrameLayout

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var returnValue = false

        when (item.itemId) {
            R.id.navigation_home -> {
                currentController = navControllerHome

                homeWrapper.visibility = View.VISIBLE
                profileWrapper.visibility = View.INVISIBLE
                exchangeWrapper.visibility = View.INVISIBLE
                chatWrapper.visibility = View.INVISIBLE
                battleWrapper.visibility = View.INVISIBLE
                app_bar.visibility = View.VISIBLE
                supportActionBar?.setTitle(R.string.title_home)

                returnValue = true
            }
            R.id.navigation_profile -> {

                currentController = navControllerProfile

                homeWrapper.visibility = View.INVISIBLE
                profileWrapper.visibility = View.VISIBLE
                exchangeWrapper.visibility = View.INVISIBLE
                chatWrapper.visibility = View.INVISIBLE
                battleWrapper.visibility = View.INVISIBLE
                app_bar.visibility = View.GONE
                supportActionBar?.setTitle(R.string.title_profile)

                returnValue = true
            }
            R.id.navigation_exchange -> {

                currentController = navControllerProfile

                homeWrapper.visibility = View.INVISIBLE
                profileWrapper.visibility = View.INVISIBLE
                exchangeWrapper.visibility = View.VISIBLE
                chatWrapper.visibility = View.INVISIBLE
                battleWrapper.visibility = View.INVISIBLE
                app_bar.visibility = View.VISIBLE
                supportActionBar?.setTitle(R.string.title_exchange)

                returnValue = true
            }
            R.id.navigation_battle -> {

                currentController = navControllerProfile

                homeWrapper.visibility = View.INVISIBLE
                profileWrapper.visibility = View.INVISIBLE
                exchangeWrapper.visibility = View.INVISIBLE
                chatWrapper.visibility = View.INVISIBLE
                battleWrapper.visibility = View.VISIBLE
                app_bar.visibility = View.VISIBLE
                supportActionBar?.setTitle(R.string.title_battle)

                returnValue = true
            }
            R.id.navigation_chat -> {

                currentController = navControllerProfile

                homeWrapper.visibility = View.INVISIBLE
                profileWrapper.visibility = View.INVISIBLE
                exchangeWrapper.visibility = View.INVISIBLE
                chatWrapper.visibility = View.VISIBLE
                battleWrapper.visibility = View.INVISIBLE
                app_bar.visibility = View.VISIBLE
                supportActionBar?.setTitle(R.string.title_chat)


                returnValue = true
            }
        }
        return@OnNavigationItemSelectedListener returnValue
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initView()

        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        Log.d("TEST2", isLoggedIn.toString())

        currentController = navControllerHome

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun initView(){
        navControllerHome = (supportFragmentManager
                .findFragmentById(R.id.content_home) as NavHostFragment)
                .navController

        navControllerProfile = (supportFragmentManager
                .findFragmentById(R.id.content_profile) as NavHostFragment)
                .navController

        navControllerExchange = (supportFragmentManager
                .findFragmentById(R.id.content_exchange) as NavHostFragment)
                .navController

        navControllerChat = (supportFragmentManager
                .findFragmentById(R.id.content_chat) as NavHostFragment)
                .navController

        navControllerBattle = (supportFragmentManager
                .findFragmentById(R.id.content_battle) as NavHostFragment)
                .navController

        homeWrapper = content_home_wrapper
        profileWrapper = content_profile_wrapper
        exchangeWrapper = content_exchange_wrapper
        chatWrapper = content_chat_wrapper
        battleWrapper = content_battle_wrapper
    }

    override fun supportNavigateUpTo(upIntent: Intent) {
        currentController.navigateUp()
    }

    override fun onSupportNavigateUp(): Boolean {
        currentController.navigateUp()
        return true
    }

    override fun onBackPressed() {
        currentController
                .let { if (it.popBackStack().not()) finish() }
                .or { finish ()}
    }
}