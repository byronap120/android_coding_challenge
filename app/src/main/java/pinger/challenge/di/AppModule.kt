package pinger.challenge.di

import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pinger.challenge.data.repository.PageSequenceRepository
import pinger.challenge.networking.FileDownloadAPI
import pinger.challenge.utility.PageSequenceCalculator
import pinger.challenge.viewmodel.PageSequenceViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val appModule = module {

    viewModel { PageSequenceViewModel(get()) }

    single { PageSequenceRepository(get(), get()) }

    single { provideFileDownloadAPI(get()) }

    single { PageSequenceCalculator() }

    factory { provideRetrofit(get()) }

    factory { provideOkHttpClient() }
}


fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl("https://raw.githubusercontent.com")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient().newBuilder().build()
}

fun provideFileDownloadAPI(retrofit: Retrofit): FileDownloadAPI =
    retrofit.create(FileDownloadAPI::class.java)
