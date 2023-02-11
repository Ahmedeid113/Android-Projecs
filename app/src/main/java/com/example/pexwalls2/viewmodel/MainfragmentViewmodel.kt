package com.example.pexwalls2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.pexwalls2.R
import com.example.pexwalls2.models.Image
import com.example.pexwalls2.networking.PexelServiceInterface
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(DelicateCoroutinesApi::class)
class MainFragmentViewModel(application: Application) : AndroidViewModel(application) {
    val data: MutableStateFlow<ArrayList<Image>> = MutableStateFlow(arrayListOf())


    fun fetchAllImages(): Job {
        return GlobalScope.launch(Dispatchers.IO) {
            val result = makeApiCall().getallimages()
            if (result.isSuccessful) data.emit(result.body()!!.photos)
        }
    }

    fun searchImages(query: String): Job {
        return GlobalScope.launch(Dispatchers.IO) {
            val result = makeApiCall().getimagewithsearch(query, 30)
            if (result.isSuccessful) {
                data.value.clear()
                data.emit(result.body()!!.photos)
            }
        }
    }

    private fun makeApiCall(): PexelServiceInterface {
        return Retrofit.Builder()
            .baseUrl(getApplication<Application>().getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PexelServiceInterface::class.java)
    }
}