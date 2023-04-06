package pinger.challenge.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import pinger.challenge.networking.FileDownloadAPI
import pinger.challenge.utility.PageSequenceCalculator
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

@ExperimentalCoroutinesApi
class PageSequenceRepositoryTest {
    private val mockWebServer = MockWebServer()
    private lateinit var fileDownloadAPI: FileDownloadAPI
    private lateinit var pageSequenceCalculator: PageSequenceCalculator
    private lateinit var pageSequenceRepository: PageSequenceRepository

    private fun getApi(): FileDownloadAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("").toString())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(FileDownloadAPI::class.java)
    }

    @Before
    fun setUp() {
        fileDownloadAPI = getApi()
        pageSequenceCalculator = PageSequenceCalculator()
        pageSequenceRepository = PageSequenceRepository(fileDownloadAPI, pageSequenceCalculator)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `fetchLogs returns expected list when server response is not empty`() = runBlocking {
        // Given
        mockWebServer.enqueue(MockResponse().setBody(getMockResponse()))

        // When
        val result = pageSequenceRepository.fetchLogs()

        // Then
        result.collect { popularSequence ->
            assertEquals(
                listOf(
                    Pair(
                        "/products/desk/\n/products/phone/\n/contact/", 1
                    ),
                ), popularSequence
            )
        }
    }

    @Test
    fun `fetchLogs returns empty list when server response is empty`() = runBlocking {
        // Given
        mockWebServer.enqueue(MockResponse().setBody(""))

        // When
        val result = pageSequenceRepository.fetchLogs()

        // Then
        result.collect { popularSequence ->
            assertEquals(emptyList<Pair<String, Int>>(), popularSequence)
        }
    }

    private fun getMockResponse(): String {
        return "123.4.5.9 - - [03/Sep/2013:18:34:48 -0600] \"GET /team/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
                "123.4.5.6 - - [03/Sep/2013:18:34:58 -0600] \"GET /products/car/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
                "123.4.5.8 - - [03/Sep/2013:18:35:08 -0600] \"GET /products/desk/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.65 Safari/537.36\"\n" +
                "123.4.5.6 - - [03/Sep/2013:18:35:18 -0600] \"GET /products/desk/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
                "123.4.5.9 - - [03/Sep/2013:18:35:28 -0600] \"GET /products/phone/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
                "123.4.5.8 - - [03/Sep/2013:18:35:38 -0600] \"GET /products/phone/ HTTP/1.1\" 500 821 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.65 Safari/537.36\"\n" +
                "123.4.5.2 - - [03/Sep/2013:18:35:48 -0600] \"GET /access/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.65 Safari/537.36\"\n" +
                "123.4.5.2 - - [03/Sep/2013:18:35:58 -0600] \"GET /products/desk/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\"\n" +
                "123.4.5.4 - - [03/Sep/2013:18:36:08 -0600] \"GET /products/desk/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.65 Safari/537.36\"\n" +
                "123.4.5.8 - - [03/Sep/2013:18:36:18 -0600] \"GET /contact/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.65 Safari/537.36\"\n" +
                "123.4.5.5 - - [03/Sep/2013:18:36:28 -0600] \"GET /about/ HTTP/1.1\" 200 3327 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:23.0) Gecko/20100101 Firefox/23.0\""
    }

}