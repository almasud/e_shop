package com.github.almasud.e_shop.domain.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val categoryId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
