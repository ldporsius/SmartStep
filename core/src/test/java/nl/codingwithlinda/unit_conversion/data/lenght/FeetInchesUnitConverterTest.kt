package nl.codingwithlinda.unit_conversion.data.lenght

import assertk.assertThat
import assertk.assertions.isEqualTo
import nl.codingwithlinda.unit_conversion.data.lenght.lenght_defs.FeetInches
import org.junit.Test

class FeetInchesUnitConverterTest {


    val converter = FeetInchesUnitConverter()

    @Test
    fun testConvertFeetInches_toCm(){
        val feetInches = FeetInches(5, 7)

        val cm = converter.toCm(feetInches)

        assertThat(cm.value).isEqualTo(170.18)
    }

    @Test
    fun testConvert_CM_toFeetInches(){
        val cm = LengthUnitConverter.Cm(170.0)
        val fi = converter.convertToFeetInches(cm)

        assertThat(fi.feet).isEqualTo(5)
        assertThat(fi.inches).isEqualTo(7)
    }
}