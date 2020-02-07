package com.rmanley.coinflipper.extensions

import android.content.res.TypedArray

fun TypedArray.toResourceIdArray() = IntArray(length()) { i -> getResourceId(i, 0) }