package bazrto.abdulrhman.ssstest.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import bazrto.abdulrhman.ssstest.R
import bazrto.abdulrhman.ssstest.config.Const
import bazrto.abdulrhman.ssstest.model.News
import bazrto.abdulrhman.ssstest.utils.DateUtils
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_news_details.*


class NewsDetailsActivity : AppCompatActivity() {

    lateinit var news:News

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_details)

        news = intent.getSerializableExtra("news") as News

        setupUI()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == R.id.browser){
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(news.web_url))
            startActivity(browserIntent)
        }
        return true;
    }

    private fun setupUI(){
        if (news.lead_paragraph.isEmpty()){
            textViewName.text = news.abstract.capitalize()
        }else{
            textViewName.text = news.lead_paragraph.capitalize()
        }
        textViewSource.text = news.source?.capitalize()
        textViewDesc.text = news.snippet.capitalize()
        textViewType.text = news.type_of_material?.capitalize()

        val timeAgo = DateUtils.covertTimeToText(news.pub_date)
        textViewDate.text = timeAgo

        if (news.multimedia.isNullOrEmpty()) {
            imageView.visibility = View.GONE
        }else{
            imageView.visibility = View.VISIBLE
            Glide.with(imageView.context).load(Const.imagesBaseUrl + news.multimedia[0].url)
                .into(imageView)
        }
    }
}