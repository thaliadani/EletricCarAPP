package com.example.eletriccarapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.eletriccarapp.R
import androidx.core.content.edit

class CalcularAutonomiaActivity: AppCompatActivity() {
    lateinit var precoKwh: EditText
    lateinit var kmPercorrido: EditText
    lateinit var btnCalcular: Button
    lateinit var resultado: TextView
    lateinit var btnVoltar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calcular_autonomia)

        setupViews()
        setupListeners()
        voltarMain()

        setupCachedResult()
    }

    private fun setupCachedResult() {
        val valorCalculado = getSharedPref()
        resultado.text = valorCalculado.toString()
    }

    fun setupViews() {
        precoKwh = findViewById(R.id.et_precoKwh)
        kmPercorrido = findViewById(R.id.et_kmPercorrido)
        btnCalcular = findViewById(R.id.btn_calcular)
        resultado = findViewById(R.id.tv_resultado)
        btnVoltar = findViewById(R.id.btn_voltar)
    }

    fun calcularAutonomia() {
        val precoKwh = precoKwh.text.toString().toFloat()
        val kmPercorrido = kmPercorrido.text.toString().toFloat()
        val result = precoKwh / kmPercorrido

        resultado.text = result.toString()
        saveSharePref(result)
    }
    fun setupListeners() {
        btnCalcular.setOnClickListener {
           calcularAutonomia()
        }
    }

    fun voltarMain(){
        btnVoltar.setOnClickListener {
            finish()
        }
    }

    fun saveSharePref(result: Float){
        val  sharedPref = getPreferences(MODE_PRIVATE) ?: return
        sharedPref.edit {
            putFloat(getString(R.string.saved_calc), result)
        }

    }

    fun getSharedPref(): Float {
        val sharedPref = getPreferences(MODE_PRIVATE)
        val calculo = sharedPref.getFloat(getString(R.string.saved_calc),0.0f)
        return calculo
    }
}