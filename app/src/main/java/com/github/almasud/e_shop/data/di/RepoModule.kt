package com.github.almasud.e_shop.data.di

import com.github.almasud.e_shop.data.repo.CategoryRepoImpl
import com.github.almasud.e_shop.domain.repo.CategoryRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

/**
 * Created by Abdullah Almasud on 6/7/22.
 * Email: dev.almasud@gmail.com
 */
@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun bindCategoryRepo(
        categoryRepoImpl: CategoryRepoImpl
    ): CategoryRepo
}