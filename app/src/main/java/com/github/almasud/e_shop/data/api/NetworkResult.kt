package com.github.almasud.e_shop.data.api

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.exception.ApolloHttpException

sealed class NetworkResult<T : Any> {
    class Success<T: Any>(val data: T) : NetworkResult<T>()
    class Error<T: Any>(val code: Int, val message: String?) : NetworkResult<T>()
    class Exception<T: Any>(val e: Throwable) : NetworkResult<T>()
    class Loading<T: Any> : NetworkResult<T>()

    companion object {
        suspend fun <T : Operation.Data> handleGraphQLApi(
            execute: suspend () -> ApolloResponse<T>
        ): NetworkResult<T> {
            return try {
                val response = execute()
                val data = response.data
                if (!response.hasErrors() && data != null) {
                    Success(data)
                } else {
                    Error(code = response.errors?.get(0).hashCode(), message = response.errors?.get(0)?.message)
                }
            } catch (e: ApolloHttpException) {
                Error(code = e.statusCode, message = e.message)
            } catch (e: Throwable) {
                Exception(e)
            }
        }
    }
}
