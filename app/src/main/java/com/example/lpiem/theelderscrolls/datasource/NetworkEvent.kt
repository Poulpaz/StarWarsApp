package com.example.lpiem.theelderscrolls.datasource

import android.content.Context
import com.google.gson.JsonParser
import retrofit2.HttpException

sealed class NetworkEvent {
    class Error(val e: Throwable) : NetworkEvent()
    object InProgress : NetworkEvent()
    object Success : NetworkEvent()
    object None : NetworkEvent()
}
