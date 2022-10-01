package com.github.almasud.e_shop.data.di

import android.content.Context
import com.github.almasud.e_shop.data.db.AppDatabase
import com.github.almasud.e_shop.data.db.dao.CategoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getInstance(context = context)

    @Provides
    fun providesCategoryDao(appDatabase: AppDatabase): CategoryDao = appDatabase.categoryDao

}