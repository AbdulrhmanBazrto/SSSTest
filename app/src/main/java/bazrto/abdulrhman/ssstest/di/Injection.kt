package bazrto.abdulrhman.ssstest.di

import androidx.lifecycle.ViewModelProvider
import bazrto.abdulrhman.ssstest.data.ApiClient
import bazrto.abdulrhman.ssstest.data.NewsRemoteDataSource
import bazrto.abdulrhman.ssstest.model.NewsDataSource
import bazrto.abdulrhman.ssstest.model.NewsRepository
import bazrto.abdulrhman.ssstest.viewmodel.ViewModelFactory

/**
 * @author Abd alrhman bazartwo
 * inject required dependencies
 */
object Injection {

    private val NEWS_DATA_SOURCE: NewsDataSource = NewsRemoteDataSource(ApiClient)
    private val newsRepository = NewsRepository(NEWS_DATA_SOURCE)
    private val newsViewModelFactory = ViewModelFactory(newsRepository)

    fun providerRepository(): NewsDataSource {
        return NEWS_DATA_SOURCE
    }

    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return newsViewModelFactory
    }
}