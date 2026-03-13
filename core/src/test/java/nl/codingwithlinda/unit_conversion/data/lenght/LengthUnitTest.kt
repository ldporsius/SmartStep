package nl.codingwithlinda.unit_conversion.data.lenght

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Test

class LengthUnitTest {

    val cm = Cm(
        valueCm = 170.0,
    )
    val feetInches = FeetInches(
        feet = 5,
        inches = 7,
    )

    @Test
    fun `convert cm to inch`() {
        assertThat(feetInches.valueCm).isEqualTo(170.18)
        assertThat(cm.convert()).isEqualTo(feetInches)

    }

    @Test
    fun `convert cm to inch 2`() {
        val cm = Cm(
            valueCm = 30.48,
        )
        val feetInches = FeetInches(1,0)
        assertThat(cm.convert()).isEqualTo(feetInches)

    }
}