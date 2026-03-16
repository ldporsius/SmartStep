package nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight

import nl.codingwithlinda.unit_conversion.data.weight.LBSWeight
import nl.codingwithlinda.unit_conversion.data.weight.WeightUnitConverter
import org.junit.Test

class PrintKgToPoundsMap {

    @Test
    fun `print list of pounds and kg`() {
        val poundsKg = weightRangePounds.map {
            val pound = LBSWeight(it.toDouble())
            val kg = WeightUnitConverter.toKg(pound)

            it to kg.weight
        }

        poundsKg.groupBy { (pounds, kg) ->
            kg
        }.mapValues{ (i, pairs) ->
            println("$i kg == ")
            pairs.map { it.first }.onEach {p->
                println("--- $p pounds")
            }
        }
    }
}