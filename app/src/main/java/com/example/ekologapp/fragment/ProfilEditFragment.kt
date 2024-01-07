package com.example.ekologapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.ekologapp.R
import com.example.ekologapp.databinding.FragmentProfilEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class ProfilEditFragment : Fragment() {
    private lateinit var binding: FragmentProfilEditBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    private var isFragmentAttached = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilEditBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        isFragmentAttached = true

        getCurrentUser()

        binding.btnBack.setOnClickListener{
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, ProfilFragment())
            transaction.commit()
        }
        return binding.root
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
                            val password = snapshot.child("password").getValue(String::class.java)

                            binding.inputUsername.setText(username)
                            binding.inputEmail.setText(email)
                            binding.inputPassword.setText(password)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
}
