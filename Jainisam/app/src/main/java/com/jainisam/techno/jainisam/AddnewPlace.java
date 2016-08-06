package com.jainisam.techno.jainisam;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class AddnewPlace extends AppCompatActivity {

    SharedPreferences sharepref;

    Spinner dynamicSpinner;
    ArrayAdapter<String> spineradapter;
    ProgressDialog progressDialog2;


    ArrayList spiner_array;

    String res2,str_name,str_address,str_telno,str_mobno,str_city,str_catagory,str_email,puseremail;
    EditText et_name,et_address,et_city,et_telno,et_mobno,et_email;

    MyTextView tv_spinertitle;
    Button btn_submit;


    String yes="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnew_place);

        if (android.os.Build.VERSION.SDK_INT > 14) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

           new Getcatagory().execute();

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        puseremail=sharepref.getString("key_useremail", null).toString();

        tv_spinertitle=(MyTextView)this.findViewById(R.id.tv_addplace_spinettitle);

        et_name=(EditText)this.findViewById(R.id.et_name_addplace);
        et_address=(EditText)this.findViewById(R.id.et_address_addplace);
        et_city=(EditText)this.findViewById(R.id.et_city_addplace);
        et_telno=(EditText)this.findViewById(R.id.et_telephn_addplace);
        et_mobno=(EditText)this.findViewById(R.id.et_mobile_addplace);
        et_email=(EditText)this.findViewById(R.id.et_email_addplace);
        btn_submit=(Button)this.findViewById(R.id.btn_submit_addplace);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                str_name=et_name.getText().toString();
                str_address=et_address.getText().toString();
                str_city=et_city.getText().toString();
                str_telno=et_telno.getText().toString();
                str_mobno=et_mobno.getText().toString();
                str_email=et_email.getText().toString();

                int pos =dynamicSpinner.getSelectedItemPosition();

                str_catagory = dynamicSpinner.getSelectedItem().toString();




                if(et_email.getText().toString().length()!=0){

                    String before_attherate=et_email.getText().toString();
                    before_attherate=before_attherate.substring(0,before_attherate.indexOf("\u0040"));

                    String after_attherate=et_email.getText().toString();
                    after_attherate=after_attherate.substring(after_attherate.indexOf("\u0040")+1, after_attherate.indexOf("."));

                    if(et_email.getText().toString().length()!=0 && !et_email.getText().toString().matches(emailPattern)
                            || et_email.getText().toString().matches("[0-9]+@[0-9]+@[0-9]")
                            || et_email.getText().toString().equalsIgnoreCase("abc@abc.com")
                            || before_attherate.equalsIgnoreCase(after_attherate)) {
                        et_email.setError("Enter Valid Email Address!");
                    }else if(before_attherate.length()<=4 || before_attherate.equalsIgnoreCase("12345")
                            || before_attherate.equalsIgnoreCase("12345456789")
                            ||before_attherate.equalsIgnoreCase("1234567890") ||
                            before_attherate.equalsIgnoreCase("abcde") ||
                            before_attherate.equalsIgnoreCase("abcdef") ||
                            before_attherate.equalsIgnoreCase("qwert") ||
                            before_attherate.equalsIgnoreCase("qwerty") ||
                            before_attherate.equalsIgnoreCase("asdfg")||
                            before_attherate.equalsIgnoreCase("asdfgh")){
                        et_email.setError("Try other email please!!!");
                    }else if(after_attherate.length()<=4 || after_attherate.equalsIgnoreCase("12345")
                            || after_attherate.equalsIgnoreCase("12345456789")
                            ||after_attherate.equalsIgnoreCase("1234567890") ||
                            after_attherate.equalsIgnoreCase("abcde") ||
                            after_attherate.equalsIgnoreCase("abcdef") ||
                            after_attherate.equalsIgnoreCase("qwert") ||
                            after_attherate.equalsIgnoreCase("qwerty") ||
                            after_attherate.equalsIgnoreCase("asdfg")||
                            after_attherate.equalsIgnoreCase("asdfgh")||
                            after_attherate.equalsIgnoreCase("email")){
                        et_email.setError("Try other email please!!!");
                    }
                }else if(str_name.isEmpty() || et_name.getText().toString().length()==0){
                    et_name.setError("Enter Name!");
                }else if(str_address.isEmpty() || et_address.getText().toString().length()==0){
                    et_address.setError("Enter Address!");
                }else if(str_city.isEmpty() || et_city.getText().toString().length()==0){
                    et_city.setError("Enter City!");
                }else if(et_mobno.getText().toString().length()!=0 && et_mobno.getText().toString().length() < 10
                        || et_mobno.getText().toString().equalsIgnoreCase("1234567890")){
                    et_mobno.setError("Enter Correct Mobile Number!");
                }else if(pos == 0 || str_catagory.isEmpty() || str_catagory.equalsIgnoreCase("null")){
                    Toast.makeText(AddnewPlace.this,
                            "Please Select the Place type !!", Toast.LENGTH_LONG)
                            .show();
                    tv_spinertitle.setError("Please Select the Container type !!");
                }
                else{
                    // Log.d("usr email", Ruser_email);
                    new Sendmail().execute();


                }
            }
        });

        spiner_array = new ArrayList<String>();

        dynamicSpinner = (Spinner) findViewById(R.id.dynamic_spinner);

        //String[] items = new String[] { "Chai Latte", "Green Tea", "Black Tea" };

        spineradapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, spiner_array);


        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                str_catagory=parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });




    }


    class Getcatagory extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");

            //pg_bar.setVisibility(View.VISIBLE);

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog2 = ProgressDialog.show(AddnewPlace.this, "Loading", "Please Wait...", true, false);
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

                HttpPost post = new HttpPost("http://technocratsappware.com/androidapps/gettotal_place_type.php");
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                //params.add(new BasicNameValuePair("loadcount",Integer.toString(load_count)));


                //response mate niche no code

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);
                HttpEntity resEntity = responsePOST.getEntity();
                if (resEntity != null) {
                    String resp2 = EntityUtils.toString(resEntity);
                    res2 = resp2;

                    //  System.out.println("response got from server----- "+resp);


                }}catch(Exception e){
                e.printStackTrace();

            }



