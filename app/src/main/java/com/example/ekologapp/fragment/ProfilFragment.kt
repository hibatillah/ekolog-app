package com.example.ekologapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.ekologapp.R
import com.example.ekologapp.databinding.FragmentProfilBinding
import com.google.android.material.tabs.TabLayout

class ProfilFragment: Fragment(R.layout.fragment_profil) {
//    private lateinit var binding: FragmentProfilBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profil, container, false)
    }
}
