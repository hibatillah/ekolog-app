package com.example.ekologapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.ekologapp.R
import com.example.ekologapp.databinding.FragmentProfilLaporanBinding

class ProfilLaporanFragment : Fragment() {
    private lateinit var binding: FragmentProfilLaporanBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilLaporanBinding.inflate(layoutInflater)
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()

        transaction.replace(R.id.container, ProfilLaporanFragment())
        transaction.commit()

        val tabSaved = binding.tabTersimpan
        val editProfil = binding.editProfil

        tabSaved.setOnClickListener {
            transaction.replace(R.id.container, ProfilSavedFragment())
            transaction.commit()
        }

        editProfil.setOnClickListener {
            transaction.replace(R.id.container, ProfilEditFragment())
            transaction.commit()
        }

        return binding.root
    }
}
