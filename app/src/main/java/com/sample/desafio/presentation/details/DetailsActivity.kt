package com.sample.desafio.presentation.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.sample.desafio.R
import com.sample.desafio.databinding.ActivityDetailsBinding
import com.sample.desafio.presentation.hits.HitStateUi

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val hit = intent.getParcelableExtra<HitStateUi>(HIT_EXTRA)
        val myWebView: WebView = findViewById(R.id.wvContent)
        myWebView.apply {
            loadUrl(hit?.url.orEmpty())
            webViewSetting()
        }
        supportActionBar?.title = hit?.title
    }

    fun webViewSetting() {
        var webSetting = binding.wvContent.settings
        webSetting.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
        }
        binding.wvContent.webViewClient = WebViewClient()
    }

    companion object {
        const val HIT_EXTRA = "HIT_EXTRA"
        fun getIntent(context: Context, hit: HitStateUi) =
            Intent(context, DetailsActivity::class.java).apply {
                putExtra(HIT_EXTRA, hit)
            }
    }
}