package com.example.ekologapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.ekologapp.R
import com.example.ekologapp.Laporan
import com.google.firebase.database.FirebaseDatabase

class LaporanHomeAdapter (
    val laporanContext: Context,
    val layoutResId: Int,
    val laporanList: List<Laporan>
): ArrayAdapter<Laporan>(laporanContext, layoutResId, laporanList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(laporanContext)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val bencana: TextView = view.findViewById(R.id.bencana)
        val tanggal: TextView = view.findViewById(R.id.tanggal_bencana)
        val lokasi: TextView = view.findViewById(R.id.lokasi_bencana)
        val kota: TextView = view.findViewById(R.id.kota_bencana)
        val provinsi: TextView = view.findViewById(R.id.provinsi_bencana)
        val meninggal: TextView = view.findViewById(R.id.jumlah_meninggal)
        val terluka: TextView = view.findViewById(R.id.jumlah_terluka)
        val rumah: TextView = view.findViewById(R.id.jumlah_rumah_rusak)
        val fasilitas: TextView = view.findViewById(R.id.jumlah_fasum_rusak)
        val penyebab: TextView = view.findViewById(R.id.penyebab_bencana)

        val laporan = laporanList[position]

        bencana.text = "Nama : " + laporan
        tanggal.text = "Tanggal Bencana : " + laporan
        lokasi.text = "Lokasi Bencana : " + laporan
        kota.text = "Kota/Kabupaten : " + laporan
        provinsi.text = "Provinsi : " + laporan
        meninggal.text = "Jumlah yang Meninggal : " + laporan+ " orang"
        terluka.text = "Jumlah yang Terluka : " + laporan + " orang"
        rumah.text = "Jumlah Rumah Rusak : " + laporan+ " unit"
        fasilitas.text = "Jumlah Fasilitas Rusak : " + laporan+ " unit"
        penyebab.text = "Penyebab : " + laporan

        return view
    }
}
