package com.example.ekologapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginStart
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.ekologapp.R
import com.example.ekologapp.databinding.FragmentProfilBinding

class ProfilFragment: Fragment() {
    private lateinit var binding: FragmentProfilBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilBinding.inflate(layoutInflater)
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()

        transaction.replace(R.id.container_profil, ProfilLaporanFragment())
        transaction.commit()

        val tabLaporan = binding.tabLaporan
        val tabTersimpan = binding.tabTersimpan
        val indicator = binding.tabIndicator

        tabLaporan.setOnClickListener {
            indicator.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                marginStart = 15
            }
            transaction.replace(R.id.container_profil, ProfilLaporanFragment())
            transaction.commit()
        }

        tabTersimpan.setOnClickListener {
            indicator.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                marginStart = 200
            }
            transaction.replace(R.id.container_profil, ProfilSavedFragment())
            transaction.commit()
        }
        return binding.root
    }
}
