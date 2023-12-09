package com.example.ekologapp

import android.R
import android.os.Bundle
import android.view.View
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.ekologapp.databinding.ActivityMainBinding
import com.example.ekologapp.fragment.CreateFragment
import com.example.ekologapp.fragment.HomeFragment
import com.example.ekologapp.fragment.LaporanFragment
import com.example.ekologapp.fragment.ProfilFragment
import com.google.android.material.bottomappbar.BottomAppBar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navbar: BottomAppBar = binding.navbar
        val viewPager: ViewPager = binding.viewPager
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        viewPagerAdapter.tambahFragment(HomeFragment())
        viewPagerAdapter.tambahFragment(CreateFragment())
        viewPagerAdapter.tambahFragment(LaporanFragment())
        viewPagerAdapter.tambahFragment(ProfilFragment())

        viewPager.adapter = viewPagerAdapter
    }

    internal class ViewPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(
        fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ){
        private val fragments: ArrayList<Fragment>
        private val juduls: ArrayList<String>

        init {
            fragments = ArrayList()
            juduls = ArrayList()
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        fun tambahFragment(fragment: Fragment) {
            fragments.add(fragment)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return juduls[position]
        }
    }
}
