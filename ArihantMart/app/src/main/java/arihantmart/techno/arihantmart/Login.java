package arihantmart.techno.arihantmart;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

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
    Button btn_login,btn_signup;
    TextView tv_skip,tv_forgetpassword;
    EditText et_email,et_password;
    String email,password,res,email_frg_pwd,refreshedToken;

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

        if (android.os.Build.VERSION.SDK_INT > 14) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        btn_login=(Button)this.findViewById(R.id.btn_signin);
        btn_signup=(Button)this.findViewById(R.id.btn_sigup);

        et_email=(EditText)this.findViewById(R.id.et_email);
        et_password=(EditText)this.findViewById(R.id.et_password);

        tv_forgetpassword=(TextView)this.findViewById(R.id.tv_forgetpassword);
        tv_skip=(TextView)this.findViewById(R.id.tv_skip);

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

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this,
                        Signup.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


            }
        });

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                sharepref.edit().putString("key_login","yes").apply();
                sharepref.edit().putString("key_useremail", "demo@demo.com").apply();
                sharepref.edit().putString("key_usermobno", "1234567890").apply();
                sharepref.edit().putString("key_userID", "0").apply();
                sharepref.edit().putString("key_username", "Demo User").apply();

                startActivity(new Intent(Login.this,
                        Home.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();


            }
        });

        final Dialog dialog = new Dialog(Login.this);



        tv_forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                dialog.setContentView(R.layout.forget_pwd_dialog);

                dialog.setCanceledOnTouchOutside(false);
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                /*lp.width = 900;
                lp.height = 700;*/
                lp.gravity = Gravity.CENTER;
                lp.dimAmount = 0;
                dialog.getWindow().setAttributes(lp);

                final EditText et_frg_pwd =(EditText)dialog.findViewById(R.id.et_frg_pwd);
                Button btn_submit = (Button) dialog.findViewById(R.id.btn_frg_pwd);


                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        email_frg_pwd=et_frg_pwd.getText().toString();

                        if(et_frg_pwd.getText().toString().length()==0 || !et_frg_pwd.getText().toString().matches(emailPattern)
                                || et_frg_pwd.getText().toString().matches("[0-9]+@[0-9]+@[0-9]") || et_frg_pwd.getText().toString().equalsIgnoreCase("abc@abc.com"))
                        {
                            et_frg_pwd.setError("Enter Valid Email Address.");

                        }else{
                            email_frg_pwd=et_frg_pwd.getText().toString();
                            new GetPassword().execute();
                            dialog.dismiss();
                        }

                    }
                });

                dialog.show();
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
            //Log.d("pre execute", "Executando onPreExecute ingredients");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(Login.this, "Loading", "Please Wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }


        @Override
        protected String doInBackground(Object... parametros) {
            // TODO: attempt authentication against a network service.


            //Log.d("post execute", "Executando doInBackground   ingredients");



            try{
                //request mate nicheno code

                HttpClient client = new DefaultHttpClient();
                //String postURL = "http://169.254.76.188:8084/Sunil/order_entery";
                //HttpPost post = new HttpPost(postURL);

                HttpPost post = new HttpPost("http://arihantmart.com/androidapp/login.php?email_address="+email+"&password="+password+"&token_firebase="+refreshedToken);
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

                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.



                response_string=obj.getString("status");

                if(response_string.equalsIgnoreCase("OTP")){



                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), "Verify Mobile No !", Snackbar.LENGTH_LONG)
                            .setAction("OK", new View.OnClickListener() {
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

                    Toast.makeText(Login.this, "Verify Mobile No !",Toast.LENGTH_LONG).show();

                    String mobno=obj.getString("Login");



                    Intent login_pg = new Intent(Login.this,Otp_screren.class);
                    login_pg.putExtra("mobileno",mobno);
                    login_pg.putExtra("email",email);
                    startActivity(login_pg);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();



                }else{

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
                            String name = array_res.getJSONObject(0).getString("name");
                            String id = array_res.getJSONObject(0).getString("user_id");
                            //Log.d("usr email",user_email);

                            sharepref.edit().putString("key_login","yes").commit();
                            sharepref.edit().putString("key_useremail", user_email).commit();
                            sharepref.edit().putString("key_usermobno", mobileno).commit();
                            sharepref.edit().putString("key_username", name).commit();
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


    class GetPassword extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String item_ingre;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(Login.this, "Loading", "Please Wait...", true, false);
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

                HttpPost post = new HttpPost("http://arihantmart.com/androidapp/forgetpassword.php?email_address="+email_frg_pwd);//2015-5-15
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
                Toast.makeText(Login.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                // Log.i("RESPONSE", res);

                response_string = obj.getString("info");

                JSONArray array_res = new JSONArray(response_string);

                if (array_res.length() != 0) {

                    String Rpwd = array_res.getJSONObject(0).getString("password");


                    GMailSender mailsender = new GMailSender("technocratsappware@gmail.com", "technocratsappware@9033228796");
                    String[] toArr = {"technocratsappware@gmail.com", email_frg_pwd};
                    mailsender.set_to(toArr);
                    mailsender.set_from("technocratsappware@gmail.com");
                    mailsender.set_subject("Jainisam Forget Password");
                    mailsender.setBody("Hello.\nDear Member\n\n" +
                            "\nThis mail contains your password for Arihant Mart android application so we suggest you to delete it after read for safety." +
                            "\n\n" +
                            "\nPassword is:" +
                            "\n" + Rpwd +
                            "\n\nif you get this email by mistake or this is not concern with you we advise you to delete this mail as soon as possible and kindly inform to owner company Technocrats Appware, otherwise if it will contain private information so you may have to face problame issue regarding this." +
                            "\nThank You\n.........Auto Generated Mail.........");

                    try {
                        //mailsender.addAttachment("/sdcard/filelocation");

                        if (mailsender.send()) {
                            Toast.makeText(Login.this,
                                    "Email was sent successfully.",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Login.this, "Email was not sent.",
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {

                        Log.e("MailApp", "Could not send email", e);
                    }


                    // Log.d("usr email", Ruser_email);


                }else{
                    Toast.makeText(Login.this, "Sorry!!!Email not registered with us!",
                            Toast.LENGTH_LONG).show();

                }
            }

            catch(Exception e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            progressDialog.dismiss();




        }
    }










}
