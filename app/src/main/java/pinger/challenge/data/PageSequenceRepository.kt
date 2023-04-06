package pinger.challenge.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.BufferedSource
import pinger.challenge.networking.FileDownloadAPI
import pinger.challenge.parsing.ApacheLogParser
import pinger.challenge.utility.PageSequenceCalculator
import java.io.IOException


class PageSequenceRepository(
    private val fileDownloadAPI: FileDownloadAPI,
    private val pageSequenceCalculator: PageSequenceCalculator,
) {
    private val pathSequenceList = mutableListOf<String>()
    private val numberOfConsecutivePages = 3

    suspend fun fetchLogs(): Flow<List<Pair<String, Int>>> = flow {
        try {
            pathSequenceList.clear()
            val response = fileDownloadAPI.fetchLogs()
            getLinesOfInputFromSource(response.source())
            val logs = parseApacheLogs(pathSequenceList)
            val popularSequence = calculateMostPopularPathSequences(logs)
            emit(popularSequence)
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    private fun parseApacheLogs(pathSequenceData: MutableList<String>): HashMap<String, MutableList<String>> {
        return ApacheLogParser(pathSequenceData).parseLogsForEachUser()
    }

    private fun calculateMostPopularPathSequences(parsedLogs: HashMap<String, MutableList<String>>): List<Pair<String, Int>> {
        return pageSequenceCalculator.getMostCommonPageSequences(
            parsedLogs,
            numberOfConsecutivePages
        )
    }

    private fun getLinesOfInputFromSource(source: BufferedSource) {
        try {
            while (!source.exhausted()) {
                val line = source.readUtf8Line()
                if (line != null) {
                    pathSequenceList.add(line)
                }
            }
        } catch (e: IOException) {
            throw Exception(e)
        }
    }
}