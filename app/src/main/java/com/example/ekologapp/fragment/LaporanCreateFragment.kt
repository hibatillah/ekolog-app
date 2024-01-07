package com.example.ekologapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.ekologapp.Laporan
import com.example.ekologapp.R
import com.example.ekologapp.databinding.FragmentLaporanCreateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LaporanCreateFragment: Fragment(), View.OnClickListener {
    lateinit var binding: FragmentLaporanCreateBinding
    private lateinit var ref: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLaporanCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ref = FirebaseDatabase.getInstance().getReference("Laporan")
        binding.btnCreateLaporan.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        simpanData()
    }

    private fun simpanData() {
        val bencana = binding.bencana.text.toString().trim()
        val tanggal = binding.tanggalBencana.text.toString().trim()
        val lokasi = binding.lokasiBencana.text.toString().trim()
        val kota = binding.kotaBencana.text.toString().trim()
        val provinsi = binding.provinsiBencana.text.toString().trim()
        val meninggalText = binding.jumlahMeninggal.text.toString().trim()
        val terlukaText = binding.jumlahTerluka.text.toString().trim()
        val rumahText = binding.jumlahRumahRusak.text.toString().trim()
        val fasilitasText = binding.jumlahFasumRusak.text.toString().trim()
        val penyebab = binding.penyebabBencana.text.toString().trim()
        val userRole = "user"

        val meninggal = meninggalText.toIntOrNull()
        val terluka = terlukaText.toIntOrNull()
        val rumah = rumahText.toIntOrNull()
        val fasilitas = fasilitasText.toIntOrNull()
        var kerusakan = "Belum"
        var userId = ""
        var userName = ""

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            userId = user.uid
            userName = user.displayName.toString()
        }

        if (bencana.isEmpty() || tanggal.isEmpty() || lokasi.isEmpty() ||
            kota.isEmpty() || provinsi.isEmpty() || meninggalText.isEmpty() || terlukaText.isEmpty() ||
            rumahText.isEmpty() || fasilitasText.isEmpty() || penyebab.isEmpty() ||kerusakan.isEmpty())
        {
            Toast.makeText(
                requireContext(),
                "Isi seluruh kolom data!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (meninggal == null || terluka == null || rumah == null || fasilitas == null) {
            Toast.makeText(
                requireContext(),
                "",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val laporanId = ref.push().key
        val laporan = Laporan(laporanId!!, bencana, tanggal, lokasi, kota, provinsi, meninggal, terluka, rumah, fasilitas, penyebab, kerusakan, userId, userName, userRole)

        laporanId?.let {
            ref.child(it).setValue(laporan).addOnCompleteListener { task ->
                if(isAdded) {
                    if (task.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Laporan berhasil dibuat",
                            Toast.LENGTH_SHORT
                        ).show()
                        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                        transaction.replace(R.id.container, ProfilFragment())
                        transaction.commit()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Data gagal ditambahkan!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
