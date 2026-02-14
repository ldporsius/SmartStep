package nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight

import assertk.assertThat
import assertk.assertions.isBetween
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystems
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.WeightUnits
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.Weights
import org.junit.Assert.*
import org.junit.Test
import kotlin.math.roundToInt

class WeightUnitConverterTest {


    @Test
    fun `test weight ranges`(){
        println("weightRangeKg: ${weightRangeKg.toList()}")
        println("weightRangePounds: $weightRangePounds")
        assertTrue(56 in weightRangePounds)
        assertTrue(440 in weightRangePounds)

    }

    @Test
    fun `test weight conversion`(){
        val kg = 60
        val pounds = WeightUnitConverter.convert(kg.toDouble(), from = UnitSystems.SI, to = Weights.LBS.system).roundToInt()
        println("kg: $kg, pounds: $pounds")
        assertTrue(pounds in weightRangePounds)
        assertThat(pounds).isBetween(132,133 )

        val pounds2 = 132
        val kg2 = WeightUnitConverter.convert(pounds2.toDouble(), from = UnitSystems.IMPERIAL, to = Weights.KG.system).roundToInt()
        println("pounds: $pounds2, kg: $kg2")
        assertTrue(kg2 in weightRangeKg)
        assertThat(kg2).isBetween(60,61)


        val pounds200 = 200.0
        val kg90 = WeightUnitConverter.convert(pounds200, from = UnitSystems.IMPERIAL, to = Weights.KG.system).roundToInt()
        println("pounds: $pounds2, kg: $kg2")
        assertThat(kg90).isBetween(90,91)

    }
}