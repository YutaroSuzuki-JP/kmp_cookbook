package org.example.project.di

import org.example.project.storage.AppSettings
import org.koin.dsl.module

actual val storageModule = module {
    single { AppSettings() }
}
