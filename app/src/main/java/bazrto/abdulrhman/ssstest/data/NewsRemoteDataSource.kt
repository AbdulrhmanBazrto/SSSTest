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

    override fun retrieveNews(callback: OperationCallback<News>) {

        call = service?.articles()
        call?.enqueue(object : Callback<ArticlesResponse> {
            override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                callback.onError(t.message)
            }

            override fun onResponse(
                call: Call<ArticlesResponse>,
                response: Response<ArticlesResponse>
            ) {
                if (response.code() == 200) {
                    response.body()?.let {
                        if (response.isSuccessful && (it.isSuccess())) {
                            callback.onSuccess(it.response?.docs)
                        } else {
                            callback.onError("un expected response")
                        }
                    }
                }else{
                    callback.onError(response.errorBody()?.string())
                }
            }
        })
    }

}