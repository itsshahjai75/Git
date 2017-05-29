package com.arihantmartadmin.jay.arihantmart_admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.StrictMode;
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

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

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

public class Login extends AppCompatActivity {


    Button btn_login;

    EditText et_email,et_password;
    String email,password,res,refreshedToken;

    SharedPreferences sharepref;

    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  //==1) for activity====
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/ProductSans-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        //==fontChanger.replaceFonts((ViewGroup)convertView);

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);




        btn_login=(Button)this.findViewById(R.id.btn_signin);


        et_email=(EditText)this.findViewById(R.id.et_email);
        et_password=(EditText)this.findViewById(R.id.et_password);


        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        final String email_txt = et_email.getText().toString().trim();

        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetPresent) {
                    // Internet Connection is Present
                    // make HTTP requests


                    if(et_email.getText().toString().length()==0 || !et_email.getText().toString().matches(emailPattern)
                            || et_email.getText().toString().matches("[0-9]+@[0-9]+@[0-9]")
                            || et_email.getText().toString().equalsIgnoreCase("abc@abc.com")){
                        et_email.setError("Enter Valid Email Address!");
                    }else if(et_password.getText().toString().length()<6){
                        et_password.setError("Password must be 6 digit or more!");
                    }else {

                        email=et_email.getText().toString();
                        password=et_password.getText().toString();
                        refreshedToken = FirebaseInstanceId.getInstance().getToken();
                       // Log.d("token",refreshedToken);
                        new UserLoginTask().execute();
                    }


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

                    Toast.makeText(Login.this, "  No Internet Connection!!!.  ", Toast.LENGTH_LONG).show();


                }



            }
        });

       /* if(sharepref.getString("key_login", "no").equals("yes")){

            startActivity(new Intent(Login.this,Home.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();

        }else{

            startActivity(new Intent(Login.this,Login.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();

        }
*/




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
            progressDialog = ProgressDialog.show(Login.this, "Loading", "Please Wait...", true, false);
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

                HttpPost post = new HttpPost("http://arihantmart.com/androidapp/loginadmin.php?email_address="+email+"&password="+password+"&token_firebase="+refreshedToken);
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

                Toast.makeText(Login.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                Log.i("RESPONSE", res);


                response_string=obj.getString("Login");




                    if(response_string.length()!=0 || !response_string.isEmpty() || response_string!=null){

                        if(response_string.equalsIgnoreCase("Not matched")){


                            Snackbar snackbar = Snackbar
                                    .make(findViewById(android.R.id.content), "E-mail or Password does not matched !", Snackbar.LENGTH_LONG)
                                    .setAction("try again", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {


                                        }
                                    });

                            // Changing message text color
                            snackbar.setActionTextColor(Color.BLUE);

                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.YELLOW);
                            snackbar.show();

                            Toast.makeText(Login.this,"E-mail or Password does not matched !",Toast.LENGTH_LONG).show();

                        }

                        else{

                            JSONArray array_res=new JSONArray(response_string);



                            String user_email = array_res.getJSONObject(0).getString("email");
                            String mobileno = array_res.getJSONObject(0).getString("mobileno");
                            String id = array_res.getJSONObject(0).getString("admin_id");
                            Log.d("usr email",user_email);

                            sharepref.edit().putString("key_login","yes").commit();
                            sharepref.edit().putString("key_useremail", user_email).commit();
                            sharepref.edit().putString("key_usermobno", mobileno).commit();

                            sharepref.edit().putString("key_userID", id).commit();

                            Intent login_pg = new Intent(Login.this,Home.class);
                            startActivity(login_pg);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            finish();

                        }
                    }else{


                        Snackbar snackbar = Snackbar
                                .make(findViewById(android.R.id.content),"Sorry!!!Something Wrong try after some time !", Snackbar.LENGTH_LONG)
                                .setAction("try again", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                    }
                                });

                        // Changing message text color
                        snackbar.setActionTextColor(Color.BLUE);

                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();

                        Toast.makeText(Login.this,"Sorry!!!Something Wrong try after some time !",Toast.LENGTH_LONG).show();


                    }






            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog.dismiss();



        }

        @Override
        protected void onCancelled() {

        }
    }









}
