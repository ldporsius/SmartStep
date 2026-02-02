package nl.codingwithlinda.smartstep.core.domain.unit_conversion

interface UnitConverter<N: Number> {
    fun convert(from: N): N
}