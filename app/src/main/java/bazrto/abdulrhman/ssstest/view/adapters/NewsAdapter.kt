package bazrto.abdulrhman.ssstest.view.adapters

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
import bazrto.abdulrhman.ssstest.utils.DateUtils
import kotlinx.android.synthetic.main.row_news.view.*

/**
 * @author Abd alrhman bazartwo
 */
class NewsAdapter(
    private var news: MutableList<News>,
    private val listener: CustomViewHolderListener
) :
    RecyclerView.Adapter<NewsAdapter.MViewHolder>() {

    interface CustomViewHolderListener {
        fun onCustomItemClicked(item:News,imageView: ImageView?)
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
        if (news.isNullOrEmpty()) {
            news = data.toMutableList()
            notifyDataSetChanged()
        }else{
            val oldSize = news.size
            news.addAll(data)
            notifyItemRangeInserted(oldSize,data.size)
        }
    }

    class MViewHolder(view: View, private val listener: CustomViewHolderListener) :
        RecyclerView.ViewHolder(view) {
        private val textViewName: TextView = view.textViewName
        private val textViewDate: TextView = view.textViewDate
        private val textViewSource: TextView = view.textViewSource
        private val imageView: ImageView = view.imageView
        fun bind(news: News) {

            if (news.lead_paragraph.isEmpty()){
                textViewName.text = news.abstract.capitalize()
            }else{
                textViewName.text = news.lead_paragraph.capitalize()
            }

            textViewSource.text = news.source?.capitalize()

            val timeAgo = DateUtils.covertTimeToText(news.pub_date)
            textViewDate.text = timeAgo

            if (news.multimedia.isNullOrEmpty()) {
               imageView.visibility = View.GONE
            }else{
                imageView.visibility = View.VISIBLE
                Glide.with(imageView.context).load(Const.imagesBaseUrl + news.multimedia[0].url)
                    .into(imageView)
            }
            itemView.setOnClickListener {
                listener.onCustomItemClicked(news,imageView)
            }
        }
    }
}