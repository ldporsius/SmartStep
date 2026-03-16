package nl.codingwithlinda.unit_conversion.data.lenght

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Test
import kotlin.math.roundToInt

class LengthUnitConverterTest {

    val lengthConverter = LengthUnitConverter()

    @Test
    fun testLengthConverter_CmInch(){
        val cm = LengthUnitConverter.Cm(1.0)
        val inch = lengthConverter.toInch(cm)

        val rounded = (inch.value * 100).roundToInt().toDouble() / 100
        assertThat(rounded).isEqualTo(.39)

    }
    @Test
    fun testLengthConverter_CmInchesFeet(){
        val cm = LengthUnitConverter.Cm(100.0)
        val inch = lengthConverter.toInch(cm)
        val feet = lengthConverter.toFeet(inch)

        val rounded = (feet.value * 100).roundToInt().toDouble() / 100
        assertThat(rounded).isEqualTo(3.28)
    }

    @Test
    fun testLengthConverter_CmToFeet(){
        val cm = LengthUnitConverter.Cm(100.0)
        val feet = lengthConverter.toFeet(cm)

        val rounded = (feet.value * 100).roundToInt().toDouble() / 100
        assertThat(rounded).isEqualTo(3.28)
    }


}