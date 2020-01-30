package com.example.coinflipper

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.core.view.setPadding

class ImageSpinnerAdapter(context: Context, private val resourceIds: Array<Int>)
    : ArrayAdapter<Int>(context, R.layout.support_simple_spinner_dropdown_item, resourceIds) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) = getImageForPosition(position)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup) = getImageForPosition(position)

    private fun getImageForPosition(position: Int) = ImageView(context).apply {
        setBackgroundResource(resourceIds[position])
        val itemWidth = resources.getDimensionPixelSize(R.dimen.image_spinner_item_width)
        val itemHeight = resources.getDimensionPixelSize(R.dimen.image_spinner_item_height)
        val itemPadding = resources.getDimensionPixelSize(R.dimen.image_spinner_item_padding)
        layoutParams = AbsListView.LayoutParams(itemWidth, itemHeight)
        setPadding(itemPadding)
    }
}