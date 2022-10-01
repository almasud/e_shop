package com.github.almasud.e_shop.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.almasud.e_shop.BuildConfig
import com.github.almasud.e_shop.data.db.dao.CategoryDao
import com.github.almasud.e_shop.data.db.dao.RemoteKeysDao
import com.github.almasud.e_shop.domain.model.entity.Category
import com.github.almasud.e_shop.domain.model.entity.RemoteKeys

@Database(
    entities = [Category::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(value = [Converter::class])
abstract class AppDatabase : RoomDatabase() {

    abstract val categoryDao: CategoryDao
    abstract val remoteKeysDao: RemoteKeysDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "${BuildConfig.APPLICATION_ID}.app.db"
            )
                .build()
    }
}
