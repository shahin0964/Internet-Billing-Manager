package com.example

import org.junit.Test
import org.junit.Assert.*

class TestParser {
    @Test
    fun testParse() {
        println("PARSED4: " + "1,500".toDoubleOrNull())
        println("PARSED5: " + "1,500".replace(",", "").toDoubleOrNull())
        println("PARSED6: " + "500,50".toDoubleOrNull())
    }
}
