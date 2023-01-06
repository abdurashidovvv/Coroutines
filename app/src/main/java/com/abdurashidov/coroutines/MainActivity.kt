package com.abdurashidov.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.abdurashidov.coroutines.databinding.ActivityMainBinding
import com.abdurashidov.coroutines.models.Valyuta
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch(Dispatchers.IO){
            Log.d(TAG, "onCreate: ${getData()}")
        }
    }

    suspend fun getData():List<Valyuta>{
        return GlobalScope.async(Dispatchers.IO) {
            val url= URL(" http://cbu.uz/uzc/arkhiv-kursov-valyut/json/")
            val connection=url.openConnection() as HttpURLConnection
            connection.connect()
            val inputStream=connection.inputStream
            val bufferedReader=inputStream.bufferedReader()
            val gs=bufferedReader.readLine()

            val gson= Gson()
            val type=object: TypeToken<ArrayList<Valyuta>>(){}.type
            val list=gson.fromJson<ArrayList<Valyuta>>(gs, type)
            return@async list
        }.await()
    }
}