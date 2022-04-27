package bazrto.abdulrhman.ssstest.model

import bazrto.abdulrhman.ssstest.data.OperationCallback

/**
 * @author Abd alrhman bazartwo
 */
class NewsRepository(private val newsDataSource: NewsDataSource) {

    fun fetchNews(page: Int,callback: OperationCallback<News>) {
        newsDataSource.retrieveNews(page,callback)
    }

    fun cancel() {

    }
}