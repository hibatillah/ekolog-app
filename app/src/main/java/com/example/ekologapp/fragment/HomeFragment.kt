package com.example.ekologapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.ekologapp.Laporan
import com.example.ekologapp.R
import com.example.ekologapp.adapter.CardAdapter
import com.example.ekologapp.databinding.FragmentHomeBinding
import com.example.ekologapp.databinding.FragmentProfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var laporanAdmin: MutableList<Laporan>
    private lateinit var laporanUser: MutableList<Laporan>
    private lateinit var ref: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ref = FirebaseDatabase.getInstance().getReference("Laporan")
        laporanAdmin = mutableListOf()
        laporanUser= mutableListOf()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded) {
                    if (snapshot.exists()) {
                        laporanAdmin.clear()
                        laporanUser.clear()

                        for (a in snapshot.children) {
                            val laporan = a.getValue(Laporan::class.java)

                            laporan?.let {
                                if (laporan.userRole == "user") {
                                    laporanUser.add(it)
                                } else {
                                    laporanAdmin.add(it)
                                }
                            }
                        }

                        val adapterAdmin = CardAdapter(
                            requireActivity(),
                            R.layout.card_vertical,
                            laporanAdmin
                        )

                        val adapterUser = CardAdapter(
                            requireActivity(),
                            R.layout.card_horizontal,
                            laporanUser
                        )
                        binding.listLaporanHorizontal.adapter = adapterAdmin
                        binding.listLaporanVertical.adapter = adapterUser
                        println("Output: " + laporanAdmin)
                        println("Output: " + laporanUser)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })
    }
}
