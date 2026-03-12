package nl.codingwithlinda.smartstep.features.statistics.domain.unit_conversion

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import nl.codingwithlinda.unit_conversion.data.KM
import nl.codingwithlinda.unit_conversion.data.METER
import nl.codingwithlinda.unit_conversion.data.MILE
import nl.codingwithlinda.unit_conversion.data.convertDistance
import nl.codingwithlinda.unit_conversion.data.km
import nl.codingwithlinda.unit_conversion.data.mile
import org.junit.Test

class ConvertDistanceTest {


    @Test
    fun testConvertDistanceKMToMileDataClass() {
        val km = km(1.0)
        val result = convertDistance(km, MILE)
        assertThat(result).isInstanceOf(mile::class)
        assertThat(result.value).isEqualTo(1 / 1.60934)
    }

    @Test
    fun testConvertBehaviorIsIdempotent() {
        val km = km(1.0)

        val result = convertDistance(km, KM)
        val result2 = convertDistance(result, KM)
        assertThat(result2).isEqualTo(result)

    }
}