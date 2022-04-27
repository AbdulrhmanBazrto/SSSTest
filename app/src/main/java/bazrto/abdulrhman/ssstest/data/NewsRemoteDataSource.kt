package bazrto.abdulrhman.ssstest.data

import bazrto.abdulrhman.ssstest.model.News
import bazrto.abdulrhman.ssstest.model.NewsDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author Abd alrhman bazartwo
 */
class NewsRemoteDataSource(apiClient: ApiClient) : NewsDataSource {

    private var call: Call<ArticlesResponse>? = null
    private val service = apiClient.build()

    override suspend fun retrieveNews(page: Int, callback: OperationCallback<News>) {

        val apiResponse: ArticlesResponse? = service?.articles("dubai", page)
        if (apiResponse != null && apiResponse.isSuccess()) {
            var response = apiResponse.response
            callback.onSuccess(response?.docs)
        } else {
            callback.onError(apiResponse.toString())
        }

        call?.enqueue(object : Callback<ArticlesResponse> {
            override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                callback.onError(t.message)
            }

            override fun onResponse(
                call: Call<ArticlesResponse>,
                response: Response<ArticlesResponse>
            ) {

            }
        })
    }

    override fun cancel() {
        call?.cancel()
    }
}