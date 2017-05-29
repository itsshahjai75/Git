package arihantmart.techno.arihantmart;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class User_profile extends AppCompatActivity {




    private Bitmap bitmap;

    private Uri mImageCaptureUri;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;



    ImageView user_dp;
    EditText et_name,et_moileno,et_email,et_address1,et_address2,et_address3,et_landmark,et_city,et_pincode;
    Spinner sp_gender;
    Button btn_submit,btn_change_pwd;

    String pname,pemail,pmobileno,pgender,paddress_line1,paddress_line2,paddress_line3,plandmark,pcity,ppincode,img_string,bitmap_str,res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/ProductSans-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        //==fontChanger.replaceFonts((ViewGroup)convertView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        user_dp=(ImageView)this.findViewById(R.id.user_dp);

        et_name=(EditText)this.findViewById(R.id.et_name_profile);
        et_email=(EditText)this.findViewById(R.id.et_email_profile);
        et_moileno=(EditText)this.findViewById(R.id.et_mobno_profile);
        et_address1=(EditText)this.findViewById(R.id.et_address1_profile);
        et_address2=(EditText)this.findViewById(R.id.et_address2_profile);
        et_address3=(EditText)this.findViewById(R.id.et_address3_profile);
        et_landmark=(EditText)this.findViewById(R.id.et_lanmark_profile);
        et_city=(EditText)this.findViewById(R.id.et_city_profile);
        et_pincode=(EditText)this.findViewById(R.id.et_pincode_profile);

        sp_gender=(Spinner)this.findViewById(R.id.spinner_gender);
        btn_submit=(Button)this.findViewById(R.id.btn_submit_profile);
        btn_change_pwd=(Button)this.findViewById(R.id.btn_changepwd);
        btn_change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(User_profile.this,Change_password.class));
            }
        });


        et_email.setEnabled(false);
        et_email.setFocusable(false);


        Bundle p = getIntent().getExtras();
        pname=p.getString("name");
        pemail=p.getString("email");
        pmobileno=p.getString("mobile");
        paddress_line1=p.getString("address_line1");
        paddress_line2=p.getString("address_line2");
        paddress_line3=p.getString("address_line3");
        plandmark=p.getString("landmark");
        pcity=p.getString("city");
        ppincode=p.getString("pincode");
        pgender=p.getString("gender");

        //Log.d("system out==",pname+pemail+pmobileno+paddress+pcity+pstate+pgender);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_gender.setAdapter(adapter);
        if (!pgender.equals(null)) {
            int spinnerPosition = adapter.getPosition(pgender);
            sp_gender.setSelection(spinnerPosition);
        }else{

        }


        et_name.setText(pname);
        et_email.setText(pemail);
        et_moileno.setText(pmobileno);

        if(paddress_line1.equalsIgnoreCase("null")){

        }else{
            et_address1.setText(paddress_line1);
        }

        if(paddress_line2.equalsIgnoreCase("null")){

        }else{
            et_address2.setText(paddress_line2);
        }

        if(paddress_line3.equalsIgnoreCase("null")){

        }else{
            et_address3.setText(paddress_line3);
        }

        if(plandmark.equalsIgnoreCase("null")){

        }else{
            et_landmark.setText(plandmark);
        }

        if(pcity.equalsIgnoreCase("null")){

        }else{
            et_city.setText(pcity);
        }

        if(ppincode.equalsIgnoreCase("null")){

        }else{
            et_pincode.setText(ppincode);
        }




        try {
            bitmap_str =p.getString("bitmap");

            Picasso.with(this)
                    .load("http://arihantmart.com/androidapp/images/"+bitmap_str)
                    .placeholder(R.drawable.profile_icon) // optional
                    .error(R.drawable.profile_icon)         // optional
                    .noFade()
                    .memoryPolicy(MemoryPolicy.NO_CACHE )
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(user_dp);

        }catch (Exception expbitmap){
            expbitmap.printStackTrace();
        }



        sp_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                pgender=sp_gender.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

                //Log.d("nothing changed====","as it is spiner got");
            }
        });



        user_dp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                selectImage();

            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                pname=et_name.getText().toString();
                pmobileno=et_moileno.getText().toString();
                paddress_line1=et_address1.getText().toString();
                paddress_line2=et_address2.getText().toString();
                paddress_line3=et_address3.getText().toString();
                plandmark=et_landmark.getText().toString();
                pcity=et_city.getText().toString();
                ppincode=et_pincode.getText().toString();


                bitmap = ((BitmapDrawable)user_dp.getDrawable()).getBitmap();
                ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
                byte [] b=baos.toByteArray();
                img_string= Base64.encodeToString(b, Base64.DEFAULT);




                //System.out.print("req are"+pname+pemail+pmobileno+pgender+paddress_line1+pcity+ppincode+img_string);
                new Update_user().execute();


            }
        });




    }




    class Update_user extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String item_ingre;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            //Log.d("pre execute", "Executando onPreExecute ingredients");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(User_profile.this, "Loading", "Please Wait--------.", true, false);
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


                HttpPost post = new HttpPost("http://arihantmart.com/androidapp/update_info.php");//2015-5-15
                List<NameValuePair> params = new ArrayList<NameValuePair>();


                params.add(new BasicNameValuePair("user_name",pname));
                params.add(new BasicNameValuePair("mobile_no",pmobileno));
                params.add(new BasicNameValuePair("email_address",pemail));
                params.add(new BasicNameValuePair("gender",pgender));
                params.add(new BasicNameValuePair("address_line1",paddress_line1));
                params.add(new BasicNameValuePair("address_line2",paddress_line2));
                params.add(new BasicNameValuePair("address_line3",paddress_line3));
                params.add(new BasicNameValuePair("landmark",plandmark));
                params.add(new BasicNameValuePair("city",pcity));
                params.add(new BasicNameValuePair("pincode",ppincode));
                params.add(new BasicNameValuePair("user_pro_pic",img_string));


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



                Toast.makeText(User_profile.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                //Log.i("RESPONSE", res);

                Intent home= new Intent(User_profile.this,Home.class);
                startActivity(home);
                finish();

                Toast.makeText(User_profile.this,"Update Done...",Toast.LENGTH_LONG).show();
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

                    user_dp.setImageBitmap(photo);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

}
