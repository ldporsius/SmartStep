package nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight

import org.junit.Assert.*
import org.junit.Test

class WeightUnitConverterTest {


    @Test
    fun `test weight ranges`(){
        println("weightRangeKg: ${weightRangeKg.toList()}")
        println("weightRangePounds: $weightRangePounds")
        assertTrue(56 in weightRangePounds)
        assertTrue(440 in weightRangePounds)

    }
}