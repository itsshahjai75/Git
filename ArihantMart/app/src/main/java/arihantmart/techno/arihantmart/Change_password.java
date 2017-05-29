package arihantmart.techno.arihantmart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Change_password extends AppCompatActivity {

    EditText et_oldpwd,et_newpwd,et_repeatpwd;
    Button btn_submit;
    String res,email,oldpwd,newpwd;

    SharedPreferences sharepref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/ProductSans-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        //==fontChanger.replaceFonts((ViewGroup)convertView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_oldpwd=(EditText)this.findViewById(R.id.et_old_pwd);
        et_newpwd=(EditText)this.findViewById(R.id.et_new_pwd);
        et_repeatpwd=(EditText)this.findViewById(R.id.et_repeat_pwd);

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        email=sharepref.getString("key_useremail", "null");

        btn_submit=(Button)this.findViewById(R.id.btn_submit_changepwd);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_oldpwd.getText().toString().length()==0){
                    et_oldpwd.setError("Field missing!");
                }else if(et_newpwd.getText().toString().length()==0){
                    et_newpwd.setError("Field missing!");
                }else if(et_repeatpwd.getText().toString().length()==0){
                    et_repeatpwd.setError("Field missing!");
                }else if(!et_repeatpwd.getText().toString().equals(et_newpwd.getText().toString())){
                    et_newpwd.setError("Password not matched!");
                }else{

                    newpwd=et_newpwd.getText().toString();
                    oldpwd=et_oldpwd.getText().toString();
                    new Update_password().execute();

                }




            }
        });







    }


    class Update_password extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String item_ingre;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            //Log.d("pre execute", "Executando onPreExecute ingredients");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(Change_password.this, "Loading", "Please Wait--------.", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            System.out.println("On do in back ground----done-------");


            //Log.d("post execute", "Executando doInBackground   ingredients");



            try{
                //request mate nicheno code

                HttpClient client = new DefaultHttpClient();
                //String postURL = "http://169.254.76.188:8084/Sunil/order_entery";
                //HttpPost post = new HttpPost(postURL);


                HttpPost post = new HttpPost("http://arihantmart.com/androidapp/changepassword.php");//2015-5-15
                List<NameValuePair> params = new ArrayList<NameValuePair>();


                params.add(new BasicNameValuePair("email_address",email));
                params.add(new BasicNameValuePair("oldpwd",oldpwd));
                params.add(new BasicNameValuePair("newpwd",newpwd));


                // Log.d("imgstring",img_string);
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

            String response_string="";
            System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res == null || res.equals("")) {

                Toast.makeText(Change_password.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                Log.i("RESPONSE", res);
                response_string=obj.getString("msg");
                if(response_string.equalsIgnoreCase("Password updated")){

                    Intent home= new Intent(Change_password.this,Home.class);
                    startActivity(home);
                    finish();

                    Toast.makeText(Change_password.this,"Update Done...",Toast.LENGTH_LONG).show();

                }else{

                    Toast.makeText(Change_password.this,"Pasword does not matched...",Toast.LENGTH_LONG).show();
                }

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
                finish();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

       /* if(sharepref.getString("key_login","yes").equals("yes")){

            finish();
        }else{
            System.exit(0);
            finish();
        }*/
    }
}
