package arihantmart.techno.arihantmart;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainHomeFragment extends Fragment {

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
    public MainHomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "fonts/ProductSans-Regular.ttf");
       // fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
         //==fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        View convertView = inflater.inflate(R.layout.fragment_main_home, container, false);
        fontChanger.replaceFonts((ViewGroup)convertView);


        pager_indicator = (LinearLayout) convertView.findViewById(R.id.viewPagerCountDots);


        intro_images = (ViewPager) convertView.findViewById(R.id.pager_introduction);
       /* btnNext = (ImageButton) convertView.findViewById(R.id.btn_next);
        btnFinish = (ImageButton) convertView.findViewById(R.id.btn_finish);


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




        collapsingToolbarLayout = (CollapsingToolbarLayout)convertView.findViewById(R.id.collapsing_toolbar);
        //toolbar = (Toolbar)convertView.findViewById(R.id.toolbar_cart);

        collapsingToolbarLayout.setTitle("");

        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAdapter = new ViewPagerAdapter(getContext(), mImageResources);
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
        timer.schedule(new UpdateTimeTask(), 2000, 6000);



        ScrollableGridView gridview = (ScrollableGridView) convertView.findViewById(R.id.gridview);

        final List<ItemObject_gridview> allItems = getAllItemObject();
        CustomAdapter_Gridview customAdapter = new CustomAdapter_Gridview(getActivity(), allItems);
        gridview.setAdapter(customAdapter);


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = items.get(position).getContent().toString();


               /* selectedItem = selectedItem.substring(0, selectedItem.length() - 1);
                selectedItem = selectedItem.toLowerCase();*/
               // Toast.makeText(getActivity(), selectedItem+"position=="+position, Toast.LENGTH_SHORT).show();

                //Log.e("selected position=",Integer.toString(position));
                Intent result = new Intent(getActivity(), Sub_categorylist.class);
                result.putExtra("category_name", selectedItem);
                result.putExtra("category_grid_id", Integer.toString(position));
                result.putExtra("category_image",items.get(position).getImageResource());
                result.putExtra("search_keyword","null");
                startActivity(result);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });


        return convertView;
    }


    private void setUiPageViewController() {

        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getContext());
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
                        //Log.i("timer_+", abc);
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
