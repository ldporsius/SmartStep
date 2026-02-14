package nl.codingwithlinda.smartstep.core.domain.unit_conversion

import kotlin.math.roundToInt


sealed interface UnitSystems {
    object IMPERIAL : UnitSystems
    object SI : UnitSystems
}


sealed interface LengthUnits{
    object CM : LengthUnits {
        override val system: UnitSystems
            get() = UnitSystems.SI
    }

    object FEET_INCHES : LengthUnits {
        override val system: UnitSystems
            get() = UnitSystems.IMPERIAL
    }

    val system: UnitSystems
}


enum class Weights(val system: UnitSystems){
    GRAMS(UnitSystems.SI),
    KG(UnitSystems.SI),
    LBS(UnitSystems.IMPERIAL)
}


sealed interface WeightUnits {
    val kgToGramFactor: Double
        get() = 1000.0

    val kgToPoundsFactor: Double
        get() = 2.20462


    data class Grams(val grams: Int) : WeightUnits {
        override val system: UnitSystems
            get() = UnitSystems.SI

        inline fun <reified T: WeightUnits>convert(
            target: Weights,
        ): T {
            return when (target) {
                Weights.GRAMS-> this
                Weights.KG -> {
                    val converted = grams / kgToGramFactor
                    KG(converted.roundToInt())
                }

                Weights.LBS -> {
                    val converted = (grams / kgToGramFactor) * kgToPoundsFactor
                    LBS(converted.roundToInt())
                }
            } as T
        }
    }
    data class KG(val kg: Int): WeightUnits {
        override val system: UnitSystems
            get() = UnitSystems.SI

        inline fun <reified T: WeightUnits>convert(
            target: Weights,
        ): T{
            return when (target) {
                Weights.KG -> this
                Weights.GRAMS -> {
                    val converted = kg * kgToGramFactor
                    Grams(converted.roundToInt())
                }

                Weights.LBS -> {
                    val converted = (kg * kgToPoundsFactor).roundToInt()
                    LBS(converted)
                }
            } as T
        }
    }

    data class LBS(val pounds: Int): WeightUnits{
        override val system: UnitSystems
            get() = UnitSystems.IMPERIAL

        inline fun <reified T: WeightUnits>convert(
            target: Weights,
        ): T{
            return when (target) {
                Weights.LBS -> this as T
                Weights.KG -> {
                    val converted = (pounds / kgToPoundsFactor).roundToInt()
                    KG(converted) as T
                }
                Weights.GRAMS -> {
                    val converted = ((pounds / kgToPoundsFactor) * kgToGramFactor).roundToInt()
                    Grams(converted) as T
                }
            }
        }
    }
    val system: UnitSystems
}
