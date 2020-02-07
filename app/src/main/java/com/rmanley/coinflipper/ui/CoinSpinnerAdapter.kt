package com.rmanley.coinflipper.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.rmanley.coinflipper.R
import com.rmanley.coinflipper.model.Coin

class CoinSpinnerAdapter(context: Context, private val coins: Array<Coin>)
    : ArrayAdapter<Coin>(context,
    R.layout.image_spinner_item, coins) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) = getBoundView(position, convertView, parent)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup) = getBoundView(position, convertView, parent)

    private fun getBoundView(position: Int, convertView: View?, parent: ViewGroup): View {
        val coin = coins[position]
        val view = convertView
            ?: LayoutInflater.from(context).inflate(R.layout.image_spinner_item, parent, false)
        return view.apply {
            val coinImage = findViewById<ImageView>(R.id.spinner_item_image)
            val coinText = findViewById<TextView>(R.id.spinner_item_text)
            coinImage.setImageResource(coin.spriteId)
            coinText.text = coin.coinColor.name
        }
    }
}