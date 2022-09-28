package com.github.almasud.e_shop.domain.model

import com.github.almasud.e_shop.CategoryListByParentIdQuery

data class Category(
    val uid: String?,
    val enName: String?,
    val bnName: String?,
    val parent: Parent?,
    val parents: List<Parent?>?,
    val image: Image?,
    val attributeSetUid: String?,
    val isActive: Boolean?,
    val inActiveNote: String?,
    val createdAt: Any?,
    val updatedAt: Any?
) {
    companion object {
        fun toCategory(category: CategoryListByParentIdQuery.Category?) =
            Category(
                uid = category?.uid,
                enName = category?.enName,
                bnName = category?.bnName,
                parent = Parent.toParent(category?.parent),
                parents = Parent.toParentList(category?.parents),
                image = Image.toImage(category?.image),
                attributeSetUid = category?.attributeSetUid,
                isActive = category?.isActive,
                inActiveNote = category?.inActiveNote,
                createdAt = category?.createdAt,
                updatedAt = category?.updatedAt
            )
    }
}