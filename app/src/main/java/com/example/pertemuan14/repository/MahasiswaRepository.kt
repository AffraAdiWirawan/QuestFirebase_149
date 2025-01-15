package com.example.pertemuan14.repository

import com.example.pertemuan14.model.Mahasiswa
import kotlinx.coroutines.flow.Flow

interface MahasiswaRepository {
    fun getMahasiswa() : Flow<List<Mahasiswa>>
    suspend fun insertMahasiswa(mahasiswa: Mahasiswa)
    suspend fun updateMahasiswa(mahasiswa: Mahasiswa)
    suspend fun deleteMahasiswa(mahasiswa: Mahasiswa)
    fun getMahasiswaByNim(nim: String) : Flow<Mahasiswa>
}
