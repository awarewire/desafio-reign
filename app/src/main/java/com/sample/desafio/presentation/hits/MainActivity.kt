package com.sample.desafio.presentation.hits

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sample.desafio.R
import com.sample.desafio.databinding.ActivityMainBinding
import com.sample.desafio.presentation.commons.observeEvent
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

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        observeData()
        viewModel.initViewModel()
    }

    private fun setupView() {
        val pullToRefresh = findViewById<SwipeRefreshLayout>(R.id.srlRefresh)
        binding.rvHits.apply {
            this.adapter = hitsAdapter
            this.layoutManager = LinearLayoutManager(context)
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvHits)
        val dividerItemDecoration = DividerItemDecoration(
            binding.rvHits.context,
            DividerItemDecoration.VERTICAL
        )
        binding.rvHits.addItemDecoration(dividerItemDecoration)
        pullToRefresh.setOnRefreshListener {
            viewModel.refreshData()
            pullToRefresh.isRefreshing = false
        }
    }

    private fun observeData() {
        viewModel.mainStateUi.observe(this) { mainStateUi ->
            when (mainStateUi) {
                is MainStateUi.DisplayHits -> {
                    handleViews()
                    this.hitsList = mainStateUi.hist
                    this.hitsAdapter.setList(this.hitsList)
                }
                is MainStateUi.ErrorNetwork -> {
                    handleViews(showError = true)
                }
            }
        }
        viewModel.navigationUi.observeEvent(this) { navigationUi ->
            when (navigationUi) {
                is MainNavigateUi.GoDetails -> {
                    startActivity(
                        DetailsActivity.getIntent(this, navigationUi.hist)
                    )
                }
                is MainNavigateUi.NotFoundUrl -> {
                    Toast.makeText(
                        this,
                        getString(R.string.web_not_available),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun handleViews(showError: Boolean = false) {
        binding.rvHits.isVisible = !showError
        binding.tvErrorMessage.isVisible = showError
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
        viewModel.navigateToDetails(hit)
    }
}