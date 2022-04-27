package bazrto.abdulrhman.ssstest.model

import bazrto.abdulrhman.ssstest.data.OperationCallback

/**
 * @author Abd alrhman bazartwo
 */
interface NewsDataSource {

    fun retrieveNews(callback: OperationCallback<News>)
}