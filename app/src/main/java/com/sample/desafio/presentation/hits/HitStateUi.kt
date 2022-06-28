package com.sample.desafio.presentation.hits

import android.os.Parcelable
import android.text.format.DateUtils
import com.sample.desafio.domain.HitDomain
import kotlinx.parcelize.Parcelize

@Parcelize
data class HitStateUi(
    val id: String,
    val title: String,
    val dateCreated: Long,
    val author: String,
    val url: String
) : Parcelable {
    fun retrieveAuthorAndDate(): String {
        return "$author - ${DateUtils.getRelativeTimeSpanString(dateCreated)}"
    }
}

fun List<HitDomain>.toListStateUi() = this.map { entity -> entity.toStateUi() }
fun HitDomain.toStateUi() = HitStateUi(
    id = id,
    title = title,
    dateCreated = dateCreated,
    author = author,
    url = url
)