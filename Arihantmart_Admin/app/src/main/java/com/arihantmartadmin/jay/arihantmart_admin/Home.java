package com.arihantmartadmin.jay.arihantmart_admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;


public class Home extends AppCompatActivity {


    Button btn_allorders,btn_allitems,btn_newadd,btn_dailyoffer;

    private static final String TAG = "MainActivity";

    SharedPreferences sharepref;

    Boolean isInternetPresent = false;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/ProductSans-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        //==fontChanger.replaceFonts((ViewGroup)convertView);

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        if (android.os.Build.VERSION.SDK_INT > 14) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        searchView=(SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("Itemname/Company name/Brand name");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent result = new Intent(Home.this, Sub_categorylist.class);
                result.putExtra("category_name", query);
                result.putExtra("category_grid_id","null");
                result.putExtra("category_image","miscellaneous_icon");
                result.putExtra("search_keyword",query);
                startActivity(result);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });


        btn_allorders=(Button)this.findViewById(R.id.btn_allorders);
        btn_allitems=(Button)this.findViewById(R.id.btn_viewall);
        btn_newadd=(Button)this.findViewById(R.id.btn_addnew);
        btn_dailyoffer=(Button)this.findViewById(R.id.btn_dailyoffer);

        btn_allorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,Orders_short.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btn_allitems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,Show_allitems.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btn_newadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,Add_new_items.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btn_dailyoffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,AddDailyOffer.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }
}
