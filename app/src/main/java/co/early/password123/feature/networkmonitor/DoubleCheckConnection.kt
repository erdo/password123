package co.early.password123.feature.networkmonitor

import co.early.asaf.core.logging.AndroidLogger
import co.early.asaf.core.logging.Logger
import co.early.asaf.retrofit.InterceptorLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class DoubleCheckConnection {

    @JvmOverloads
    fun canWeConnect(logger: Logger = AndroidLogger()): Boolean {

        val client = OkHttpClient().newBuilder()
                .addInterceptor(InterceptorLogging(logger)).build()

        val request = Request.Builder().url("https://google.com").head().build()
        var response: Response? = null
        try {
            response = client.newCall(request).execute()
            return response!!.isSuccessful
        } catch (e: IOException) {
            return false
        } finally {
            if (response != null) {
                response.body()!!.close()
            }
        }
    }

    companion object {
        private val TAG = DoubleCheckConnection::class.java.simpleName
    }

}
