package org.example.project.di

import androidx.room.Room
import org.example.project.infra.database.AppDatabase
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
actual val databaseModule = module {
    single<AppDatabase> {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = true,
            error = null
        )
        val dbPath = documentDirectory?.path + "/artwork_db.db"
        Room.databaseBuilder<AppDatabase>(name = dbPath).build()
    }
    single { get<AppDatabase>().artworkDao() }
}
