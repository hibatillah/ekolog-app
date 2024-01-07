package com.example.ekologapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import com.example.ekologapp.R
import com.example.ekologapp.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignupFragment : Fragment(R.layout.fragment_signup) {
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        // get form element
        val usernameInput: EditText = view.findViewById(R.id.input_username_signup)
        val emailInput: EditText = view.findViewById(R.id.input_email_signup)
        val passwordInput: EditText = view.findViewById(R.id.input_password_signup)
        val btnDaftar: Button = view.findViewById(R.id.submit_signup)
        val btnBack: ConstraintLayout = view.findViewById(R.id.btn_back)

        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()

        btnDaftar.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // validate input
            if (username.isEmpty()) {
                Toast.makeText(requireContext(), "Username harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                Toast.makeText(requireContext(), "Email harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(requireContext(), "Password harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // signup process
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // signup success
                        val user = auth.currentUser
                        val userId = user?.uid

                        // save to Realtime Database
                        val database = FirebaseDatabase.getInstance()
                        val reference = database.getReference("User") // location to save data

                        val userData = User(userId!!, username, email, password)

                        userId?.let {
                            reference.child(it).setValue(userData)
                                .addOnSuccessListener {
                                    // data saved to database
                                    Toast.makeText(requireContext(),
                                        "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show()

                                    // redirect to login page
                                    if (!requireActivity().isFinishing && !requireActivity().isDestroyed) {
                                        transaction.replace(R.id.container_onboard, LoginFragment())
                                        transaction.commit()
                                    }
                                }
                                .addOnFailureListener { e ->
                                    // data failed to save
                                    Toast.makeText(requireContext(),
                                        "Pendaftaran gagal dilakukan: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        // signup failed
                        val exception = task.exception
                        Toast.makeText(requireContext(),
                            "Pendaftaran gagal: ${exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        btnBack.setOnClickListener{
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container_onboard, OnboardingFragment())
            transaction.commit()
        }
    }
}
