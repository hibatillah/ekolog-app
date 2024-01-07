package com.example.ekologapp

data class Laporan(
    val id: String,
    val bencana: String,
    val tanggal: String,
    val lokasi: String,
    val kota: String,
    val provinsi: String,
    val meninggal: Int,
    val terluka: Int,
    val rumah: Int,
    val fasilitas: Int,
    val penyebab: String,
    val kerusakan: String?,
    val userId: String,
    val userName: String,
    val userRole: String,
) {
    constructor() : this("","","","","","",
        0,0,0,0, "","","","", "")
}
