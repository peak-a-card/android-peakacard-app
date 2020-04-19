package com.peakacard.core.infrastructure

import android.app.Application
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseApp
import com.peakacard.core.infrastructure.di.coreModules
import com.peakacard.core.infrastructure.di.coreUiModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import timber.log.Timber

abstract class PeakACardApplication : Application() {

    abstract val modules: List<Module>

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        FirebaseApp.initializeApp(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
        }

        startKoin {
            androidLogger()
            androidContext(this@PeakACardApplication)
            modules(coreModules)
            modules(coreUiModules)
            modules(modules)
        }
    }
}
