package com.leapi.animals

import android.app.Application
import com.leapi.animals.di.PrefsModule
import com.leapi.animals.util.SharedPreferencesHelper

class PrefsModuleTest(val mockPrefs: SharedPreferencesHelper) : PrefsModule() {
    override fun provideSharedPreferences(app: Application): SharedPreferencesHelper {
        return mockPrefs
    }

}