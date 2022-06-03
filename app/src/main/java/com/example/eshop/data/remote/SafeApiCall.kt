package com.example.eshop.data.remote

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.net.ssl.SSLException

sealed class ResultWrapper<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T? = null) : ResultWrapper<T>(data)
    class Loading<T>(data: T? = null) : ResultWrapper<T>(data)
    class Error<T>(message: String, data: T? = null) : ResultWrapper<T>(data, message)
}

suspend inline fun <T> requestFlow(
    dispatcher: CoroutineDispatcher,
    crossinline apiCall: suspend () -> Response<T>
): Flow<ResultWrapper<T>> {
    return flow {
        try {
            emit(ResultWrapper.Loading())
            val response = apiCall()
            if (response.isSuccessful && response.body() != null) {
                emit(ResultWrapper.Success(response.body()))
            }
        } catch (e: SSLException) {
            emit(ResultWrapper.Error("Please Check Your Internet"))
        } catch (e: IOException) {
            emit(ResultWrapper.Error("Please Check Your Internet"))
        } catch (e: HttpException) {
            emit(ResultWrapper.Error("Please Check Your Internet"))
        } catch (e: Throwable) {
            emit(ResultWrapper.Error("Please Check Your Internet"))
        }
    }
}