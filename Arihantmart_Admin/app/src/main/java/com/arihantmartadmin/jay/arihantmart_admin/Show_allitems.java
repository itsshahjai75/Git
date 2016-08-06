package com.arihantmartadmin.jay.arihantmart_admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Show_allitems extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    //Toolbar toolbar;

    // private ImageButton btnNext, btnFinish;
    private ViewPager intro_images;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private ViewPagerAdapter mAdapter;

    private int[] mImageResources = {
            R.drawable.front_image1,
            R.drawable.front_image2,
            R.drawable.front_image3,
            R.drawable.front_image4,
    };


    List<ItemObject_gridview> items = new ArrayList<>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_allitems);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/ProductSans-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==this = inflater.inflate(R.layout.listitem, null);
        //==fontChanger.replaceFonts((ViewGroup)this);

       // sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        if (Build.VERSION.SDK_INT > 14) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.

        pager_indicator = (LinearLayout) this.findViewById(R.id.viewPagerCountDots);


        intro_images = (ViewPager) this.findViewById(R.id.pager_introduction);
       /* btnNext = (ImageButton) this.findViewById(R.id.btn_next);
        btnFinish = (ImageButton) this.findViewById(R.id.btn_finish);


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intro_images.setCurrentItem((intro_images.getCurrentItem() < dotsCount)
                        ? intro_images.getCurrentItem() + 1 : 0);

            }
        });
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().finish();
                intro_images.setCurrentItem((intro_images.getCurrentItem() < dotsCount)
                        ? 0:0);

            }
        });*/




        collapsingToolbarLayout = (CollapsingToolbarLayout)this.findViewById(R.id.collapsing_toolbar);
        //toolbar = (Toolbar)this.findViewById(R.id.toolbar_cart);

        collapsingToolbarLayout.setTitle("");

        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAdapter = new ViewPagerAdapter(Show_allitems.this, mImageResources);
        intro_images.setAdapter(mAdapter);
        intro_images.setCurrentItem(0);
        intro_images.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                }

                dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

                if (position + 1 == dotsCount) {
                   /* btnNext.setVisibility(View.GONE);
                    btnFinish.setVisibility(View.VISIBLE);*/
                } else {
                   /* btnNext.setVisibility(View.VISIBLE);
                    btnFinish.setVisibility(View.GONE);*/
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setUiPageViewController();


        Timer timer = new Timer();
        timer.schedule(new UpdateTimeTask(), 2000, 4000);



        ScrollableGridView gridview = (ScrollableGridView) this.findViewById(R.id.gridview);

        final List<ItemObject_gridview> allItems = getAllItemObject();
        CustomAdapter_Gridview customAdapter = new CustomAdapter_Gridview(Show_allitems.this, allItems);
        gridview.setAdapter(customAdapter);


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = items.get(position).getContent().toString();


               /* selectedItem = selectedItem.substring(0, selectedItem.length() - 1);
                selectedItem = selectedItem.toLowerCase();*/
                // Toast.makeText(getActivity(), selectedItem+"position=="+position, Toast.LENGTH_SHORT).show();

                Log.e("selected position=",Integer.toString(position));
                Intent result = new Intent(Show_allitems.this, Sub_categorylist.class);
                result.putExtra("category_name", selectedItem);
                result.putExtra("category_grid_id", Integer.toString(position));
                result.putExtra("category_image",items.get(position).getImageResource());
                result.putExtra("search_keyword","null");
                startActivity(result);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });



    }


    private void setUiPageViewController() {

        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(Show_allitems.this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }



    class UpdateTimeTask extends TimerTask {
        public void run() {
            intro_images.post(new Runnable() {
                public void run() {

                    if (intro_images.getCurrentItem() < 3) {
                        intro_images.setCurrentItem(intro_images.getCurrentItem() + 1, true);
                        String abc = String.valueOf(intro_images.getCurrentItem());
                        Log.i("timer_+", abc);
                    } else {
                        intro_images.setCurrentItem(0, true);
                    }
                }
            });
        }
    }



    private List<ItemObject_gridview> getAllItemObject(){
        ItemObject_gridview itemObject = null;
        items.clear();
        items.add(new ItemObject_gridview("Grains/Beans", "grain_icon"));
        items.add(new ItemObject_gridview("Masalas/Spices", "masala_icon"));
        items.add(new ItemObject_gridview("Snacks", "snacks_icon"));

        items.add(new ItemObject_gridview("Dry Fruits", "dryfruit_icon"));
        items.add(new ItemObject_gridview("Instant Cook", "instantcook_icon"));
        items.add(new ItemObject_gridview("Coffee/Tea", "teacoffice_icon"));

        items.add(new ItemObject_gridview("Breads/Breakfast", "breads_icon"));
        items.add(new ItemObject_gridview("Chocolates", "sweetchocalate_icon"));
        items.add(new ItemObject_gridview("Biscuits/Cookies", "biscuits_icon"));


        items.add(new ItemObject_gridview("Oils/Ghee", "oilghee_icon"));
        items.add(new ItemObject_gridview("Salt/Sugar", "sugarsalt_icon"));
        items.add(new ItemObject_gridview("Pickles/Sauces", "saucespikal_icon"));

        items.add(new ItemObject_gridview("Home Care", "housecleaning_icon"));
        items.add(new ItemObject_gridview("Beverages", "beverages_icon"));
        items.add(new ItemObject_gridview("Baby Care", "babycare_icon"));

        items.add(new ItemObject_gridview("Personal Care", "personalcare_icon"));
        items.add(new ItemObject_gridview("Miscellaneous", "miscellaneous_icon"));


       /* items.add(new ItemObject("Image Three", "three"));
        items.add(new ItemObject("Image Four", "four"));
        items.add(new ItemObject("Image Five", "five"));
        items.add(new ItemObject("Image Six", "six"));
        items.add(new ItemObject("Image Seven", "seven"));
        items.add(new ItemObject("Image Eight", "eight"));*/
        return items;
    }



}

