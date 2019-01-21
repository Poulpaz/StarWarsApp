package com.example.lpiem.theelderscrolls.datasource.response

import com.google.gson.annotations.SerializedName

open class BaseResponse(
        @SerializedName("message") var message: String?
)