package healthportfolios.techno.healthportfolios;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class Show_reports extends AppCompatActivity {


    SharedPreferences sharepref;
    CustomRecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    Boolean isInternetPresent = false;
    ArrayList results;
    String userid,user_mono,user_email,res;

    TextView tv_username,tv_logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_reports);


        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/ProductSans-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        //==fontChanger.replaceFonts((ViewGroup)convertView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        user_email=sharepref.getString("key_useremail", null).toString();
        user_mono=sharepref.getString("key_usermobno", null).toString();
        userid=sharepref.getString("key_userID", null).toString();




        tv_logout=(TextView)this.findViewById(R.id.btn_logout);
        tv_username=(TextView)this.findViewById(R.id.tv_useremail);

        tv_username.setText(user_email);
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Show_reports.this, HomeScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                sharepref.edit().putString("key_login","no").commit();
                sharepref.edit().putString("key_useremail","no user").commit();

            }
        });
        // Initialize recycler view
        mRecyclerView = (CustomRecyclerView) findViewById(R.id.reports_recyclierview);
        mRecyclerView.setHasFixedSize(true);


        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

       /* // check for Internet status
        if (isInternetPresent) {*/
            // Internet Connection is Present
            // make HTTP requests

            results = new ArrayList<ReportsListviewObject>();
            mAdapter = new MyRecyclerAdapter_Post(results, getBaseContext());

            new GetAllReports().execute();



        /*} else {
            // Internet connection is not present
            // Ask user to connect to Internet

            mRecyclerView.setVisibility(View.GONE);

        }*/



    }


    class GetAllReports extends AsyncTask<Object, Void, String> {

        /*
               protected ProgressDialog progressDialog;*/


        protected ProgressDialog progressDialog;
        String response_string;

        String item_ingre;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");

            progressDialog = ProgressDialog.show(Show_reports.this, "Loading", "Please Wait...", true, false);
            //inicia diálogo de progress, mostranto processamento com servidor.
            // progressDialog = ProgressDialog.show(Home_screen_navigation.this, "Loading", "Please Wait--------.", true, false);
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

                HttpPost post = new HttpPost("http://myhealthportfolios.com/usermedicalreports.php?id="+userid);
                List<NameValuePair> params = new ArrayList<NameValuePair>();


                //response mate niche no code

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);
                HttpEntity resEntity = responsePOST.getEntity();
                if (resEntity != null) {
                    String resp = EntityUtils.toString(resEntity);
                    res = resp;

                    //  System.out.println("response got from server----- "+resp);


                }}catch(Exception e){
                e.printStackTrace();

            }

            return res;

        }



        @Override
        protected void onPostExecute(String result)
        {

            String response_string="";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res == null || res.equals("")) {



                Toast.makeText(Show_reports.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                // Log.i("RESPONSE", res);

                response_string = obj.getString("info");


                JSONArray array_res = new JSONArray(response_string);

                if (array_res.length() == 0) {

                    Toast.makeText(Show_reports.this,"No data found!",Toast.LENGTH_LONG).show();

                } else {



                    mRecyclerView.setAdapter(mAdapter);





                    for (int j = 0; j < array_res.length(); j++) {

                        String patient_id = array_res.getJSONObject(j).getString("patient_id");
                        String date_of_report = array_res.getJSONObject(j).getString("date_of_report");
                        String labs = array_res.getJSONObject(j).getString("labs");
                        String report_type = array_res.getJSONObject(j).getString("report_type");
                        String medical_report_url = array_res.getJSONObject(j).getString("medical_report");
                        String medical_report_id = array_res.getJSONObject(j).getString("medical_report_id");





                        ReportsListviewObject obj1 = new ReportsListviewObject(date_of_report,"Report Type : "+report_type,"Report ID : "+medical_report_id,"Lab : "+labs,medical_report_url,null,null);
                        results.add(obj1);



                        // Log.d("usr Img",j+"=="+ Rpost_image);
                    }


                }
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog.dismiss();

            mRecyclerView.setVisibility(View.VISIBLE);




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


    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        super.onBackPressed();

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                System.exit(0);
                getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
            finish();

    }

}
