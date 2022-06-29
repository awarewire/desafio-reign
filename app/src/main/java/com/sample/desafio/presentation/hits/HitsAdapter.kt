package com.sample.desafio.presentation.hits

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.sample.desafio.databinding.HitViewBinding


class HitsAdapter(
    private var hits: List<HitStateUi> = listOf(),
    private val listener: (HitStateUi) -> Unit = {}
) : RecyclerView.Adapter<HitsAdapter.HitViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setList(items: List<HitStateUi>) {
        this.hits = items
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): HitViewHolder {
        val itemBinding = HitViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HitViewHolder(itemBinding)
    }

    override fun onBindViewHolder(parent: HitViewHolder, position: Int) {
        parent.bind(hits[position])
    }

    override fun getItemCount() = hits.size

    inner class HitViewHolder(private val hitViewBinding: HitViewBinding) :
        RecyclerView.ViewHolder(hitViewBinding.root) {
        fun bind(hit: HitStateUi) {
            hitViewBinding.tvTitle.text = hit.title
            hitViewBinding.tvAuthorDate.text = hit.retrieveAuthorAndDate()
            hitViewBinding.root.setOnClickListener { listener.invoke(hit) }
        }

        fun getViewForeground(): ConstraintLayout = hitViewBinding.clHitContent
    }
}