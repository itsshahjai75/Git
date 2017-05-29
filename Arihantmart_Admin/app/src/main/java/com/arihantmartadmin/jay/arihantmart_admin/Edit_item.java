package com.arihantmartadmin.jay.arihantmart_admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Edit_item extends AppCompatActivity {

    SharedPreferences sharepref;
    TextView tv_itemcode;
    EditText  et_itemname,et_itemcomapyname,et_mrp,et_ourptice,et_discounbt,et_quantity;
    Spinner sp_catagory;
    Button btn_change,btn_delete;
    ArrayAdapter<CharSequence> adapter;
    String img_string,str_itemcode,res,admin_email,str_catagory,str_comapnyname,str_itemname,str_mrp,str_ourprice,str_discount,str_quantity;
    ImageView itemimage;


    private Bitmap bitmap;

    private Uri mImageCaptureUri;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/ProductSans-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        //==fontChanger.replaceFonts((ViewGroup)convertView);

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        admin_email = sharepref.getString("key_useremail", "null");

        tv_itemcode=(TextView)this.findViewById(R.id.tv_item_code);

        et_itemcomapyname=(EditText)this.findViewById(R.id.et_itemcomapnyname);
        et_itemname=(EditText)this.findViewById(R.id.et_itemname);
        et_mrp=(EditText)this.findViewById(R.id.et_mrp);
        et_ourptice=(EditText)this.findViewById(R.id.et_ourprice);
        et_discounbt=(EditText)this.findViewById(R.id.et_discount);
        et_quantity=(EditText)this.findViewById(R.id.et_quantity);

        sp_catagory=(Spinner)this.findViewById(R.id.spinner_catagory);

        itemimage=(ImageView)this.findViewById(R.id.itemimage);





        itemimage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                selectImage();

            }
        });

        btn_change=(Button)this.findViewById(R.id.btn_submit_change);
        btn_delete=(Button)this.findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                alertbox.setMessage("Item will be removed from Item Master Database.");
                alertbox.setTitle("Delete Item ?");
                alertbox.setIcon(R.mipmap.ic_launcher);

                alertbox.setNeutralButton("Delete",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0,int arg1) {

                                new Deleteitem().execute();
                            }
                        });
                alertbox.show();

            }
        });
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_comapnyname=et_itemcomapyname.getText().toString();
                str_itemname=et_itemname.getText().toString();
                str_mrp=et_mrp.getText().toString();
                str_ourprice=et_ourptice.getText().toString();
                str_discount=et_discounbt.getText().toString();
                str_quantity=et_quantity.getText().toString();

                if(str_catagory.isEmpty()||str_comapnyname.isEmpty()||str_itemname.isEmpty()||str_mrp.isEmpty()||str_ourprice.isEmpty()||str_discount.isEmpty()||str_quantity.isEmpty()){

                    Toast.makeText(Edit_item.this,"Some thing Missing",Toast.LENGTH_LONG).show();
                }else{

                    bitmap = ((BitmapDrawable)itemimage.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
                    byte [] b=baos.toByteArray();
                    img_string= Base64.encodeToString(b, Base64.DEFAULT);

                    new Updateitem().execute();
                }

            }
        });

        new Getsingleitem().execute();




        str_itemcode=getIntent().getStringExtra("itemcode");

        adapter = ArrayAdapter.createFromResource(this, R.array.query_suggestions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_catagory.setAdapter(adapter);




        sp_catagory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                str_catagory=sp_catagory.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

                Log.d("nothing changed====","as it is spiner got");
            }
        });



    }

    class Getsingleitem extends AsyncTask<Object, Void, String> {

        ProgressDialog progressDialog;
        String item_ingre;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            Log.d("pre execute", "add tocart");
            progressDialog =  new ProgressDialog(Edit_item.this);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Object... parametros) {

            // System.out.println("On do in back ground----done-------");


            //Log.d("post execute", "Executando doInBackground   ingredients");



            try{
                //request mate nicheno code

                HttpClient client = new DefaultHttpClient();
                String query="http://arihantmart.com/androidapp/getsingleitemdetails.php?itemcode="+str_itemcode;
                query=query.replaceAll(" ","%20");
                //String query = URLEncoder.encode("http://arihantmart.com/androidapp/itembycatgory.php?catagoryname="+str_catgry_name, "utf-8");
                HttpPost post = new HttpPost(query);
                List<NameValuePair> params = new ArrayList<NameValuePair>();


                //response mate niche no code

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);
                HttpEntity resEntity = responsePOST.getEntity();
                if (resEntity != null) {
                    String resp = EntityUtils.toString(resEntity);
                    res = resp;

                    //  System.out.println("response got from server----- "+resp);


                }}catch(Exception e){
                e.printStackTrace();

            }

            return res;

        }



        @Override
        protected void onPostExecute(String result)
        {

            String response_string="";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res == null || res.equals("")) {



                Toast.makeText(getBaseContext(),"No data found!",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }

            try {

                Log.i("RESPONSE", res);
                JSONObject obj = new JSONObject(res);




                response_string = obj.getString("info");


                JSONArray array_res = new JSONArray(response_string);

                if (array_res.length() == 0) {

                    Toast.makeText(getBaseContext(),"No data found!",Toast.LENGTH_LONG).show();

                } else {






                    for (int j = 0; j < array_res.length(); j++) {

                        String item_code = array_res.getJSONObject(j).getString("item_code");
                        String item_compnay_name = array_res.getJSONObject(j).getString("item_compnay_name");
                        String item_name = array_res.getJSONObject(j).getString("item_name");
                        String item_category = array_res.getJSONObject(j).getString("item_category");
                        String item_sub_categary = array_res.getJSONObject(j).getString("item_sub_categary");
                        String mrp = array_res.getJSONObject(j).getString("mrp");
                        String our_price = array_res.getJSONObject(j).getString("our_price");
                        String quantity = array_res.getJSONObject(j).getString("quantity");
                        String discount = array_res.getJSONObject(j).getString("discount");

                        double MRP = Double.parseDouble(mrp);
                        double OURPRICE = Double.parseDouble(our_price);



                        double PER_ITEM_SAVE = MRP - OURPRICE;

                        String saving_amount = Double.toString(PER_ITEM_SAVE);




                        tv_itemcode.setText(item_code);
                        et_itemcomapyname.setText(item_compnay_name);
                        et_itemname.setText(item_name);

                        if (!item_category.equals(null)) {
                            int spinnerPosition = adapter.getPosition(item_category);
                            sp_catagory.setSelection(spinnerPosition);
                            str_catagory=item_category;
                        }else{

                        }
                        et_mrp.setText(mrp);
                        et_ourptice.setText(our_price);
                        et_discounbt.setText(discount);
                        et_quantity.setText(quantity);


                        String itemname=item_name.replace(" ","");
                        Log.d("name===",itemname);



                        Picasso.with(Edit_item.this)
                                .load("http://arihantmart.com/androidapp/images/"+itemname+".jpg")
                                .placeholder(R.drawable.noimage) // optional
                                .error(R.drawable.noimage)         // optional
                                .memoryPolicy(MemoryPolicy.NO_CACHE )
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .into(itemimage);






                        // Log.d("usr Img",j+"=="+ Rpost_image);
                    }


                }
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog.dismiss();




        }
    }



    class Updateitem extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String item_ingre;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            Log.d("pre execute", "Executando onPreExecute ingredients");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(Edit_item.this, "Loading", "Please Wait--------.", true, false);
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


                HttpPost post = new HttpPost("http://arihantmart.com/androidapp/updateitemadmin.php");//2015-5-15
                List<NameValuePair> params = new ArrayList<NameValuePair>();


                params.add(new BasicNameValuePair("itemcode",str_itemcode));
                params.add(new BasicNameValuePair("companyname",str_comapnyname));
                params.add(new BasicNameValuePair("itemname",str_itemname));
                params.add(new BasicNameValuePair("catagory",str_catagory));
                params.add(new BasicNameValuePair("mrp",str_mrp));
                params.add(new BasicNameValuePair("ourprice",str_ourprice));
                params.add(new BasicNameValuePair("discount",str_discount));
                params.add(new BasicNameValuePair("qunatity",str_quantity));
                params.add(new BasicNameValuePair("added_by",admin_email));
                params.add(new BasicNameValuePair("item_pic",img_string));




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



                Toast.makeText(Edit_item.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                Log.i("RESPONSE", res);

                Intent home= new Intent(Edit_item.this,Home.class);
                startActivity(home);
                finish();

                Toast.makeText(Edit_item.this,"Update Done...",Toast.LENGTH_LONG).show();
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog.dismiss();




        }
    }


    class Deleteitem extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String item_ingre;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            Log.d("pre execute", "Executando onPreExecute ingredients");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(Edit_item.this, "Loading", "Please Wait--------.", true, false);
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


                HttpPost post = new HttpPost("http://arihantmart.com/androidapp/deleteitem.php");//2015-5-15
                List<NameValuePair> params = new ArrayList<NameValuePair>();


                params.add(new BasicNameValuePair("itemcode",str_itemcode));
                //Log.d("item code",str_itemcode);




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



                Toast.makeText(Edit_item.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);



                response_string=obj.getString("msg");
                if(response_string.equalsIgnoreCase("deleted")){

                    Intent home= new Intent(Edit_item.this,Home.class);
                    startActivity(home);
                    finish();

                    Toast.makeText(Edit_item.this,"Deleted Done...",Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(Edit_item.this,"technical error...",Toast.LENGTH_LONG).show();

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case PICK_FROM_CAMERA:
                doCrop();

                break;

            case PICK_FROM_FILE:
                mImageCaptureUri = data.getData();

                doCrop();

                break;

            case CROP_FROM_CAMERA:
                Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");

                    itemimage.setImageBitmap(photo);
                }

                File f = new File(mImageCaptureUri.getPath());

                if (f.exists()) f.delete();

                break;

        }
    }


    private void selectImage() {


        final String [] items			= new String [] {"Take from camera", "Select from gallery"};
        ArrayAdapter<String> adapter	= new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
        android.support.v7.app.AlertDialog.Builder builder		= new android.support.v7.app.AlertDialog.Builder(this);

        builder.setTitle("Select Image");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //pick from camera
                if (item == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                            "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                    try {
                        intent.putExtra("return-data", true);

                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    //pick from file
                    Intent intent = new Intent();

                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                }
            }
        });

        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );

        int size = list.size();

        if (size == 0) {
            Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();

            return;
        } else {
            intent.setData(mImageCaptureUri);

            intent.putExtra("outputX", 800);
            intent.putExtra("outputY", 800);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1.7);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            if (size == 1) {
                Intent i 		= new Intent(intent);
                ResolveInfo res	= list.get(0);

                i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title 	= getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon		= getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent= new Intent(intent);

                    co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(getApplicationContext(), cropOptions);

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("Choose Crop App");
                builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialog, int item ) {
                        startActivityForResult( cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
                    }
                });

                builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel( DialogInterface dialog ) {

                        if (mImageCaptureUri != null ) {
                            getContentResolver().delete(mImageCaptureUri, null, null );
                            mImageCaptureUri = null;
                        }
                    }
                } );

                android.support.v7.app.AlertDialog alert = builder.create();

                alert.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
