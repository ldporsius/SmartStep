package nl.codingwithlinda.unit_conversion.domain

interface UnitType{
    val baseFactor: Double
}

interface LengthUnitType: UnitType
interface WeightUnitType: UnitType
interface DistanceUnitType: UnitType





