package nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion

import org.junit.Assert.*
import org.junit.Test

class HeightUnitConverterTest {

    val heightConverter = HeightUnitConverter()

    @Test
    fun `test zero cm returns pair of (0,0)`(){
        val value = 0.toDouble()
        val result = heightConverter.toUi(value)

        assertTrue(result.first.equals("0"))
        assertTrue(result.second.equals("0"))
    }

    @Test
    fun `test one cm returns pair of (0,0)`(){
        val value = 1.toDouble()
        val result = heightConverter.toUi(value)

        assertTrue(result.first.equals("0"))
        assertTrue(result.second.equals("0"))
    }

    @Test
    fun `test 175 cm returns pair of (5,9)`(){
        val value = 175.toDouble()
        val result = heightConverter.toUi(value)

        assertEquals("5", result.first, )
        assertEquals("9", result.second, )
    }
    @Test
    fun `test 176 cm returns pair of (5,9)`(){
        val value = 176.toDouble()
        val result = heightConverter.toUi(value)

        assertEquals("5", result.first, )
        assertEquals("9", result.second, )
    }
    @Test
    fun `test 183 cm returns pair of (6,0)`(){
        val value = 183.toDouble()
        val result = heightConverter.toUi(value)

        assertEquals("6", result.first, )
        assertEquals("0", result.second, )
    }

}