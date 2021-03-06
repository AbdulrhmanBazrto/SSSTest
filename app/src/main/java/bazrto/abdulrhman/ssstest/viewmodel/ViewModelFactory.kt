package bazrto.abdulrhman.ssstest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import bazrto.abdulrhman.ssstest.model.NewsRepository

/**
 * @author Abd alrhman bazartwo
 */
class ViewModelFactory(private val repository: NewsRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(repository) as T
    }
}