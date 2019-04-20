package com.news.newsreports;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.design.card.MaterialCardView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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

public class Splash_Activity extends AppCompatActivity implements View.OnClickListener {

    private String URL="https://newsapi.org/v2/top-headlines?apiKey=40ab81231e4c43fd925cdc3a0d08f7f6&language=en&";
    private ArrayList<NewsModel> Business_List,Entertainment_List,Health_List,Science_List,Sports_List,Technology_List;
    private String[] Category={"business","entertainment","health","science","sports","technology"};
    private static int i=0;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private MaterialCardView materialCardView;
    private LinearLayout parent_layout;
    private ImageView imageView;
    private String[] CountryCode=new String[54],Country_Names=new String[54];
    private RadioGroup rg;
    private Button Ok_Button;
    private int Selected_CountryCode;
    //private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       /* mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setTitle("Please wait");
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();*/
        initViews();
    }




    @Override
    public void onClick(View view) {
        materialCardView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        editor.putInt("Country code", rg.getCheckedRadioButtonId());
        editor.apply();
        Selected_CountryCode=rg.getCheckedRadioButtonId();
        fetchingData();
    }

    private void fetchingData(){
        FetchingNews object1 = new FetchingNews();
        object1.setParams(Splash_Activity.this);
        Thread myThread = new Thread(object1);
        myThread.start();
    }


    private void initViews()// Initialising all Views
    {
        materialCardView=findViewById(R.id.ParentCardView);
        parent_layout=findViewById(R.id.Parent_layout);
        imageView=findViewById(R.id.SplashLogo);
        Ok_Button=findViewById(R.id.OK_Button);
        Ok_Button.setOnClickListener(this);
        CountryCode=getResources().getStringArray(R.array.Countries_codes);
        Country_Names=getResources().getStringArray(R.array.Countries_Names);
        pref = getSharedPreferences("MyPref", 0);
        Selected_CountryCode = pref.getInt("Country code", 0);
        //Log.d( "initViews: ", Country_Code);
        if (Selected_CountryCode<=0){
            imageView.setVisibility(View.GONE);
            Ok_Button.setVisibility(View.GONE);
            createRadioButton();
        }else {
            materialCardView.setVisibility(View.GONE);
            fetchingData();

        }
    }

    private void createRadioButton() //Radio buttons is created and set to the Parent view
    {

        rg = new RadioGroup(this); //create the RadioGroup

        rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.HORIZONTAL
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(20, 0, 10, 0);
        rg.setLayoutParams(params);

        for (int i = 0; i < CountryCode.length; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setChecked(false);
            radioButton.setTextSize(15);
            radioButton.setText(Country_Names[i]+" ("+CountryCode[i]+")");
            radioButton.setId(i+1);
            rg.addView(radioButton);
        }
        parent_layout.addView(rg);//you add the whole RadioGroup to the layout
        setRadioButtonListeners();
    }


    private void setRadioButtonListeners()//Adding Listeners to Radio Buttons
    {
      rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(RadioGroup radioGroup, int i) {
              if (Ok_Button.getVisibility()==View.GONE){
                  Ok_Button.setVisibility(View.VISIBLE);
              }
              //RadioButton rb=findViewById(radioGroup.getCheckedRadioButtonId());
              Toast.makeText(Splash_Activity.this,"Selected country is "+Country_Names[radioGroup.getCheckedRadioButtonId()-1],Toast.LENGTH_LONG).show();
          }
      });
    }

}
