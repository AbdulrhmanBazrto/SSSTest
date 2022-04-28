package bazrto.abdulrhman.ssstest.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import bazrto.abdulrhman.ssstest.R
import bazrto.abdulrhman.ssstest.config.Const
import bazrto.abdulrhman.ssstest.model.News
import bazrto.abdulrhman.ssstest.utils.DateUtils
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_news_details.*

class NewsDetailsFragment : Fragment() {

    private lateinit var news: News

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            news = it.getSerializable("news") as News
        }
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.slide_left)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        menuInflater.inflate(R.menu.options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == R.id.browser) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(news.web_url))
            startActivity(browserIntent)
        } else if (item.itemId == android.R.id.home) {
            findNavController().popBackStack()
        }
        return true;
    }

    private fun setupUI() {

        imageView.transitionName = news._id

        if (news.lead_paragraph.isEmpty()) {
            textViewName.text = news.abstract.capitalize()
        } else {
            textViewName.text = news.lead_paragraph.capitalize()
        }
        textViewSource.text = news.source?.capitalize()
        textViewDesc.text = news.snippet.capitalize()
        textViewType.text = news.type_of_material?.capitalize()

        val timeAgo = DateUtils.covertTimeToText(news.pub_date)
        textViewDate.text = timeAgo

        if (news.multimedia.isNullOrEmpty()) {
            imageView.visibility = View.GONE
        } else {
            imageView.visibility = View.VISIBLE
            Glide.with(imageView.context).load(Const.imagesBaseUrl + news.multimedia[0].url)
                .into(imageView)
        }
    }

    companion object {
        fun newInstance(news: News) =
            NewsDetailsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("news", news)
                }
            }
    }
}