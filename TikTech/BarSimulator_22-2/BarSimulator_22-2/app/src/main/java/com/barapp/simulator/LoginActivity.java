package com.barapp.simulator;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.barapp.simulator.utils.Const;
import com.barapp.simulator.utils.Utils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Arrays;


import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {


    String[] states = new String[]{"Select State","American Samoa","Arizona","Alaska","Alabama","Arkansas","California","Colorado","Connecticut","Delaware","District of Columbia", "Florida",
            "Georgia","Guam","Hawaii","Idaho","Illinois","Indiana","Iow","Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts",
            "Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire","New Jersey","New Mexico",
            "New York","North Carolina","North Dakota","Northern Mariana Islands","Ohio","Oklahoma","Oregon","Puerto Rico" , "Pennsylvania" , "Rhode Island" ,
            "South CarolinaSouth Dakota" , "Tennessee" , "Texas" , "U.S." , "Utah" , "Vermont","Virginia","Virgin Islands","Washington","West Virginia","Wisconsin","Wyoming"};

    Spinner state_spin,state_spin_fb;
    TextView fb_login ,login ,signup,tv_forget_pwd;
    EditText et_user ,et_pass , et_email;

    private CallbackManager callbackManager;
    public AccessToken currentAccessToken;

    String emailPattern;
    String email_frg_pwd="";

    static String email , password , state="" , profilePicURL , str_fb_email , str_fb_username , str_fb_fbid ,  type = "0";

    Dialog dialog_fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_landscap);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().hide();

        callbackManager = CallbackManager.Factory.create();

         emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        fb_login = (TextView)findViewById(R.id.login_with_fb);
        login = (TextView)findViewById(R.id.login);
        signup = (TextView)findViewById(R.id.signup);

        state_spin = (Spinner)findViewById(R.id.spinner_state);
        state_spin.setAdapter(new CustomSpinner_Adapter(LoginActivity.this ,states ,0));
        state_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       // state_spin.setOnItemClickListener();

        et_user = (EditText)findViewById(R.id.username);
        et_pass = (EditText)findViewById(R.id.password);



        fb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               LoginWithFB_Dailog();
            }
        });


        fb_login.setCompoundDrawablePadding(40);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this , SignUPActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = et_user.getText().toString();
                password = et_pass.getText().toString();

                if (et_user.getText().toString().length()==0 ||
                        !et_user.getText().toString().matches(emailPattern)
                        || et_user.getText().toString().matches("[0-9]+@[0-9]+@[0-9]") ||
                        et_user.getText().toString().equalsIgnoreCase("abc@abc.com")) {
                    et_user.setError("Enter e-mail address.");
                } else if (et_pass.getText().toString().length()==0 ||
                        et_pass.getText().toString().equalsIgnoreCase("")) {
                    et_pass.setError("Enter password.");
                } else if (state_spin.getSelectedItem().toString().equalsIgnoreCase("Select State")) {
                    TextView errorText = (TextView)state_spin.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Select State. !");//changes the selected item text to this
                }else {
                   // state  =  state_spin.getSelectedItem().toString();
                    new LoginTask().execute();

                }
            }
        });

        final Dialog dialog_frgpwd = new Dialog(LoginActivity.this);
        tv_forget_pwd= (TextView)this.findViewById(R.id.tv_forget_pwd);
        tv_forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                dialog_frgpwd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog_frgpwd.setContentView(R.layout.forget_pwd_dialog);

                dialog_frgpwd.setCanceledOnTouchOutside(true);
                WindowManager.LayoutParams lp = dialog_frgpwd.getWindow().getAttributes();
                lp.width = 700;
                lp.height = 700;
                lp.gravity = Gravity.CENTER;
                lp.dimAmount = 0;
                dialog_frgpwd.getWindow().setAttributes(lp);

                final EditText et_frg_pwd =(EditText)dialog_frgpwd.findViewById(R.id.frg_et_email);
                TextView btn_submit = (TextView) dialog_frgpwd.findViewById(R.id.frg_tv_submit);


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
                            dialog_frgpwd.dismiss();
                        }

                    }
                });

                dialog_frgpwd.show();
            }
        });



    }





    //============================FB starts here==============================================================

    private String facebook_id,f_name, m_name, l_name, gender, profile_image, full_name, email_id,mobile_no,password_fbgmail;


    private void LoginWithFB_Dailog() {
        dialog_fb =new Dialog(LoginActivity.this);
        dialog_fb.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_fb.setContentView(R.layout.activity_login_withfb);

        dialog_fb.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = dialog_fb.getWindow().getAttributes();
        lp.width = 800;
        lp.height = 650;
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0;
        dialog_fb.getWindow().setAttributes(lp);

        RadioGroup rg = (RadioGroup)dialog_fb.findViewById(R.id.rg);
        RadioButton rb_owner = (RadioButton)dialog_fb.findViewById(R.id.owner);
        RadioButton rb_user  = (RadioButton)dialog_fb.findViewById(R.id.user);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group  , int checkedId) {
                if(checkedId == R.id.owner) {
                    type = "0";
                }else if(checkedId == R.id.user){
                    type = "1";
                }
            }
        });

        et_email  = (EditText)dialog_fb.findViewById(R.id.et_emailid);
        state_spin_fb = (Spinner)this.dialog_fb.findViewById(R.id.statedialog_fb);
        state_spin_fb.setAdapter(new CustomSpinner_Adapter_facebook(LoginActivity.this , states , 1));
        state_spin_fb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        final LoginButton tv_submit_fb = (LoginButton)this.dialog_fb.findViewById(R.id.login_button);

        et_email.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length()>5){
                    if(et_email.getText().toString().length()==0 || !et_email.getText().toString().matches(emailPattern)
                            || et_email.getText().toString().matches("[0-9]+@[0-9]+@[0-9]") || et_email.getText().toString().equalsIgnoreCase("abc@abc.com"))
                    {
                        et_email.setError("Enter Valid Email Address.");
                        return;
                    }else{
                        tv_submit_fb.setVisibility(View.VISIBLE);
                        tv_submit_fb.setEnabled(true);
                    }

                }else{
                    tv_submit_fb.setVisibility(View.GONE);
                    tv_submit_fb.setEnabled(false);
                }
            }
        });


        tv_submit_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (et_email.getText().toString().length()==0 ||
                        !et_email.getText().toString().matches(emailPattern)
                        || et_email.getText().toString().matches("[0-9]+@[0-9]+@[0-9]") ||
                        et_email.getText().toString().equalsIgnoreCase("abc@abc.com")) {
                    et_email.setError("Enter e-mail address.");
                    return;
                } else if (state_spin_fb.getSelectedItem().toString().equalsIgnoreCase("Select State")) {
                    TextView errorText = (TextView)state_spin_fb.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Select State. !");//changes the selected item text to this
                    return;
                } else {
                    if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null) {
                        LoginManager.getInstance().logOut();
                    } else {
                      // Log.d("Fb Login CLicked ===","Clicked ====");

                        str_fb_email = et_email.getText().toString();

                        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
                        dialog_fb.dismiss();

                        tv_submit_fb.setReadPermissions("email");

                        tv_submit_fb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                System.out.println("LoginResult :"+loginResult);

                                Log.d("cal back fire==","call back fire"+loginResult);

                                facebook_id = f_name = m_name = l_name = gender = profile_image = full_name = email_id = "";

                                if (AccessToken.getCurrentAccessToken() != null) {
                                    RequestData();
                                    Profile profile = Profile.getCurrentProfile();
                                    if (profile != null) {
                                        facebook_id = profile.getId();
                                        str_fb_fbid = facebook_id;
                                        Log.e("facebook_id", facebook_id);
                                        f_name = profile.getFirstName();
                                        //Log.e("f_name", f_name);
                                        m_name = profile.getMiddleName();
                                        //Log.e("m_name", m_name);
                                        l_name = profile.getLastName();
                                        //Log.e("l_name", l_name);
                                        full_name = profile.getName();
                                        //Log.e("full_name", full_name);
                                        profile_image = profile.getProfilePictureUri(400, 400).toString();
                                       // Log.e("profile_image", profile_image);
                                        str_fb_username = full_name;

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
                                exception.printStackTrace();
                            }
                        });

                    }
                }
            }
        });
        dialog_fb.show();
    }

    public void RequestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                JSONObject json = response.getJSONObject();
                System.out.println("Json data :"+json);
                //System.out.println("Json GraphResponse :"+response);

                try {
                    if(json != null){
                        /*String text = "<b>Name :</b> "+json.getString("name")+"<br><br><b>Email :</b> "+json.getString("email")
                                +"<br><br><b>Profile link :</b> "+json.getString("link");*/
                        //details_txt.setText(Html.fromHtml(text));
                        if(!json.isNull("email")){
                            email=json.getString("email");
                                str_fb_email=email;
                        }else{
                            str_fb_email=str_fb_email;
                        }
                        password_fbgmail="loginbyfb"+facebook_id;

                        if(str_fb_fbid.length()==0 || str_fb_fbid.equalsIgnoreCase("")){
                            Log.d("FB_DATA== ","<b>Name :</b> "+json.getString("name")+"<br><br><b>Email :</b> "+json.getString("email")
                                    +"<br><br><b>Profile link :</b> "+json.getString("link"));
                            //profile.setProfileId(json.getString("id"));
                        }else{
                            new LoginWithFB().execute();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (Exception ep){
                    ep.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
        //parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();

       /* String userID = AccessToken.getCurrentAccessToken().getUserId();
        String AuthToken = AccessToken.getCurrentAccessToken().getToken();
        System.out.println("usr _id"+userID);
        System.out.println("fb auth token "+AuthToken);*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }



    //
    public class CustomSpinner_Adapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private String[] asr;
        private int statetype;

        public CustomSpinner_Adapter(Context context , String[] asr , int statetype) {
            this.asr=asr;
            activity = context;
            this.statetype = statetype;
        }



        public int getCount() {
            return asr.length;
        }

        public Object getItem(int i) {
            return asr[i];
        }

        public long getItemId(int i) {
            return (long)i;
        }



        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(activity);
            txt.setPadding(16 , 16 , 16 , 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr[position]);
            txt.setTextColor(Color.parseColor("#000000"));

            return  txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(activity);
            txt.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
            txt.setPadding(10 , 10 , 10 , 10);
            txt.setTextSize(20);
            txt.setCompoundDrawablePadding(10);
            if(statetype == 1){
                txt.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.box_light_grey1));
                txt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.down_arrow_small ,0);
            }else{
                txt.setCompoundDrawablesWithIntrinsicBounds(0 , 0 , android.R.drawable.arrow_down_float , 0);
            }

            txt.setText(asr[i]);
            txt.setTextColor(Color.parseColor("#ffffff"));

            return  txt;
        }

    }

    public class CustomSpinner_Adapter_facebook extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private String[] asr;
        private int statetype;

        public CustomSpinner_Adapter_facebook(Context context , String[] asr , int statetype) {
            this.asr=asr;
            activity = context;
            this.statetype = statetype;
        }



        public int getCount() {
            return asr.length;
        }

        public Object getItem(int i) {
            return asr[i];
        }

        public long getItemId(int i) {
            return (long)i;
        }



        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(activity);
            txt.setPadding(16 , 16 , 16 , 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr[position]);
            txt.setTextColor(Color.parseColor("#000000"));

            return  txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(activity);
            txt.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
            txt.setPadding(10 , 10 , 10 , 10);
            txt.setTextSize(20);
            txt.setCompoundDrawablePadding(10);
            if(statetype == 1){
                txt.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.box_light_grey1));
                txt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.down_arrow_small ,0);
            }else{
                txt.setCompoundDrawablesWithIntrinsicBounds(0 , 0 , android.R.drawable.arrow_down_float , 0);
            }

            txt.setText(asr[i]);
            txt.setTextColor(Color.parseColor("#000000"));

            return  txt;
        }

    }

    public class GetPassword extends AsyncTask<Object, Void, String> {


        protected ProgressDialog progressDialog;
        String res;
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
            Log.d("on back ground","");

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action","forgot_password")
                    .add("email",email_frg_pwd)
                    .build();
            Request request = new Request.Builder()
                    .url(Const.API_URL).post(formBody).build();

            try {
                Response response = client.newCall(request).execute();
                res = response.body().string();

                Log.e("response" , res);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }



        @Override
        protected void onPostExecute(String result)
        {
            if(res == null || res.equals("")){
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext() , "Network Error Or Error" , Toast.LENGTH_SHORT).show();
                return;
            }


            progressDialog.dismiss();
            try {
                JSONObject jobjres =new JSONObject(res);
                String status_code = jobjres.getString("status_code");
                if(status_code.equals("200")){

                    Toast.makeText(LoginActivity.this , "Mail Sent!" , Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(LoginActivity.this , jobjres.getString("message") , Toast.LENGTH_SHORT).show();
                }

                startActivity(new Intent(LoginActivity.this,LoginActivity.class));
                finish();

            } catch (Exception e) {
                e.printStackTrace();
            }

            progressDialog.dismiss();
        }
    }

    private class LoginTask extends AsyncTask<Void ,Void ,Void>{

        ProgressDialog pDialog;
        String res;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(LoginActivity.this , "Login" ,"Please Wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {

            Log.d("state string",state);

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action" , "login")
                    .add("email" , email)
                    .add("password" , password)
                    .add("state" , state)
                    .build();
            Request request = new Request.Builder()
                    .url(Const.API_URL).post(formBody).build();

            try {
                Response response = client.newCall(request).execute();
                res = response.body().string();

                Log.e("response" , res);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(res == null || res.equals("")){
                pDialog.dismiss();
                Toast.makeText(getApplicationContext() , "Network Error Or Error" , Toast.LENGTH_SHORT).show();
                return;
            }


            pDialog.dismiss();
            try {
                JSONObject jobjres =new JSONObject(res);
                String status_code = jobjres.getString("status_code");
                if(status_code.equals("200")){

                    JSONObject jobj = jobjres.getJSONObject("data");

                    String id     = jobj.getString("id");
                    String emailid = jobj.getString("email");
                    String statename = jobj.getString("state");
                    String type  = jobj.getString("type");
                    String username = jobj.getString("username");
                    String barname  = jobj.getString("barname");
                    String total_coin  = jobj.getString("total_coin");

                    Utils.setPrefs(LoginActivity.this , Const.USER_ID , id);
                    Utils.setPrefs(LoginActivity.this , Const.EMAIL , emailid);
                    Utils.setPrefs(LoginActivity.this , Const.STATE , statename);
                    Utils.setPrefs(LoginActivity.this , Const.ISLOGIN , "yes");
                    Utils.setPrefs(LoginActivity.this , Const.TYPE , type);
                    Utils.setPrefs(LoginActivity.this , Const.USERNAME , username);
                    Utils.setPrefs(LoginActivity.this , Const.COINS , total_coin);

                    if(type.equals("0")){
                        if(barname.equals("")){
                            startActivity(new Intent(LoginActivity.this , BarNameActivity.class));
                            finish();
                        }else{
                            Intent intent = new Intent(LoginActivity.this , OwnerMenuActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }else{
                        startActivity(new Intent(LoginActivity.this , UserMenuActivity.class));
                        finish();
                    }
                    Toast.makeText(LoginActivity.this , "Logged In Successfully" , Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this , jobjres.getString("message") , Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            pDialog.dismiss();
        }
    }

    private class LoginWithFB extends AsyncTask<Void , Void ,Void>{

        ProgressDialog pDialog;
        String res;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(LoginActivity.this , "" ,"Please Wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {

            Log.d("state on API",state);
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action" , "facebook")
                    .add("facebook_id" , str_fb_fbid)
                    .add("email" , str_fb_email)
                    .add("full_name" , str_fb_username)
                    .add("state" , state)
                    .add("type" , type)
                    .build();
            Log.d("str_fb_email==type",str_fb_email+type);
            Request request = new Request.Builder()
                    .url(Const.API_URL).post(formBody).build();

            try {
                Response response = client.newCall(request).execute();
                res = response.body().string();

                Log.e("response" , res);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(res == null || res.equals("")){
                pDialog.dismiss();

                Toast.makeText(getApplicationContext() , "Network Error Or Error" ,Toast.LENGTH_SHORT).show();

                return;
            }


            dialog_fb.dismiss();

            try {
                JSONObject jsonObj =new JSONObject(res);
                String status_code = jsonObj.getString("status_code");

                if(status_code.equals("200")){
                    pDialog.dismiss();

                    JSONObject jObjUser = jsonObj.getJSONObject("user");

                    String id  = jObjUser.getString("id");
                    String type = jObjUser.getString("type");
                    String username = jObjUser.getString("username");
                    String barname  = jObjUser.getString("barname");
                    String profile_icon = jObjUser.getString("profile_pic");
                    String total_coin  = jObjUser.getString("total_coin");

                    Utils.setPrefs(LoginActivity.this , Const.EMAIL , email);
                    Utils.setPrefs(LoginActivity.this , Const.STATE , state);
                    Utils.setPrefs(LoginActivity.this , Const.ISLOGIN , "yes");
                    Utils.setPrefs(LoginActivity.this , Const.TYPE , type);
                    Utils.setPrefs(LoginActivity.this , Const.USERNAME , username);
                    Utils.setPrefs(LoginActivity.this , Const.USER_ID , id);
                    Utils.setPrefs(LoginActivity.this , Const.COINS , total_coin);

                    Log.e("islogin and type" , Utils.getPrefs(LoginActivity.this , Const.ISLOGIN)+" "+Utils.getPrefs(LoginActivity.this , Const.TYPE));

                    if(type.equals("0")){
                        if(barname.equals("")){
                            startActivity(new Intent(LoginActivity.this , BarNameActivity.class));
                            finish();
                        }else{
                            Intent intent = new Intent(LoginActivity.this , OwnerMenuActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }else{
                        startActivity(new Intent(LoginActivity.this , UserMenuActivity.class));
                        finish();
                    }
                }else{
                    Toast.makeText(getApplicationContext() , jsonObj.getString("message") , Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            pDialog.dismiss();

        }

    }



}
