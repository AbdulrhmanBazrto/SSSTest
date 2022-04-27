package bazrto.abdulrhman.ssstest.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import bazrto.abdulrhman.ssstest.R
import bazrto.abdulrhman.ssstest.config.Const
import bazrto.abdulrhman.ssstest.model.News
import kotlinx.android.synthetic.main.row_news.view.*

/**
 * @author Abd alrhman bazartwo
 */
class NewsAdapter(
    private var news: List<News>,
    private val listener: CustomViewHolderListener
) :
    RecyclerView.Adapter<NewsAdapter.MViewHolder>() {

    interface CustomViewHolderListener {
        fun onCustomItemClicked()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_news, parent, false)
        return MViewHolder(view, listener)
    }

    override fun onBindViewHolder(vh: MViewHolder, position: Int) {
        vh.bind(news[position])
    }

    override fun getItemCount(): Int {
        return news.size
    }

    fun update(data: List<News>) {
        news = data
        notifyDataSetChanged()
    }

    class MViewHolder(view: View, private val listener: CustomViewHolderListener) :
        RecyclerView.ViewHolder(view) {
        private val textViewName: TextView = view.textViewName
        private val imageView: ImageView = view.imageView
        fun bind(news: News) {
            textViewName.text = news.snippet.capitalize()
            if (news.multimedia.isNotEmpty()) {
                Glide.with(imageView.context).load(Const.imagesBaseUrl + news.multimedia[0].url)
                    .into(imageView)
            }
            itemView.setOnClickListener {
                listener.onCustomItemClicked()
            }
        }
    }
}