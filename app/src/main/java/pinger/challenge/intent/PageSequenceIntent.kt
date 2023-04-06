package pinger.challenge.intent

sealed class PageSequenceIntent {
    object FetchLogsIntent : PageSequenceIntent()
}