package nl.codingwithlinda.smartstep.core.domain.util

abstract class Memento<T> {

    private val history: MutableList<T> = mutableListOf()

    fun save(item: T){
        history.add(item)
    }
    fun restore(index: Int): T{
        return history[index]
    }
    fun restoreLast(): T{
        return history.last()
    }

    fun clear(){
        history.clear()
    }
}