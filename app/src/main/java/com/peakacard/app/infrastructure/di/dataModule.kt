package com.peakacard.app.infrastructure.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.dsl.module

val dataModule = module {
    single { Firebase.firestore }
}