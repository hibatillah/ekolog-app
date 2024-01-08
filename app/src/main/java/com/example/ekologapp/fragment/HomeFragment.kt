package com.example.ekologapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.example.ekologapp.Laporan
import com.example.ekologapp.R
import com.example.ekologapp.adapter.CardAdapter
import com.example.ekologapp.databinding.FragmentHomeBinding
import com.example.ekologapp.databinding.FragmentProfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var laporanAdmin: MutableList<Laporan>
    private lateinit var laporanUser: MutableList<Laporan>
    private lateinit var ref: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayout: LinearLayout = binding.linearHorizontal
        val layoutInflater = LayoutInflater.from(requireContext())

        ref = FirebaseDatabase.getInstance().getReference("Laporan")
        laporanAdmin = mutableListOf()
        laporanUser= mutableListOf()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded) {
                    if (snapshot.exists()) {
                        laporanAdmin.clear()
                        laporanUser.clear()

                        for (a in snapshot.children) {
                            val laporan = a.getValue(Laporan::class.java)

                            laporan?.let {
                                if (laporan.userRole == "user") {
                                    laporanUser.add(it)
                                } else {
                                    val view: View = layoutInflater.inflate(R.layout.card_vertical, linearLayout, false)
                                    val title = view.findViewById<TextView>(R.id.card_title)
                                    val author = view.findViewById<TextView>(R.id.card_author)
                                    val date = view.findViewById<TextView>(R.id.card_date)
                                    val content = view.findViewById<TextView>(R.id.card_content)
                                    val parent = view.findViewById<CardView>(R.id.card_parent)

                                    title.setText(laporan.bencana)
                                    author.setText(laporan.userName)
                                    date.setText(laporan.tanggal)
                                    content.setText(laporan.penyebab)

                                    linearLayout?.addView(view)

                                    parent.setOnClickListener {
                                        val bundle = Bundle()
                                        bundle.putString("id", laporan.id)

                                        val laporanFragment = LaporanFragment()
                                        laporanFragment.arguments = bundle

                                        val fragmentManager = (requireContext() as FragmentActivity).supportFragmentManager
                                        val transaction: FragmentTransaction = fragmentManager.beginTransaction()

                                        transaction.replace(R.id.container, laporanFragment)
                                        transaction.addToBackStack(null)  // Untuk menambahkan ke tumpukan kembali
                                        transaction.commit()
                                    }
                                }
                            }
                        }

                        val adapterUser = CardAdapter(
                            requireActivity(),
                            R.layout.card_horizontal,
                            laporanUser
                        )
                        binding.listLaporanVertical.adapter = adapterUser
                        println("Output: " + laporanUser)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })
    }
}
