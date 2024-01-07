package com.example.ekologapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.ekologapp.Laporan
import com.example.ekologapp.OnboardActivity
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
    private lateinit var firebaseAuth: FirebaseAuth
    private var isFragmentAttached = false

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

        binding.logout.setOnClickListener {
            firebaseAuth.signOut()
            val i = Intent(requireContext(), OnboardActivity::class.java)
            startActivity(i)
            requireActivity().finish()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFragmentAttached = true

        getCurrentUser()

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

    private fun getCurrentUser() {
        val user = firebaseAuth.currentUser
        val userId = user?.uid

        userId?.let {
            ref.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (isFragmentAttached) { // Pastikan fragment masih terpasang sebelum menggunakan konteks
                        if (snapshot.exists()) {
                            val email = snapshot.child("email").getValue(String::class.java)
                            val username = snapshot.child("username").getValue(String::class.java)

                            binding.userName.setText(username)
                            binding.userEmail.setText(email)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
}
