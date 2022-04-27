package bazrto.abdulrhman.ssstest.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bazrto.abdulrhman.ssstest.data.OperationCallback
import bazrto.abdulrhman.ssstest.model.News
import bazrto.abdulrhman.ssstest.model.NewsRepository
import kotlinx.coroutines.launch
import kotlin.math.log

/**
 * @author Abd alrhman bazartwo
 */
class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    var page: Int = 0
    private val _news = MutableLiveData<List<News>>().apply { value = emptyList() }
    val news: LiveData<List<News>> = _news

    val isLastPage = MutableLiveData<Boolean>()

    val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> = _onMessageError

    private val _isEmptyList = MutableLiveData<Boolean>()
    val isEmptyList: LiveData<Boolean> = _isEmptyList

    init {
        loadNews()
    }

    internal fun loadNextPage(){
        page++
        loadNews()
    }

    private fun loadNews() {
        _isLoading.value = true
        viewModelScope.launch {
            repository.fetchNews(page,object : OperationCallback<News> {
                override fun onError(error: String?) {
                    _isLoading.value = false
                    _onMessageError.value = error.toString()
                }

                override fun onSuccess(data: List<News>?) {
                    _isLoading.value = false
                    if (data.isNullOrEmpty()) {
                        _isEmptyList.value = true
                        isLastPage.value = true

                    } else {
                        _news.value = data
                        isLastPage.value = false
                    }
                }
            })
        }

    }

}