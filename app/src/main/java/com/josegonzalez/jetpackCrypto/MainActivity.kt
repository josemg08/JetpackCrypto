package com.josegonzalez.jetpackCrypto

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.josegonzalez.jetpackCrypto.databinding.ActivityMainBinding
import com.josegonzalez.jetpackCrypto.ui.activities.CryptoListActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            delay(2000L)
            startActivity(Intent(this@MainActivity, CryptoListActivity::class.java))
            finish()
        }
    }
}