package nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight

import org.junit.Test

class PrintKgToPoundsMap {



    @Test
    fun `print list of pounds and kg`() {
        val poundsKg = weightRangePounds.map {
            val pound = LBSWeight(it.toDouble())
            val kg = convertWeight(pound, KG)

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

    @Test
    fun `print list of kg and pounds`() {
        kgToPounds.forEach { (kg, pounds) ->
            println("$kg kg == ")
            pounds.onEach {p->
                println("--- $p pounds")
            }
        }
    }



}