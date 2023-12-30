package com.example.ekologapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.ekologapp.R
import com.example.ekologapp.databinding.FragmentProfilEditBinding

class ProfilEditFragment : Fragment() {
    private lateinit var binding: FragmentProfilEditBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilEditBinding.inflate(layoutInflater)

        binding.btnBack.setOnClickListener{
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, ProfilLaporanFragment())
            transaction.commit()
        }
        return binding.root
    }
}
