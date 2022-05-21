package com.example.eshop.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.net.ssl.SSLException

sealed class Result<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T? = null) : Result<T>(data)
    class Loading<T>(data: T? = null) : Result<T>(data)
    class Error<T>(message: String, data: T? = null) : Result<T>(data, message)
}

suspend inline fun <T> requestFlow(
    dispatcher: CoroutineDispatcher,
    crossinline apiCall: suspend () -> Response<T>
): Flow<Result<T>> {
    return flow {
        try {
            emit(Result.Loading())
            val response = apiCall()
            if (response.isSuccessful && response.body() != null) {
                emit(Result.Success(response.body()))
            }
        } catch (e: SSLException) {
            emit(Result.Error("Please Check Your Internet"))
        } catch (e: IOException) {
            emit(Result.Error("Please Check Your Internet"))
        } catch (e: HttpException) {
            emit(Result.Error("Please Check Your Internet"))
        } catch (e: Throwable) {
            emit(Result.Error("Please Check Your Internet"))
        }
    }
}