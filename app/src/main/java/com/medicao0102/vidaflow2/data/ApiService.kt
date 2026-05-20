package com.medicao0102.vidaflow2.data

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.res.integerResource
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
import androidx.core.content.edit
import io.ktor.client.plugins.auth.authProviders
import io.ktor.client.plugins.auth.clearAuthTokens
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import java.io.PushbackInputStream

sealed class UiState<out T>() {
  data class Success<out T>(val result: T) : UiState<T>()
  data class Error(val e: Exception, val errorResponse: ErrorResponse) : UiState<Nothing>()
  object Loading : UiState<Nothing>()
}

class ApiService(
  private val ctx: Context
) {

  val BASE_URL = "http://10.0.2.2:5000/v1"

  val sp = ctx.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

  val bearerTokensStorage =
    mutableListOf(BearerTokens(getLoginResponse()?.token ?: "", ""))

  val client = HttpClient(Android) {
    install(ContentNegotiation) {
      json(
        Json {
          ignoreUnknownKeys = true
        })
    }
    install(Auth) {
      bearer {
        loadTokens { bearerTokensStorage.last() }
        refreshTokens { null }
      }
    }
  }


  fun getHabitStatusList(): List<HabitStatus>? {
    try {
      val list = sp.getString("habits_status_list", null)
        ?.let { Json.decodeFromString<List<HabitStatus>>(it) }
      return list
    } catch (e: Exception) {
      return null
    }
  }

  fun getHabitStatus(id: Int): HabitStatus? {
    return getHabitStatusList()?.find { it.habitId == id }
  }

  fun putHabitSatus(newHabitStatus: HabitStatus) {
    val oldHabitStatus = getHabitStatus(newHabitStatus.habitId)
    val list = mutableListOf<HabitStatus>()
    getHabitStatusList()?.let {
      list.addAll(getHabitStatusList()!!)
    }
    list.removeIf { it.habitId == newHabitStatus.habitId }
    list.add(newHabitStatus)
    sp.edit {
      putString(
        "habits_status_list",
        Json.encodeToString(
          list
        )
      )
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


  fun updateLoginResponse(loginResponse: LoginResponse) {
    sp.edit {
      putString(
        "login_response", Json.encodeToString(loginResponse)
      )
    }
  }

  fun getLoginResponse(): LoginResponse? {
    return try {
      sp.getString("login_response", null)?.let { Json.decodeFromString(it) }
    } catch (e: Exception) {
      null
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
          client.clearAuthTokens()
          bearerTokensStorage.add(BearerTokens(response.body<LoginResponse>().token, ""))
          updateLoginResponse(response.body<LoginResponse>())
          return UiState.Success(response.body<LoginResponse>())
        }

        else -> {
          try {
            return UiState.Error(
              Exception("Desconhecido"), response.body<ErrorResponse>()
            )
          } catch (e1: Exception) {
            Log.d("ola", e1.toString())
            return UiState.Error(
              e1, ErrorResponse("Requisição retornoou um erro deconhecido!", "unknow")
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
            return UiState.Error(
              Exception("Desconhecido"), response.body<ErrorResponse>()
            )
          } catch (e1: Exception) {
            return UiState.Error(
              e1, ErrorResponse("Requisição retornoou um erro deconhecido!", "unknow")
            )
          }
        }
      }

    } catch (e: Exception) {
      e.printStackTrace()
      return UiState.Error(e, ErrorResponse("Erro desconhecido", "unknow"))
    }

  }


  suspend fun getTip(): UiState<TipResponse> {
    try {
      val response = client.get("$BASE_URL/dica")

      Log.d("getTip", response.toString())
      when (response.status.value) {
        200 -> {
          val result = UiState.Success(response.body<TipResponse>())
          return result
        }

        401 -> {
          val result = response.body<ErrorResponse>()
          return UiState.Error(Exception("Token inválido"), result)
        }

        else -> {
          return UiState.Error(
            Exception("Error desconhecido"),
            ErrorResponse("Erro desconhecido", "unknow")
          )
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
      return UiState.Error(e, ErrorResponse("Erro desconhecido", "unknow"))
    }
  }

  suspend fun getHabit(userId: String): UiState<List<HabitResponse>> {
    try {

      val response = client.get("$BASE_URL/habits/$userId")

      Log.d("habit", response.toString())
      Log.d("habit2", response.bodyAsText())
      when (response.status.value) {
        200 -> {
          val result = UiState.Success(response.body<List<HabitResponse>>())
          return result
        }

        401 -> {
          val result = response.body<ErrorResponse>()
          return UiState.Error(Exception("Token inválido"), result)
        }

        404 -> {
          val result = response.body<ErrorResponse>()
          return UiState.Error(Exception("Não encontrado"), result)
        }

        else -> {
          return UiState.Error(
            Exception("Error desconhecido"),
            ErrorResponse("Erro desconhecido", "unknow")
          )
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
      return UiState.Error(e, ErrorResponse("Erro desconhecido", "unknow"))
    }
  }

}