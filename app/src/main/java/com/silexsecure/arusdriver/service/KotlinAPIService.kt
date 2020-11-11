package com.silexsecure.arusdriver.service

import com.silexsecure.arusdriver.model.ReportResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface KotlinAPIService {

    @POST("consumersreports/add")
    fun registrationPost(@Body request: RequestBody): Call<ReportResponse>
}