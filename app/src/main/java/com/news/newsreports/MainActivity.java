package com.news.newsreports;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout Refresh_Layout;
    private RecyclerView BusinessCycle,EntertainmentCycle,HealthCycle,ScienceCycle,SportsCycle,TechnologyCycle;
    private ArrayList<NewsModel> Business_List,Entertainment_List,Health_List,Science_List,Sports_List,Technology_List;
    private String[] Category={"business","entertainment","health","science","sports","technology"};
    private TextView Business_TextView,Entertainment_TextView,Health_TextView,Science_TextView,Sports_TextView,Technology_TextView,Your_Country;
    private LinearLayout Your_Country_Layout;
    private Snackbar mSnackbar;
    private ScrollView ParentLayout;
    private SharedPreferences pref;
    private String[] CountryCode=new String[54],Country_Names=new String[54];
    private SharedPreferences.Editor editor;
    private int Selected_CountryCode;
    private RadioGroup rg;
    private ProgressDialog mProgressDiglog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mProgressDiglog=new ProgressDialog(this);
        mProgressDiglog.setIndeterminate(true);
        mProgressDiglog.setTitle("Please wait");
        mProgressDiglog.setMessage("Loading...");
        mProgressDiglog.setCancelable(false);
        if (mProgressDiglog.isShowing()){
            mProgressDiglog.dismiss();
        }
        getListFromIntent();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private void CountryDialog() //Dialog box for changing the Country
    {//Typeface face= Typeface.createFromAsset(getAssets(),"font/opensans_regular.ttf");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,R.style.MyAlertDialogTheme);
        //alertDialogBuilder.set
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        LinearLayout ParentLayout=new LinearLayout(this);
        ParentLayout.setLayoutParams(param1);
        ScrollView scrollView=new ScrollView(this);
        scrollView.setScrollBarSize(0);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setHorizontalScrollBarEnabled(false);
        LinearLayout InnerLayout=new LinearLayout(this);
        InnerLayout.setLayoutParams(param1);

        rg = new RadioGroup(this); //create the RadioGroup

        rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.HORIZONTAL
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.MATCH_PARENT
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
        InnerLayout.addView(rg);
        scrollView.addView(InnerLayout);
        //ParentLayout.addView(Title);
        ParentLayout.addView(scrollView);

        alertDialogBuilder.setView(ParentLayout);
        alertDialogBuilder.setTitle("Select Your Country");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(rg.getCheckedRadioButtonId()>0){
                    pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    editor = pref.edit();
                    editor.putInt("Country code", rg.getCheckedRadioButtonId());
                    editor.apply();
                    Selected_CountryCode=rg.getCheckedRadioButtonId();
                    mProgressDiglog.show();
                    fetchingData();
                }
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        try {
            alertDialog.show();
        } catch (Exception e) {
            // WindowManager$BadTokenException will be caught and the app would
            // not display the 'Force Close' message
            e.printStackTrace();
        }


    }





    private void setListener()//Setting listener to the TextViews
    {

        Business_TextView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, Separate_Category.class);
            intent.putExtra("list", (Serializable) Business_List);
            intent.putExtra("Category", "Business");
            startActivity(intent);
        }});
        Entertainment_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Separate_Category.class);
                intent.putExtra("list",(Serializable) Entertainment_List);
                intent.putExtra("Category","Entertainment");
                startActivity(intent);
            }
        });
        Health_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Separate_Category.class);
                intent.putExtra("list",(Serializable) Health_List);
                intent.putExtra("Category","Health");
                startActivity(intent);
            }
        });
        Science_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Separate_Category.class);
                intent.putExtra("list",(Serializable) Science_List);
                intent.putExtra("Category","Science");
                startActivity(intent);
            }
        });
        Sports_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Separate_Category.class);
                intent.putExtra("list",(Serializable) Sports_List);
                intent.putExtra("Category","Sports");
                startActivity(intent);
            }
        });
        Technology_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Separate_Category.class);
                intent.putExtra("list",(Serializable) Technology_List);
                intent.putExtra("Category","Technology");
                startActivity(intent);
            }
        });

        Refresh_Layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                   fetchingData();
            }
        });

        Your_Country_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CountryDialog();
            }
        });

    }


    private void fetchingData() //Fetching data from API using thread
    {
        FetchingNews object1 = new FetchingNews();
        object1.setParams(MainActivity.this);
        Thread myThread = new Thread(object1);
        myThread.start();
    }


    private void initTextViews()//Initialising TextViews
    {
        Business_TextView=findViewById(R.id.Cat_Business);
        Entertainment_TextView=findViewById(R.id.Cat_Entertainment);
        Health_TextView=findViewById(R.id.Cat_Health);
        Science_TextView=findViewById(R.id.Cat_Science);
        Sports_TextView=findViewById(R.id.Cat_Sports);
        Technology_TextView=findViewById(R.id.Cat_Technology);
        ParentLayout=findViewById(R.id.ScrollView);
        Refresh_Layout=findViewById(R.id.Refresh_layout);
        Refresh_Layout.setRefreshing(false);
        Your_Country=findViewById(R.id.Your_Country);
        Your_Country_Layout=findViewById(R.id.Your_Country_Layout);
        Your_Country.setText(Country_Names[Selected_CountryCode-1]+" ("+CountryCode[Selected_CountryCode-1]+")");
        mSnackbar = Snackbar.make(ParentLayout, "Press again to exit ", Snackbar.LENGTH_SHORT);
        setListener();
        initRecyclerViews();
    }


    private void getListFromIntent()//getting the List from the previous activity via intent
    {
        //Bundle bundle=getIntent().getBundleExtra("Bundle");
        Business_List=(ArrayList<NewsModel>) getIntent().getSerializableExtra(Category[0]);
        Entertainment_List=(ArrayList<NewsModel>) getIntent().getSerializableExtra(Category[1]);
        Health_List=(ArrayList<NewsModel>) getIntent().getSerializableExtra(Category[2]);
        Science_List =(ArrayList<NewsModel>) getIntent().getSerializableExtra(Category[3]);
        Sports_List=(ArrayList<NewsModel>) getIntent().getSerializableExtra(Category[4]);
        Technology_List=(ArrayList<NewsModel>) getIntent().getSerializableExtra(Category[5]);
        pref = getSharedPreferences("MyPref", 0);
        Selected_CountryCode = pref.getInt("Country code", 0);
        CountryCode=getResources().getStringArray(R.array.Countries_codes);
        Country_Names=getResources().getStringArray(R.array.Countries_Names);
        int temp1=Business_List.size();
        int temp2=Entertainment_List.size();
        int temp3=Health_List.size();
        int temp4=Science_List.size();
        int temp5=Sports_List.size();
        int temp6=Technology_List.size();
        Log.d( "getListFromIntent: ",temp1+"-"+temp2+"-"+temp3+"-"+temp4+"-"+temp5+"-"+temp6);
        initTextViews();
    }


    private void initRecyclerViews() //Initialising RecyclerViews
    {
        BusinessCycle=new RecyclerView(this);
        BusinessCycle=findViewById(R.id.Business_RecyclerView);
        BusinessCycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        BusinessCycle.setAdapter(new HorizontalListAdapter(Business_List));

        EntertainmentCycle=new RecyclerView(this);
        EntertainmentCycle=findViewById(R.id.Entertainment_RecyclerView);
        EntertainmentCycle.hasFixedSize();
        EntertainmentCycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        EntertainmentCycle.setAdapter(new HorizontalListAdapter(Entertainment_List));

        HealthCycle=new RecyclerView(this);
        HealthCycle=findViewById(R.id.Health_RecyclerView);
        HealthCycle.hasFixedSize();
        HealthCycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        HealthCycle.setAdapter(new HorizontalListAdapter(Health_List));

        ScienceCycle=new RecyclerView(this);
        ScienceCycle=findViewById(R.id.Science_RecyclerView);
        ScienceCycle.hasFixedSize();
        ScienceCycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ScienceCycle.setAdapter(new HorizontalListAdapter(Science_List));

        SportsCycle=new RecyclerView(this);
        SportsCycle=findViewById(R.id.Sports_RecyclerView);
        SportsCycle.hasFixedSize();
        SportsCycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        SportsCycle.setAdapter(new HorizontalListAdapter(Sports_List));

        TechnologyCycle=new RecyclerView(this);
        TechnologyCycle=findViewById(R.id.Technology_RecyclerView);
        TechnologyCycle.hasFixedSize();
        TechnologyCycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        TechnologyCycle.setAdapter(new HorizontalListAdapter(Technology_List));

    }


    @Override
    public void onBackPressed() {
        if (mSnackbar.isShown()) {
            onStop();
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
        } else {
            mSnackbar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
