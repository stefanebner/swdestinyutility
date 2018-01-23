package sebner.dev.swdestinyutilitykotlin.data.network

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

/*
 * Utility class. Ceates URL to the SW Destiny DB website and does the okhttp request
 */
class NetworkUtils {
    private val setsUrl: String = "http://swdestinydb.com/api/public/sets/"
    private val cardsUrl: String = "http://swdestinydb.com/api/public/cards/"
    private val ext: String = ".json"

    fun getSetsUrl(): String {
        return setsUrl
    }

    fun getCardsUrl(setCode: String): String {
        return cardsUrl + setCode + ext
    }

    @Throws(IOException::class)
    fun getJsonResponse(url: String): String? {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        return response?.body()?.string()
    }
}