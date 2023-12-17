package com.example.ekologapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ekologapp.databinding.ActivityOnboardBinding
import com.example.ekologapp.fragment.LoginFragment
import com.example.ekologapp.fragment.OnboardingFragment
import com.example.ekologapp.fragment.SignupFragment

class OnboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.container_onboard, OnboardingFragment())
        fragmentTransaction.commit()
    }
}
