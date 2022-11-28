package com.example.ubiquitihomework.network

import com.example.ubiquitihomework.misc.API_KEI
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val url = request.url.newBuilder().addQueryParameter("api_key", API_KEI).build()
        val newRequest: Request = request.newBuilder()
            .url(url)
            .build()
        return chain.proceed(newRequest)
    }
}