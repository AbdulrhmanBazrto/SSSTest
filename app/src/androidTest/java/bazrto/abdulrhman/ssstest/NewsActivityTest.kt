package bazrto.abdulrhman.ssstest

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import bazrto.abdulrhman.ssstest.model.Headline
import bazrto.abdulrhman.ssstest.model.News
import bazrto.abdulrhman.ssstest.view.NewsListingActivity
import bazrto.abdulrhman.ssstest.view.adapters.NewsAdapter
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class NewsActivityTest {

    @get:Rule
    var rule = ActivityTestRule(
        NewsListingActivity::class.java
    )

    @Test
    @Throws(Exception::class)
    fun ensureListViewIsPresent() {
        val activity = rule.activity
        val viewById = activity.findViewById<View>(R.id.recyclerView)
        Assert.assertNotNull(viewById)
        Assert.assertThat(
            viewById, Matchers.instanceOf(
                RecyclerView::class.java
            )
        )
        val listView = viewById as RecyclerView
        val adapter = listView.adapter
        Assert.assertThat(
            adapter, Matchers.instanceOf(
                NewsAdapter::class.java
            )
        )
        Assert.assertTrue(adapter!!.itemCount > 0)
    }

    @Test
    @Throws(Exception::class)
    fun checkViews() {
        val activity = rule.activity
        val viewById = activity.findViewById<View>(R.id.recyclerView)
        Assert.assertNotNull(viewById)
        Assert.assertThat(
            viewById, Matchers.instanceOf(
                RecyclerView::class.java
            )
        )
        val listView = viewById as RecyclerView
        val adapter = listView.adapter
        Assert.assertThat(
            adapter, Matchers.instanceOf(
                NewsAdapter::class.java
            )
        )
        Assert.assertTrue(adapter!!.itemCount > 0)
    }

    @Test
    @Throws(Exception::class)
    fun clickOnNewsRow() {

        val activity = rule.activity as NewsListingActivity
        activity.openDetailsActivity(
            News(
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
        )

        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}