package com.example.autopddand
import android.app.admin.TargetUser
import okhttp3.OkHttpClient
import com.google.gson.JsonObject
import org.json.JSONObject
import android.util.Log

class GetSms {

    val TAG = "AutoPddAnd"

    val serviceId = "1368"
    val countryId = "5"
    val apiKey = "9UjknRBJgCHcfJYICoNRpOCAUjyJFhKe"
    val url = "https://api.sms-man.com/control/get-number?token=" + apiKey + "&country_id=" + countryId + "&application_id=" + serviceId
     fun getNumber(): JSONObject {
        val client = OkHttpClient()
        val request = okhttp3.Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val body = response.body?.string()
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
    }

    fun getSms(cc :JSONObject):String {
        val requestid = cc.get("request_id").toString()
        val requestUrl =
            "https://api.sms-man.com/control/get-sms?token=$apiKey&request_id=$requestid"
        val client = OkHttpClient()
        val request = okhttp3.Request.Builder().url(requestUrl).build()
        val response = client.newCall(request).execute()
        val body = response.body?.string()
        val jsonParser = com.google.gson.JsonParser()
        val jsonElement = jsonParser.parse(body)
        val json = jsonElement.asJsonObject
        println(json)
        val sms_code = json.get("sms_code").asString
        println("sms_code:$sms_code")
        return sms_code

    }

    fun getCount(user_agent:String):Int{
        //!使用okhttp3获取远端数据
        val baseurl= "http://65.49.233.120:8082/api/allpdd"
        val client = okhttp3.OkHttpClient()
        val request = okhttp3.Request.Builder().url(baseurl).build()
        try {
            val response = client.newCall(request).execute()
            val body = response.body?.string()
            val jsonParser = com.google.gson.JsonParser()
            val jsonElement = jsonParser.parse(body)
            val json = jsonElement.asJsonArray
            var count = 0
            //遍历json数组,如果其中有任何一个元素的value等于user_agent,则返回这个元素的count
            for (i in 0 until json.size()){
                val jsonObject = json.get(i).asJsonObject
                val ua = jsonObject.get("userAgent").asString
                Log.d(TAG, "ua:$ua")
                if (user_agent in ua){
                    count = jsonObject.get("count").asInt
                }
            }
            return count
        }catch (e:Exception){
            e.printStackTrace()
            return 0
        }
    }

}