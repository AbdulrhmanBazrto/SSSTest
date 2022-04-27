package bazrto.abdulrhman.ssstest.model

import bazrto.abdulrhman.ssstest.data.ArticlesResponse
import bazrto.abdulrhman.ssstest.data.OperationCallback

/**
 * @author Abd alrhman bazartwo
 */
class NewsRepository(private val newsDataSource: NewsDataSource) {

    suspend fun fetchNews(page: Int,callback: OperationCallback<News>) {
        newsDataSource.retrieveNews(page,callback)
    }

    fun cancel() {
        newsDataSource.cancel()
    }
}