package bazrto.abdulrhman.ssstest.view

import PaginationScrollListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import bazrto.abdulrhman.ssstest.R
import bazrto.abdulrhman.ssstest.di.Injection
import bazrto.abdulrhman.ssstest.model.News
import bazrto.abdulrhman.ssstest.view.adapters.NewsAdapter
import bazrto.abdulrhman.ssstest.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.layout_error.*


/**
 * @author Abd alrhman bazartwo
 */
class NewsListingActivity : AppCompatActivity() {

    private val viewModel by viewModels<NewsViewModel> {
        Injection.provideViewModelFactory()
    }
    private lateinit var adapter: NewsAdapter

    /**
     * listen to recycler view item click to open the details page
     */
    val listener = object : NewsAdapter.CustomViewHolderListener {
        override fun onCustomItemClicked(item: News,imageView: ImageView?) {
            openDetailsActivity(item,imageView)
        }
    }

    /**
     * @param imageView this param might be null if we are running unit test
     */
    fun openDetailsActivity(item: News, imageView: ImageView?) {

        if (imageView != null) {
            val activityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, "imageMain")
            val intent = Intent(baseContext, NewsDetailsActivity::class.java)
            intent.putExtra("news", item)
            startActivity(intent, activityOptionsCompat.toBundle())
        } else {
            val intent = Intent(baseContext, NewsDetailsActivity::class.java)
            intent.putExtra("news", item)
            startActivity(intent)
        }
    }

    /**
     * this function is called from the testing unit
     */
    public fun openDetailsActivity(item: News) {
        openDetailsActivity(item,null)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setupViewModel()
        setupUI()
    }

    //ui
    private fun setupUI() {
        adapter = NewsAdapter(viewModel.news.value?.toMutableList() ?: mutableListOf(), listener)
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.addOnScrollListener(object :
            PaginationScrollListener(recyclerView.layoutManager as LinearLayoutManager) {

            // to load more item on scrolling
            override fun loadMoreItems() {
                viewModel.loadNextPage()
            }

            // to not load more items if we are on the last page of the API
            override val isLastPage: Boolean
                get() {
                    return if (viewModel.isLastPage.value != null)
                        viewModel.isLastPage.value!!
                    else
                        true
                }

            // to not load more items if we are already loding
            override val isLoading: Boolean
                get() {
                    return if (viewModel._isLoading.value != null)
                        viewModel._isLoading.value!!
                    else
                        true
                }
        })


        recyclerView.adapter = adapter
    }

    //view model
    private fun setupViewModel() {
        viewModel.news.observe(this, renderNews)
        viewModel._isLoading.observe(this, isLoadingObserver)
        viewModel.onMessageError.observe(this, onMessageErrorObserver)
        viewModel.isEmptyList.observe(this, emptyListObserver)
    }

    //observers
    private val renderNews = Observer<List<News>> {
        Log.v(TAG, "data updated $it")
        layoutError.visibility = View.GONE
        layoutEmpty.visibility = View.GONE
        var animateView = false
        if (adapter.itemCount == 0) {
            animateView = true
        }
        adapter.update(it)
        if (animateView) {
            recyclerView.scheduleLayoutAnimation()
        }
    }

    private val isLoadingObserver = Observer<Boolean> {
        Log.v(TAG, "isViewLoading $it")
        val visibility = if (it) View.VISIBLE else View.GONE
        progressBar.visibility = visibility
    }

    private val onMessageErrorObserver = Observer<Any> {
        Log.v(TAG, "onMessageError $it")
        layoutEmpty.visibility = View.GONE
        textViewError.text = "Error $it"

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error!!")
        builder.setMessage(it.toString())
        builder.setPositiveButton(android.R.string.ok) { dialog, which ->
            dialog.dismiss()
        }
        builder.show()

    }

    private val emptyListObserver = Observer<Boolean> {
        Log.v(TAG, "emptyListObserver $it")
        layoutEmpty.visibility = View.VISIBLE
        layoutError.visibility = View.GONE
    }

    companion object {
        const val TAG = "CONSOLE"
    }
}
