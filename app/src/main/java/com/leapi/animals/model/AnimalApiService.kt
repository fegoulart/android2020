package com.leapi.animals.model

import com.leapi.animals.di.DaggerApiComponent
import io.reactivex.Single
import javax.inject.Inject

class AnimalApiService {

    //private val BASE_URL = "https://us-central1-apis-4674e.cloudfunctions.net/"


    //    private val api = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//        .build()
//        .create(AnimalApi::class.java)
    @Inject
    lateinit var api: AnimalApi

    init {
        //Classe gerada automaticamente Dagger + nome da interface
        DaggerApiComponent.create().inject(this)
    }


    fun getApiKey(): Single<ApiKey> {
        return api.getApiKey()
    }

    fun getAnimals(key: String): Single<List<Animal>> {
        return api.getAnimals(key)
    }
}