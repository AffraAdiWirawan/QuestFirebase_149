package com.example.pertemuan14.repository

import android.net.wifi.aware.AwareResources
import com.example.pertemuan14.model.Mahasiswa
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class NetworkMahasiswaRepository(
    private val firestore: FirebaseFirestore
): MahasiswaRepository {
    override suspend fun getMahasiswa(): Flow<List<Mahasiswa>>  = callbackFlow{
        val mhsCollection = firestore.collection("Mahasiswa")
            .orderBy("nim",Query.Direction.ASCENDING)
            .addSnapshotListener{
                value,error-> if (value != null){
                    val mhsList = value.documents.mapNotNull {
                        it.toObject(Mahasiswa::class.java)!!

                    }
                     trySend(mhsList)
                }

            }
        awaitClose {
            mhsCollection.remove()
        }

    }

    override suspend fun insertMahasiswa(mahasiswa: Mahasiswa) {

    }

    override suspend fun updateMahasiswa(nim: String, mahasiswa: Mahasiswa) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMahasiswa(nim: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getMahasiswaByNim(nim: String): Flow<Mahasiswa> {
        TODO("Not yet implemented")
    }

}