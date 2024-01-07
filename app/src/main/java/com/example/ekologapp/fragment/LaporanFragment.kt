package com.example.ekologapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.ekologapp.R
import com.example.ekologapp.databinding.FragmentLaporanBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LaporanFragment : Fragment() {
    private lateinit var binding: FragmentLaporanBinding
    private lateinit var ref: DatabaseReference
    private var isFragmentAttached = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLaporanBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ref = FirebaseDatabase.getInstance().getReference("Laporan")
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        isFragmentAttached = true

        displayUserInfo()

        binding.backBtn.setOnClickListener {
            transaction.replace(R.id.container, HomeFragment())
            transaction.commit()
        }
    }

    private fun displayUserInfo() {
        val laporanId = getArguments()?.getString("id");

        laporanId?.let {
            ref.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (isFragmentAttached) { // Pastikan fragment masih terpasang sebelum menggunakan konteks
                        if (snapshot.exists()) {
                            val email = snapshot.child("email").getValue(String::class.java)
                            val username = snapshot.child("nama").getValue(String::class.java)

                            val judul = snapshot.child("bencana").getValue(String::class.java)
                            val tanggal = snapshot.child("tanggal").getValue(String::class.java)
                            val penulis = snapshot.child("userName").getValue(String::class.java)
                            val lokasi = snapshot.child("lokasi").getValue(String::class.java)
                            val kota = snapshot.child("kota").getValue(String::class.java)
                            val provinsi = snapshot.child("provinsi").getValue(String::class.java)
                            val meninggal = snapshot.child("meninggal").getValue(Long::class.java)
                            val terluka = snapshot.child("terluka").getValue(Long::class.java)
                            val rumah = snapshot.child("rumah").getValue(Long::class.java)
                            val fasilitas = snapshot.child("fasilitas").getValue(Long::class.java)
                            val kerusakan = snapshot.child("kerusakan").getValue(String::class.java)
                            val penyebab = snapshot.child("penyebab").getValue(String::class.java)

                            binding.laporanJudul.setText(judul)
                            binding.laporanTanggal.setText(tanggal)
                            binding.laporanPenulis.setText(penulis)
                            binding.laporanLokasi.setText(lokasi)
                            binding.laporanKota.setText(kota)
                            binding.laporanProvinsi.setText(provinsi)
                            binding.laporanMeninggal.setText(meninggal.toString())
                            binding.laporanTerluka.setText(terluka.toString())
                            binding.laporanRumah.setText(rumah.toString())
                            binding.laporanFasilitas.setText(fasilitas.toString())
                            binding.laporanKerusakan.setText(kerusakan)
                            binding.laporanPenyebab.setText(penyebab)

//                            profileImageUri?.let {
//                                Glide.with(requireContext())
//                                    .load(profileImageUri)
//                                    .placeholder(R.drawable.fotoprofil)
//                                    .error(R.drawable.fotoprofil)
//                                    .into(binding.imageViewProfile)
//                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
}
