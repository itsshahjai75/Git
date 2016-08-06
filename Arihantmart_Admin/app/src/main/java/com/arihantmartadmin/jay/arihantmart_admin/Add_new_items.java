package com.arihantmartadmin.jay.arihantmart_admin;

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
import android.support.v7.app.AlertDialog;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Add_new_items extends AppCompatActivity {


    SharedPreferences sharepref;
    TextView tv_itemcode;
    EditText et_itemname,et_itemcomapyname,et_mrp,et_ourptice,et_discounbt,et_quantity;
    Spinner sp_catagory;
    Button btn_change;
    ArrayAdapter<CharSequence> adapter;
    String img_string,res,admin_email,str_catagory,str_comapnyname,str_itemname,str_mrp,str_ourprice,str_discount,str_quantity;


    private Bitmap bitmap;

    private Uri mImageCaptureUri;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;



    ImageView itemimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_items);

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


        btn_change=(Button)this.findViewById(R.id.btn_submit_insert);
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

                    Toast.makeText(Add_new_items.this,"Some thing Missing",Toast.LENGTH_LONG).show();
                }else{


                    bitmap = ((BitmapDrawable)itemimage.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
                    byte [] b=baos.toByteArray();
                    img_string= Base64.encodeToString(b, Base64.DEFAULT);

                    new Additem().execute();
                }

            }
        });





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
            progressDialog = ProgressDialog.show(Add_new_items.this, "Loading", "Please Wait--------.", true, false);
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


                HttpPost post = new HttpPost("http://arihantmart.com/androidapp/additem.php");//2015-5-15
                List<NameValuePair> params = new ArrayList<NameValuePair>();


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



                Toast.makeText(Add_new_items.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }

            try {


                Log.i("RESPONSE", res);
                JSONObject obj = new JSONObject(res);

                response_string=obj.getString("msg");
                if(response_string.equalsIgnoreCase("Done item added!")){

                    Intent home= new Intent(Add_new_items.this,Home.class);
                    startActivity(home);
                    finish();

                    Toast.makeText(Add_new_items.this,"Instertd Done...",Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(Add_new_items.this,"technical error...",Toast.LENGTH_LONG).show();

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
        AlertDialog.Builder builder		= new AlertDialog.Builder(this);

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

        final AlertDialog dialog = builder.create();
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

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

                AlertDialog alert = builder.create();

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
