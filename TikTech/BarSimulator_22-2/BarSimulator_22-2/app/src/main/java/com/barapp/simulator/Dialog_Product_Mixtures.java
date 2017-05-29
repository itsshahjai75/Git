package com.barapp.simulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.barapp.simulator.Adapter.ProductMixture_Adapter;
import com.barapp.simulator.Object_Getter_Setter.PurchaseDetailList_Object;

import java.util.ArrayList;

public class Dialog_Product_Mixtures extends AppCompatActivity {

    RecyclerView listRecycler;
    ImageView img_back;
    ArrayList<PurchaseDetailList_Object> listWines =new ArrayList<>();

    String products , products_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_product_mixtures);

        getSupportActionBar().hide();

        listRecycler = (RecyclerView)findViewById(R.id.listwines);
        img_back   = (ImageView)findViewById(R.id.backbtn);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listRecycler.setLayoutManager(mLayoutManager);
        listRecycler.setItemAnimator(new DefaultItemAnimator());

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        products   = getIntent().getStringExtra("products");
        products_price = getIntent().getStringExtra("products_prices");


        String[] productarray = products.split(",");
        String[] productprices = products_price.split(",");


        listWines.clear();
        for(int i=0; i<productarray.length; i++){
            listWines.add(new PurchaseDetailList_Object("" , productarray[i] , productprices[i] , ""));
        }
        listRecycler.setAdapter(new ProductMixture_Adapter(listWines , Dialog_Product_Mixtures.this));

    }




}
