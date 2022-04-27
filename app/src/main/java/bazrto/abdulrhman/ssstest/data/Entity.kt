package bazrto.abdulrhman.ssstest.data

import bazrto.abdulrhman.ssstest.model.Response

/**
 * @author Abd alrhman bazartwo
 */
data class ArticlesResponse(val status: String?, val copyright: String?, val response: Response?) {
    fun isSuccess(): Boolean = (status == "OK")
}