package com.cudo.bluetoothscannearby.utils

object GlobalFun {

    val MEASURE_POWER = -69

    fun calculateAccuracy(rssi: Double): Double {
        if (rssi == 0.0) {
            return -1.0 // if we cannot determine accuracy, return -1.
        }
        val ratio = rssi * 1.0 / MEASURE_POWER
        return if (ratio < 1.0) {
            Math.pow(ratio, 10.0)
        } else {
            0.89976 * Math.pow(ratio, 7.7095) + 0.111
        }
    }
}