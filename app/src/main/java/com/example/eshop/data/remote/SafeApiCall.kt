package com.example.eshop.data.remote

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.net.ssl.SSLException

sealed class ResultWrapper<out T> {
    data class Success<out T>(val data: T) : ResultWrapper<T>()
    data class Error<out T>(val message: String?) : ResultWrapper<T>()
    object Loading : ResultWrapper<Nothing>()
}

suspend inline fun <T> requestFlow(
    dispatcher: CoroutineDispatcher,
    crossinline apiCall: suspend () ->Response<T>
) = flow {
    emit(ResultWrapper.Loading)
    try {
        val response=apiCall()
        val responseBody=response.body()
        if (response.isSuccessful && responseBody!=null) {
            emit(ResultWrapper.Success(responseBody))
        }else{
            val errorBody=response.errorBody()
            if (errorBody!=null){

            }else{
                emit(ResultWrapper.Error<T>("error is here for you to smile"))
            }
        }
    } catch (e:SSLException) {
        emit(ResultWrapper.Error(e.message))
    }catch (e:IOException){
        emit(ResultWrapper.Error(e.message))
    }catch (e:HttpException){
        emit(ResultWrapper.Error(e.message))
    } catch (e:Throwable){
        emit(ResultWrapper.Error(e.message))
    }finally {

    }
}