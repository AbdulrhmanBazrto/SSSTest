package bazrto.abdulrhman.ssstest.view

import PaginationScrollListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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

    val listener = object : NewsAdapter.CustomViewHolderListener {
        override fun onCustomItemClicked(item: News) {
            openDetailsActivity(item)
        }
    }

    public fun openDetailsActivity(item: News) {
        val intent = Intent(baseContext, NewsDetailsActivity::class.java)
        intent.putExtra("news", item)
        startActivity(intent)
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
            override fun loadMoreItems() {
                viewModel.loadNextPage()
            }

            override val isLastPage: Boolean
                get() {
                    return if (viewModel.isLastPage.value != null)
                        viewModel.isLastPage.value!!
                    else
                        true
                }
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
        adapter.update(it)
    }

    private val isLoadingObserver = Observer<Boolean> {
        Log.v(TAG, "isViewLoading $it")
        val visibility = if (it) View.VISIBLE else View.GONE
        progressBar.visibility = visibility
    }

    private val onMessageErrorObserver = Observer<Any> {
        Log.v(TAG, "onMessageError $it")
//        layoutError.visibility = View.VISIBLE
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

    override fun onResume() {
        super.onResume()
    }

    companion object {
        const val TAG = "CONSOLE"
    }
}
