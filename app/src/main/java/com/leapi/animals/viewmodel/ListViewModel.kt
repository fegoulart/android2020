package com.leapi.animals.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.leapi.animals.di.AppModule
import com.leapi.animals.di.CONTEXT_APP
import com.leapi.animals.di.DaggerViewModelComponent
import com.leapi.animals.di.TypeOfContext
import com.leapi.animals.model.Animal
import com.leapi.animals.model.AnimalApiService
import com.leapi.animals.model.ApiKey
import com.leapi.animals.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListViewModel(application: Application) : AndroidViewModel(application) {

    constructor(application: Application, test: Boolean = true) : this(application) {
        injected = true
    }

    val animals by lazy { MutableLiveData<List<Animal>>() }
    val loadError by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    private val disposable = CompositeDisposable()

    //private val apiService = AnimalApiService()
    @Inject
    lateinit var apiService: AnimalApiService


    //private val prefs = SharedPreferencesHelper(getApplication())
    @Inject
    @field: TypeOfContext(CONTEXT_APP)
    lateinit var prefs: SharedPreferencesHelper

    //    init {
//        //DaggerViewModelComponent.create().inject(this)
    fun inject_abacaxi() {
        if (!injected) {
            DaggerViewModelComponent.builder()
                .appModule(AppModule(getApplication()))
                .build()
                .inject(this)
        }
    }

    private var invalidApiKey = false
    private var injected = false

    fun refresh() {
        inject_abacaxi()
        loading.value = true
        invalidApiKey = false
        val key: String? = prefs.getApiKey()
        if (key.isNullOrEmpty()) {
            getKey()
        } else {
            getAnimals(key)
        }
    }

    fun hardRefresh() {
        inject_abacaxi()
        loading.value = true
        getKey()
    }

    private fun getKey() {
        //Adds a disposable to this container or disposes it if the container has been disposed.
        disposable.add(
            //Adiciona um disposableSingleObserver que retorna do Single.subscribeWith().
            apiService.getApiKey()
                //Utiliza um Scheduler do tipo newThread para o upstream
                .subscribeOn(Schedulers.newThread())
                //Utiliza o scheduler da mainthread para o downstream
                .observeOn(AndroidSchedulers.mainThread())
                //retorna o disposableSingleObserver
                .subscribeWith(object : DisposableSingleObserver<ApiKey>() {
                    override fun onSuccess(key: ApiKey) {
                        if (key.key.isNullOrEmpty()) {
                            loadError.value = true
                            loading.value = false
                        } else {
                            prefs.saveApiKey(key.key)
                            getAnimals(key.key)
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (!invalidApiKey) {
                            invalidApiKey = true
                            getKey()
                        } else {
                            e.printStackTrace()
                            loading.value = false
                            loadError.value = true
                        }
                    }
                })
        )
    }

    private fun getAnimals(key: String) {
        disposable.add(
            apiService.getAnimals(key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Animal>>() {
                    override fun onSuccess(list: List<Animal>) {
                        loadError.value = false
                        animals.value = list
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        loading.value = false
                        animals.value = null
                        loadError.value = true
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}