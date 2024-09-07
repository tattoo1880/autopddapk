package com.example.autopddand
import okhttp3.OkHttpClient
import com.google.gson.JsonObject
import org.json.JSONObject

class GetSms {

    val serviceId = "1368"
    val countryId = "5"
    val apiKey = "9UjknRBJgCHcfJYICoNRpOCAUjyJFhKe"
    val url = "https://api.sms-man.com/control/get-number?token=" + apiKey + "&country_id=" + countryId + "&application_id=" + serviceId
     fun getNumber(): JSONObject {
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
         val cc = JSONObject(json.toString())
         println(cc)
         return cc


//        return body
    }

}