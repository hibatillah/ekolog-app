package com.example.ekologapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.ekologapp.R
import com.example.ekologapp.databinding.FragmentProfilLaporanBinding
import com.example.ekologapp.databinding.FragmentProfilSavedBinding

class ProfilSavedFragment : Fragment() {
    private lateinit var binding: FragmentProfilSavedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilSavedBinding.inflate(layoutInflater)
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()

        transaction.replace(R.id.container, ProfilSavedFragment())
        transaction.commit()

        val tabLaporan = binding.tabLaporan
        val editProfil = binding.editProfil

        tabLaporan.setOnClickListener {
            transaction.replace(R.id.container, ProfilLaporanFragment())
            transaction.commit()
        }

        editProfil.setOnClickListener {
            transaction.replace(R.id.container, ProfilEditFragment())
            transaction.commit()
        }

        return binding.root
    }
}
