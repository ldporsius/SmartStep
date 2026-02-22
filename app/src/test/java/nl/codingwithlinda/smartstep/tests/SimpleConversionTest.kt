package nl.codingwithlinda.smartstep.tests

import assertk.assertThat
import assertk.assertions.isEqualTo
import nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight.kgToPounds
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ErrorCollector
import kotlin.math.roundToInt


@Ignore
class SimpleConversionTest {

    @get:Rule
    val collector = ErrorCollector()

    val odds = IntRange(1,100).step(2)
    val evens = IntRange(2,100).step(2)

    val mapEvens = evens.map { e->
        val nearestOddLow = odds.firstOrNull {o->
            o == e/2
        } ?: (e/2 -1)

        e to nearestOddLow
    }.toMap()



    @Test
    fun `print list of odd and even`() {
        mapEvens.forEach { (e, o) ->
            println("$e -> $o")
        }
    }

    @Test
    fun `convert even and odd - use integers`() {

        ////////// 2 is ok //////////////////////////////
        val converted2ToOdd = convertEvenToOddDivide(2)
        println("converted2ToOdd: $converted2ToOdd")
        assertThat(converted2ToOdd % 2).isEqualTo(1)
        assertThat(converted2ToOdd).isEqualTo(1)

        val converted1ToEven = convertOddToEvenMultiply(converted2ToOdd)
        assertThat(converted1ToEven).isEqualTo(2)

        //////////// 4 is problem /////////////////////////
        val converted4ToOdd = convertEvenToOddDivide(4)
        println("converted4ToOdd: $converted4ToOdd")
        assertThat(converted4ToOdd % 2).isEqualTo(1)

        val convertedBackToEven = convertOddToEvenMultiply(converted4ToOdd)
        assertThat(convertedBackToEven).isEqualTo(4)

    }

    @Test
    fun `convert even and odd - remember original value`() {
        val originalValue = 4

        val converted4ToOdd = convertEvenToOddDivide(originalValue)
        val convertedBackToEven = convertOddToEvenMultiply(converted4ToOdd)

        val restored = if (convertedBackToEven != originalValue) originalValue else convertedBackToEven

        assertThat(restored).isEqualTo(4)
    }


    @Ignore
    @Test
    fun `convert even and odd - odd value first`() {

        (3 .. 11).step(2).forEach { odd ->
            val originalValue = odd

            val convertedToEven = convertOddToEvenDivide(originalValue)
            assertThat(convertedToEven % 2).isEqualTo(0)

            val convertedBackToOdd = convertEvenToOddMultiple(convertedToEven)

            println("converted $odd ToEven: $convertedToEven")
            println("converted $convertedToEven BackToOdd: $convertedBackToOdd")

            collector.checkThat(
                convertedBackToOdd, equalTo(odd)
            )
        }
    }

    ///////////////////////////////////////////////////////////
    val kgs = listOf(1, 2, 3)
    val pounds = listOf(2, 3, 4, 5, 6)

    @Test
    fun `test keeping reference to original value`() {
        //reference to pound input as single source of truth
        var rememberPound: Int
        //we choose 1 kg from the possible values in list kg
        val inputKg1 = lookupKg(1.0)
        //convert kg to pounds and store it in the reference
        val convertedKgToPounds = kgToPounds(inputKg1.toDouble())
        rememberPound = lookupPound(convertedKgToPounds)

        assertThat(convertedKgToPounds).isEqualTo(2.0)
        assertThat(rememberPound).isEqualTo(2)

        //we switch back to display the value in kg, the original was 1
        val convertedPoundsToKg = poundsToKg(rememberPound.toDouble())
        val displayKg = lookupKg(convertedPoundsToKg)
        assertThat(displayKg).isEqualTo(1)


        //we choose 3 pounds from the possible values in list pounds
        val inputPounds = lookupPound(3.0)
        //we store the input directly in the reference
        rememberPound = inputPounds
        //we switch to display the value in kg
        val convertedPoundsToKg2 = poundsToKg(rememberPound.toDouble())
        val displayKg2 = lookupKg(convertedPoundsToKg2)
        assertThat(displayKg2).isEqualTo(2)
        //we switch back to display the value in pounds
        val convertedKgToPounds2 = kgToPounds(displayKg2.toDouble())
        //assertThat(convertedKgToPounds2).isEqualTo(3.0)

        val displayPounds2 = lookupPound(rememberPound.toDouble())
        assertThat(displayPounds2).isEqualTo(3)

    }
        ////////////////////////////////////////////

        fun kgToPounds(kg: Double): Double {
            return kg * 2.0
        }

        fun poundsToKg(pounds: Double): Double {
            return pounds / 2.0
        }

        fun lookupKg(kg: Double): Int{
            return kgs.find {
                kg.roundToInt() == it
            } ?: -1
        }
        fun lookupPound(pound: Double): Int{
            return pounds.find {
                pound.roundToInt() == it
            } ?: -1
        }





    fun convertEvenToOddDivide(e: Int): Int{
        println("converting $e to odd")
        val converted = e/2
        println("result:$converted . result modulo 2: ${converted % 2}")
        return if(converted % 2 == 1) converted else converted + 1
    }
    fun convertOddToEvenMultiply(o: Int) = o*2


    fun convertEvenToOddMultiple(e: Int): Int{
        return e * 2 - 1
    }
    fun convertOddToEvenDivide(o: Int): Int{
        val converted = (o.toDouble() / 2).roundToInt()

        return if(converted % 2 == 0) converted else converted + 1
    }
}