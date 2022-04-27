package bazrto.abdulrhman.ssstest.data

import bazrto.abdulrhman.ssstest.config.Const
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * @author Abd alrhman bazartwo
 */
object ApiClient {


    private var servicesApiInterface: ServicesApiInterface? = null

    fun build(): ServicesApiInterface? {
        var builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(Const.apiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())

        var httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor())
        httpClient.addInterceptor(Interceptor {
            val request = it.request()
            val url = request.url()
            val newUrl = url.newBuilder().addQueryParameter("api-key", Const.apiKey).build()
            val newRequest = request.newBuilder().url(newUrl).build()

            return@Interceptor it.proceed(newRequest)
        })

        var retrofit: Retrofit = builder.client(httpClient.build()).build()
        servicesApiInterface = retrofit.create(
            ServicesApiInterface::class.java
        )

        return servicesApiInterface as ServicesApiInterface
    }

    private fun interceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    interface ServicesApiInterface {

        @GET("/svc/search/v2/articlesearch.json?q=dubai&page=0")
        fun articles(): Call<ArticlesResponse>

    }
}