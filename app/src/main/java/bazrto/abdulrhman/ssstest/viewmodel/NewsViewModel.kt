package bazrto.abdulrhman.ssstest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bazrto.abdulrhman.ssstest.data.ArticlesResponse
import bazrto.abdulrhman.ssstest.data.OperationCallback
import bazrto.abdulrhman.ssstest.model.News
import bazrto.abdulrhman.ssstest.model.NewsRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * @author Abd alrhman bazartwo
 */
class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    // page number to load
    var page: Int = 0
    // data loaded from API
    private val _news = MutableLiveData<List<News>>().apply { value = emptyList() }
    val news: LiveData<List<News>> = _news

    val isLastPage = MutableLiveData<Boolean>()

    val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // error message to be displayed
    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> = _onMessageError

    private val _isEmptyList = MutableLiveData<Boolean>()
    val isEmptyList: LiveData<Boolean> = _isEmptyList

    var job: Job? = null

    // coroutine exception handler to get the error message that happened in coroutine call
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        run {
            _isLoading.postValue(false)
            _onMessageError.postValue(throwable.localizedMessage)
        }
    }

    init {
        // load news
        loadNews()
    }

    // increase page number and load next page
    internal fun loadNextPage() {
        page++
        loadNews()
    }

    fun loadNews() {
        _isLoading.value = true
        job = viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            repository.fetchNews(page, object : OperationCallback<News> {
                override fun onError(error: String?) {
                    _isEmptyList.postValue(true)
                    _isLoading.postValue(false)
                    _onMessageError.postValue(error.toString())
                }

                override fun onSuccess(data: List<News>?) {
                    _isLoading.postValue(false)
                    if (data.isNullOrEmpty()) {
                        _isEmptyList.postValue(true)
                        isLastPage.postValue(true)

                    } else {
                        _news.postValue(data)
                        isLastPage.postValue(false)
                    }
                }

                override fun onCancel() {
                    _isEmptyList.postValue(true)
                    _isLoading.postValue(false)
                    _onMessageError.postValue("request cancelled")
                }
            })
        }

    }

    // stop the coroutine if the view model is stopped
    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}