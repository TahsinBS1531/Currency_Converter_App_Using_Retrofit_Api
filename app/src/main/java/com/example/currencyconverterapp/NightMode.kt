package com.example.currencyconverterapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.example.currencyconverterapp.databinding.ActivityMainBinding

class NightMode : AppCompatActivity(){

    lateinit var switchMode:SwitchCompat
    lateinit var sharedpreferences:SharedPreferences
    lateinit var editor:SharedPreferences.Editor

    var nightMode:Boolean = false

    fun show(){

        switchMode = findViewById(R.id.switchMode)
        sharedpreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE)
        nightMode = sharedpreferences.getBoolean("nightMode",false)

        if (nightMode){
            switchMode.setChecked(true)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        switchMode.setOnClickListener {
            if(nightMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor = sharedpreferences.edit()
                editor.putBoolean("nightMode",false)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor = sharedpreferences.edit()
                editor.putBoolean("nightMode",true)
            }
            editor.apply()
        }

    }


}