//            progressDialog.dismiss();
            return res2;

        }



        @Override
        protected void onPostExecute(String result)
        {

            String response_string="";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res2 == null || res2.equals("")) {



                Toast.makeText(AddnewPlace.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                //
                progressDialog2.dismiss();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res2);


//                 Log.i("RESPONSE", res2);

                response_string = obj.getString("info");


                JSONArray array_res = new JSONArray(response_string);

                if (array_res.length() == 0) {

                    //                  Toast.makeText(Home_screen_navigation.this,"No data found!",Toast.LENGTH_LONG).show();

                } else {


                    dynamicSpinner.setAdapter(spineradapter);
                    spiner_array.clear();
                    spineradapter.notifyDataSetChanged();
                    spiner_array.add("Select");

                    for (int j = 0; j < array_res.length(); j++) {

                        String place_type = array_res.getJSONObject(j).getString("place_type");

                        String place_type_output = place_type.substring(0, 1).toUpperCase() + place_type.substring(1);

                        spiner_array.add(place_type_output);



                    }
                    spineradapter.notifyDataSetChanged();




                }
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog2.dismiss();

           /* if(mSwipeRefreshLayout.isRefreshing()==true){
                mSwipeRefreshLayout.setRefreshing(false);}*/



        }
    }

    class Sendmail extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");

            //pg_bar.setVisibility(View.VISIBLE);

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog2 = ProgressDialog.show(AddnewPlace.this, "Loading", "Please Wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.

        }

        @Override
        protected String doInBackground(Object... parametros) {

            // System.out.println("On do in back ground----done-------");


            //Log.d("post execute", "Executando doInBackground   ingredients");



            try {
                GMailSender mailsender = new GMailSender("technocratsappware@gmail.com", "technocratsappware@9033228796");
                String[] toArr = {"technocratsappware@gmail.com"};
                mailsender.set_to(toArr);
                mailsender.set_from("technocratsappware@gmail.com");
                mailsender.set_subject("Jainisam new Place add");
                mailsender.setBody("Hello.\nPlace add Request by"+puseremail +"\n\n" +
                        "\nThis mail contains Add new placedetails." +
                        "\n\n" +
                        "\n Place Name:" + str_name +
                        "\n Place address:" + str_address +
                        "\n Place city:" + str_city +
                        "\n Place tele.no:" + str_telno +
                        "\n Place mobile.no:" + str_mobno +
                        "\n Place email:" + str_email +
                        "\n Place placetype:" + str_catagory +
                        "\n added by USER name:" + puseremail +
                        "\n\n" +
                        "\nThank You\n.........Auto Generated Mail.........");

                try {
                    //mailsender.addAttachment("/sdcard/filelocation");

                    if (mailsender.send()) {
                        Toast.makeText(AddnewPlace.this,
                                "Thank You for support, Details will be added Soon!!!.",
                                Toast.LENGTH_LONG).show();
                        yes="yes";
                        finish();
                    } else {
                        Toast.makeText(AddnewPlace.this, "Sorry!!!Something Wrong,try after some time.",
                                Toast.LENGTH_LONG).show();
                        yes="no";

                    }
                } catch (Exception e) {

                    Log.e("MailApp", "Could not send email", e);
                    yes="no";
                    progressDialog2.dismiss();
                }
            }catch (Exception mailexp){
                mailexp.printStackTrace();
            }

            return yes;
        }



        @Override
        protected void onPostExecute(String result)
        {

            String response_string="";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (yes == null || yes.equals("no")) {



                Toast.makeText(AddnewPlace.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                //
                progressDialog2.dismiss();
                return;
            }
            else{
                Toast.makeText(AddnewPlace.this, "Done", Toast.LENGTH_LONG).show();
                //
                progressDialog2.dismiss();
                return;
            }



           /* if(mSwipeRefreshLayout.isRefreshing()==true){
                mSwipeRefreshLayout.setRefreshing(false);}*/



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


        finish();
    }


}
