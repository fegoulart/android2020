package com.leapi.animals

import com.leapi.animals.di.ApiModule
import com.leapi.animals.model.AnimalApiService

class ApiModuleTest(val mockService: AnimalApiService) : ApiModule() {
    override fun provideAnimalApiService(): AnimalApiService {
        return mockService
    }
}