package com.medicao0102.vidaflow2.data

import android.content.Context
import android.util.Log
import androidx.compose.runtime.remember
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

sealed class UiState<out T>() {
  data class Success<out T>(val result: T) : UiState<T>()
  data class Error(val e: Exception, val errorResponse: ErrorResponse) : UiState<Nothing>()
  object Loading : UiState<Nothing>()
}

class ApiService(
  private val ctx: Context
) {

  val BASE_URL = "http://10.0.2.2:5000/v1"

  val bearerTokensList = mutableListOf<BearerTokens>(BearerTokens("", ""))

  val client = HttpClient(Android) {
    install(ContentNegotiation) {
      json(
        Json {
          ignoreUnknownKeys = true
        }
      )
    }
    install(Auth) {
      bearer {
        loadTokens { bearerTokensList.last() }
        refreshTokens { null }
      }
    }
  }


  suspend fun isStatusAvailable(): Boolean {
    try {
      val response = client.get("$BASE_URL/status")
      Log.d("status-response", response.status.value.toString())
      val result = response.body<StatusResponse>()
      Log.d("status-result", result.toString())
      return response.status.value == 200


    } catch (e: Exception) {
      e.printStackTrace()
      return false
    }
  }

  suspend fun login(request: LoginRequest): UiState<LoginResponse> {
    try {
      val response = client.post("$BASE_URL/login") {
        contentType(ContentType.Application.Json)
        setBody(request)
      }
      when (response.status.value) {
        200 -> {
          return UiState.Success(response.body<LoginResponse>())
        }

        else -> {
          try {
            return UiState.Error(Exception("Desconhecido"), response.body<ErrorResponse>())
          } catch (e1: Exception) {
            return UiState.Error(
              e1,
              ErrorResponse("Requisição retornoou um erro deconhecido!", "unknow")
            )
          }
        }
      }

    } catch (e: Exception) {
      e.printStackTrace()
      return UiState.Error(e, ErrorResponse("Erro desconhecido", "unknow"))
    }

  }




  suspend fun register(request: RegisterRequest): UiState<RegisterResponse> {
    try {
      val response = client.post("$BASE_URL/users") {
        contentType(ContentType.Application.Json)
        setBody(request)
      }
      when (response.status.value) {
        201 -> {
          return UiState.Success(response.body<RegisterResponse>())
        }

        else -> {
          try {
            return UiState.Error(Exception("Desconhecido"), response.body<ErrorResponse>())
          } catch (e1: Exception) {
            return UiState.Error(
              e1,
              ErrorResponse("Requisição retornoou um erro deconhecido!", "unknow")
            )
          }
        }
      }

    } catch (e: Exception) {
      e.printStackTrace()
      return UiState.Error(e, ErrorResponse("Erro desconhecido", "unknow"))
    }

  }

}