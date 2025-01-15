package com.example.pertemuan14.di

import android.content.Context
import com.example.pertemuan14.repository.MahasiswaRepository
import com.example.pertemuan14.repository.NetworkRepositoryMhs
import com.google.firebase.firestore.FirebaseFirestore


interface AppContainer {
    val mahasiswaRepository: MahasiswaRepository
}

class MahasiswaContainer(private val context: Context) : InterfaceContainerApp {
    private val firestore : FirebaseFirestore = FirebaseFirestore.getInstance()
    override val mahasiswaRepository: MahasiswaRepository by lazy {
        NetworkRepositoryMhs(firestore)
    }
}