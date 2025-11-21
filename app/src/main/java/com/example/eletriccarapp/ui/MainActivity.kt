package com.example.eletriccarapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.eletriccarapp.R
import com.example.eletriccarapp.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    lateinit var fabCalcularAutonomia: FloatingActionButton

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.fv_navigation)
        setupWithNavController(binding.bottomNavigationView, navController)

        setupView()
        setupListener()
    }
    fun setupView() {
        fabCalcularAutonomia = findViewById(R.id.fab_calcular)
    }
    fun setupListener() {
        fabCalcularAutonomia.setOnClickListener {
            val intent = Intent(this, CalcularAutonomiaActivity::class.java)
            startActivity(intent)
        }
    }

}


