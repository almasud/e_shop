# e-Shop
A simple Android application that displays a list of category items. Every category item has an expandable list of categories. The expandable list of categories also has another list of categories.

## Screenshot
<p align="center">
  <img width="250" height="450" src="https://raw.githubusercontent.com/almasud/e_shop/master/screenshots/screen_01.jpeg" alt="Category Screen"/>
</p>

## Used
<div style="display:flex;">

- Kotlin (Programming language)
- Kotlin Coroutine (Asynchronous task)
- Jetpack Navigation Components
- Apollo (GraphQL client)
- Room Database
- Paging 3 (Load and display pages)
- Hilt (Dependency Injection)
- Glide (Image loading & caching)

</div>

## GraphQL query
```
{
    getCategories(
        filter: { parentCategoryUid: "root" },
        pagination: { limit: 10, skip: 0 }
    ) {
        result {
            categories {
                uid
                bnName
                enName
                parent {
                    uid
                    bnName
                    enName
                }
                parents {
                    uid
                    bnName
                    enName
                }
                image {
                    name
                    url
                    signedUrl
                }
                attributeSetUid
                isActive
                inActiveNote
                createdAt
                updatedAt
            }
        }
    }
}
```


##### Thanks & regards
[Abdullah Almasud](https://almasud.github.io)
