package com.example.coinflipper

import android.content.res.TypedArray

fun TypedArray.toResourceIdArray() = IntArray(length()) { i -> getResourceId(i, 0) }