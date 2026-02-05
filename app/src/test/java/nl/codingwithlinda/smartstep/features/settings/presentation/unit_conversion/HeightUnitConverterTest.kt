package nl.codingwithlinda.smartstep.features.settings.presentation.unit_conversion

import nl.codingwithlinda.smartstep.core.domain.unit_conversion.height.HeightUnitConverter
import org.junit.Assert.*
import org.junit.Test

class HeightUnitConverterTest {

    val heightConverter = HeightUnitConverter

    @Test
    fun `test zero cm returns pair of (0,0)`(){
        val value = 0.toDouble()
        val result = heightConverter.toImperial(value)

        assertTrue(result.first.equals("0"))
        assertTrue(result.second.equals("0"))
    }

    @Test
    fun `test one cm returns pair of (0,0)`(){
        val value = 1.toDouble()
        val result = heightConverter.toImperial(value)

        assertTrue(result.first.equals("0"))
        assertTrue(result.second.equals("0"))
    }

    @Test
    fun `test 175 cm returns pair of (5,9)`(){
        val value = 175.toDouble()
        val result = heightConverter.toImperial(value)

        assertEquals("5", result.first, )
        assertEquals("9", result.second, )
    }
    @Test
    fun `test 176 cm returns pair of (5,9)`(){
        val value = 176.toDouble()
        val result = heightConverter.toImperial(value)

        assertEquals("5", result.first, )
        assertEquals("9", result.second, )
    }
    @Test
    fun `test 180 cm returns pair of (5,11)`(){
        val value = 180.toDouble()
        val result = heightConverter.toImperial(value)

        assertEquals("5", result.first, )
        assertEquals("11", result.second, )
    }
    @Test
    fun `test 183 cm returns pair of (6,0)`(){
        val value = 183.toDouble()
        val result = heightConverter.toImperial(value)

        assertEquals("6", result.first, )
        assertEquals("0", result.second, )
    }

    @Test
    fun `test feet inches from ui`(){
        val feet = 5
        val inches = 9
        val result = heightConverter.toSI(feet, inches)
        assertEquals(175.0, result, 0.5)

    }

    @Test
    fun `test (5,11)feet inches returns 180cm`(){
        val feet = 5
        val inches = 11
        val result = heightConverter.toSI(feet, inches)
        assertEquals(180.0, result, 0.5)

    }

}