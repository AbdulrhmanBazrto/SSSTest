package bazrto.abdulrhman.ssstest.data

/**
 * @author Abd alrhman bazartwo
 * call back from the network layer
 */
interface OperationCallback<T> {
    fun onSuccess(data: List<T>?)
    fun onError(error: String?)
    fun onCancel()
}