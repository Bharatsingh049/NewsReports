package com.news.newsreports;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.card.MaterialCardView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IllegalFormatException;

import retrofit2.Call;
import retrofit2.Callback;

public class FetchingNews implements Runnable {
    private static final String sKey = "40ab81231e4c43fd925cdc3a0d08f7f6";
    private String URL = "https://newsapi.org/v2/top-headlines?apiKey=40ab81231e4c43fd925cdc3a0d08f7f6&language=en&";
    private ArrayList<NewsModel> Business_List, Entertainment_List, Health_List, Science_List, Sports_List, Technology_List;
    private String[] Category = {"business", "entertainment", "health", "science", "sports", "technology"};
    private static int i;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private MaterialCardView materialCardView;
    private LinearLayout parent_layout;
    private ImageView imageView;
    private String[] CountryCode = new String[54], Country_Names = new String[54];
    private RadioGroup rg;
    private Button Ok_Button;
    private int Selected_CountryCode;
    private Context MyContext;

    public void setParams(Context context) {
        this.MyContext = context;
        init_List();
    }

    private void init_List() //Initialising all lists here
    {
        i = 0;
        Business_List = new ArrayList<>();
        Entertainment_List = new ArrayList<>();
        Health_List = new ArrayList<>();
        Science_List = new ArrayList<>();
        Sports_List = new ArrayList<>();
        Technology_List = new ArrayList<>();
        CountryCode = MyContext.getResources().getStringArray(R.array.Countries_codes);
        Country_Names = MyContext.getResources().getStringArray(R.array.Countries_Names);
        int temp = CountryCode.length;
        Log.d("init_List: ", temp + "");
        pref = MyContext.getSharedPreferences("MyPref", 0);
        Selected_CountryCode = pref.getInt("Country code", 0);

    }

    private void Reload_Dialog(String str) {

        android.support.v7.app.AlertDialog.Builder Builder =
                new android.support.v7.app.AlertDialog.Builder(MyContext);
        Builder.setCancelable(false);
        Builder.setTitle(str);
        Builder.setMessage("Want to retry");
        Builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                run();
            }
        });
        Builder.show();

    }

    @Override
    public void run() {
        Log.d("run: ", URL);
        String url = URL + "country=" + CountryCode[Selected_CountryCode - 1] + "&category=" + Category[i];
        Log.d("run: ", url);
        try {

            Log.d("run: ", url);
            final Call<Model> modelList = NewsAPI.getService().getModelList(Category[i], sKey, CountryCode[Selected_CountryCode - 1], "en");
            modelList.enqueue(new Callback<Model>() {
                @Override
                public void onResponse(Call<Model> call, retrofit2.Response<Model> response) {
                    Model list = response.body();
                    int temp=list.getArticles().size();
                    Log.d("Retrofit list ",  temp+ "");
                }

                @Override
                public void onFailure(Call<Model> call, Throwable t) {
                    Log.e("onFailure", " Some error Occur", t);
                }
            });
            StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response: ", response.toString());
                    //mProgressDialog.dismiss();
                    //SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
                    //queryBuilder.setTables(tablename);
                    try {
                        JSONObject jObject = new JSONObject(response);
                        String status = jObject.getString("status");
                        //String sortby = jObject.getString("sortBy");
                        Log.e("Response: ", response.toString());
                        if (status.equals("ok")) {
                            JSONArray jsonArray = jObject.getJSONArray("articles");
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonobject = jsonArray.getJSONObject(j);
                                Log.e("InnerData: ", jsonobject.toString());
                                NewsModel mDataList = new NewsModel();
                                String date = jsonobject.getString("publishedAt");
                                Log.d("onResponse: ", date);

                                if (!TextUtils.isEmpty(date) && !TextUtils.equals(date, "null")) {
                                    String[] arr = date.split("-");
                                    Log.d("onResponse: ", arr[0]);
                                    Log.d("onResponse: ", arr[1]);
                                    //arr[2]=extractNumber(arr[2]);
                                    Log.d("onResponse: ", arr[2]);
                                    mDataList.setDateArray(arr);
                                }

                                mDataList.setDescription(jsonobject.getString("description"));
                                mDataList.setTitle(jsonobject.getString("title"));
                                mDataList.setUrl(jsonobject.getString("url"));
                                mDataList.setimage(jsonobject.getString("urlToImage"));

                                if (i == 0) {
                                    Business_List.add(mDataList);
                                } else if (i == 1) {
                                    Entertainment_List.add(mDataList);
                                } else if (i == 2) {
                                    Health_List.add(mDataList);
                                } else if (i == 3) {
                                    Science_List.add(mDataList);
                                } else if (i == 4) {
                                    Sports_List.add(mDataList);
                                } else if (i == 5) {
                                    Technology_List.add(mDataList);
                                }

                            }
                            if (i < 5) {
                                i++;
                                run();
                            } else {
                                int temp1 = Business_List.size();
                                int temp2 = Entertainment_List.size();
                                int temp3 = Health_List.size();
                                int temp4 = Science_List.size();
                                int temp5 = Sports_List.size();
                                int temp6 = Technology_List.size();
                                Log.d("getListFromIntent: ", temp1 + "-" + temp2 + "-" + temp3 + "-" + temp4 + "-" + temp5 + "-" + temp6);
                                //mProgressDialog.dismiss();
                                Intent i = new Intent(MyContext, MainActivity.class);
                                //Bundle bundle = new Bundle();
                                i.putExtra(Category[0], (Serializable) Business_List);
                                i.putExtra(Category[1], (Serializable) Entertainment_List);
                                i.putExtra(Category[2], (Serializable) Health_List);
                                i.putExtra(Category[3], (Serializable) Science_List);
                                i.putExtra(Category[4], (Serializable) Sports_List);
                                i.putExtra(Category[5], (Serializable) Technology_List);
                                //i.putExtra("MosqueList",(Serializable)list);
                                //i.putExtra(Category[0], (Serializable) Business_List);
                                MyContext.startActivity(i);
                            }


                        }


                    } catch (NullPointerException ex) {
                        Log.e("Error ", ex.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //mProgressDialog.dismiss();
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        /*Toast.makeText(MyContext, "Failed to fetch data." +
                                " Please check your network connection", Toast.LENGTH_SHORT).show();*/
                        Reload_Dialog("Failed to fetch data." +
                                " Please check your network connection");

                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(MyContext, "Failed to fetch data." + " Please check your network connection", Toast.LENGTH_SHORT).show();
                        Reload_Dialog("No internet connection");
                        //TODO
                    } else if (error instanceof ServerError) {
                        //TODO
                        Log.e("Server error", error.toString());
                        Reload_Dialog("Server error");
                    } else if (error instanceof NetworkError) {
                        //TODO
                        Log.e("Network error", error.toString());
                        Reload_Dialog("Network error");
                    } else if (error instanceof ParseError) {
                        //TODO
                        Log.e("Parse Error ", error.toString());
                        Reload_Dialog("Network error");
                    }
                }
            }) {

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    if (response.headers == null) {
                        // cant just set a new empty map because the member is final.
                        response = new NetworkResponse(
                                response.statusCode,
                                response.data,
                                Collections.<String, String>emptyMap(), // this is the important line, set an empty but non-null map.
                                response.notModified,
                                response.networkTimeMs);
                    }

                    return super.parseNetworkResponse(response);
                }
            };

            //*********To Retry sending*********************************************
            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue queue = Volley.newRequestQueue(MyContext);
            queue.add(jsonArrayRequest);


        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } catch (IllegalFormatException ex) {
            ex.printStackTrace();
        }

    }
}
