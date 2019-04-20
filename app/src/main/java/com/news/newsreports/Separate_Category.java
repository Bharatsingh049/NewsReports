package com.news.newsreports;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

public class Separate_Category extends AppCompatActivity {
    private RecyclerView CategoryCycle;
    private ArrayList<NewsModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_separate_category);
        getDataFromIntent();
    }

    private void getDataFromIntent() //getting data from previous Activity via Intent
    {
        list=(ArrayList<NewsModel>) getIntent().getSerializableExtra("list");
        int temp=list.size();
        Log.d( "getDataFromIntent: ",temp+"");
        String cat=getIntent().getStringExtra("Category");
        getSupportActionBar().setTitle(cat);
        init_RecyclerView();
    }

    private void init_RecyclerView() //Initialising RecyclerView
    {
        CategoryCycle=new RecyclerView(this);
        CategoryCycle=findViewById(R.id.Category_RecyclerView);
        CategoryCycle.setLayoutManager(new LinearLayoutManager(this));
        CategoryCycle.setAdapter(new CategoryRecyclerViewAdapter(list));


    }
}
