package bazrto.abdulrhman.ssstest.model

import java.io.Serializable

/**
 * @author : Abd alrhman bazartwo
 */

data class Response(val docs: List<News>?, val meta: Meta?) : Serializable

data class Meta(val hits: Int, val offset: Int, val time: Int) : Serializable

data class News(
    val _id: String,
    val abstract: String,
    val web_url: String,
    val snippet: String,
    val lead_paragraph: String,
    val multimedia: List<MultiMedia>,
    val headline: Headline,
    val pub_date: String,
    val document_type: String,
    val news_desk: String,
    val section_name: String,
    val word_count: Int,
    val uri: String
) : Serializable

data class MultiMedia(
    val rank: Int,
    val subtype: String,
    val caption: String?,
    val credit: String?,
    val type: String,
    val url: String,
    val height: Int,
    val width: Int,
    val subType: String,
    val crop_name: String
) : Serializable

data class Headline(
    val main: String,
    val kicker: String?,
    val content_kicker: String?,
    val print_headline: String?,
    val name: String?,
    val seo: String?,
    val sub: String?
) : Serializable
