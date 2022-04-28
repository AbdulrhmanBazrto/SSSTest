package bazrto.abdulrhman.ssstest

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.test.ext.junit.rules.ActivityScenarioRule
import bazrto.abdulrhman.ssstest.capture
import bazrto.abdulrhman.ssstest.config.Const
import bazrto.abdulrhman.ssstest.data.OperationCallback
import bazrto.abdulrhman.ssstest.model.*
import bazrto.abdulrhman.ssstest.utils.DateUtils
import bazrto.abdulrhman.ssstest.view.NewsListingActivity
import bazrto.abdulrhman.ssstest.viewmodel.NewsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


/**
 * @author Abd alrhman bazartwo
 */
class NewsUnitTest {

    @Mock
    private lateinit var newsDataSource: NewsDataSource

    @Mock
    private lateinit var context: Application

    @Captor
    private lateinit var operationCallbackCaptor: ArgumentCaptor<OperationCallback<News>>

    private lateinit var viewModel: NewsViewModel
    private lateinit var repository: NewsRepository

    private lateinit var isViewLoadingObserver: Observer<Boolean>
    private lateinit var onMessageErrorObserver: Observer<Any>
    private lateinit var emptyListObserver: Observer<Boolean>
    private lateinit var onRenderNewsObserver: Observer<List<News>>

    private lateinit var newsEmptyList: List<News>
    private lateinit var newsList: List<News>
    private lateinit var nextPageNewsList: List<News>


    @get:Rule
    val rule = InstantTaskExecutorRule()

//    @get:Rule
//    var activityRule: ActivityScenarioRule<NewsListingActivity> = ActivityScenarioRule(NewsListingActivity::class.java)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        `when`(context.applicationContext).thenReturn(context)

        repository = NewsRepository(newsDataSource)
        viewModel = NewsViewModel(repository)

        mockData()
        mockNextPageData()
        setupObservers()
    }

    @Test
    fun `retrieve news with ViewModel and Repository returns empty data`() {
        Assert.assertNotNull(Const.apiBaseUrl)
        Assert.assertNotNull(Const.apiKey)
        Assert.assertNotNull(Const.imagesBaseUrl)
        Assert.assertEquals(viewModel.page, 0)
        with(viewModel) {
            loadNews()
            isLoading.observeForever(isViewLoadingObserver)
            isEmptyList.observeForever(emptyListObserver)
            news.observeForever(onRenderNewsObserver)
        }
        viewModel.viewModelScope.launch(Dispatchers.Unconfined) {
            verify(newsDataSource, times(2)).retrieveNews(
                anyInt(),
                capture(operationCallbackCaptor)
            )
        }
        Assert.assertTrue(viewModel.isLoading.value == true)
        operationCallbackCaptor.value.onSuccess(newsEmptyList)

        Assert.assertNotNull(viewModel.isLoading.value)
        Assert.assertTrue(viewModel.isEmptyList.value == true)
        Assert.assertTrue(viewModel.news.value?.size == 0)
        Assert.assertEquals(viewModel.page, 0)
    }

    @Test
    fun `retrieve news with ViewModel and Repository returns full data`() {
        Assert.assertEquals(viewModel.page, 0)
        with(viewModel) {
            loadNews()
            isLoading.observeForever(isViewLoadingObserver)
            news.observeForever(onRenderNewsObserver)
        }
        viewModel.viewModelScope.launch(Dispatchers.Unconfined) {
            verify(newsDataSource, times(2)).retrieveNews(
                anyInt(),
                capture(operationCallbackCaptor)
            )
        }
        Assert.assertTrue(viewModel.isLoading.value == true)
        operationCallbackCaptor.value.onSuccess(newsList)


        Assert.assertNotNull(viewModel.isLoading.value)
        Assert.assertTrue(viewModel.news.value?.size == 3)
        Assert.assertEquals(viewModel.page, 0)
        Assert.assertNotNull(DateUtils.covertTimeToText(viewModel.news.value?.first()?.pub_date))
        val firstNotNullOf: News? = viewModel.news.value?.firstOrNull { it.multimedia.isNotEmpty() }
        Assert.assertNotNull(firstNotNullOf?.multimedia?.get(0)?.url)
    }

    @Test
    fun `retrieve next news page with ViewModel and Repository returns data`() {
        Assert.assertEquals(viewModel.page, 0)
        with(viewModel) {
            loadNextPage()
            isLoading.observeForever(isViewLoadingObserver)
            onMessageError.observeForever(onMessageErrorObserver)
        }
        viewModel.viewModelScope.launch(Dispatchers.Unconfined) {
            verify(newsDataSource, times(2)).retrieveNews(
                anyInt(),
                capture(operationCallbackCaptor)
            )
        }
        Assert.assertTrue(viewModel.isLoading.value == true)
        operationCallbackCaptor.value.onSuccess(nextPageNewsList)

        Assert.assertNotNull(viewModel.isLoading.value)
        Assert.assertTrue(viewModel.news.value?.size == 2)
        Assert.assertNotEquals(viewModel.page, 0)
    }

    @Test
    fun `retrieve news with ViewModel and Repository returns an error`() {
        with(viewModel) {
            loadNews()
            isLoading.observeForever(isViewLoadingObserver)
            onMessageError.observeForever(onMessageErrorObserver)
        }
        viewModel.viewModelScope.launch(Dispatchers.Unconfined) {
            verify(newsDataSource, times(2)).retrieveNews(
                anyInt(),
                capture(operationCallbackCaptor)
            )
        }
        Assert.assertTrue(viewModel.isLoading.value == true)
        operationCallbackCaptor.value.onError("An error occurred")
        Assert.assertNotNull(viewModel.isLoading.value)
        Assert.assertNotNull(viewModel.onMessageError.value)
        Assert.assertTrue(viewModel.isEmptyList.value == true)
    }

