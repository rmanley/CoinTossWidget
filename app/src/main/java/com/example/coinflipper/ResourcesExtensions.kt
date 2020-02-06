package com.example.coinflipper

import android.content.res.Resources

fun Resources.getResourceIdsArray(id: Int): IntArray {
    val typedArray = obtainTypedArray(id)
    val resourceIds = typedArray.toResourceIdArray()
    typedArray.recycle()
    return resourceIds
}