package org.example.project.di

import org.example.project.infra.network.MetApi
import org.example.project.infra.repository.ArtworkRepositoryImpl
import org.example.project.domain.usecase.SearchArtworksUseCase
import org.example.project.domain.usecase.GetFavoriteArtworksUseCase
import org.example.project.domain.usecase.ToggleFavoriteArtworkUseCase
import org.example.project.domain.repository.ArtworkRepository
import org.example.project.infra.network.MetApiImpl
import org.example.project.presentation.MainViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val networkModule = module {
    single {
        HttpClient(get()) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }
    single<MetApi> { MetApiImpl(get()) }
}

val repositoryModule = module {
    single<ArtworkRepository> { ArtworkRepositoryImpl(get(), get()) }
}

val domainModule = module {
    single { SearchArtworksUseCase(get()) }
    single { GetFavoriteArtworksUseCase(get()) }
    single { ToggleFavoriteArtworkUseCase(get()) }
}

val presentationModule = module {
    viewModel { MainViewModel(get(), get(), get()) }
}

val appModule = listOf(
    networkModule,
    platformNetworkModule,
    databaseModule,
    repositoryModule,
    domainModule,
    presentationModule
)

