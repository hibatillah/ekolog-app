package com.example.ekologapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.ekologapp.R
import com.example.ekologapp.Laporan
import com.google.firebase.database.FirebaseDatabase

class LaporanEditAdapter (
    val laporanContext: Context,
    val layoutResId: Int,
    val laporanList: List<Laporan>
): ArrayAdapter<Laporan>(laporanContext, layoutResId, laporanList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(laporanContext)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val judul = view.findViewById<TextView>(R.id.edit_bencana)
        val tanggal = view.findViewById<TextView>(R.id.edit_tanggal_bencana)
        val lokasi = view.findViewById<TextView>(R.id.edit_lokasi_bencana)
        val kota = view.findViewById<TextView>(R.id.edit_kota_bencana)
        val provinsi = view.findViewById<TextView>(R.id.edit_provinsi_bencana)
        val meninggal = view.findViewById<TextView>(R.id.edit_jumlah_meninggal)
        val terluka = view.findViewById<TextView>(R.id.edit_jumlah_terluka)
        val rumah = view.findViewById<TextView>(R.id.edit_jumlah_rumah_rusak)
        val fasilitas = view.findViewById<TextView>(R.id.edit_jumlah_fasum_rusak)
        val penyebab = view.findViewById<TextView>(R.id.edit_penyebab_bencana)
        val btnEdit = view.findViewById<Button>(R.id.btn_edit_laporan   )

        val laporan = laporanList[position]

        judul.text = laporan.bencana
        tanggal.text = laporan.tanggal
        lokasi.text = laporan.lokasi
        kota.text = laporan.kota
        provinsi.text = laporan.provinsi
        meninggal.text = laporan.meninggal.toString()
        terluka.text = laporan.terluka.toString()
        rumah.text = laporan.rumah.toString()
        fasilitas.text = laporan.fasilitas.toString()
        penyebab.text = laporan.penyebab

        btnEdit.setOnClickListener {
            UpdateData(laporan)
        }

        return view
    }

    private fun UpdateData(laporan: Laporan) {
        val inflater = LayoutInflater.from(laporanContext)
        val view = inflater.inflate(R.layout.fragment_laporan_edit, null)
        val LaporanDb = FirebaseDatabase.getInstance().getReference("Laporan")

        val judulInput = view.findViewById<TextView>(R.id.edit_bencana)
        val tanggalInput = view.findViewById<TextView>(R.id.edit_tanggal_bencana)
        val lokasiInput = view.findViewById<TextView>(R.id.edit_lokasi_bencana)
        val kotaInput = view.findViewById<TextView>(R.id.edit_kota_bencana)
        val provinsiInput = view.findViewById<TextView>(R.id.edit_provinsi_bencana)
        val meninggalInput = view.findViewById<TextView>(R.id.edit_jumlah_meninggal)
        val terlukaInput = view.findViewById<TextView>(R.id.edit_jumlah_terluka)
        val rumahInput = view.findViewById<TextView>(R.id.edit_jumlah_rumah_rusak)
        val fasilitasInput = view.findViewById<TextView>(R.id.edit_jumlah_fasum_rusak)
        val penyebabInput = view.findViewById<TextView>(R.id.edit_penyebab_bencana)

        val judul = judulInput.text.toString().trim()
        val tanggal = tanggalInput.text.toString().trim()
        val lokasi = lokasiInput.text.toString().trim()
        val kota = kotaInput.text.toString().trim()
        val provinsi = provinsiInput.text.toString().trim()
        val meninggalText = meninggalInput.text.toString().trim()
        val terlukaText = terlukaInput.text.toString().trim()
        val rumahText = rumahInput.text.toString().trim()
        val fasilitasText = fasilitasInput.text.toString().trim()
        val penyebab = penyebabInput.text.toString().trim()

        val meninggal = meninggalText.toIntOrNull()
        val terluka = terlukaText.toIntOrNull()
        val rumah = rumahText.toIntOrNull()
        val fasilitas = fasilitasText.toIntOrNull()

        if (judul.isEmpty() || lokasi.isEmpty() || tanggal.isEmpty() || kota.isEmpty() || provinsi.isEmpty() ||
            meninggal == null || terluka == null || rumah == null ||fasilitas == null || penyebab.isEmpty()) {
            Toast.makeText(laporanContext, "Isi data secara lengkap tidak boleh kosong", Toast.LENGTH_SHORT)
                .show()
        } else {
            // Proses update data
            val updatedLaporan = Laporan(laporan.id, judul, tanggal, lokasi, kota, provinsi, meninggal, terluka, rumah, fasilitas, penyebab, laporan.kerusakan, laporan.userId, laporan.userName, laporan.userRole)
            LaporanDb.child(laporan.id).setValue(updatedLaporan)
            Toast.makeText(laporanContext, "Laporan Berhasil diupdate", Toast.LENGTH_SHORT).show()
        }
    }
}
