/*
 * Copyright (C) 2023 Xizhi Zhu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.xizzhu.android.joshua.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object OpenRouterClient {

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Calls the OpenRouter chat completions API and returns the model's text response.
     *
     * @param verseReference  Human-readable reference, e.g. "John 3:16"
     * @param verseText       The actual verse text
     * @return The AI explanation string
     * @throws Exception on network or API error
     */
    suspend fun explain(verseReference: String, verseText: String): String =
        withContext(Dispatchers.IO) {
            val prompt = OpenRouterConfig.buildPrompt(verseReference, verseText)

            val body = JSONObject().apply {
                put("model", OpenRouterConfig.MODEL_ID)
                put("max_tokens", OpenRouterConfig.MAX_TOKENS)
                put("temperature", OpenRouterConfig.TEMPERATURE)
                put("messages", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("content", prompt)
                    })
                })
            }.toString()

            val request = Request.Builder()
                .url(OpenRouterConfig.API_BASE_URL)
                .addHeader("Authorization", "Bearer ${OpenRouterConfig.OPENROUTER_API_KEY}")
                .addHeader("Content-Type", "application/json")
                .addHeader("HTTP-Referer", "me.xizzhu.android.joshua") // optional but good practice
                .post(body.toRequestBody("application/json".toMediaType()))
                .build()

            val response = httpClient.newCall(request).execute()
            val responseBody = response.body?.string()
                ?: throw Exception("Empty response from OpenRouter")

            if (!response.isSuccessful) {
                throw Exception("OpenRouter API error ${response.code}: $responseBody")
            }

            // Parse: choices[0].message.content
            JSONObject(responseBody)
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content")
                .trim()
        }
}
