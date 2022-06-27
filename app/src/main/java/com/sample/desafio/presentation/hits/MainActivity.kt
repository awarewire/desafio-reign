package com.sample.desafio.presentation.hits

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sample.desafio.R
import com.sample.desafio.presentation.details.DetailsActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), HitItemTouchHelper.RecyclerItemTouchHelperListener {
    private val hitsAdapter: HitsAdapter by lazy { HitsAdapter(listener = this::clickItem) }
    private val itemTouchHelperCallback by lazy {
        HitItemTouchHelper(0, ItemTouchHelper.LEFT, this)
    }
    private val viewModel by viewModels<HitsViewModel>()
    private var hitsList: List<HitStateUi> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
        observeData()
        viewModel.initViewModel()
    }

    private fun setupView() {
        val recyclerView = findViewById<RecyclerView>(R.id.rvHits)
        val pullToRefresh = findViewById<SwipeRefreshLayout>(R.id.srlRefresh)
        recyclerView.apply {
            this.adapter = hitsAdapter
            this.layoutManager = LinearLayoutManager(context)
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)
        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            DividerItemDecoration.VERTICAL
        )
        recyclerView.addItemDecoration(dividerItemDecoration)
        pullToRefresh.setOnRefreshListener {
            viewModel.refreshData()
            pullToRefresh.isRefreshing = false
        }
    }

    private fun observeData() {
        viewModel.hits.observe(this) { list ->
            this.hitsList = list
            this.hitsAdapter.setList(list)
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
        if (viewHolder is HitsAdapter.HitViewHolder) {
            val hit = hitsList.getOrNull(viewHolder.getAdapterPosition())
            hit?.let {
                viewModel.removeItem(it.id)
            }
        }
    }

    private fun clickItem(hit: HitStateUi) {
        if (hit.url.isEmpty()) {
            this.showToastWebViewEmpty()
        } else {
            startActivity(DetailsActivity.getIntent(this, hit))
        }
    }

    private fun showToastWebViewEmpty() {
        Toast.makeText(this, getString(R.string.web_not_available), Toast.LENGTH_LONG).show()
    }
}