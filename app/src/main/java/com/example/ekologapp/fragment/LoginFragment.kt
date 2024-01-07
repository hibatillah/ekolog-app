package com.example.ekologapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import com.example.ekologapp.MainActivity
import com.example.ekologapp.R
import com.example.ekologapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        // get form element
        val emailInput = view.findViewById<EditText>(R.id.input_email_login)
        val passwordInput = view.findViewById<EditText>(R.id.input_password_login)
        val btnLogin = view.findViewById<Button>(R.id.submit_login)
        val btnBack = view.findViewById<ConstraintLayout>(R.id.btn_back)

        btnLogin.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // validate input
            if (email.isEmpty()) {
                Toast.makeText(requireContext(), "Email harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(requireContext(), "Password harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // login process
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val userId = user?.uid

                        val database = FirebaseDatabase.getInstance()
                        val reference = database.getReference("User")

                        userId?.let {
                            reference.child(it).get().addOnSuccessListener { snapshot ->
                                val intent = Intent(requireContext(), MainActivity::class.java)
                                startActivity(intent)
                                requireActivity().finish() // close current activity
                            }.addOnFailureListener { e ->
                                // login failed
                                Toast.makeText(requireContext(),
                                    "Login gagal: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // login failed
                        val exception = task.exception
                        if (exception is FirebaseAuthInvalidUserException
                                || exception is FirebaseAuthInvalidCredentialsException) {
                            // wrong email or password
                            Toast.makeText(requireContext(),
                                "Email atau password salah", Toast.LENGTH_SHORT).show()
                        } else {
                            // other failure
                            Toast.makeText(requireContext(),
                                "Login gagal: ${exception?.message}", Toast.LENGTH_SHORT).show()
                        }
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