//    @Test
//    fun `create news listing activity`() {

//        val moveToState = activityRule.scenario.moveToState(Lifecycle.State.CREATED)

//        Assert.assertTrue(moveToState.state == Lifecycle.State.CREATED)

//        val activity: NewsListingActivity = activityRule.scenario
//        val viewById: View = activity.findViewById(bazrto.abdulrhman.ssstest.R.id.recyclerView)
//        assertThat(viewById, notNullValue())
//        assertThat(viewById, instanceOf(RecyclerView::class.java))
//        val listView: RecyclerView = viewById as RecyclerView
//        val adapter = listView.adapter
//        assertThat(adapter, instanceOf(NewsAdapter::class.java))
//        assert(adapter?.itemCount!! > 5)
//    }

    private fun setupObservers() {
        isViewLoadingObserver = mock(Observer::class.java) as Observer<Boolean>
        onMessageErrorObserver = mock(Observer::class.java) as Observer<Any>
        emptyListObserver = mock(Observer::class.java) as Observer<Boolean>
        onRenderNewsObserver = mock(Observer::class.java) as Observer<List<News>>
    }

    private fun mockData() {
        newsEmptyList = emptyList()
        val mockList: MutableList<News> = mutableListOf()
        mockList.add(
            News(
                "id",
                "headline",
                "www.nytimes.com",
                document_type = "",
                news_desk = "",
                pub_date = "2022-04-26T09:00:38+0000",
                headline = Headline("", "", "", "", "", "", ""),
                lead_paragraph = "",
                multimedia = listOf<MultiMedia>(
                    MultiMedia(
                        0,
                        "image",
                        "title",
                        "",
                        "",
                        "www.nytimes.com",
                        155,
                        100,
                        "",
                        ""
                    )
                ),
                word_count = 10,
                section_name = "",
                snippet = "",
                source = "",
                type_of_material = "",
                uri = "",
            )
        )
        mockList.add(
            News(
                "id2",
                "headline",
                "www.nytimes.com",
                document_type = "",
                news_desk = "",
                pub_date = "2022-04-26T09:00:38+0000",
                headline = Headline("", "", "", "", "", "", ""),
                lead_paragraph = "",
                multimedia = listOf(),
                word_count = 10,
                section_name = "",
                snippet = "",
                source = "",
                type_of_material = "",
                uri = "",
            )
        )
        mockList.add(
            News(
                "id3",
                "headline",
                "www.nytimes.com",
                document_type = "",
                news_desk = "",
                pub_date = "2022-04-26T09:00:38+0000",
                headline = Headline("", "", "", "", "", "", ""),
                lead_paragraph = "",
                multimedia = listOf(),
                word_count = 10,
                section_name = "",
                snippet = "",
                source = "",
                type_of_material = "",
                uri = "",
            )
        )

        newsList = mockList.toList()
    }

    private fun mockNextPageData() {
        val mockList: MutableList<News> = mutableListOf()
        mockList.add(
            News(
                "id",
                "headline",
                "www.nytimes.com",
                document_type = "",
                news_desk = "",
                pub_date = "2022-04-26T09:00:38+0000",
                headline = Headline("", "", "", "", "", "", ""),
                lead_paragraph = "",
                multimedia = listOf(),
                word_count = 10,
                section_name = "",
                snippet = "",
                source = "",
                type_of_material = "",
                uri = "",
            )
        )
        mockList.add(
            News(
                "id2",
                "headline",
                "www.nytimes.com",
                document_type = "",
                news_desk = "",
                pub_date = "2022-04-26T09:00:38+0000",
                headline = Headline("", "", "", "", "", "", ""),
                lead_paragraph = "",
                multimedia = listOf(),
                word_count = 10,
                section_name = "",
                snippet = "",
                source = "",
                type_of_material = "",
                uri = "",
            )
        )

        nextPageNewsList = mockList.toList()
    }
}