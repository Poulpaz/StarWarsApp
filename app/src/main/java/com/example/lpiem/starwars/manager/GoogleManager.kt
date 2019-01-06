package com.example.lpiem.starwars.manager

import android.content.Context
import com.example.lpiem.starwars.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_main.view.*

class GoogleManager(val context : Context){

    private lateinit var mGoogleSignInClient : GoogleSignInClient

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(R.string.client_server_id.toString())
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun getGoogleSignInClient() : GoogleSignInClient{
        return mGoogleSignInClient
    }
}