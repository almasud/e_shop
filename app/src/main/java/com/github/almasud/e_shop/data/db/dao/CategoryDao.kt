package com.github.almasud.e_shop.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.almasud.e_shop.domain.model.entity.Category

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCategories(repos: List<Category>)

    @Query("SELECT * FROM categories WHERE parent_uid==:parentId")
    fun getCategoriesByParentId(parentId: String): PagingSource<Int, Category>

    @Query("DELETE FROM categories WHERE parent_uid=:parentId")
    suspend fun clearCategoriesByParentId(parentId: String)

    @Query("DELETE FROM categories")
    suspend fun clearCategories()
}
