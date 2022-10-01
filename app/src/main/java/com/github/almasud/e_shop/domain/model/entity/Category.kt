package com.github.almasud.e_shop.domain.model.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.almasud.e_shop.CategoryListByParentIdQuery
import com.github.almasud.e_shop.domain.model.Image
import com.github.almasud.e_shop.domain.model.Parent

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    val uid: String,
    val enName: String?,
    val bnName: String?,
    @Embedded(prefix = "parent_")
    val parent: Parent?,
    val parents: List<Parent?>?,
    @Embedded(prefix = "image_")
    val image: Image?,
    val attributeSetUid: String?,
    val isActive: Boolean?,
    val inActiveNote: String?,
    val createdAt: String?,
    val updatedAt: String?
) {
    companion object {
        fun toCategory(category: CategoryListByParentIdQuery.Category) =
            Category(
                uid = category.uid!!,
                enName = category.enName,
                bnName = category.bnName,
                parent = Parent.toParent(category.parent),
                parents = Parent.toParentList(category.parents),
                image = Image.toImage(category.image),
                attributeSetUid = category.attributeSetUid,
                isActive = category.isActive,
                inActiveNote = category.inActiveNote,
                createdAt = category.createdAt.toString(),
                updatedAt = category.updatedAt.toString()
            )
    }
}