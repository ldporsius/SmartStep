package nl.codingwithlinda.unit_conversion.data.weight

sealed interface Weight{
    val baseFactor: Double

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
}

interface ConcreteWeight{
    val weight: Double
    val unit: Weight
}

data class GramsWeight(
    override val weight: Double,
    override val unit: Weight = Weight.GRAM
): ConcreteWeight

data class KGWeight(
    override val weight: Double,
    override val unit: Weight = Weight.KG
): ConcreteWeight

data class LBSWeight(
    override val weight: Double,
): ConcreteWeight{
    override val unit: Weight = Weight.LBS
}



