package bazrto.abdulrhman.ssstest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bazrto.abdulrhman.ssstest.data.OperationCallback
import bazrto.abdulrhman.ssstest.model.News
import bazrto.abdulrhman.ssstest.model.NewsRepository

/**
 * @author Abd alrhman bazartwo
 */
class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _news = MutableLiveData<List<News>>().apply { value = emptyList() }
    val news: LiveData<List<News>> = _news

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> = _isViewLoading

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> = _onMessageError

    private val _isEmptyList = MutableLiveData<Boolean>()
    val isEmptyList: LiveData<Boolean> = _isEmptyList

    init {
        loadNews()
    }

    private fun loadNews() {
        _isViewLoading.value = true
        repository.fetchNews(object : OperationCallback<News> {
            override fun onError(error: String?) {
                _isViewLoading.value = false
                _onMessageError.value = error.toString()
            }

            override fun onSuccess(data: List<News>?) {
                _isViewLoading.value = false
                if (data.isNullOrEmpty()) {
                    _isEmptyList.value = true

                } else {
                    _news.value = data
                }
            }
        })
    }

}