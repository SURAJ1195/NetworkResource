package com.example.mynetworkcall

import android.annotation.SuppressLint
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class HandlerImpl : HandlerInterface {
    /**
     * [handleNetworkResponse] handle the API response,
     * convert the dto response to domain response
     * extracting the error according to the error code
     * **/
    override fun <T, O> handleNetworkResponse(
        call: suspend () -> Response<T>, mapFun: (it: T) -> O
    ): Flow<NetworkResource<O>> {
        return flow {
            emit(NetworkResource.Loading(true))
            try {

                val response = call.invoke()
                val code = response.code()
                if (response.isSuccessful) {
                    val data = response.body()?.let { mapFun(it) }
                    emit(NetworkResource.Success(data))
                } else {
                    val errorBody = response.errorBody()!!.string()
                    try {
                        val jObjError = JSONObject(errorBody)
                        emit(NetworkResource.Error("Response Error", jObjError, code))
                    } catch (e: Exception) {
                        e.message?.let { emit(NetworkResource.Error(it, null, code)) }
                    }
                }
            } catch (e: IOException) {
                e.message?.let { emit(NetworkResource.Error(it)) }
            } catch (e: HttpException) {
                e.message?.let { emit(NetworkResource.Error(it)) }
            } catch (e: IllegalStateException) {
                e.message?.let { emit(NetworkResource.Error(it)) }
            } catch (e: NullPointerException) {
                e.message?.let { emit(NetworkResource.Error(it)) }
            } catch (e: JsonSyntaxException) {
                e.message?.let { emit(NetworkResource.Error(it)) }
            } catch (e: Exception) {
                e.message?.let { emit(NetworkResource.Error(it)) }
            }
            emit(NetworkResource.Loading(false))

        }
    }


    override fun <T> handleNetworkResponse(response: Response<T>): Flow<NetworkResource<T>> {
        return flow {
            emit(NetworkResource.Loading(isLoading = true))
            try {
                val code = response.code()
                if (response.isSuccessful) {
                    emit(NetworkResource.Success(response.body()))
                } else {
                    val errorBody = response.errorBody()?.string()
                    try {
                        val jObjError = errorBody?.let { JSONObject(it) }
                        emit(NetworkResource.Error("Network Error", jObjError, code))
                    } catch (e: Exception) {
                        emit(NetworkResource.Error("UNKNOWN ERROR", code = code))
                    }
                }
            } catch (e: IOException) {
                e.message?.let { emit(NetworkResource.Error(it)) }
            } catch (e: HttpException) {
                e.message?.let { emit(NetworkResource.Error(it)) }
            } catch (e: IllegalStateException) {
                e.message?.let { emit(NetworkResource.Error(it)) }
            } catch (e: NullPointerException) {
                e.message?.let { emit(NetworkResource.Error(it)) }
            } catch (e: JsonSyntaxException) {
                e.message?.let { emit(NetworkResource.Error(it)) }
            } catch (e: Exception) {
                e.message?.let { emit(NetworkResource.Error(it)) }
            }
            emit(NetworkResource.Loading(isLoading = false))

        }
    }


    /**
     * [handleFlow] takes the response from use case function as Resource<> with in Main Coroutine Scope
     * return the extracted response with in onLoading(),onFailure(),onSuccess()
     * Call within IO Scope
     * **/
    override suspend fun <T> handleFlow(
        flow: Flow<NetworkResource<T>>,
        onLoading: suspend (it: Boolean) -> Unit,
        onFailure: suspend (it: String, errorObject: JSONObject, code: Int) -> Unit,
        onSuccess: suspend (it: T) -> Unit
    ) {
        flow.collectLatest {
            when (it) {
                is NetworkResource.Error -> {
                    onFailure.invoke(it.message!!, it.errorObject!!, it.code!!)
                }

                is NetworkResource.Loading -> {
                    onLoading.invoke(it.isLoading)
                }

                is NetworkResource.Success -> {
                    onSuccess.invoke(it.data!!)
                }
            }

        }
    }


    @SuppressLint("LogNotTimber")
    override fun <T> handleNetworkCall(call: Call<T>): Flow<NetworkResource<T>> {
        var code: Int?
        return flow {
            emit(NetworkResource.Loading(isLoading = true))
            try {
                val apiCall = call.awaitHandler()
                if (apiCall.isSuccessful) {
                    code = apiCall.code()
                    val body = apiCall.body()
                    emit(NetworkResource.Success(body))
                } else {
                    val errorBody = apiCall.getJSONObject()
                    code = apiCall.code()
                    val message = apiCall.message()
                    emit(NetworkResource.Error(message, errorBody, code))
                }
            } catch (e: IOException) {
                e.message?.let { emit(NetworkResource.Error(it)) }
            } catch (e: HttpException) {
                val errorBody = e.response()?.getJSONObject()
                code = e.code()
                val message = e.message()
                emit(NetworkResource.Error(message, errorBody, code))
            } catch (e: Exception) {
                e.message?.let { emit(NetworkResource.Error(it)) }
            } catch (e: JsonSyntaxException) {
                e.message?.let { emit(NetworkResource.Error(it)) }
            }
            emit(NetworkResource.Loading(isLoading = false))

        }
    }
    internal suspend fun <T> Call<T>.awaitHandler(): Response<T> = suspendCoroutine { continuation ->
        val callback = object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                continuation.resume(response)
            }

            override fun onFailure(call: Call<T>, t: Throwable) = continuation.resumeWithException(t)
        }
        enqueue(callback)
    }

    internal fun Response<*>.getJSONObject(): JSONObject? {
        return try {
            errorBody()?.string()?.let { JSONObject(it) }
        } catch (exception: Exception) {
            null
        }

//cr velox
}
}