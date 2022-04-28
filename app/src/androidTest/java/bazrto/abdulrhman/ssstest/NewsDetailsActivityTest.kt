package bazrto.abdulrhman.ssstest

import android.content.Intent
import android.text.format.DateUtils
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import bazrto.abdulrhman.ssstest.model.Headline
import bazrto.abdulrhman.ssstest.model.News
import bazrto.abdulrhman.ssstest.view.NewsDetailsActivity
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@MediumTest
@RunWith(AndroidJUnit4::class)
class NewsDetailsActivityTest {

    val news = News(
    "id2",
    "headline",
    "https://www.nytimes.com",
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

    @get:Rule
    val rule: ActivityTestRule<NewsDetailsActivity> =
        object : ActivityTestRule<NewsDetailsActivity>(NewsDetailsActivity::class.java) {
            override fun getActivityIntent(): Intent {
                val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
                return Intent(targetContext, NewsDetailsActivity::class.java).apply {
                    putExtra("news", news)
                }
            }
        }


    @Test
    @Throws(Exception::class)
    fun makeSureViewWasBinded() {
        val activity = rule.activity
        val date = activity.findViewById<TextView>(R.id.textViewDate)
        val desc = activity.findViewById<TextView>(R.id.textViewDesc)
        Assert.assertTrue(date.text == bazrto.abdulrhman.ssstest.utils.DateUtils.covertTimeToText(news.pub_date))
        Assert.assertTrue(desc.text == news.snippet.capitalize())
    }
}