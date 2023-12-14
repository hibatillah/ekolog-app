package com.example.ekologapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ekologapp.databinding.ActivityOnboardBinding
import com.example.ekologapp.fragment.LoginFragment
import com.example.ekologapp.fragment.SignupFragment

class OnboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        binding.btnDaftar.setOnClickListener {
            fragmentTransaction.replace(R.id.container_onboard, SignupFragment())
            fragmentTransaction.commit()
        }

        binding.btnMasuk.setOnClickListener {
            fragmentTransaction.replace(R.id.container_onboard, LoginFragment())
            fragmentTransaction.commit()
        }
    }
}
