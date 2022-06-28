package com.sample.desafio.presentation.hits

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.sample.desafio.R


class HitsAdapter(
    private var hits: List<HitStateUi> = listOf(),
    private val listener: (HitStateUi) -> Unit = {}
) : RecyclerView.Adapter<HitsAdapter.HitViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setList(items: List<HitStateUi>) {
        this.hits = items
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): HitViewHolder =
        HitViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.hit_view, parent, false)
        )

    override fun onBindViewHolder(parent: HitViewHolder, position: Int) {
        parent.bind(hits[position])
    }

    override fun getItemCount() = hits.size

    inner class HitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(hit: HitStateUi) {
            itemView.findViewById<TextView>(R.id.tvTitle).text = hit.title
            itemView.findViewById<TextView>(R.id.tvAuthorDate).text = hit.retrieveAuthorAndDate()
            itemView.setOnClickListener { listener.invoke(hit) }
        }

        fun getViewForeground(): ConstraintLayout = itemView.findViewById(R.id.clHitContent)
    }
}