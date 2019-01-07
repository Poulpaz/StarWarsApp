package com.example.lpiem.theelderscrolls.manager

import android.content.Context
import com.example.lpiem.theelderscrolls.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class GoogleConnectionManager(val context : Context){

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