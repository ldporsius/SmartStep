package nl.codingwithlinda.unit_conversion.data.distance

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import org.junit.Test

class ConvertDistanceTest {

    val converter = DistanceConverter

    @Test
    fun testConvertDistanceKMToMileDataClass() {
        val km = ConcreteDistance.km(1.0)
        val result = converter.toMile(km)
        assertThat(result).isInstanceOf(ConcreteDistance.mile::class)
        assertThat(result.value).isEqualTo(1 / 1.60934)
    }

    @Test
    fun testConvertBehaviorIsIdempotent() {
        val km = ConcreteDistance.km(1.0)

        val result = converter.toKm(km)
        val result2 = converter.toKm(result)

        assertThat(result2).isEqualTo(result)

    }
}