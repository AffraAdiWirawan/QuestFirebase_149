package com.example.pertemuan14

import android.app.Application
import com.example.pertemuan14.di.MahasiswaContainer
import com.google.firebase.FirebaseApp

class MahasiswaApplications : Application() {
    lateinit var container: MahasiswaContainer

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        container = MahasiswaContainer(this)
    }
}