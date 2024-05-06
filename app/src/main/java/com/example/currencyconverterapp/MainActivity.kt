package com.example.currencyconverterapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.currencyconverterapp.data.models.Data
import com.example.currencyconverterapp.data.models.currencyData
import com.example.currencyconverterapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.lang.StringBuilder


class MainActivity : AppCompatActivity() {

    lateinit var fromSpinner: Spinner
    lateinit var toSpinner:Spinner
    lateinit var txtView: TextView
    lateinit var btn:Button
    lateinit var darkBtn:Switch
    lateinit var headerTxt:TextView
    lateinit var fromTxt :TextView
    lateinit var toTxt:TextView

    lateinit var currency:Data

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val nightMode = NightMode()
        nightMode.show()

        //Spinner from
        val currencyArray = resources.getStringArray(R.array.currency_codes)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,currencyArray)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        fromSpinner = findViewById(R.id.spFromCurrency)
        fromSpinner.adapter = adapter

        lateinit var fromSelectedItem:String
        fromSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Handle item selection here
                fromSelectedItem = parent?.getItemAtPosition(position).toString()
                Toast.makeText(this@MainActivity, "Selected item: $fromSelectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        }


        //Spinner to


        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item,currencyArray)

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        toSpinner = binding.spToCurrency

        toSpinner.adapter = adapter2

        lateinit var toSelectedItem:String
        toSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Handle item selection here
                toSelectedItem = parent?.getItemAtPosition(position).toString()
                Toast.makeText(this@MainActivity, "Selected item: $toSelectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        }

        txtView = binding.txtView5

        //Evrything About Retrofit
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://app.freecurrencyapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)


        val retrofitData = retrofitBuilder.getCurrencyData()

        retrofitData.enqueue(object : Callback<currencyData?> {
            override fun onResponse(p0: Call<currencyData?>, p1: Response<currencyData?>) {
                if (p1.isSuccessful) {
                    val responseBody = p1.body()
                    currency = responseBody?.data!!

                    btn = binding.convertBtn
                    btn.setOnClickListener {
                        convertMoney(btn)

                    }


                    val collectData = StringBuilder()

                    // Iterate through the currency map and append currency names and exchange rates to the StringBuilder


                    // Use collectData StringBuilder as needed
                    Log.d("MainActivity", "Currency data: $collectData")
                } else {
                    Log.d("MainActivity", "Response unsuccessful: ${p1.code()}")
                }

            }

            override fun onFailure(p0: Call<currencyData?>, p1: Throwable) {
                Log.d("Main Activity", "On Failure: "+p1.message)
            }
        })

        //Dark Mode enabled
        darkBtn = binding.darkBtn
        fromTxt = binding.textView
        toTxt = binding.textView2
        darkBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                txtView.setTextColor(Color.WHITE)
                binding.headingTxt.setTextColor(Color.WHITE)
                binding.etFrom.setBackgroundColor(Color.WHITE)
                binding.spToCurrency.setBackgroundColor(Color.WHITE)
                binding.spFromCurrency.setBackgroundColor(Color.WHITE)
                binding.textView.setTextColor(Color.WHITE)
                binding.textView2.setTextColor(Color.WHITE)
                binding.main.setBackgroundColor(Color.BLACK)

            }else{
                txtView.setTextColor(Color.BLACK)
                binding.headingTxt.setTextColor(Color.BLACK)
                binding.etFrom.setBackgroundColor(Color.WHITE)
                //binding.spToCurrency.setBackgroundColor(Color.BLACK)
                //binding.spFromCurrency.setBackgroundColor(Color.BLACK)
                binding.textView.setTextColor(Color.BLACK)
                binding.textView2.setTextColor(Color.BLACK)
                binding.main.setBackgroundColor(Color.WHITE)
            }
        }


    }

    fun convertMoney(view: View) {
        var amount = binding.etFrom.text.toString()
        var finalAmount = amount.toDoubleOrNull()
        var fromCurrency = binding.spFromCurrency.selectedItem.toString()
        var fromRate = getRate(fromCurrency)

        var amountInUSD = finalAmount!!.div(fromRate)




        var toCurrency = binding.spToCurrency.selectedItem.toString()
        var rate = getRate(toCurrency)

        var result = amountInUSD?.times(rate)

        binding.txtView5.text = "${finalAmount} ${fromCurrency} = ${result} ${toCurrency}"
    }

    fun getRate(toCurrency: String): Double {
        when (toCurrency) {
            "AUD" -> return currency.AUD
            "BGN" -> return currency.BGN
            "BRL" -> return currency.BRL
            "CAD" -> return currency.CAD
            "CHF" -> return currency.CHF
            "CNY" -> return currency.CNY
            "CZK" -> return currency.CZK
            "DKK" -> return currency.DKK
            "EUR" -> return currency.EUR
            "GBP" -> return currency.GBP
            "HKD" -> return currency.HKD
            "HRK" -> return currency.HRK
            "HUF" -> return currency.HUF
            "IDR" -> return currency.IDR
            "ILS" -> return currency.ILS
            "INR" -> return currency.INR
            "ISK" -> return currency.ISK
            "JPY" -> return currency.JPY
            "KRW" -> return currency.KRW
            "MXN" -> return currency.MXN
            "MYR" -> return currency.MYR
            "NOK" -> return currency.NOK
            "NZD" -> return currency.NZD
            "PHP" -> return currency.PHP
            "PLN" -> return currency.PLN
            "RON" -> return currency.RON
            "RUB" -> return currency.RUB
            "SEK" -> return currency.SEK
            "SGD" -> return currency.SGD
            "THB" -> return currency.THB
            "TRY" -> return currency.TRY
            "ZAR" -> return currency.ZAR
            else -> return 0.0
        }
    }



}