package com.rmanley.coinflipper.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CoinFlipResult(val isHeads: Boolean, val timesFlipped: Int) : Parcelable