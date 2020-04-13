package com.peakacard.app

import android.app.Application
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import com.google.firebase.FirebaseApp
import com.peakacard.app.cards.infrastructure.di.cardsModule
import com.peakacard.app.infrastructure.di.dataModule
import com.peakacard.app.session.infrastructure.di.sessionModule
import com.peakacard.app.participant.infrastructure.di.participantModule
import com.peakacard.app.voting.infrastructure.di.votingModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class PeakACardApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)
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
            modules(dataModule,
                cardsModule,
                sessionModule, votingModule, participantModule)
        }
    }
}
