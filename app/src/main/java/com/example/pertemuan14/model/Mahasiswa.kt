package com.example.pertemuan14.model

data class Mahasiswa (
    val nim: String,
    val nama: String,
    val alamat: String,
    val jenis_kelamin: String,
    val kelas: String,
    val angkatan: String,
    val judulskripsi : String? = null,
    val dosensatu : String? = null,
    val dosendua : String? = null
)
{
    constructor(
    ):this("","","","","","","","","")
}
