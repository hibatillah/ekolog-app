package com.example.ekologapp.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.android.volley.Request
import com.example.ekologapp.Laporan
import com.example.ekologapp.R
import com.example.ekologapp.databinding.FragmentLaporanCreateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.Charset
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.io.IOException
import java.net.ConnectException
import java.util.Calendar
import com.android.volley.toolbox.JsonObjectRequest as JsonObjectRequest

class LaporanCreateFragment: Fragment(), View.OnClickListener {
    lateinit var binding: FragmentLaporanCreateBinding
    private lateinit var ref: DatabaseReference
    private lateinit var selectedDate: Calendar
    private var isFragmentAttached = false
    private val url = "http://10.0.2.2:5000/predict"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLaporanCreateBinding.inflate(inflater, container, false)

        selectedDate = Calendar.getInstance()

        binding.tanggalBencana.setOnClickListener {
            showDatePickerDialog()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFragmentAttached = true
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
        var kerusakan = "belum"
        var userId = ""
        var userName = ""

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            userId = user.uid
            userName = user.displayName.toString()
        }

        userId?.let {
            ref.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (isFragmentAttached) { // Pastikan fragment masih terpasang sebelum menggunakan konteks
                        if (snapshot.exists()) {
                            val id = snapshot.child("id").getValue(String::class.java)
                            val username = snapshot.child("username").getValue(String::class.java)

                            if (id != null && username != null) {
                                userId = id
                                userName = username
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        //=== Send request to python backend for Data Science ===//
        /*
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val data = jsonObject.getInt("prediksi_list")
                    kerusakan = when (data) {
                        0 -> "Rendah"
                        1 -> "Tinggi"
                        else -> "Kosong"
                    }
                } catch (e: ConnectException) {
                    e.printStackTrace();
                    System.out.println("Gagal terhubung: " + e);
                } catch (e: IOException) {
                    e.printStackTrace();
                    System.out.println("Error IO: " + e);
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    requireContext(),
                    "Error: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["rumah"] = rumah.toString()
                params["fasum"] = fasilitas.toString()
                return params
            }
        }

        val queue = Volley.newRequestQueue(requireContext())
        queue.add(stringRequest)
         */

        if (bencana.isEmpty() || tanggal.isEmpty() || lokasi.isEmpty() ||
            kota.isEmpty() || provinsi.isEmpty() || penyebab.isEmpty() || kerusakan.isEmpty())
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
                binding.tanggalBencana.text = selectedDateString
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )

        // Tampilkan dialog
        datePickerDialog.show()
    }
}
