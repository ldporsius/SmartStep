package nl.codingwithlinda.smartstep.core.domain.unit_conversion.height

import assertk.assertThat
import assertk.assertions.isEqualTo
import nl.codingwithlinda.unit_conversion.data.height.Length
import nl.codingwithlinda.unit_conversion.data.height.LengthUnits
import org.junit.Test

class LengthTest {


    @Test
    fun `test cm to feetInches conversion`(){
        val maxCm = Length.Cm(maxHeightCm)

        val feetInches = maxCm.convert<Length.FeetInches>(LengthUnits.FEET_INCHES)

        assertThat(feetInches.feet).isEqualTo(7)
        assertThat(feetInches.inches).isEqualTo(11)
    }

    @Test
    fun `test feetInches to cm conversion`(){
        val maxFeetInches = Length.FeetInches(maxHeightFeet, maxHeightInches)
        val cm = maxFeetInches.convert<Length.Cm>(LengthUnits.CM)


        assertThat(cm.cm).isEqualTo(maxHeightCm)

    }
}