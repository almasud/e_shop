package com.github.almasud.e_shop.domain.model

import com.github.almasud.e_shop.CategoryListByParentIdQuery

data class Parent(
    val uid: String?,
    val enName: String?,
    val bnName: String?
) {
    companion object {
        fun toParent(parent: CategoryListByParentIdQuery.Parent?): Parent =
            Parent(
                uid = parent?.uid,
                enName = parent?.enName,
                bnName = parent?.bnName
            )

        fun toParent(parent: CategoryListByParentIdQuery.Parent1?): Parent =
            Parent(
                uid = parent?.uid,
                enName = parent?.enName,
                bnName = parent?.bnName
            )

        fun toParentList(parentList: List<CategoryListByParentIdQuery.Parent1?>?): List<Parent>? =
            parentList?.map { parent1 -> toParent(parent1) }
    }
}