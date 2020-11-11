package com.silexsecure.arusdriver.service

import com.silexsecure.arusdriver.util.SessionManager
import com.silexsecure.arusdriver.util.Config


object ApiUtils {

    val BASE_URL = Config.BASE_URL

    private var sessionManager: SessionManager? = null

    val apiService: KotlinAPIService
        get() = RetrofitClient.getClient(BASE_URL)!!.create(KotlinAPIService::class.java)


}