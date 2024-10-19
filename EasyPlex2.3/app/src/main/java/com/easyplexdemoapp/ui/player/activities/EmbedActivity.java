package com.easyplexdemoapp.ui.player.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.databinding.ActivityEmbedBinding;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.util.Constants;
import com.easyplexdemoapp.util.Tools;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class EmbedActivity extends AppCompatActivity {

    private static final String TAG = "EmbedActivity";

    ActivityEmbedBinding binding;

    @Inject
    SettingsManager settingsManager;

    private String initialUrl;
    private List<String> validDomains;

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            if (isValidDomain(url)) {
                return false; // Let the WebView load the page
            } else {
                Log.d(TAG, "Blocked navigation to: " + url);
                return true; // Block the navigation
            }
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            if (url.contains("disable-devtool.min.js")) {
                // Return an empty response to block the script
                return new WebResourceResponse("application/javascript", "UTF-8", new ByteArrayInputStream("".getBytes()));
            }
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (!isValidDomain(url)) {
                view.stopLoading();
                view.loadUrl(initialUrl);
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_embed);

        Tools.setSystemBarTransparent(this);
        Tools.hideSystemPlayerUi(this, true, 0);

        Intent receivedIntent = getIntent();
        initialUrl = receivedIntent.getStringExtra(Constants.MOVIE_LINK);

        if (initialUrl != null) {
            setupValidDomains(initialUrl);
            setupWebView(initialUrl);
        } else {
            Log.e(TAG, "No movie link provided");
            finish();
        }
    }

    private void setupValidDomains(String url) {
        validDomains = new ArrayList<>();
        String domain = getDomainName(url);
        validDomains.add(domain);
        validDomains.add("cdn.vidsrc.stream"); // Add the CDN domain
        // Add any other domains you consider valid
    }

    private String getDomainName(String url) {
        String domain = url;
        int doubleSlash = domain.indexOf("//");
        if (doubleSlash > 0) {
            domain = domain.substring(doubleSlash + 2);
        }
        int slash = domain.indexOf('/');
        if (slash > 0) {
            domain = domain.substring(0, slash);
        }
        return domain;
    }

    private boolean isValidDomain(String url) {
        String domain = getDomainName(url);
        return validDomains.contains(domain);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView(String url) {
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(binding.webView, true);

        binding.webView.setWebViewClient(new CustomWebViewClient());

        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        // Set a desktop Mozilla Firefox user agent
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:100.0) Gecko/20100101 Firefox/100.0";
        webSettings.setUserAgentString(userAgent);

        binding.webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (binding.webView.canGoBack()) {
            String currentUrl = binding.webView.getUrl();
            if (isValidDomain(currentUrl)) {
                binding.webView.goBack();
            } else {
                binding.webView.loadUrl(initialUrl);
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (binding.webView != null) {
            binding.webView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (binding.webView != null) {
            binding.webView.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        if (binding.webView != null) {
            binding.webView.stopLoading();
            binding.webView.onPause();
            binding.webView.clearCache(true);
            binding.webView.clearHistory();
            binding.webView.destroy();
        }
        super.onDestroy();
    }
}