package nl.codingwithlinda.smartstep.core.domain.unit_conversion.weight

import kotlin.math.roundToInt

sealed interface Weight{
    val baseFactor: Double
}

object GRAM : Weight {
    override val baseFactor: Double
        get() = 1.0
}

object KG : Weight {
    override val baseFactor: Double
        get() = 1000.0
}

object LBS : Weight {
    override val baseFactor: Double
        get() = 453.59237
}

interface ConcreteWeight{
    val weight: Int
    val unit: Weight
}

data class GramsWeight(
    override val weight: Int,
    override val unit: Weight = GRAM
): ConcreteWeight

data class KGWeight(
    override val weight: Int,
    override val unit: Weight = KG
): ConcreteWeight

data class LBSWeight(
    override val weight: Int,

): ConcreteWeight{
    override val unit: Weight = LBS
}

fun convertWeight(
    concreteWeight: ConcreteWeight,
    target: Weight
): ConcreteWeight {
    val convertedWeight =
        concreteWeight.weight * concreteWeight.unit.baseFactor / target.baseFactor
    return when(target) {
        GRAM -> GramsWeight(convertedWeight.roundToInt())
        KG -> KGWeight(convertedWeight.roundToInt())
        LBS -> LBSWeight(convertedWeight.roundToInt())
    }
}

fun fromPreviousPounds(kg: KGWeight, pounds: Int): LBSWeight{

    val correspondingPounds = kgToPounds[kg] ?:emptyList()

    println("--- KG --- correspondingPounds: $correspondingPounds")

    if (pounds in correspondingPounds) {
        println("--- KG --- returning previous pounds: $pounds")
        return LBSWeight(pounds)
    }
    val converted = convertWeight(kg, LBS)

    return converted as LBSWeight
}