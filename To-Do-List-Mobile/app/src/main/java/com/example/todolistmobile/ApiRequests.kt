package com.example.todolistmobile

import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException

class ApiRequests {
    private val BASE_URL = "http://10.0.2.2:8000/api/tasks/" // Замени на URL своего API
    private val client = OkHttpClient()
    private val gson = Gson()

    // Метод для получения задач (GET запрос)
    fun getTasks(callback: ApiCallback) {
        val request = Request.Builder()
            .url(BASE_URL)
            .header("Accept", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    responseData?.let {
                        // Парсинг JSON в список задач

                        val tasks: Array<Task> = gson.fromJson(it, Array<Task>::class.java)
                        callback.onSuccess(ArrayList(tasks.toList()))
                    }
                } else {
                    callback.onError(IOException("Unexpected code ${response.code}"))
                }
            }
        })
    }

    // Метод для создания новой задачи (POST запрос)
    fun createTasks(tasks: ArrayList<Task>, callback: ApiCallback) {
        val JSON = "application/json; charset=utf-8".toMediaType()
        val jsonString = gson.toJson(tasks)

        val body = RequestBody.create(JSON, jsonString)
        val request = Request.Builder()
            .url(BASE_URL+"load_tasks/")
            .post(body)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    callback.onSuccess(ArrayList())

                }
                else {
                    callback.onError(IOException("Unexpected code ${response.code}"))
                }
            }
        })
    }


    // Интерфейс для обработки результатов
    interface ApiCallback {
        fun onSuccess(tasks: ArrayList<Task>)
        fun onError(e: Exception)
    }
}
