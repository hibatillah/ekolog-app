package com.example.ekologapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.ekologapp.R
import com.example.ekologapp.Laporan

class LaporanDetailAdapter (
    val laporanContext: Context,
    val layoutResId: Int,
    val laporanList: List<Laporan>
): ArrayAdapter<Laporan>(laporanContext, layoutResId, laporanList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(laporanContext)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val judul = view.findViewById<TextView>(R.id.laporan_judul)
        val tanggal = view.findViewById<TextView>(R.id.laporan_tanggal)
        val penulis = view.findViewById<TextView>(R.id.laporan_penulis)
        val lokasi = view.findViewById<TextView>(R.id.laporan_lokasi)
        val kota = view.findViewById<TextView>(R.id.laporan_kota)
        val provinsi = view.findViewById<TextView>(R.id.laporan_provinsi)
        val meninggal = view.findViewById<TextView>(R.id.laporan_meninggal)
        val terluka = view.findViewById<TextView>(R.id.laporan_terluka)
        val rumah = view.findViewById<TextView>(R.id.laporan_rumah)
        val fasilitas = view.findViewById<TextView>(R.id.laporan_fasilitas)
        val kerusakan= view.findViewById<TextView>(R.id.laporan_kerusakan)
        val penyebab = view.findViewById<TextView>(R.id.laporan_penyebab)

        val laporan = laporanList[position]

        judul.text = laporan.bencana
        tanggal.text = laporan.tanggal
        penulis.text = laporan.userName
        lokasi.text = laporan.lokasi
        kota.text = laporan.kota
        provinsi.text = laporan.provinsi
        meninggal.text = laporan.meninggal.toString()
        terluka.text = laporan.terluka.toString()
        rumah.text = laporan.rumah.toString()
        fasilitas.text = laporan.fasilitas.toString()
        kerusakan.text = laporan.kerusakan
        penyebab.text = laporan.penyebab

        return view
    }
}
