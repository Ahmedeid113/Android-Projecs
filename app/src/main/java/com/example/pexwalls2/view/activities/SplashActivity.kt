package com.example.pexwalls2.view.activities

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import com.example.pexwalls2.databinding.ActivitySplashBinding
import com.example.pexwalls2.view.fragments.DialogFRagment
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        GlobalScope.launch(Dispatchers.Main) {
            delay(3000)
            val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(info)
            if (capabilities != null && capabilities.hasCapability((NetworkCapabilities.NET_CAPABILITY_INTERNET))) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            } else {
                binding.progressCircular.visibility = View.GONE
                val dialog = DialogFRagment.getInstance()
                dialog.isCancelable = false
                dialog.show(supportFragmentManager, null)
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ahmed", "onDestroy: destroyed")
    }
}