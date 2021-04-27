package com.cudo.bluetoothscannearby.utils

import org.junit.Assert.*
import org.junit.Test

class GlobalFunTest{

    @Test
    fun validationCalculationAccuracy(){
        val input = -69.0
        val expect = 1.0

        assertEquals(expect, GlobalFun.calculateAccuracy(input), 0.1)
    }

}