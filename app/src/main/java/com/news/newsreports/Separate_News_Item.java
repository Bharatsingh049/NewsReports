package com.news.newsreports;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class Separate_News_Item extends AppCompatActivity implements View.OnClickListener {
    private WebView News_webview;
    private NewsModel model;
    private ProgressDialog mdialog;
    private  String URL;
    private ImageView Back_Arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_separate_news_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");
        getDataFromIntent();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void getDataFromIntent()//Getting Data From The Previous Activity via Intent
    {
        model=(NewsModel)getIntent().getSerializableExtra("data");
        URL=model.getUrl();
        Log.d("getDataFromIntent: ",URL);
        init_WebView();
    }

    private void init_WebView(){ //Initialising WebView
        News_webview=new WebView(this);
        News_webview=findViewById(R.id.News_WebView);
        Back_Arrow=findViewById(R.id.Back_Arrow);
        Back_Arrow.setOnClickListener(this);
        News_webview.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        News_webview.getSettings().setJavaScriptEnabled(true);
        //URL=getIntent().getStringExtra("data");
        mdialog=new ProgressDialog(this);
        mdialog.setMessage("Loading...");
        mdialog.setCancelable(false);
        mdialog.show();
        News_webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView w,String url){
                super.onPageFinished(w,url);
                mdialog.dismiss();
            }
        });
        News_webview.loadUrl(URL);
    }


    @Override
    public void onClick(View view) {
        super.onBackPressed();
    }
}
