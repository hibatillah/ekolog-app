package com.example.ekologapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.ekologapp.Laporan
import com.example.ekologapp.R
import com.example.ekologapp.adapter.CardAdapter
import com.example.ekologapp.databinding.FragmentProfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfilFragment : Fragment() {
    private lateinit var binding: FragmentProfilBinding
    private lateinit var laporanList: MutableList<Laporan>
    private lateinit var ref: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilBinding.inflate(layoutInflater)
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()

        val editProfil = binding.editProfil

        editProfil.setOnClickListener {
            transaction.replace(R.id.container, ProfilEditFragment())
            transaction.commit()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()

        var userId = ""

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            userId = user.uid
        }

        ref = FirebaseDatabase.getInstance().getReference("Laporan")
        laporanList = mutableListOf()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded) {
                    if (snapshot.exists()) {
                        val list = binding.listLaporanProfil
                        laporanList.clear()

                        for (a in snapshot.children) {
                            val laporan = a.getValue(Laporan::class.java)
                            laporan?.let {
                                if (laporan.userId == userId) {
                                    laporanList.add(it)
                                }
                            }
                        }
                        val adapter = CardAdapter(
                            requireActivity(),
                            R.layout.card_horizontal,
                            laporanList
                        )
                        list.adapter = adapter
                        println("Output: " + laporanList)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })
    }
}
