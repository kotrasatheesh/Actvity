package com.example.asutosh.aebug;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import static com.android.volley.VolleyLog.TAG;

public class WebView_activity extends AppCompatActivity {

   WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webview=(WebView)findViewById(R.id.webview);
        Intent intent=getIntent();
        String url=intent.getStringExtra("URL");
        String myPdfUrl =url;
        //   String url = "http://docs.google.com/gview?embedded=true&url=" + myPdfUrl;
        Log.i(TAG, "Opening PDF: " + myPdfUrl);
        webview.setWebViewClient(new AppWebViewClients());
        webview.getSettings().setJavaScriptEnabled(true);
        // webview.getSettings().setUseWideViewPort(true);
        webview.loadUrl("http://docs.google.com/gview?embedded=true&url="
                + myPdfUrl);
        //  webview.loadUrl(myPdfUrl);
    }
}
