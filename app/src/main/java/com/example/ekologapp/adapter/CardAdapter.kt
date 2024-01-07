package com.example.ekologapp.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.*
import com.example.ekologapp.R
import com.example.ekologapp.Laporan
import com.example.ekologapp.fragment.LaporanFragment

class CardAdapter (
    val laporanContext: Context,
    val layoutResId: Int,
    val laporanList: List<Laporan>
): ArrayAdapter<Laporan>(laporanContext, layoutResId, laporanList)  {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(laporanContext)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val parent = view.findViewById<CardView>(R.id.card_parent)
        val bencana = view.findViewById<TextView>(R.id.card_title)
        val penulis = view.findViewById<TextView>(R.id.card_author)
        val tanggal = view.findViewById<TextView>(R.id.card_date)
        val konten = view.findViewById<TextView>(R.id.card_content)

        val laporan = laporanList[position]

        bencana.text = laporan.bencana
        penulis.text = laporan.userName
        tanggal.text = laporan.tanggal
        konten.text = laporan.penyebab

        parent.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", laporan.id)

            val laporanFragment = LaporanFragment()
            laporanFragment.arguments = bundle

            val fragmentManager = (laporanContext as FragmentActivity).supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()

            transaction.replace(R.id.container, laporanFragment)
            transaction.addToBackStack(null)  // Untuk menambahkan ke tumpukan kembali
            transaction.commit()
        }

        return view
    }
}
