package bazrto.abdulrhman.ssstest.model

import bazrto.abdulrhman.ssstest.data.OperationCallback

/**
 * @author Abd alrhman bazartwo
 */
class NewsRepository(private val newsDataSource: NewsDataSource) {

    fun fetchNews(callback: OperationCallback<News>) {
        newsDataSource.retrieveNews(callback)
    }

    fun cancel() {

    }
}