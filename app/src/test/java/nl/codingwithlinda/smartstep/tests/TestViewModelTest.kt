package nl.codingwithlinda.smartstep.tests

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TestViewModelTest {

    val testDispatcher = StandardTestDispatcher()
    lateinit var viewModel: TestViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = TestViewModel()

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test state works with data class new instance`()= runTest {
        viewModel.testDataState.test {
            val em0 = awaitItem()
            assertEquals(em0.name, "default")
            viewModel.updateTestData("after emission0")

            val em1 = awaitItem()
            assertEquals(em1.name, "after emission0")
        }
    }

    @Test
    fun `test state works with data class copy`()= runTest {
        viewModel.testDataState.test {
            val em0 = awaitItem()
            assertEquals(em0.name, "default")
            viewModel.updateTestDataWithCopy("after emission0")

            val em1 = awaitItem()
            assertEquals(em1.name, "after emission0")
        }
    }

    @Test
    fun `test state works with data class two fields new instance`()= runTest {
        viewModel.testDataStateTwo.test {
            val em0 = awaitItem()
            assertEquals(em0.name, "default")
            viewModel.updateTestDataTwoNewInstance("after emission0")

            val em1 = awaitItem()
            assertEquals(em1.name, "after emission0")
        }
    }

    @Test
    fun `test state works with data class two fields copy`()= runTest {
        viewModel.testDataStateTwo.test {
            val em0 = awaitItem()
            assertEquals(em0.name, "default")
            viewModel.updateTestDataTwoWithCopy("after emission0")

            val em1 = awaitItem()
            assertEquals(em1.name, "after emission0")
        }
    }

}