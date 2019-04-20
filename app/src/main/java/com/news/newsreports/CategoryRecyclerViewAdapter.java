package com.news.newsreports;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.RecyclerViewHolder> {
    private ArrayList<NewsModel> list;
    private Context MyContext;

    public CategoryRecyclerViewAdapter(ArrayList<NewsModel> list){
        this.list=list;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        MyContext=viewGroup.getContext();
        LayoutInflater inflater=LayoutInflater.from(MyContext);
        View view=inflater.inflate(R.layout.categoryrecyclerview_cardlayout,viewGroup,false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, final int i) {
        Glide.with(MyContext)
                .load(list.get(i).getimage())
                .crossFade()
                .into(recyclerViewHolder.Image);
        recyclerViewHolder.Title.setText(list.get(i).getTitle());
        /*recyclerViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyContext,Separate_News_Item.class);
                intent.putExtra("data",(Serializable) list.get(i));
                MyContext.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView Title;
        CardView cardView;
        ImageView Image;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            Title=itemView.findViewById(R.id.Separate_Title);
            Image=itemView.findViewById(R.id.Separate_Image);
            cardView=itemView.findViewById(R.id.card);
        }

        @Override
        public void onClick(View view) {
            Intent intent=new Intent(MyContext,Separate_News_Item.class);
            intent.putExtra("data",(Serializable) list.get(getAdapterPosition()));
            MyContext.startActivity(intent);
        }
    }
}
