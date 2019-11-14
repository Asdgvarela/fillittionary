package com.maangata.fillit_tionary.Dagger

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.maangata.fillit_tionary.Room.FillitDatabase
import com.maangata.fillit_tionary.Utils.Constants.DB_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module
class FillitDatabaseModule() {

    @Provides
    @Singleton
    fun getFillitDatabase(app: Application): FillitDatabase {
        return Room.databaseBuilder(
            app.applicationContext,
            FillitDatabase::class.java,
            DB_NAME
        ).build()
    }
}