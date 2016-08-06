package arihantmart.techno.arihantmart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Delivery_details extends AppCompatActivity {


    EditText et_name,et_mono,et_add1,et_add2,et_add3,et_landmark,et_city,et_pin;

    Button btn_submit;

    String name,email,mobno,add1,add2,add3,landmakr,city,pin,res;

    Boolean isInternetPresent = false;
    SharedPreferences sharepref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_details);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/ProductSans-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        //==fontChanger.replaceFonts((ViewGroup)convertView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        email=sharepref.getString("key_useremail", "null");


        et_name = (EditText)this.findViewById(R.id.et_name);
        et_mono = (EditText)this.findViewById(R.id.et_mobno);
        et_add1 = (EditText)this.findViewById(R.id.et_address1);
        et_add2 = (EditText)this.findViewById(R.id.et_address2);
        et_add3 = (EditText)this.findViewById(R.id.et_address3);
        et_landmark = (EditText)this.findViewById(R.id.et_lanmark);
        et_city = (EditText)this.findViewById(R.id.et_city);
        et_pin = (EditText)this.findViewById(R.id.et_pincode);

        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        if(isInternetPresent){
            new GetUserData().execute();
        }else{

        }


        btn_submit = (Button)this.findViewById(R.id.btn_submit_delivery);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(et_name.getText().toString().length()==0){
                    et_name.setError("Enter Name!");
                }else if(et_mono.getText().toString().length()==0
                        || et_mono.getText().toString().length() < 10
                        || et_mono.getText().toString().equalsIgnoreCase("1234567890")){
                    et_mono.setError("Enter Correct Mobile Number!");
                }else if(et_add1.getText().toString().length()==0){
                    et_add1.setError("Enter Details!");
                }else if(et_add2.getText().toString().length()==0){
                    et_add2.setError("Enter Details!");
                }else if(et_add3.getText().toString().length()==0){
                    et_add3.setError("Enter Details!");
                }else if(et_landmark.getText().toString().length()==0){
                    et_landmark.setError("Enter Details!");
                }else if(et_city.getText().toString().length()==0){
                    et_city.setError("Enter Details!");
                }else if(et_pin.getText().toString().length()==0 || et_pin.getText().toString().length()>6
                        || et_pin.getText().toString().length()<6){
                    et_pin.setError("Enter proper pin!");
                }/*else if( !et_city.getText().toString().contains("Botad")
                  || !et_city.getText().toString().contains("botad")){
                    et_city.setError("Sorry We are now only in Botad/Bhavnagar,Gujarat!");
                }*/else if(!et_pin.getText().toString().contains("364")){
                    et_pin.setError("Sorry We are now only in Botad/Bhavnagar,Gujarat!");
                }
                else{

                    name=et_name.getText().toString();
                    mobno=et_mono.getText().toString();
                    add1=et_add1.getText().toString();
                    add2=et_add2.getText().toString();
                    add3=et_add3.getText().toString();
                    landmakr=et_landmark.getText().toString();
                    city=et_city.getText().toString();
                    pin=et_pin.getText().toString();



                    if (isInternetPresent) {
                        // Internet Connection is Present
                        // make HTTP requests
                      //  new Add_delivery_address().execute();

                        Intent profile = new Intent(Delivery_details.this,Payment_method.class);
                        profile.putExtra("name", name);
                        profile.putExtra("mobile", mobno);
                        profile.putExtra("address_line1", add1);
                        profile.putExtra("address_line2", add2);
                        profile.putExtra("address_line3", add3);
                        profile.putExtra("landmark", landmakr);
                        profile.putExtra("city", city);
                        profile.putExtra("pincode", pin);
                       startActivity(profile);




                    }else{

                        Snackbar snackbar = Snackbar
                                .make(findViewById(android.R.id.content), " Sorry! No Internet!!!", Snackbar.LENGTH_LONG);

                        // Changing message text color
                        snackbar.setActionTextColor(Color.BLUE);

                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();

                        Toast.makeText(Delivery_details.this, "  No Internet Connection!!!.  ", Toast.LENGTH_LONG).show();


                    }




                }


            }
        });






    }







    class GetUserData extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String item_ingre;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(Delivery_details.this, "Loading", "Please Wait...", true, false);
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

                HttpPost post = new HttpPost("http://arihantmart.com/androidapp/info_address.php?email_address="+email);//2015-5-15
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

            String response_string="";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res == null || res.equals("")) {



                progressDialog.dismiss();
                Toast.makeText(Delivery_details.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                // Log.i("RESPONSE", res);

                response_string=obj.getString("info");

                JSONArray array_res=new JSONArray(response_string);

                String Ruser_email = array_res.getJSONObject(0).getString("email");
                String Ruser_name = array_res.getJSONObject(0).getString("name");
                String Rmobile_no = array_res.getJSONObject(0).getString("mobileno");
                String Rgender = array_res.getJSONObject(0).getString("gender");
                String Raddress_line1 = array_res.getJSONObject(0).getString("address_line1");
                String Raddress_line2 = array_res.getJSONObject(0).getString("address_line2");
                String Raddress_line3 = array_res.getJSONObject(0).getString("address_line3");
                String Rlanmark = array_res.getJSONObject(0).getString("landmark");
                String Rpincode = array_res.getJSONObject(0).getString("pincode");
                String Rcity = array_res.getJSONObject(0).getString("city");
                String Rstate = array_res.getJSONObject(0).getString("state");



                et_name.setText(Ruser_name);
                //et_email.setText(Ruser_email);
                et_mono.setText(Rmobile_no);


                if(Raddress_line1.equalsIgnoreCase("null")){

                }else{
                    et_add1.setText(Raddress_line1);
                }

                if(Raddress_line2.equalsIgnoreCase("null")){

                }else{
                    et_add2.setText(Raddress_line2);
                }

                if(Raddress_line3.equalsIgnoreCase("null")){

                }else{
                    et_add3.setText(Raddress_line3);
                }

                if(Rlanmark.equalsIgnoreCase("null")){

                }else{
                    et_landmark.setText(Rlanmark);
                }

                if(Rcity.equalsIgnoreCase("null")){

                }else{
                    et_city.setText(Rcity);
                }

                if(Rpincode.equalsIgnoreCase("null")){

                }else{
                    et_pin.setText(Rpincode);
                }




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
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }




}
