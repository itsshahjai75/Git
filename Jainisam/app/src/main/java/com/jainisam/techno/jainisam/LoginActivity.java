package com.jainisam.techno.jainisam;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;



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
import java.util.Arrays;
import java.util.List;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.model.people.Person;
import java.io.InputStream;
/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {



//============================FB starts here==============================================================
    CallbackManager callbackManager;
    private String facebook_id,f_name, m_name, l_name, gender, profile_image, full_name, email_id,mobile_no,password_fbgmail;


    /**
     * Id to identity READ_CONTACTS permission request.
     */
    //private static final int REQUEST_READ_CONTACTS = 0;
    String res;
    String email,password;

    SharedPreferences sharepref;
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;

    private View mLoginFormView;
    Button btn_tv_forgetpwd;
    Button btn_fb_login;

    Boolean isInternetPresent = false;
    String email_frg_pwd="";
//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        if (android.os.Build.VERSION.SDK_INT > 14) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }





        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        //populateAutoComplete();

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        final String email_txt = mEmailView.getText().toString().trim();


        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        final Dialog dialog = new Dialog(LoginActivity.this);



        btn_tv_forgetpwd= (Button)this.findViewById(R.id.btn_forgetpwd);
        btn_tv_forgetpwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {





                dialog.setContentView(R.layout.forget_pwd_dialog);

                dialog.setCanceledOnTouchOutside(false);
                    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                    lp.width = 700;
                    lp.height = 500;
                    lp.gravity = Gravity.CENTER;
                    lp.dimAmount = 0;
                    dialog.getWindow().setAttributes(lp);

     final EditText et_frg_pwd =(EditText)dialog.findViewById(R.id.et_frg_pwd);
     Button btn_submit = (Button) dialog.findViewById(R.id.btn_frg_pwd);


    btn_submit.setOnClickListener(new OnClickListener() {
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


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL || isInternetPresent) {

                    email = mEmailView.getText().toString();
                    password = mPasswordView.getText().toString();
                    showProgress(true);
                    mAuthTask = new UserLoginTask();
                    mAuthTask.execute();

                    return true;
                }
                return false;
            }
        });



        Button btn_signin = (Button) findViewById(R.id.btn_signin);
        btn_signin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetPresent) {
                    // Internet Connection is Present
                    // make HTTP requests


                    if(mEmailView.getText().toString().length()==0 || !mEmailView.getText().toString().matches(emailPattern)
                            || mEmailView.getText().toString().matches("[0-9]+@[0-9]+@[0-9]")
                            || mEmailView.getText().toString().equalsIgnoreCase("abc@abc.com")){
                        mEmailView.setError("Enter Valid Email Address!");
                    }else if(mPasswordView.getText().toString().length()<6){
                        mPasswordView.setError("Password must be 6 digit or more!");
                    }else {

                        email=mEmailView.getText().toString();
                        password=mPasswordView.getText().toString();
                        showProgress(true);
                        mAuthTask = new UserLoginTask();
                        mAuthTask.execute();
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

                    Toast.makeText(LoginActivity.this, "  No Internet Connection!!!.  ", Toast.LENGTH_LONG).show();


                }




            }
        });

        Button btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this,
                        Signup.class));
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        btn_fb_login= (Button)findViewById(R.id.btn_fb_login);





        btn_fb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                final Dialog dialog = new Dialog(LoginActivity.this);


                dialog.setContentView(R.layout.loginbyfb_mobile);

                dialog.setCanceledOnTouchOutside(false);
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.width = 700;
                lp.height = 500;
                lp.gravity = Gravity.CENTER;
                lp.dimAmount = 0;
                dialog.getWindow().setAttributes(lp);

                final EditText et_mobileno = (EditText) dialog.findViewById(R.id.et_loginbyfb_mobile);
                final LoginButton btn_submit = (LoginButton) dialog.findViewById(R.id.btn_loginbyfb_submit);


                btn_submit.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        mobile_no = et_mobileno.getText().toString();

                        if (et_mobileno.getText().toString().length() == 0
                                || et_mobileno.getText().toString().length() < 10
                                || et_mobileno.getText().toString().equalsIgnoreCase("1234567890")) {
                            et_mobileno.setError("Enter Valid Mobile No!!!");

                        } else {

                            callbackManager = CallbackManager.Factory.create();

                            dialog.dismiss();

                            btn_submit.setReadPermissions("public_profile email");

                            btn_submit.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                                @Override
                                public void onSuccess(LoginResult loginResult) {

                                    facebook_id = f_name = m_name = l_name = gender = profile_image = full_name = email_id = "";

                                    if (AccessToken.getCurrentAccessToken() != null) {
                                        RequestData();
                                        Profile profile = Profile.getCurrentProfile();
                                        if (profile != null) {
                                            facebook_id = profile.getId();
                                            Log.e("facebook_id", facebook_id);
                                            f_name = profile.getFirstName();
                                            Log.e("f_name", f_name);
                                            m_name = profile.getMiddleName();
                                            Log.e("m_name", m_name);
                                            l_name = profile.getLastName();
                                            Log.e("l_name", l_name);
                                            full_name = profile.getName();
                                            Log.e("full_name", full_name);
                                            profile_image = profile.getProfilePictureUri(400, 400).toString();
                                            Log.e("profile_image", profile_image);
                                        }
                    /*share.setVisibility(View.VISIBLE);
                    details.setVisibility(View.VISIBLE);*/
                                    }
                                }

                                @Override
                                public void onCancel() {

                                }

                                @Override
                                public void onError(FacebookException exception) {
                                }
                            });


                        }

                    }
                });

                dialog.show();

            }
        });


    }

    public void RequestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object,GraphResponse response) {

                JSONObject json = response.getJSONObject();
                System.out.println("Json data :"+json);
                try {
                    if(json != null){
                        String text = "<b>Name :</b> "+json.getString("name")+"<br><br><b>Email :</b> "+json.getString("email")
                                +"<br><br><b>Profile link :</b> "+json.getString("link");
                        //details_txt.setText(Html.fromHtml(text));
                        email=json.getString("email");
                        password_fbgmail="loginbyfb"+facebook_id;
                        Log.d("FB_DATA== ",text);
                        //profile.setProfileId(json.getString("id"));
                        new Signup_async().execute();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            callbackManager.onActivityResult(requestCode, resultCode, data);

    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
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
            progressDialog = ProgressDialog.show(LoginActivity.this, "Loading", "Please Wait--------.", true, false);
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

                HttpPost post = new HttpPost("http://technocratsappware.com/androidapps/login.php?email_address="+email+"&password="+password);
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
            mAuthTask = null;
            System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res == null || res.equals("")) {



                Toast.makeText(LoginActivity.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


               Log.i("RESPONSE", res);

                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.



                response_string=obj.getString("Login");

                JSONArray array_res=new JSONArray(response_string);

                String user_email = array_res.getJSONObject(0).getString("email_address");
                String user_name = array_res.getJSONObject(0).getString("user_name");
                Log.d("usr email",user_email);

                if(user_email.equalsIgnoreCase("NO")){


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

                    Toast.makeText(LoginActivity.this,"E-mail or Password does not matched !",Toast.LENGTH_LONG).show();

                }

                else{


                    /*String SRtime=getIntent().getStringExtra("PsunriseTime");
                    String SStime=getIntent().getStringExtra("PsunsetTime");
                    String DLtime=getIntent().getStringExtra("PdayLength");
                    String NLtime=getIntent().getStringExtra("PnightLength");
                    String Dchoghdiya=getIntent().getStringExtra("PdaychoghdiyaDuration");
                    String Nchoghdiya=getIntent().getStringExtra("PnightchoghdiyaDuration");
*/

                    sharepref.edit().putString("key_login","yes").commit();
                    sharepref.edit().putString("key_useremail", user_email).commit();
                    sharepref.edit().putString("key_username", user_name).commit();

                    Intent login_pg = new Intent(LoginActivity.this,Home_screen_navigation.class);

                  /*  login_pg.putExtra("PsunriseTime", SRtime);
                    login_pg.putExtra("PsunsetTime", SStime);
                    login_pg.putExtra("PdayLength", DLtime);
                    login_pg.putExtra("PnightLength", NLtime);
                    login_pg.putExtra("PdaychoghdiyaDuration", Dchoghdiya);
                    login_pg.putExtra("PnightchoghdiyaDuration", Nchoghdiya);
*/

                    startActivity(login_pg);
                    finish();


                }

            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog.dismiss();
            showProgress(false);



        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
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
            progressDialog = ProgressDialog.show(LoginActivity.this, "Loading", "Please Wait...", true, false);
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

                HttpPost post = new HttpPost("http://technocratsappware.com/androidapps/forgetpassword.php?email_address="+email_frg_pwd);//2015-5-15
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



                Toast.makeText(LoginActivity.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
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
                            "\nThis mail contains your password for jainsam android application so we suggest you to delete it after read for safety." +
                            "\n\n" +
                            "\nPassword is:" +
                            "\n" + Rpwd +
                            "\n\nif you get this email by mistake or this is not concern with you we advise you to delete this mail as soon as possible and kindly inform to owner company Technocrats Appware, otherwise if it will contain private information so you may have to face problame issue regarding this." +
                            "\nThank You\n.........Auto Generated Mail.........");

                    try {
                        //mailsender.addAttachment("/sdcard/filelocation");

                        if (mailsender.send()) {
                            Toast.makeText(LoginActivity.this,
                                    "Email was sent successfully.",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Email was not sent.",
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {

                        Log.e("MailApp", "Could not send email", e);
                    }


                    // Log.d("usr email", Ruser_email);


                }else{
                    Toast.makeText(LoginActivity.this, "Sorry!!!Email not registered with us!",
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


    class Signup_async extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String response_string;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            Log.d("pre execute", "Executando onPreExecute ingredients");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(LoginActivity.this, "Loading", "Please Wait--------.", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            System.out.println("On do in back ground----done-------");


            Log.d("post execute", "Executando doInBackground   ingredients");



            try{
                //request mate nicheno code

                HttpClient client = new DefaultHttpClient();
                //String postURL = "http://169.254.76.188:8084/Sunil/order_entery";
                //HttpPost post = new HttpPost(postURL);

                HttpPost post = new HttpPost("http://technocratsappware.com/androidapps/signup.php");
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("user_name",full_name));
                params.add(new BasicNameValuePair("mobile_no",mobile_no));

                params.add(new BasicNameValuePair("password",password_fbgmail));
                params.add(new BasicNameValuePair("email_address",email));


                //response mate niche no code

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);
                HttpEntity resEntity = responsePOST.getEntity();
                if (resEntity != null) {
                    String resp = EntityUtils.toString(resEntity);
                    res = resp;

                    System.out.println("response got from server----- "+resp);


                }}catch(Exception e){
                e.printStackTrace();

            }



//            progressDialog.dismiss();
            return res;

        }



        @Override
        protected void onPostExecute(String result)
        {

            System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res == null || res.equals("")) {



                Toast.makeText(LoginActivity.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                Log.i("RESPONSE", res);

                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.



                response_string=obj.getString("msg");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("Done User added!")){

                    sharepref.edit().putString("key_login","yes").commit();
                    sharepref.edit().putString("key_useremail", email).commit();
                    sharepref.edit().putString("key_username", full_name).commit();

                    Intent login_pg = new Intent(LoginActivity.this,Home_screen_navigation.class);
                    startActivity(login_pg);
                    finish();
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), "Successfully Registered!", Snackbar.LENGTH_LONG)
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
                }

                else{

                    sharepref.edit().putString("key_login","yes").commit();
                    sharepref.edit().putString("key_useremail", email).commit();
                    sharepref.edit().putString("key_username", full_name).commit();

                    Intent login_pg = new Intent(LoginActivity.this,Home_screen_navigation.class);
                    startActivity(login_pg);
                    finish();
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), "User already Registered !", Snackbar.LENGTH_LONG)
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


                }










            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog.dismiss();




        }
    }
    
    
    
}

