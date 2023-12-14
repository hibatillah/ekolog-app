package com.example.ekologapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ekologapp.databinding.ActivityMainBinding
import com.example.ekologapp.fragment.HomeFragment
import com.example.ekologapp.fragment.LaporanCreateFragment
import com.example.ekologapp.fragment.ProfilFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeFragment(HomeFragment())

        binding.navbar.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.tab_home -> changeFragment(HomeFragment())
                R.id.tab_laporan -> changeFragment(LaporanCreateFragment())
                R.id.tab_profil -> changeFragment(ProfilFragment())
                else -> {}
            }
            true
        }
    }

    private fun changeFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }
}
