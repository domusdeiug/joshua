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

/**
 * Configuration for OpenRouter AI integration.
 *
 * To get started:
 * 1. Sign up at https://openrouter.ai and create an API key.
 * 2. Replace OPENROUTER_API_KEY below with your actual key.
 * 3. Optionally change MODEL_ID to any model available on OpenRouter.
 *    Browse models at https://openrouter.ai/models
 */
object OpenRouterConfig {

    // -------------------------------------------------------------------------
    // REQUIRED: paste your OpenRouter API key here
    // -------------------------------------------------------------------------
    const val OPENROUTER_API_KEY: String = me.xizzhu.android.joshua.BuildConfig.OPENROUTER_API_KEY

    // -------------------------------------------------------------------------
    // Model selection
    // Some good options:
    //   "mistralai/mistral-7b-instruct"      – fast, free tier available
    //   "google/gemma-3-12b-it:free"         – free, solid quality
    //   "anthropic/claude-haiku-4-5"         – fast Anthropic model
    //   "openai/gpt-4o-mini"                 – OpenAI lightweight model
    // -------------------------------------------------------------------------
    const val MODEL_ID: String = "openrouter/free"

    // -------------------------------------------------------------------------
    // API settings (safe defaults — change only if you know what you're doing)
    // -------------------------------------------------------------------------
    const val API_BASE_URL: String = "https://openrouter.ai/api/v1/chat/completions"

    /** Maximum tokens the model may generate in its response. */
    const val MAX_TOKENS: Int = 512

    /**
     * Temperature controls creativity/randomness (0.0 = deterministic, 1.0 = creative).
     * 0.7 is a good balance for explanatory text.
     */
    const val TEMPERATURE: Double = 0.5

    // -------------------------------------------------------------------------
    // Prompt template — feel free to adjust the tone or language
    // -------------------------------------------------------------------------
    fun buildPrompt(verseReference: String, verseText: String): String = """
        You are a knowledgeable and pastoral Bible commentary assistant.
        
        Provide an insightful explanation of the following Bible verse.
        Cover the historical/cultural context and the etymology of any key words or phrases.
        Then give an exegetical explanation of the verse from different traditions and theologies. 
        The key here is to give the reader an all-round explanation of the verse and all the information they need
        to make their own conclusions.
        
        Verse: $verseReference
        Text: "$verseText"
    """.trimIndent()
}
