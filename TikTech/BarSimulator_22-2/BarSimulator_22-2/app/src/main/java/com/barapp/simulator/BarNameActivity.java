package com.barapp.simulator;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.barapp.simulator.utils.Const;
import com.barapp.simulator.utils.Utils;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BarNameActivity extends AppCompatActivity {


    EditText et_barname;
    ImageView img_mic , img_submit;


    private final int SPEECH_RECOGNITION_CODE = 1;
    String  owner_id ,barname="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barname);

        getSupportActionBar().hide();

        et_barname = (EditText)findViewById(R.id.et_barname);
        img_mic   = (ImageView)findViewById(R.id.img_mic);
        img_submit = (ImageView)findViewById(R.id.submit);


        img_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                promptSpeechInput();
            }
        });

        img_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                owner_id = Utils.getPrefs(BarNameActivity.this , Const.USER_ID);
                barname = et_barname.getText().toString();
                if(et_barname.getText().toString().length()==0
                        || et_barname.getText().toString().equalsIgnoreCase("")){
                    et_barname.setError("Enter Bar Name.");
                }else{
                    //barname=et_barname.getText().toString();
                    new SetBarName_Task().execute();
                }
            }
        });

    }



    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE , Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL , RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,5000);

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT , "Speak something...");
        try {
            startActivityForResult(intent , SPEECH_RECOGNITION_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Sorry! Speech recognition is not supported in this device.",
                    Toast.LENGTH_SHORT).show();
        }
    }

//

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    et_barname.setText(result.get(0));
                }
                break;
            }
        }
    }



    private class SetBarName_Task extends AsyncTask<Void ,Void ,Void> {

        ProgressDialog pDialog;
        String res;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(BarNameActivity.this , "Signing Up" , "Please Wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action" , "owner")
                    .add("passing_value" , "add_bar")
                    .add("owner_id" , owner_id)
                    .add("bar_name" , barname)
                    .build();

            Request request = new Request.Builder().url(Const.API_URL)
                    .post(formBody).build();

            try {
                Response response = client.newCall(request).execute();
                res = response.body().string();
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

            pDialog.dismiss();
            try {
                JSONObject jsonObj =new JSONObject(res);
                String code = jsonObj.getString("status_code");
                if(code.equals("200")){

                    startActivity(new Intent(BarNameActivity.this , OwnerMenuActivity.class));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext() , jsonObj.getString("message") , Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Check if user is currently logged in
        if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null){
            //Logged in so show the login button


            LoginManager.getInstance().logOut();

            Utils.ClearAllPrefs(BarNameActivity.this);
            //sharepref.edit().clear().commit();
            //sharepref.edit().putString("key_login","no").commit();

            Intent intenta = new Intent(getApplicationContext(), LoginActivity.class);
            intenta.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intenta);
            finish();

        }else{

            //sharepref.edit().clear().commit();
            //sharepref.edit().putString("key_login","no").commit();

            Utils.ClearAllPrefs(BarNameActivity.this);

            Intent intenta = new Intent(BarNameActivity.this, LoginActivity.class);
            intenta.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intenta);
            finish();

        }
    }
}
