package arihantmart.techno.arihantmart;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

public class Orderhistory extends AppCompatActivity {


    CustomRecyclerView mRecyclerView;

    ArrayList results;
    private CustomRecyclerView.Adapter adapter;


    Boolean isInternetPresent = false;
    SharedPreferences sharepref;
    String res,user_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderhistory);


        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/ProductSans-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        //==fontChanger.replaceFonts((ViewGroup)convertView);

      getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize recycler view
        mRecyclerView = (CustomRecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        user_email=sharepref.getString("key_useremail", "null");
        // username=sharepref.getString("key_username", "null");




        results = new ArrayList<DataObject_ShortOrderlist>();
        adapter = new MyRecyclerAdapter_ShortOrderlist(results, this);






        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests

            new GetUserOrders().execute();


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

            Toast.makeText(Orderhistory.this, "  No Internet Connection!!!.  ", Toast.LENGTH_LONG).show();


        }





    }


    class GetUserOrders extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String item_ingre;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(Orderhistory.this, "Loading", "Please Wait...", true, false);
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

                HttpPost post = new HttpPost("http://arihantmart.com/androidapp/user_orders.php?email_address="+user_email);//2015-5-15
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
                Toast.makeText(Orderhistory.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                // Log.i("RESPONSE", res);

                response_string=obj.getString("info");

                JSONArray array_res=new JSONArray(response_string);



                mRecyclerView.setAdapter(adapter);
                results.clear();

                String items="";

                for (int j = 0; j < array_res.length(); j++) {


                    String item_name = array_res.getJSONObject(j).getString("item_name");
                    String item_id = array_res.getJSONObject(j).getString("item_id");
                    String mrp = array_res.getJSONObject(j).getString("mrp");
                    String ourprice = array_res.getJSONObject(j).getString("ourprice");
                    String user_email = array_res.getJSONObject(j).getString("user_email");
                    String item_quantity = array_res.getJSONObject(j).getString("item_quantity");
                    String order_time = array_res.getJSONObject(j).getString("order_time");
                    String order_number = array_res.getJSONObject(j).getString("order_number");
                    String order_id = array_res.getJSONObject(j).getString("order_id");
                    String order_status = array_res.getJSONObject(j).getString("order_status");

                    int MRP = Integer.parseInt(mrp);
                    int OURPRICE = Integer.parseInt(ourprice);
                    int QUTY = Integer.parseInt(item_quantity);


                    int PER_ITEM_SAVE = MRP - OURPRICE;
                    int PER_ITEM_TOTATSAVE = PER_ITEM_SAVE * QUTY;

                    int PER_ITEM_TOTAL = OURPRICE * QUTY;

                    Total_cart_amount = Total_cart_amount + PER_ITEM_TOTAL;
                    Total_cart_saving_amount = Total_cart_saving_amount + PER_ITEM_TOTATSAVE;


                    DateFormat df_post = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
                    df_post.setTimeZone(TimeZone.getTimeZone("MST"));

                    Date date_post = df_post.parse(order_time);
                    //String Rpost_time_new=df_post.format(date_post.getTime());
                    //Log.d("without adding zone",new SimpleDateFormat("dd/MM/yyyy hh:mm aa").format(date_post.getTime()));


                    // Rpost_time=new SimpleDateFormat("dd/MM/yyyy hh:mm aa").format(date_post.getTime() +TimeZone.getDefault().getOffset((date_post.getTime())));

                    order_time = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.US).format(date_post.getTime());

                    /*if(j>0 && order_number.equalsIgnoreCase(array_res.getJSONObject(j-1).getString("order_number"))){

                        items = items+"\n\n"+item_name+"( MRP: ₹ "+mrp+")"+"\n"+"Our Price : ₹ "+ourprice+"\t\t\tQty: "+item_quantity+"\t\t\t"+"= ₹ "+Integer.toString(PER_ITEM_TOTAL);
                    }else{
                        items = items+"\n\n"+item_name+"( MRP: ₹ "+mrp+")"+"\n"+"Our Price : ₹ "+ourprice+"\t\t\tQty: "+item_quantity+"\t\t\t"+"= ₹ "+Integer.toString(PER_ITEM_TOTAL);

                    }*/

                    items =item_name+"( MRP: ₹ "+mrp+")"+"\n\n"+"Our Price : ₹ "+ourprice+"\t\t\tQty: "+item_quantity+"\t\t\t"+"= ₹ "+Integer.toString(PER_ITEM_TOTAL);


                    DataObject_ShortOrderlist obj1 = new DataObject_ShortOrderlist(order_number ,order_time, user_email, Integer.toString(PER_ITEM_TOTAL),Integer.toString(PER_ITEM_TOTATSAVE),order_status);
                    results.add(obj1);
                    adapter.notifyDataSetChanged();




                }

                //tv_summary.setText("Summary(till today) :\n"+"Total Paid : ₹"+Total_cart_amount+"\nTotal Save : ₹"+Total_cart_saving_amount);





                // Log.d("usr email", Ruser_email);





            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog.dismiss();




        }
    }


















    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
