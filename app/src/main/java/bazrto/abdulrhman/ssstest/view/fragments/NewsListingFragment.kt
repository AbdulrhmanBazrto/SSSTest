package bazrto.abdulrhman.ssstest.view.fragments

import PaginationScrollListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import bazrto.abdulrhman.ssstest.R
import bazrto.abdulrhman.ssstest.di.Injection
import bazrto.abdulrhman.ssstest.model.News
import bazrto.abdulrhman.ssstest.view.NewsDetailsActivity
import bazrto.abdulrhman.ssstest.view.NewsListingActivity
import bazrto.abdulrhman.ssstest.view.adapters.NewsAdapter
import bazrto.abdulrhman.ssstest.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_news_listing.*
import kotlinx.android.synthetic.main.layout_error.*

class NewsListingFragment : Fragment() {

    var inflate: View? = null

    private val viewModel: NewsViewModel by activityViewModels() {
        Injection.provideViewModelFactory()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (inflate == null) {
            inflate = inflater.inflate(R.layout.fragment_news_listing, container, false)
        }
        return inflate
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupUI()
    }

    private lateinit var adapter: NewsAdapter

    /**
     * listen to recycler view item click to open the details page
     */
    val listener = object : NewsAdapter.CustomViewHolderListener {
        override fun onCustomItemClicked(item: News, imageView: ImageView?) {
            openDetailsActivity(item, imageView)
        }
    }

    /**
     * @param imageView this param might be null if we are running unit test
     */
    fun openDetailsActivity(item: News, imageView: ImageView?) {

        if (imageView != null) {
            val bundle = Bundle()
            bundle.putSerializable("news", item)
            val extras = FragmentNavigatorExtras(
                imageView to ViewCompat.getTransitionName(imageView)!!
            )
            findNavController().navigate(R.id.action_details, bundle, null, extras)

        } else {
//            val intent = Intent(requireActivity(), NewsDetailsActivity::class.java)
//            intent.putExtra("news", item)
//            startActivity(intent)
        }
    }

    /**
     * this function is called from the testing unit
     */
    public fun openDetailsActivity(item: News) {
        openDetailsActivity(item, null)
    }

    //ui
    private fun setupUI() {
        adapter = NewsAdapter(viewModel.news.value?.toMutableList() ?: mutableListOf(), listener)
        recyclerView.layoutManager = LinearLayoutManager(context)

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
        viewModel.news.observe(viewLifecycleOwner, renderNews)
        viewModel._isLoading.observe(viewLifecycleOwner, isLoadingObserver)
        viewModel.onMessageError.observe(viewLifecycleOwner, onMessageErrorObserver)
        viewModel.isEmptyList.observe(viewLifecycleOwner, emptyListObserver)
    }

    //observers
    private val renderNews = Observer<List<News>> {
        Log.v(NewsListingActivity.TAG, "data updated $it")
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
        Log.v(NewsListingActivity.TAG, "isViewLoading $it")
        val visibility = if (it) View.VISIBLE else View.GONE
        progressBar.visibility = visibility
    }

    private val onMessageErrorObserver = Observer<Any> {
        Log.v(NewsListingActivity.TAG, "onMessageError $it")
        layoutEmpty.visibility = View.GONE
        textViewError.text = "Error $it"

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Error!!")
        builder.setMessage(it.toString())
        builder.setPositiveButton(android.R.string.ok) { dialog, which ->
            dialog.dismiss()
        }
        builder.show()

    }

    private val emptyListObserver = Observer<Boolean> {
        Log.v(NewsListingActivity.TAG, "emptyListObserver $it")
        layoutEmpty.visibility = View.VISIBLE
        layoutError.visibility = View.GONE
    }

    companion object {
        fun newInstance() = NewsListingFragment()

    }
}