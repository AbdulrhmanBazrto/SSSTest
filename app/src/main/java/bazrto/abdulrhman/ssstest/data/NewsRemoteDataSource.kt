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

    private val service = apiClient.build()

    override suspend fun retrieveNews(page: Int, callback: OperationCallback<News>) {

        val apiResponse: ArticlesResponse? = service?.articles("dubai", page)
        if (apiResponse != null && apiResponse.isSuccess()) {
            var response = apiResponse.response
            callback.onSuccess(response?.docs)
        } else {
            callback.onError(apiResponse.toString())
        }
    }
}