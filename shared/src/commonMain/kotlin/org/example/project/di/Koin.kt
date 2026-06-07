package org.example.project.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(appModule)
}

// iOS helper (since Swift doesn't support default arguments in Kotlin functions directly)
fun initKoin() = initKoin {}

fun doInitKoin() = initKoin {}

