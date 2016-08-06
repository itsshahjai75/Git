package arihantmart.techno.arihantmart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CartProducts extends AppCompatActivity {

    private int visibleThreshold = 5;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;
    int load_count=1;

    Toolbar toolbar;
    CustomRecyclerView mRecyclerView;
    View footerLayout;


    ArrayList results;
    private CustomRecyclerView.Adapter adapter;


    Boolean isInternetPresent = false;

    TextView tv_totalamt,tv_savingamt;

    SharedPreferences sharepref;
    String res,user_email;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_products);


        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/ProductSans-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        //==fontChanger.replaceFonts((ViewGroup)convertView);


        collapsingToolbarLayout = (CollapsingToolbarLayout)this.findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar)this.findViewById(R.id.toolbar_cart);

        collapsingToolbarLayout.setTitle("Shoping Bag");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



         tv_totalamt = (TextView)this.findViewById(R.id.tv_total_amount);
         tv_savingamt = (TextView)this.findViewById(R.id.tv_savingamount);


   // Fetching footer layout
        footerLayout = findViewById(R.id.footerView);

// Fetching the textview declared in footer.xml

       TextView checkout = (TextView) footerLayout.findViewById(R.id.btn_chkout);

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postshare = new Intent(CartProducts.this,Delivery_details.class);
               // postshare.putExtra("postshare","post");
                startActivity(postshare);

            }
        });


        // Initialize recycler view
        mRecyclerView = (CustomRecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
         user_email=sharepref.getString("key_useremail", "null");
        // username=sharepref.getString("key_username", "null");




        results = new ArrayList<DataObject_CartItem>();
        adapter = new MyRecyclerAdapter_Cartlist(results, this);





        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests

            new GetUserCart().execute();


        } else {

            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), " Sorry! No Internet!!!", Snackbar.LENGTH_LONG);

            // Changing message text color
            snackbar.setActionTextColor(Color.BLUE);

            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();

            Toast.makeText(CartProducts.this, "  No Internet Connection!!!.  ", Toast.LENGTH_LONG).show();


        }





        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView mRecyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (mRecyclerView == null || mRecyclerView.getChildCount() == 0) ? 0 : mRecyclerView.getChildAt(0).getTop();
                // mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
//=========================================================================================================
                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    //onHide();


                    footerLayout.animate().translationY(footerLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
                    //floatingActionButton.animate().translationY(floatingActionButton.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();

                    controlsVisible = false;
                    scrolledDistance = 0;
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    //onShow();
                    footerLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                   // floatingActionButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();

                    controlsVisible = true;
                    scrolledDistance = 0;


                }

                if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                    scrolledDistance += dy;
                }

                //======================================================================================================

                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    /*if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {

                            textView_loadmore.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                            textView_loadmore.setVisibility(View.VISIBLE);



                        }
                    }*/


                    /*if (!loading && (totalItemCount - visibleItemCount) <= (pastVisiblesItems + visibleThreshold)) {
                        // End has been reached

                       *//* // Do something
                        current_page++;

                        onLoadMore(current_page);*//*

                        loading = true;

                    }*/


                }

                //=======================================================================================================
            }

            @Override
            public void onScrollStateChanged(RecyclerView mRecyclerView, int newState) {
                super.onScrollStateChanged(mRecyclerView, newState);
            }


        });




    }



    class GetUserCart extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String item_ingre;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(CartProducts.this, "Loading", "Please Wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            // System.out.println("On do in back ground----done-------");


            //Log.d("post execute", "Executando doInBackground   ingredients");



            try{
                //request mate nicheno code

                HttpClient client = new DefaultHttpClient();
                //String postURL = "http://169.254.76.188:8084/Sunil/order_entery";
                //HttpPost post = new HttpPost(postURL);

                HttpPost post = new HttpPost("http://arihantmart.com/androidapp/showcart.php?email_address="+user_email);//2015-5-15
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                //params.add(new BasicNameValuePair("key_item",item_ingredients));


                //response mate niche no code

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);
                HttpEntity resEntity = responsePOST.getEntity();
                if (resEntity != null) {
                    String resp = EntityUtils.toString(resEntity);
                    res = resp;

                    // System.out.println("response got from server----- "+resp);


                }}catch(Exception e){
                e.printStackTrace();

            }



