package com.example.lpiem.theelderscrolls.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.manager.PermissionManager
import com.example.lpiem.theelderscrolls.ui.fragment.DisconnectUserInterface
import com.example.lpiem.theelderscrolls.ui.fragment.ExchangeInterface
import com.example.lpiem.theelderscrolls.ui.fragment.HomeInterface
import com.example.lpiem.theelderscrolls.utils.or
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import com.facebook.AccessToken
import android.app.Activity
import android.view.*


class MainActivity : BaseActivity() {

    companion object {
        fun start(fromActivity: AppCompatActivity) {
            fromActivity.startActivity(
                    Intent(fromActivity, MainActivity::class.java)
            )
        }
    }

    private var disconnectProfileButtonMenu: MenuItem? = null
    private var listExchangeButtonMenu: MenuItem? = null

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
                displayDisconnectProfileButton(false)
                displayListExchangeButton(false)
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
                app_bar.visibility = View.VISIBLE
                displayDisconnectProfileButton(true)
                displayListExchangeButton(false)
                supportActionBar?.setTitle(R.string.title_profile)

                returnValue = true
            }
            R.id.navigation_exchange -> {

                currentController = navControllerExchange

                homeWrapper.visibility = View.INVISIBLE
                profileWrapper.visibility = View.INVISIBLE
                exchangeWrapper.visibility = View.VISIBLE
                chatWrapper.visibility = View.INVISIBLE
                battleWrapper.visibility = View.INVISIBLE
                app_bar.visibility = View.VISIBLE
                displayDisconnectProfileButton(false)
                displayListExchangeButton(true)
                supportActionBar?.setTitle(R.string.title_exchange)

                returnValue = true
            }
            R.id.navigation_battle -> {

                currentController = navControllerBattle

                homeWrapper.visibility = View.INVISIBLE
                profileWrapper.visibility = View.INVISIBLE
                exchangeWrapper.visibility = View.INVISIBLE
                chatWrapper.visibility = View.INVISIBLE
                battleWrapper.visibility = View.VISIBLE
                app_bar.visibility = View.VISIBLE
                displayDisconnectProfileButton(false)
                displayListExchangeButton(false)
                supportActionBar?.setTitle(R.string.title_battle)

                returnValue = true
            }
            R.id.navigation_chat -> {

                currentController = navControllerChat

                homeWrapper.visibility = View.INVISIBLE
                profileWrapper.visibility = View.INVISIBLE
                exchangeWrapper.visibility = View.INVISIBLE
                chatWrapper.visibility = View.VISIBLE
                battleWrapper.visibility = View.INVISIBLE
                app_bar.visibility = View.VISIBLE
                displayDisconnectProfileButton(false)
                displayListExchangeButton(false)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        menuInflater.inflate(R.menu.menu_exchange, menu)
        disconnectProfileButtonMenu = menu?.findItem(R.id.b_logout_profile_fragment)
        listExchangeButtonMenu = menu?.findItem(R.id.b_list_exchange_fragment)
        displayDisconnectProfileButton(false)
        displayListExchangeButton(false)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.b_logout_profile_fragment -> {
                val container = supportFragmentManager.findFragmentById(R.id.content_profile)
                val frg = container?.childFragmentManager?.findFragmentById(R.id.content_profile)
                if (frg is DisconnectUserInterface) {
                    frg.disconnectUser()
                }
            }
            R.id.b_list_exchange_fragment -> {
                val container = supportFragmentManager.findFragmentById(R.id.content_exchange)
                val frg = container?.childFragmentManager?.findFragmentById(R.id.content_exchange)
                if (frg is ExchangeInterface) {
                    frg.displayListExchange()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun displayDisconnectProfileButton(value: Boolean) {
        disconnectProfileButtonMenu?.isVisible = value
    }

    fun displayListExchangeButton(value: Boolean) {
        listExchangeButtonMenu?.isVisible = value
    }

    fun requestCameraPermission(){
        if (permissionManager.requestCameraPermission(this)) {
            openQrCode()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionManager.REQUEST_PERMISSION_CAMERA && grantResults[permissions.indexOf(Manifest.permission.CAMERA)] == PackageManager.PERMISSION_GRANTED) {
                openQrCode()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ScannerQrCodeActivity.QrCodeRequestCode -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.getStringExtra(ScannerQrCodeActivity.QrCodeKey)?.let {
                        val container = supportFragmentManager.findFragmentById(R.id.content_home)
                        val frg = container?.childFragmentManager?.findFragmentById(R.id.content_home)
                        if (frg is HomeInterface) {
                            frg.openCardDetails(it)
                        }
                    }
                }
            }
        }
    }

    private fun openQrCode(){
        ScannerQrCodeActivity.start(this)
    }

    override fun onBackPressed() {
        currentController
                .let { if (it.popBackStack().not()) finish() }
                .or { finish ()}
    }

    override fun onResume() {
        super.onResume()
        displayDisconnectProfileButton(false)
        displayListExchangeButton(false)
    }
}
