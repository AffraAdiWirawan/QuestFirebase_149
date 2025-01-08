package com.example.pertemuan14.di

import com.example.pertemuan14.repository.MahasiswaRepository
import com.example.pertemuan14.repository.NetworkMahasiswaRepository
import com.google.firebase.firestore.FirebaseFirestore

interface AppContainer {
    val mahasiswaRepository : MahasiswaRepository
}
class MahasiswaContainer : AppContainer {
   private val firebase: FirebaseFirestore = FirebaseFirestore.getInstance()

    override val mahasiswaRepository: MahasiswaRepository by lazy {
        NetworkMahasiswaRepository(firebase)
    }
}