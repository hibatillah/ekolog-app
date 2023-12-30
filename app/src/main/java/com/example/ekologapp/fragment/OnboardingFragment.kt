package com.example.ekologapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.ekologapp.R
import com.example.ekologapp.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {
    private lateinit var binding: FragmentOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingBinding.inflate(layoutInflater)
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()

        binding.btnMasuk.setOnClickListener {
            transaction.replace(R.id.container_onboard, LoginFragment())
            transaction.commit()
        }

        binding.btnDaftar.setOnClickListener {
            transaction.replace(R.id.container_onboard, SignupFragment())
            transaction.commit()
        }
        return binding.root
    }
}
