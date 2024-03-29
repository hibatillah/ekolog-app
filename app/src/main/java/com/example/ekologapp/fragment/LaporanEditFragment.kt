package com.example.ekologapp.fragment

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.ekologapp.Laporan
import com.example.ekologapp.R
import com.example.ekologapp.databinding.FragmentLaporanEditBinding
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.Calendar

class LaporanEditFragment: Fragment() {
    lateinit var binding: FragmentLaporanEditBinding
    private lateinit var ref: DatabaseReference
    private lateinit var storageRef: StorageReference
    private lateinit var selectedDate: Calendar
    private var isFragmentAttached = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLaporanEditBinding.inflate(inflater, container, false)

        selectedDate = Calendar.getInstance()

        binding.editTanggalBencana.setOnClickListener {
            showDatePickerDialog()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ref = FirebaseDatabase.getInstance().getReference("Laporan")
        storageRef = FirebaseStorage.getInstance().reference
        val laporanId = getArguments()?.getString("id");
        isFragmentAttached = true

        if (laporanId != null) {
            showLaporanDetail(laporanId)

            binding.btnEditLaporan.setOnClickListener {
                updateData(laporanId)
            }
        }

        binding.backBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", laporanId)

            val laporanFragment = LaporanFragment()
            laporanFragment.arguments = bundle

            val fragmentManager = (requireContext() as FragmentActivity).supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()

            transaction.replace(R.id.container, laporanFragment)
            transaction.addToBackStack(null)  // Untuk menambahkan ke tumpukan kembali
            transaction.commit()
        }
    }

    private fun showLaporanDetail(laporan: String) {
        laporan?.let {
            ref.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (isFragmentAttached) { // Pastikan fragment masih terpasang sebelum menggunakan konteks
                        if (snapshot.exists()) {
                            val judul = snapshot.child("bencana").getValue(String::class.java)
                            val tanggal = snapshot.child("tanggal").getValue(String::class.java)
                            val lokasi = snapshot.child("lokasi").getValue(String::class.java)
                            val kota = snapshot.child("kota").getValue(String::class.java)
                            val provinsi = snapshot.child("provinsi").getValue(String::class.java)
                            val meninggal = snapshot.child("meninggal").getValue(Long::class.java)
                            val terluka = snapshot.child("terluka").getValue(Long::class.java)
                            val rumah = snapshot.child("rumah").getValue(Long::class.java)
                            val fasilitas = snapshot.child("fasilitas").getValue(Long::class.java)
                            val penyebab = snapshot.child("penyebab").getValue(String::class.java)

                            binding.editBencana.setText(judul)
                            binding.editTanggalBencana.setText(tanggal)
                            binding.editLokasiBencana.setText(lokasi)
                            binding.editKotaBencana.setText(kota)
                            binding.editProvinsiBencana.setText(provinsi)
                            binding.editJumlahMeninggal.setText(meninggal.toString())
                            binding.editJumlahTerluka.setText(terluka.toString())
                            binding.editJumlahRumahRusak.setText(rumah.toString())
                            binding.editJumlahFasumRusak.setText(fasilitas.toString())
                            binding.editPenyebabBencana.setText(penyebab)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    private fun updateData(laporanId: String) {
        val LaporanDb = FirebaseDatabase.getInstance().getReference("Laporan")

        val bencana = binding.editBencana.text.toString().trim()
        val tanggal = binding.editTanggalBencana.text.toString().trim()
        val lokasi = binding.editLokasiBencana.text.toString().trim()
        val kota = binding.editKotaBencana.text.toString().trim()
        val provinsi = binding.editProvinsiBencana.text.toString().trim()
        val meninggalText = binding.editJumlahMeninggal.text.toString().trim()
        val terlukaText = binding.editJumlahTerluka.text.toString().trim()
        val rumahText = binding.editJumlahRumahRusak.text.toString().trim()
        val fasilitasText = binding.editJumlahFasumRusak.text.toString().trim()
        val penyebab = binding.editPenyebabBencana.text.toString().trim()

        val meninggal = meninggalText.toIntOrNull()
        val terluka = terlukaText.toIntOrNull()
        val rumah = rumahText.toIntOrNull()
        val fasilitas = fasilitasText.toIntOrNull()

        if (bencana.isEmpty() || tanggal.isEmpty() || lokasi.isEmpty() ||
            kota.isEmpty() || provinsi.isEmpty() || meninggalText.isEmpty() || terlukaText.isEmpty() ||
            rumahText.isEmpty() || fasilitasText.isEmpty() || penyebab.isEmpty())
        {
            Toast.makeText(
                requireContext(),
                "Isi seluruh kolom data!",
                Toast.LENGTH_SHORT
            ).show()
            return
        } else if (meninggal == null || terluka == null || rumah == null || fasilitas == null) {
            Toast.makeText(
                requireContext(),
                "Isi seluruh kolom data!",
                Toast.LENGTH_SHORT
            ).show()
            return
        } else {
            laporanId?.let {
                ref.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (isFragmentAttached) { // Pastikan fragment masih terpasang sebelum menggunakan konteks
                            if (snapshot.exists()) {
                                val id = snapshot.child("id").getValue(String::class.java)
                                val kerusakan = snapshot.child("kerusakan").getValue(String::class.java)
                                val userId = snapshot.child("userId").getValue(String::class.java)
                                val userName = snapshot.child("userName").getValue(String::class.java)
                                val userRole = snapshot.child("userRole").getValue(String::class.java)

                                if (id != null && userName != null && userId != null && userRole != null) {
                                    val updatedLaporan = Laporan(id, bencana, tanggal, lokasi, kota, provinsi, meninggal, terluka, rumah, fasilitas, penyebab, kerusakan, userId, userName, userRole)

                                    LaporanDb.child(id).setValue(updatedLaporan)
                                }
                                Toast.makeText(requireContext(), "Laporan Berhasil diupdate", Toast.LENGTH_SHORT).show()

                                val bundle = Bundle()
                                bundle.putString("id", laporanId)

                                val laporanFragment = LaporanFragment()
                                laporanFragment.arguments = bundle

                                val fragmentManager = (requireContext() as FragmentActivity).supportFragmentManager
                                val transaction: FragmentTransaction = fragmentManager.beginTransaction()

                                transaction.replace(R.id.container, laporanFragment)
                                transaction.addToBackStack(null)  // Untuk menambahkan ke tumpukan kembali
                                transaction.commit()
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                // Update tanggal yang dipilih
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // Tampilkan tanggal yang dipilih
                val selectedDateString = "$dayOfMonth/${month + 1}/$year"
                // Konversi String menjadi Editable
                val editableDateString = Editable.Factory.getInstance().newEditable(selectedDateString)

                // Set teks Editable ke EditText
                binding.editTanggalBencana.text = editableDateString
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )

        // Tampilkan dialog
        datePickerDialog.show()
    }
}
