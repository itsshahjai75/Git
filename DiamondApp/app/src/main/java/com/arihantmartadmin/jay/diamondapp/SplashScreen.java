package com.arihantmartadmin.jay.diamondapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SplashScreen extends AppCompatActivity {



    String res,req_json;
    JSONObject request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);




        Button btnget=(Button)this.findViewById(R.id.btn_get);
        btnget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                try {

                    request = new JSONObject();
                    JSONObject header = new JSONObject();
                    JSONObject body = new JSONObject();

                    body.put("shape", "round");
                    header.put("username","");
                    header.put("password","");

                    JSONArray array_request = new JSONArray();
                    array_request.put(header);
                    array_request.put(body);


                    request.put("request",array_request);

                }catch (Exception exp){
                    exp.printStackTrace();
                }


                /*req_json = "{" +
                        " \"request\": { " +
                        " \"header\": { " +
                        " \"username\": \"snexports09@gmail.com\"," +
                        " \"password\": \"AllselL099\" " +
                        "}," +
                        " \"body\": {" +
                        " \"shape\": \"round\" " +
                        "}" +
                        "}" +
                        "}";*/

                req_json= request.toString();
                Log.d("JSON REQUEST",req_json);

                new Additem().execute();
            }
        });

    }



    class Additem extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String item_ingre;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            Log.d("pre execute", "Executando onPreExecute ingredients");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(SplashScreen.this, "Loading", "Please Wait--------.", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {


            Log.d("backgrnd", "Executando doInBackground   ingredients");

            String dataUrl = "https://technet.rapaport.com/HTTP/JSON/Prices/GetPriceSheet.aspx ";
            String dataUrlParameters = req_json;
            URL url;
            HttpURLConnection connection = null;
            try {
// Create connection
                url = new URL(dataUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length","" + Integer.toString(dataUrlParameters.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);
// Send request
                DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream());
                wr.writeBytes(dataUrlParameters);
                wr.flush();
                wr.close();
// Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                res = response.toString();
                Log.d("Server response",res);

            } catch (Exception e) {

                e.printStackTrace();

            }


//            progressDialog.dismiss();
            return res;

        }



        @Override
        protected void onPostExecute(String result)
        {

            String response_string="";
            super.onPostExecute(result);

            if (res == null || res.equals("")) {



                Toast.makeText(SplashScreen.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }

            try {


                Log.i("RESPONSE", res);
               /* JSONObject obj = new JSONObject(res);

                response_string=obj.getString("msg");
                if(response_string.equalsIgnoreCase("Done item added!")){

                    Intent home= new Intent(SplashScreen.this,Home.class);
                    startActivity(home);
                    finish();

                    Toast.makeText(SplashScreen.this,"Instertd Done...",Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(SplashScreen.this,"technical error...",Toast.LENGTH_LONG).show();

                }*/




            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog.dismiss();




        }
    }

}
