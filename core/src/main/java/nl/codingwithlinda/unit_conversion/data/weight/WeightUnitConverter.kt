package nl.codingwithlinda.unit_conversion.data.weight


object WeightUnitConverter {

    fun toGram(from: ConcreteWeight): GramsWeight{
        return convertWeight(from, Weight.GRAM)
    }
    fun toKg(from: ConcreteWeight): KGWeight{
        return convertWeight(from, Weight.KG)
    }
    fun toLbs(from: ConcreteWeight): LBSWeight{
        return convertWeight(from, Weight.LBS)
    }

    private inline fun <reified T: ConcreteWeight>convertWeight(
        concreteWeight: ConcreteWeight,
        target: Weight
    ): T {
        if (target.baseFactor == 0.0) throw Exception("Cannot convert to zero")
        val convertedWeight =
            concreteWeight.weight * concreteWeight.unit.baseFactor / target.baseFactor
        return when(target) {
            Weight.GRAM -> GramsWeight(convertedWeight)
            Weight.KG -> KGWeight(convertedWeight)
            Weight.LBS -> LBSWeight(convertedWeight)
        }as T
    }

}