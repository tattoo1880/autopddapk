package com.example.autopddand

import okhttp3.OkHttpClient
import org.junit.Test
import com.google.gson.JsonObject

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    val serviceId = "1368"
    val countryId = "5"
    val apiKey = "9UjknRBJgCHcfJYICoNRpOCAUjyJFhKe"
    val url = "https://api.sms-man.com/control/get-number?token=" + apiKey + "&country_id=" + countryId + "&application_id=" + serviceId
    @Test
    fun getNumber(){
        val client = OkHttpClient()
        val request = okhttp3.Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val body = response.body?.string()
        val jsonObject = JsonObject()
        val jsonParser = com.google.gson.JsonParser()
        val jsonElement = jsonParser.parse(body)
        val json = jsonElement.asJsonObject
        val number = json.get("number").asString
        val request_id = json.get("request_id").asString
        println("number:$number")
        println("request_id:$request_id")
    }
}