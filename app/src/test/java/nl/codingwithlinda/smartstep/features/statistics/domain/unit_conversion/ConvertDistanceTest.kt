package nl.codingwithlinda.smartstep.features.statistics.domain.unit_conversion

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import org.junit.Assert.*
import org.junit.Test

class ConvertDistanceTest {


    val km = 1.0

    @Test
    fun testConvertDistanceKMToMeter() {
        val result = convertDistance(km, KM, METER)
        assertThat(result).isEqualTo(1000.0)
    }

    @Test
    fun testConvertDistanceKMToMile() {
        val result = convertDistance(km, KM, MILE)
        assertThat(result).isEqualTo(1 / 1.60934)
    }

    @Test
    fun testConvertDistanceKMToMileDataClass() {
        val km = km(1.0)
        val result = convertDistance(km,MILE)
        assertThat(result).isInstanceOf(mile::class)
        assertThat(result.value).isEqualTo(1 / 1.60934)
    }
}