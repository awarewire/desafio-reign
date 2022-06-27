package com.sample.desafio.presentation.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.sample.desafio.R
import com.sample.desafio.presentation.hits.HitStateUi

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val hit = intent.getParcelableExtra<HitStateUi>(HIT_EXTRA)
        val myWebView: WebView = findViewById(R.id.wvContent)
        myWebView.loadUrl(hit?.url.orEmpty())
        supportActionBar?.title = hit?.title
    }

    companion object {
        const val HIT_EXTRA = "HIT_EXTRA"
        fun getIntent(context: Context, hit: HitStateUi) =
            Intent(context, DetailsActivity::class.java).apply {
                putExtra(HIT_EXTRA, hit)
            }
    }
}