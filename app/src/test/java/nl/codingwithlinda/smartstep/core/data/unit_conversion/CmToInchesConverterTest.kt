package nl.codingwithlinda.smartstep.core.data.unit_conversion

import org.junit.Assert.*
import org.junit.Test

class CmToInchesConverterTest {

    val cmToInchesConverter = CmToInchesConverter()
    val inchesToFeetConverter = InchesToFeetConverter()


    @Test
    fun `test one cm is correct inches`(){
        val result = cmToInchesConverter.convert(1.0)
        assertEquals(0.3937007874, result, 0.0)

    }

    @Test
    fun `test 175 cm is correct inches`(){
        val result = cmToInchesConverter.convert(175.0)
        assertEquals(68.8976377953, result, 0.01)

    }
    @Test
    fun `test 175 cm is correct feet and inches`(){
        val inches = cmToInchesConverter.convert(175.0)
        assertEquals(68.8976377953, inches, 0.01)

        val feet = inchesToFeetConverter.convert(inches)
        assertEquals(5.74, feet, 0.1)


    }
}