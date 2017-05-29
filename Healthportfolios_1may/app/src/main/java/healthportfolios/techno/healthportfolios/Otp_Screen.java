package healthportfolios.techno.healthportfolios;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;

public class Otp_Screen extends AppCompatActivity {

    Button btn_verifyotp;

    SharedPreferences sharepref;
    Boolean isInternetPresent = false;
    EditText et_otpo;
    String res,str_otp,str_email,str_mono;
    TextView tv_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp__screen);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/ProductSans-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        //==fontChanger.replaceFonts((ViewGroup)convertView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        str_mono=getIntent().getStringExtra("mobileno");
        str_email=getIntent().getStringExtra("email");

        tv_string=(TextView)this.findViewById(R.id.tv_otptext);
        tv_string.setText("Hello,\nUser\nWe sent you a One Time Password on "+str_mono+ "for verification process.");

        et_otpo=(EditText)this.findViewById(R.id.et_otp);
        btn_verifyotp = (Button) this.findViewById(R.id.btn_verifyoitp);
        btn_verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (isInternetPresent) {*/
                    // Internet Connection is Present
                    // make HTTP requests


                    if(et_otpo.getText().toString().length()<6 || et_otpo.getText().toString().length()==0){
                        et_otpo.setError("Invalid Mobile no!");
                    }else {


                        str_otp=et_otpo.getText().toString();

                        new UserLoginTask().execute();
                    }


                /*}else{

                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), " Sorry! No Internet!!!", Snackbar.LENGTH_LONG);

                    // Changing message text color
                    snackbar.setActionTextColor(Color.BLUE);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();

                    Toast.makeText(Otp_Screen.this, "  No Internet Connection!!!.  ", Toast.LENGTH_LONG).show();


                }*/




            }
        });

    }




    public class UserLoginTask extends AsyncTask<Object, Void, String> {


        protected ProgressDialog progressDialog;
        String response_string;

        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            Log.d("pre execute", "Executando onPreExecute ingredients");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(Otp_Screen.this, "Loading", "Please Wait--------.", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }


        @Override
        protected String doInBackground(Object... parametros) {
            // TODO: attempt authentication against a network service.


            Log.d("post execute", "Executando doInBackground   ingredients");



            try{
                //request mate nicheno code

                HttpClient client = new DefaultHttpClient();
                //String postURL = "http://169.254.76.188:8084/Sunil/order_entery";
                //HttpPost post = new HttpPost(postURL);

                HttpPost post = new HttpPost("http://myhealthportfolios.com/check_otp.php?email_address="+str_email+"&&otp="+str_otp);
                List<NameValuePair> params = new ArrayList<NameValuePair>();


                //response mate niche no code

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);
                HttpEntity resEntity = responsePOST.getEntity();
                if (resEntity != null) {
                    String resp = EntityUtils.toString(resEntity);
                    res = resp;

                    //   System.out.println("response got from server----- "+resp);


                }}catch(Exception e){
                e.printStackTrace();

            }

            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res == null || res.equals("")) {

                progressDialog.dismiss();

                Toast.makeText(Otp_Screen.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            } else {
                try {
                    JSONObject obj = new JSONObject(res);


                    // Log.i("RESPONSE", res);

                    response_string = obj.getString("info");

                    JSONArray array_res = new JSONArray(response_string);

                    String ID = array_res.getJSONObject(0).getString("ID");
                    String user_email = array_res.getJSONObject(0).getString("user_email");
                    String display_name = array_res.getJSONObject(0).getString("display_name");
                    String user_status = array_res.getJSONObject(0).getString("user_status");
                    String otp = array_res.getJSONObject(0).getString("otp");


                    Intent reports = new Intent(Otp_Screen.this, Show_reports.class);
                    reports.putExtra("mobileno", str_mono);
                    reports.putExtra("email", user_email);
                    reports.putExtra("userid", ID);
                    startActivity(reports);


                    sharepref.edit().putString("key_login","yes").commit();
                    sharepref.edit().putString("key_useremail", user_email).commit();
                    sharepref.edit().putString("key_usermobno", str_mono).commit();
                    sharepref.edit().putString("key_userID", ID).commit();

                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();


                    // Log.d("usr email", Ruser_email);


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }



            }


    }

}
