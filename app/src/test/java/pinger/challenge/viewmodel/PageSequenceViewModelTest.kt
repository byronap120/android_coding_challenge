package pinger.challenge.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import pinger.challenge.data.PageSequenceRepository
import pinger.challenge.intent.PageSequenceIntent

class PageSequenceViewModelTest {

    private val mockPageSequenceRepository = mockk<PageSequenceRepository>()
    private val pageSequenceViewModel = PageSequenceViewModel(mockPageSequenceRepository)

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetchLogs sets pageSequenceData value when repository call succeeds`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Given
        val pageSequenceData = listOf("/products/phone/contact/" to 1, "/admin/phone/contact/" to 2)
        coEvery { mockPageSequenceRepository.fetchLogs() } returns flowOf(pageSequenceData)

        // When
        pageSequenceViewModel.processIntent(PageSequenceIntent.FetchLogsIntent)

        // Then
        Assert.assertEquals(pageSequenceData, pageSequenceViewModel.pageSequenceData.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetchLogs sets loadingData to true then to false`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Given
        val values = mutableListOf<Boolean>()
        val observer = Observer<Boolean> { value ->
            values.add(value)
        }
        pageSequenceViewModel.loadingData.observeForever(observer)

        // When
        pageSequenceViewModel.processIntent(PageSequenceIntent.FetchLogsIntent)

        // Then
        Assert.assertEquals(listOf(true, false), values)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetchLogs sets error value when repository call fails`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Given
        val errorMessage = "Error Unit Test"
        coEvery { mockPageSequenceRepository.fetchLogs() }.throws(Exception(errorMessage))

        // When
        pageSequenceViewModel.processIntent(PageSequenceIntent.FetchLogsIntent)

        // Then
        Assert.assertEquals(errorMessage, pageSequenceViewModel.error.value)
    }
}