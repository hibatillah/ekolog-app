package com.example.ekologapp.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.ekologapp.R
import com.example.ekologapp.databinding.FragmentProfilEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProfilEditFragment : Fragment() {
    private lateinit var binding: FragmentProfilEditBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    private lateinit var storageRef: StorageReference
    private var isFragmentAttached = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilEditBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnBack.setOnClickListener{
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, ProfilFragment())
            transaction.commit()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storageRef = FirebaseStorage.getInstance().reference
        isFragmentAttached = true

        getCurrentUser()

        binding.editUserImg.setOnClickListener {
            changeProfileImage()
        }
    }

    private fun getCurrentUser() {
        val user = firebaseAuth.currentUser
        val profileImageUri = user?.photoUrl
        val userId = user?.uid

        ref = FirebaseDatabase.getInstance().getReference("User")

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

                            profileImageUri?.let {
                                Glide.with(requireContext())
                                    .load(profileImageUri)
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .error(R.drawable.ic_launcher_background)
                                    .into(binding.userImg)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    private fun changeProfileImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri: Uri = data.data!!
            uploadImageToFirebaseStorage(imageUri)
        }
    }

    private fun uploadImageToFirebaseStorage(imageUri: Uri) {
        val user = firebaseAuth.currentUser
        val userId = user?.uid

        userId?.let {
            val profileImageRef = storageRef.child("profile_images/$it.jpg")

            profileImageRef.putFile(imageUri)
                .addOnSuccessListener { _ ->
                    profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setPhotoUri(uri)
                            .build()

                        user.updateProfile(profileUpdates)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful && isFragmentAttached) {
                                    getCurrentUser()
                                }
                            }
                    }
                }
                .addOnFailureListener { e ->
                    // Handle failure
                }
        }
    }
}
