package com.leapi.animals.di

import android.app.Application
import com.leapi.animals.util.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PrefsModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application):SharedPreferencesHelper {
        return SharedPreferencesHelper(app)
    }

}