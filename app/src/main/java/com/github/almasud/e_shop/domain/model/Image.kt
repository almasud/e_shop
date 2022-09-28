package com.github.almasud.e_shop.domain.model

import com.github.almasud.e_shop.CategoryListByParentIdQuery

data class Image(
    val name: String?,
    val url: String?,
    val signedUrl: String?
) {
    companion object {
        fun toImage(image: CategoryListByParentIdQuery.Image?) =
            Image(
                name = image?.name,
                url = image?.url,
                signedUrl = image?.signedUrl
            )
    }
}