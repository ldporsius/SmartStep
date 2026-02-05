package nl.codingwithlinda.smartstep.tests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update


data class TestData(val name: String)
data class TestDataTwo(val name: String, val age: Int)


class TestViewModel: ViewModel() {

    private val _testDataState = MutableStateFlow(TestData("default"))
    val testDataState = _testDataState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        _testDataState.value
    )

    private val _testDataStateTwo = MutableStateFlow(TestDataTwo("default", 10))
    val testDataStateTwo = _testDataStateTwo.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        _testDataStateTwo.value
    )


    fun updateTestData(name: String) {
        val oldData = testDataState.value
        val newData = TestData(name)
        println("oldData and newData-class are the same instance: ${oldData.equals(newData)}")
        _testDataState.update {
            newData
        }
    }

    fun updateTestDataWithCopy(name: String){
        val oldData = testDataState.value
        val newData = oldData.copy(name = name)
        println("oldData and newData-copy are the same instance: ${oldData.equals(newData)}")
        _testDataState.update {
            newData
        }
    }

    fun updateTestDataTwoNewInstance(name: String) {
        val oldData = testDataStateTwo.value
        val newData = TestDataTwo(name, 12)
        println("oldData and newData-class are the same instance: ${oldData.equals(newData)}")
        _testDataStateTwo.update {
            newData
        }
    }

    fun updateTestDataTwoWithCopy(name: String){
        val oldData = testDataStateTwo.value
        val newData = oldData.copy(name = name)
        println("oldData and newData-copy are the same instance: ${oldData.equals(newData)}")
        _testDataStateTwo.update {
            newData
        }
    }
}