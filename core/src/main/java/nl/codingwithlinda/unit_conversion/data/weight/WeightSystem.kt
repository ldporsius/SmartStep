package nl.codingwithlinda.unit_conversion.data.weight

sealed interface Weight{
    val baseFactor: Double
}

class TestConverter(
    override val baseFactor: Double
): Weight



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
    val weight: Double
    val unit: Weight
}

data class GramsWeight(
    override val weight: Double,
    override val unit: Weight = GRAM
): ConcreteWeight

data class KGWeight(
    override val weight: Double,
    override val unit: Weight = KG
): ConcreteWeight

data class LBSWeight(
    override val weight: Double,

): ConcreteWeight{
    override val unit: Weight = LBS
}

data class TestWeight(
    override val weight: Double,
    override val unit: Weight
): ConcreteWeight



fun convertWeight(
    concreteWeight: ConcreteWeight,
    target: Weight
): ConcreteWeight {
    if (target.baseFactor == 0.0) throw Exception("Cannot convert to zero")
    val convertedWeight =
        concreteWeight.weight * concreteWeight.unit.baseFactor / target.baseFactor
    return when(target) {
        GRAM -> GramsWeight(convertedWeight)
        KG -> KGWeight(convertedWeight)
        LBS -> LBSWeight(convertedWeight)
        is TestConverter -> TestWeight(convertedWeight, target)
    }
}


