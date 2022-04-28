package bazrto.abdulrhman.ssstest.data

/**
 * @author Abd alrhman bazartwo
 */
interface OperationCallback<T> {
    fun onSuccess(data: List<T>?)
    fun onError(error: String?)
    fun onCancel()
}