//            progressDialog.dismiss();
            return res;

        }



        @Override
        protected void onPostExecute(String result)
        {

            int Total_cart_amount=0;
            int Total_cart_saving_amount = 0;

            String response_string="";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res == null || res.equals("")) {

                progressDialog.dismiss();
                footerLayout.setVisibility(View.GONE);
                Toast.makeText(CartProducts.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                // Log.i("RESPONSE", res);

                response_string=obj.getString("info");

                JSONArray array_res=new JSONArray(response_string);



                mRecyclerView.setAdapter(adapter);
                results.clear();

                for (int j = 0; j < array_res.length(); j++) {


                    String item_name = array_res.getJSONObject(j).getString("item_name");
                    String item_id = array_res.getJSONObject(j).getString("item_id");
                    String mrp = array_res.getJSONObject(j).getString("mrp");
                    String ourprice = array_res.getJSONObject(j).getString("ourprice");
                    String user_email = array_res.getJSONObject(j).getString("user_email");
                    String item_quantity = array_res.getJSONObject(j).getString("item_quantity");
                    String added_time = array_res.getJSONObject(j).getString("added_time");
                    String item_note = array_res.getJSONObject(j).getString("item_note");
                    String cart_item_id = array_res.getJSONObject(j).getString("cart_item_id");

                    int MRP = Integer.parseInt(mrp);
                    int OURPRICE = Integer.parseInt(ourprice);
                    int QUTY = Integer.parseInt(item_quantity);


                    int PER_ITEM_SAVE = MRP - OURPRICE;
                    int PER_ITEM_TOTATSAVE = PER_ITEM_SAVE * QUTY;

                    int PER_ITEM_TOTAL = OURPRICE * QUTY;

                    Total_cart_amount = Total_cart_amount + PER_ITEM_TOTAL;
                    Total_cart_saving_amount = Total_cart_saving_amount + PER_ITEM_TOTATSAVE;


                    DateFormat df_post = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                    df_post.setTimeZone(TimeZone.getTimeZone("MST"));

                    Date date_post = df_post.parse(added_time);
                    //String Rpost_time_new=df_post.format(date_post.getTime());
                    //Log.d("without adding zone",new SimpleDateFormat("dd/MM/yyyy hh:mm aa").format(date_post.getTime()));


                    // Rpost_time=new SimpleDateFormat("dd/MM/yyyy hh:mm aa").format(date_post.getTime() +TimeZone.getDefault().getOffset((date_post.getTime())));

                    added_time = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault()).format(date_post.getTime());



                    DataObject_CartItem obj1 = new DataObject_CartItem(item_name ,mrp+"\nOur Price: ₹ "+ourprice, "check_right_icon", Integer.toString(PER_ITEM_TOTATSAVE),cart_item_id,added_time,item_quantity,item_id);
                    results.add(obj1);
                    adapter.notifyDataSetChanged();


                }

                if(Total_cart_amount<350){
                    int final_total = Total_cart_amount+20;
                    tv_totalamt.setText("Total amt : ₹ "+Integer.toString(Total_cart_amount)+"\nShipping charge : ₹ 20"+"\nPayable : ₹ "+final_total);
                }else{
                    tv_totalamt.setText("Total amt : ₹ "+Integer.toString(Total_cart_amount));
                }


                tv_savingamt.setText("Saving amt :  ₹ "+Integer.toString(Total_cart_saving_amount));



                // Log.d("usr email", Ruser_email);





            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if(results.size()==0){

                footerLayout.setVisibility(View.GONE);
            }
            progressDialog.dismiss();




        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.resultpage_menu, menu);



        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                finish();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

       /* if(sharepref.getString("key_login","yes").equals("yes")){

            finish();
        }else{
            System.exit(0);
            finish();
        }*/
    }


}
