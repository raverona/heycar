package com.heycar.codingchallenge.controller

import io.kotlintest.shouldBe
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, statements = ["TRUNCATE TABLE `listings`;"])
class ListingsControllerTest {

    @LocalServerPort
    private val port: Int = 0

    private val okHttpClient = OkHttpClient()

    @Test
    fun `should return json of successfully added listings when post json endpoint is called`() {
        val dealerId = "rafael"
        val json = """
            [
                { 
                  "make": "makeTest",
                  "model": "modelTest",
                  "kw": "2",
                  "year": "2019",
                  "color": "ColorTest",
                  "price": "2000000", 
                  "code": "codeTest222"
                }, 
                { 
                  "make": "makeTest",
                  "model": "modelTest",
                  "kw": "2",
                  "year": "2019",
                  "color": "ColorTest",
                  "price": "2000000", 
                  "code": "codeTest233"
                }
            ]
        """.trimIndent()
        val body = RequestBody.create(MediaType.get("application/json"), json)
        val request = Request.Builder()
                .url("http://localhost:$port/vehicle_listings/$dealerId")
                .post(body)
                .build()
        val response = okHttpClient.newCall(request).execute()

        response.body()!!.string().shouldBeJsonOf(json.withDealerId(dealerId))
    }

    @Test
    fun `should return json of successfully added listings when post csv file endpoint is called`() {
        val dealerId = "rafael"
        val csv = """
            code,make/model,power-in-ps,year,color,price
            1,mercedes/a 180,123,2014,black,15950
            2,audi/a3,111,2016,white,17210
            3,vw/golf,86,2018,green,14980
            4,skoda/octavia,86,2018,not-specified,16990
        """.trimIndent()
        val body = RequestBody.create(MediaType.get("text/plain"), csv)
        val request = Request.Builder()
                .url("http://localhost:$port/upload_csv/$dealerId")
                .post(body)
                .build()
        val response = okHttpClient.newCall(request).execute()

        response.body()!!.string().shouldBeJsonOf(csv.toJson().withDealerId(dealerId))
    }

    @Test
    fun `should return json of all listings if no criteria is specified when getting the search endpoint`() {
        val dealerId = "rafael"
        val data = """
            code,make/model,power-in-ps,year,color,price
            1,mercedes/a 180,123,2014,black,15950
            2,audi/a3,111,2016,white,17210
            3,vw/golf,86,2018,green,14980
            4,skoda/octavia,86,2018,not-specified,16990
        """.trimIndent()
        insertData(data, dealerId)
        val request = Request.Builder()
                .url("http://localhost:$port/search")
                .build()
        val response = okHttpClient.newCall(request).execute()

        response.body()!!.string().shouldBeJsonOf(data.toJson().withDealerId(dealerId))
    }

    @Test
    fun `should return json of all listings matching any of the specified criteria when getting the search endpoint`() {
        val dealerId = "rafaelv"
        val data = """
            code,make/model,power-in-ps,year,color,price
            1,mercedes/a 180,123,2014,black,15950
            2,audi/a3,111,2016,white,17210
            3,vw/golf,86,2018,green,14980
            4,skoda/octavia,86,2018,not-specified,16990
        """.trimIndent()
        insertData(data, dealerId)
        val request = Request.Builder()
                .url(HttpUrl.Builder()
                        .scheme("http")
                        .host("localhost")
                        .port(port)
                        .addPathSegment("search")
                        .addQueryParameter("model", "a 180")
                        .addQueryParameter("make", "vw")
                        .build())
                .build()
        val response = okHttpClient.newCall(request).execute()

        response.body()!!.string().shouldBeJsonOf(data.toJson().minusDifferentFrom("model=a 180", "make=vw").withDealerId(dealerId))
    }

    private fun insertData(data: String, dealerId: String) {
        val body = RequestBody.create(MediaType.get("text/plain"), data)
        val request = Request.Builder()
                .url("http://localhost:$port/upload_csv/$dealerId")
                .post(body)
                .build()
        okHttpClient.newCall(request).execute()
    }
}

private fun String.minusDifferentFrom(vararg criteria: String): String {
    val stringAsJsonArray = JSONArray(this).toList()
    val filteredJson = stringAsJsonArray.filter {
        it.containsCriteria(*criteria)
    }
    return filteredJson.toString()
}

private fun JSONObject.containsCriteria(vararg keysAndValues: String): Boolean {
    for (keyAndValue in keysAndValues) {
        val (key, value) = keyAndValue.split("=")
        if (this.getString(key) == value) return true
    }
    return false
}

private fun String.toJson(): String {
    val csvLines = this.split("\n").toMutableList()
    val header = csvLines.removeAt(0).split(",", "/")
    val values = csvLines.map { lines -> lines.split(",", "/") }

    val valuesAsJsonObjects = values.map { listing ->
        JSONObject()
                .put("code", listing[header.indexOf("code")])
                .put("make", listing[header.indexOf("make")])
                .put("model", listing[header.indexOf("model")])
                .put("power", listing[header.indexOf("power-in-ps")])
                .put("year", listing[header.indexOf("year")])
                .put("color", listing[header.indexOf("color")])
                .put("price", listing[header.indexOf("price")])
    }

    return JSONArray(valuesAsJsonObjects).toString()
}

private fun String.shouldBeJsonOf(json: String) {
    val generatedJsonArray = JSONArray(this)
    val expectedJsonArray = JSONArray(json)

    generatedJsonArray.length() shouldBe expectedJsonArray.length()

    for (i in 0 until expectedJsonArray.length()) {
        val generatedJsonObject = generatedJsonArray.getJSONObject(i)
        val expectedJsonObject = expectedJsonArray.toList().first { jsonObject -> jsonObject.getString("dealerId") == generatedJsonObject.getJSONObject("id").getString("dealerId") && jsonObject.getString("code") == generatedJsonObject.getJSONObject("id").getString("code") }

        generatedJsonObject.getString("color") shouldBe expectedJsonObject.getString("color")
        generatedJsonObject.getString("year") shouldBe expectedJsonObject.getString("year")
        generatedJsonObject.getString("price") shouldBe expectedJsonObject.getString("price")
        generatedJsonObject.getString("model") shouldBe expectedJsonObject.getString("model")
        generatedJsonObject.getString("make") shouldBe expectedJsonObject.getString("make")
        generatedJsonObject.optString("kw").also {
            if (!it.equals("null"))
               it shouldBe expectedJsonObject.optString ("kw")
        }
        generatedJsonObject.optString("power").also {
            if (!it.equals("null"))
                it shouldBe expectedJsonObject.optString("power") }
    }
}

private fun String.withDealerId(dealerId: String): String {
    val stringAsJsonArray = JSONArray(this)
    for (i in 0 until stringAsJsonArray.length()) {
        stringAsJsonArray.getJSONObject(i).put("dealerId", dealerId)
    }
    return stringAsJsonArray.toString()
}

private fun JSONArray.toList(): List<JSONObject> {
    val listOfJsonObjects = mutableListOf<JSONObject>()
    for (i in 0 until this.length()) {
        listOfJsonObjects.add(this.getJSONObject(i))
    }
    return listOfJsonObjects
}