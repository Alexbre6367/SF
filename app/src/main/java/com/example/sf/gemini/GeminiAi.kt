package com.example.sf.gemini


import android.graphics.Bitmap
import android.util.Log
import com.example.sf.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content

object GeminiAi {
    private val model = GenerativeModel(
        modelName = "gemini-2.5-flash-lite",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    suspend fun analyzeIMG(
        imageBitmap: Bitmap,
        promptText: String = """Твоя задача по фото понять что за еда и выдать информацию о ней, 
                включая КБЖУ. Ответ должен быть строгим без слов "Название еды",
                сразу же название и кбжу по порядку, перед и после кбжу ничего не пиши, просто 4 числа без на каждое отдельная строчка,
                не должно быть пустых строк, всё по порядку, первая строка название, вторая калорие и т.д. Если число не целое, 
                пиши через точку(пример 2.6, 1.5)"""
    ): String? {
        Log.d("MyLog", "Начало анализа: $promptText")
        return try {
            val inputContent = content {
                image(imageBitmap)
                text(promptText)
            }

            val response = model.generateContent(inputContent)
            val analysisResult = response.text
            Log.d("MyLog", "Анализ завершен: $analysisResult")
            analysisResult
        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка анализа: ${e.message}", e)
            null
        }
    }

    suspend fun analyzeFood(textFood: String): String? {
        Log.d("MyLog", "Начало анализа: $textFood")
        return try {
            val response = model.generateContent(
                content {
                    text(
                        """
                            Твоя задача по названию продукта выявить БЖУ,
                            ответом от тебя должно быть БЖУ без лишних слов,
                            то есть первая значение это Белок и ты должен указать лишь 5(условно) 
                            и другие значения через пробел без слов грамм или г, просто значения
                            Продукт: $textFood
                        """.trimIndent()
                    )
                }
            )

            val analysisFoodResult = response.text // Извлекаем текст из ответа.
            Log.d("MyLog", "Анализ завершен: $analysisFoodResult")
            analysisFoodResult
        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка анализа: ${e.message}", e)
            null
        }
    }

    suspend fun analyzeMeal(textMeal: String): String? {
        Log.d("MyLog", "Начало анализа: $textMeal")
        return try {
            val response = model.generateContent(
                content {
                    text(
                        """
                            Твоя задача проанализировать приём пищи,
                            выдать БЖУ без лишних слов, три числа по порядку через пробел
                            Приём пищи: $textMeal
                        """.trimIndent()
                    )
                }
            )

            val analysisMealResult = response.text
            Log.d("MyLog", "Анализ завершен: $analysisMealResult")
            analysisMealResult
        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка анализа: ${e.message}", e)
            null
        }
    }
}