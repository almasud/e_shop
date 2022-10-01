package com.github.almasud.e_shop.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.almasud.e_shop.domain.model.entity.RemoteKeys

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRemoteKey(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE categoryId=:categoryId")
    suspend fun remoteKeysByCategoryId(categoryId: String): RemoteKeys?

    @Query("DELETE FROM remote_keys WHERE categoryId=:categoryId")
    suspend fun clearRemoteKeysByCategoryId(categoryId: String)

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}
