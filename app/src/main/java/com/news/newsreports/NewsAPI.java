package com.news.newsreports;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class NewsAPI {
    private static final String sKey = "40ab81231e4c43fd925cdc3a0d08f7f6";

    private static final String sUrl = "https://newsapi.org/v2/";

    public static NewsService newsService = null;



    public static NewsService getService(){

        if (newsService==null){
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(sUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            newsService = retrofit.create(NewsService.class);
        }
        return newsService;
    }

    public interface  NewsService{
        @GET("top-headlines")
        Call<Model> getModelList(@Query("category") String category, @Query("apiKey") String apiKey, @Query("country") String country, @Query("language") String language);
    }
}
