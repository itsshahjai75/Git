package arihantmart.techno.arihantmart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Payment_method extends AppCompatActivity {


    private RadioGroup radioGroup;
    private RadioButton cod,netbanking,creditcared,debitcard,paytm,otherwallet;
    Button btn_paynow;

    SharedPreferences sharepref;
    String res,user_email,user_mono,name,add1,add2,add3,landmark,pincode,city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);



        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/ProductSans-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        //==fontChanger.replaceFonts((ViewGroup)convertView);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        user_email=sharepref.getString("key_useremail", "null");
        //user_mono=sharepref.getString("key_usermobno", "null");
        // username=sharepref.getString("key_username", "null");

        name=getIntent().getStringExtra("name");
        user_mono=getIntent().getStringExtra("mobile");
        add1=getIntent().getStringExtra("address_line1");
        add2=getIntent().getStringExtra("address_line2");
        add3=getIntent().getStringExtra("address_line3");
        landmark=getIntent().getStringExtra("landmark");
        city=getIntent().getStringExtra("city");
        pincode=getIntent().getStringExtra("pincode");




        radioGroup = (RadioGroup) findViewById(R.id.radioChoice);
        cod=(RadioButton)findViewById(R.id.rb_cash_on_delivery);
        netbanking=(RadioButton)findViewById(R.id.rb_net_banking);
        creditcared=(RadioButton)findViewById(R.id.rb_credit_card);
        debitcard=(RadioButton)findViewById(R.id.rb_debit_card);
        paytm=(RadioButton)findViewById(R.id.rb_paytm);
        otherwallet=(RadioButton)findViewById(R.id.rb_wallet);

        btn_paynow=(Button)this.findViewById(R.id.btn_paynow);
        btn_paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cod.isChecked()==true){
                    new Order_pay().execute();
                }else{
                    Toast.makeText(Payment_method.this,"Sorry , Currently Cash on Delivery working.only", Toast.LENGTH_SHORT).show();
                }
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                if(cod.isChecked())
                {
                    //Toast.makeText(Payment_method.this,"Tea is selected", Toast.LENGTH_SHORT).show();
                }
                else if(netbanking.isChecked())
                {
                    Toast.makeText(Payment_method.this,"Sorry , Currently Cash on Delivery working.", Toast.LENGTH_SHORT).show();
                } else if(creditcared.isChecked())
                {
                    Toast.makeText(Payment_method.this,"Sorry , Currently Cash on Delivery working.", Toast.LENGTH_SHORT).show();
                } else if(debitcard.isChecked())
                {
                    Toast.makeText(Payment_method.this,"Sorry , Currently Cash on Delivery working.", Toast.LENGTH_SHORT).show();
                } else if(paytm.isChecked())
                {
                    Toast.makeText(Payment_method.this,"Sorry , Currently Cash on Delivery working.", Toast.LENGTH_SHORT).show();
                } else if(otherwallet.isChecked())
                {
                    Toast.makeText(Payment_method.this,"Sorry , Currently Cash on Delivery working.", Toast.LENGTH_SHORT).show();
                }
            }
        });







    }


    class Order_pay extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String response_string;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            //Log.d("pre execute", "Executando onPreExecute ingredients");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(Payment_method.this, "Loading", "Please Wait--------.", true, false);
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

                HttpPost post = new HttpPost("http://arihantmart.com/androidapp/ordergen_mail.php");
                List<NameValuePair> params = new ArrayList<NameValuePair>();


                params.add(new BasicNameValuePair("user_email",user_email));
                params.add(new BasicNameValuePair("user_mobile",user_mono));
                params.add(new BasicNameValuePair("name",name));
                params.add(new BasicNameValuePair("address_line1",add1));
                params.add(new BasicNameValuePair("address_line2",add2));
                params.add(new BasicNameValuePair("address_line3",add3));
                params.add(new BasicNameValuePair("landmark",landmark));
                params.add(new BasicNameValuePair("city",city));
                params.add(new BasicNameValuePair("pincode",pincode));


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

                progressDialog.dismiss();
                Toast.makeText(Payment_method.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                //Log.i("RESPONSE", res);

                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.



                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("Order Generated!")){

                    response_string=obj.getString("msg");

                    String orderno=response_string;

                    Intent home = new Intent(Payment_method.this,Home.class);
                    home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();

                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), "Thank You Order Generated!", Snackbar.LENGTH_LONG)
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

                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), "Sorry, Technical issue,try again !", Snackbar.LENGTH_LONG)
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
