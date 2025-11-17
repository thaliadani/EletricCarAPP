package com.example.eletriccarapp.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccarapp.R
import com.example.eletriccarapp.data.RetrofitClient
import com.example.eletriccarapp.data.local.CarRepository
import com.example.eletriccarapp.domain.Carro
import com.example.eletriccarapp.ui.adapter.CarAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarFragment : Fragment() {
    private lateinit var listaCarros: RecyclerView
    private lateinit var progress: ProgressBar
    private lateinit var wifiOff: ImageView
    private lateinit var textWifiOff: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.car_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
    }

    override fun onResume() {
        super.onResume()
        if (checkForInternet(context)) {
            callService()
        } else {
            getAllLocalCars()
        }
    }

    private fun getAllLocalCars() {
        val repository = CarRepository(requireContext())
        val carList = repository.getAll()
        setupList(carList)
        listaCarros.isVisible = true
    }

    private fun emptyState() {
        progress.isVisible = false
        listaCarros.isVisible = false
        wifiOff.isVisible = true
        textWifiOff.isVisible = true
    }
    private fun setupViews(view: View) {
        view.apply {
            listaCarros = findViewById(R.id.rv_lista_carros)
            listaCarros.layoutManager = LinearLayoutManager(requireContext())
            progress = findViewById(R.id.pb_loader)
            wifiOff = findViewById(R.id.iv_wifi_off)
            textWifiOff = findViewById(R.id.tv_wifi_off)
        }
    }
    private fun setupList(lista: List<Carro>) {
        val carroAdapter = CarAdapter(lista)
        listaCarros.apply {
            isVisible = true
            adapter = carroAdapter
        }

        carroAdapter.carItemLister = { carro ->
            val carRepository = CarRepository(requireContext())
            if (carro.isFavorite) {
                carRepository.save(carro)
            } else {
                carRepository.delete(carro.id)
            }
        }
    }

    private fun callService() {
        progress.isVisible = true
        wifiOff.isVisible = false
        textWifiOff.isVisible = false

        RetrofitClient.instance.getAllCars().enqueue(object : Callback<List<Carro>> {
            override fun onResponse(call: Call<List<Carro>>, response: Response<List<Carro>>) {
                progress.isVisible = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        setupListWithFavorites(it)
                    }
                } else {
                    Log.e("CarFragment", "Response not successful: ${response.code()}")
                    emptyState()
                }
            }

            override fun onFailure(call: Call<List<Carro>>, t: Throwable) {
                progress.isVisible = false
                Log.e("CarFragment", "Error fetching cars", t)
                emptyState()
            }
        })
    }

    fun setupListWithFavorites(cars: List<Carro>) {
        val carRepository = CarRepository(requireContext())
        val localCars = carRepository.getAll()
        for (car in cars) {
            for (localCar in localCars) {
                if (car.id == localCar.id) {
                    car.isFavorite = true
                }
            }
        }
        setupList(cars)
    }


    private fun checkForInternet(context: Context?): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}