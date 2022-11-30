package com.example.ubiquitihomework.ui.preview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ubiquitihomework.model.api.ApiResult
import com.example.ubiquitihomework.model.api.response.AirStatusRecord
import com.example.ubiquitihomework.model.api.response.AirStatusResponse
import com.example.ubiquitihomework.model.repository.ApiRepository
import com.example.ubiquitihomework.rule.TestDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.*

@OptIn(ExperimentalCoroutinesApi::class)
internal class PreviewViewModelTest {

    private lateinit var viewModel: PreviewViewModel
    private val mockApiRepository = mockk<ApiRepository>(relaxed = true)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    @Before
    fun setUp() {
        viewModel = PreviewViewModel(mockApiRepository)
    }

    @Test
    fun getAirStatus_hasData_pass() = runTest {
        // arrange
        val dummyData = getDummyData()
        coEvery { mockApiRepository.getAirStatus() } returns flow { emit(dummyData) }

        // act
        viewModel.getAirStatus()
        runCurrent()

        // assert
        Assert.assertTrue(viewModel.airStatusResult.value is ApiResult.Success)
        Assert.assertEquals(
            dummyData.records.size,
            (viewModel.airStatusResult.value as ApiResult.Success).result?.size
        )
        Assert.assertEquals(
            dummyData.records,
            (viewModel.airStatusResult.value as ApiResult.Success).result
        )
    }

    @Test
    fun getAirStatus_dataException_pass() = runTest {
        // arrange
        val dummyData = getDummyData()
        coEvery { mockApiRepository.getAirStatus() } returns flow { throw Exception("APi Error") }

        // act
        viewModel.getAirStatus()
        runCurrent()

        // assert
        Assert.assertTrue(viewModel.airStatusResult.value is ApiResult.Error)
    }

    @Test
    fun getSearchAirStatus_search_pass() = runTest {
        // arrange
        val dummyData = getDummyData()
        coEvery { mockApiRepository.getAirStatus() } returns flow { emit(dummyData) }
        viewModel.getAirStatus()
        runCurrent()

        // act
        viewModel.getSearchAirStatus("b")

        // assert
        Assert.assertEquals(
            2,
            viewModel.searchStatusResult.value?.size
        )
        Assert.assertEquals(
            arrayListOf(
                AirStatusRecord("a", "b", "c", "d", "e"),
                AirStatusRecord("a1", "b1", "c1", "d1", "e1")
            ),
            viewModel.searchStatusResult.value
        )
    }

    @Test
    fun getSearchAirStatus_search_empty_pass() = runTest {
        // arrange
        val dummyData = getDummyData()
        coEvery { mockApiRepository.getAirStatus() } returns flow { emit(dummyData) }
        viewModel.getAirStatus()
        runCurrent()

        // act
        viewModel.getSearchAirStatus("")

        // assert
        Assert.assertEquals(
            0,
            viewModel.searchStatusResult.value?.size
        )
    }

    private fun getDummyData(): AirStatusResponse {
        return AirStatusResponse(
            arrayListOf(
                AirStatusRecord("a", "b", "c", "d", "e"),
                AirStatusRecord("a1", "b1", "c1", "d1", "e1"),
                AirStatusRecord("a2", "aa", "dd", "d2", "e2"),
                AirStatusRecord("a3", "aaa", "ccc", "d3", "e3")
            )
        )
    }
}