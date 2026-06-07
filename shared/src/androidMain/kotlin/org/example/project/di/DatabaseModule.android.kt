package org.example.project.di

import android.content.Context
import androidx.room.Room
import org.example.project.infra.database.AppDatabase
import org.koin.dsl.module

actual val databaseModule = module {
    single<AppDatabase> {
        val dbFile = get<Context>().getDatabasePath("artwork_db.db")
        Room.databaseBuilder<AppDatabase>(
            context = get(),
            name = dbFile.absolutePath
        ).build()
    }
    single { get<AppDatabase>().artworkDao() }
}
