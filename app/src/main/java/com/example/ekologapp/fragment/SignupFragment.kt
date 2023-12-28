package com.example.ekologapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.ekologapp.R
import com.example.ekologapp.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignupBinding.inflate(layoutInflater)
        auth = Firebase.auth

        // get form element
        val usernameInput = binding.inputUsername
        val emailInput = binding.inputEmail
        val passwordInput = binding.inputPassword

        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()

        binding.submitSignup.setOnClickListener {
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

                        val userData = HashMap<String, Any>()
                        userData["username"] = username
                        userData["email"] = email
                        userData["password"] = password

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

        binding.btnBack.setOnClickListener{
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container_onboard, OnboardingFragment())
            transaction.commit()
        }
    }
}
