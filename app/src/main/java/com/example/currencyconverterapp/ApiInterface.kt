package com.example.currencyconverterapp

import com.example.currencyconverterapp.data.models.currencyData
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("https://api.freecurrencyapi.com/v1/latest?apikey=fca_live_PVPUC3CHDvnegbPzXJ6Uq46i1R8ULVIFdK3gipZ2")
    fun getCurrencyData():Call<currencyData>
}