package com.jainisam.techno.jainisam;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Image_Crop.CropOption;
import Image_Crop.CropOptionAdapter;

public class Post_Share extends AppCompatActivity {


    ImageView img_post;
    EditText et_post_msg;
    Button btn_send;

    private Uri mImageCaptureUri;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;


    SharedPreferences sharepref;
    String Puser_name,Ppostimgbitmap,Puser_email,Ppost_msg,res;

    TextView tv_msgcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post__share);




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final String getvalue= getIntent().getStringExtra("postshare");


        img_post=(ImageView)this.findViewById(R.id.img_postshare);
        et_post_msg=(EditText)this.findViewById(R.id.et_postmsg);
        btn_send=(Button)this.findViewById(R.id.btn_postsend);
        tv_msgcount=(TextView)this.findViewById(R.id.tv_count);

        et_post_msg.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // this will show characters remaining
                tv_msgcount.setText(500 - s.toString().length() + "/500");
                /*if(et_post_msg.getText().toString().length()>500){
                    btn_send.setEnabled(false);
                    tv_msgcount.setTextColor(Color.RED);
                }else{
                    btn_send.setEnabled(true);
                    tv_msgcount.setTextColor(Color.BLACK);
                }*/
            }
        });



        img_post.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                selectImage();

            }
        });





        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        Puser_email=sharepref.getString("key_useremail", null).toString();
        Puser_name=sharepref.getString("key_username", null).toString();



        /*if(Puser_email.isEmpty() || Puser_name.isEmpty() || Puser_name.equalsIgnoreCase("null") || Puser_email.equalsIgnoreCase("null")){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), "Please,Login again.", Snackbar.LENGTH_LONG);

            // Changing message text color
            snackbar.setActionTextColor(Color.BLUE);

            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();

            Toast.makeText(Post_Share.this, "Sorry!!!  Something Wrong.  ", Toast.LENGTH_LONG).show();

            sharepref.edit().putString("key_login","no").commit();
            sharepref.edit().putString("key_useremail","no user").commit();
        }*/


        if(getvalue.equalsIgnoreCase("post")){
            img_post.setVisibility(View.GONE);
            et_post_msg.setHint("Share Latest news or Knowledgeable.");
            android.view.ViewGroup.LayoutParams layoutParams =  et_post_msg.getLayoutParams();
            layoutParams.height = 400;
            et_post_msg.setLayoutParams(layoutParams);


        }else if(getvalue.equalsIgnoreCase("image")  ){
            et_post_msg.setHint("Image Title.( 1 Line Only )");
            img_post.setVisibility(View.VISIBLE);
            et_post_msg.setMaxLines(1);
        }else if (getvalue.equalsIgnoreCase("checkedin")) {
            img_post.setVisibility(View.GONE);
            et_post_msg.setMaxLines(1);
            et_post_msg.setHint("Name of Upasrya OR Derasar OR Custom place.( 1 Line Only )");

        }


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (et_post_msg.getText().toString().length() == 0 || et_post_msg.getText().toString().isEmpty()) {
                    et_post_msg.setError("Field can not be Empty.");
                } else {

                    //Log.d("first if nu else ","2");

                    if (getvalue.equalsIgnoreCase("checkedin") && et_post_msg.getText().toString().length() != 0) {
                        Ppostimgbitmap = "null";
                        Ppost_msg = "checked in at: " + et_post_msg.getText().toString();

                        //Log.d("first if nu else ","2.1");
                    } else if (!getvalue.equalsIgnoreCase("checkedin") && getvalue.equalsIgnoreCase("post")) {
                        Ppostimgbitmap = "null";
                        Ppost_msg = et_post_msg.getText().toString();

                        //Log.d("first if nu else ","2.2");

                    } else {


                        Bitmap bitmap = ((BitmapDrawable)img_post.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
                        byte [] b=baos.toByteArray();
                        Ppostimgbitmap = Base64.encodeToString(b, Base64.DEFAULT);

                        Ppost_msg = et_post_msg.getText().toString();
                    }



                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Post_Share.this);

// set title
                    alertDialogBuilder.setTitle("Are you sure want to Post?");
                    alertDialogBuilder.setCancelable(false);


// set dialog message
                    alertDialogBuilder
                            .setMessage("This is your responsibility what you are posting, Own Risk.\nUndeleteable post.")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    if (et_post_msg.getText().toString().length() == 0 || et_post_msg.getText().toString().isEmpty()) {
                                        et_post_msg.setError("Post can not be Empty.");
                                    } else {
                                        new PostShare_async().execute();
                                        dialog.dismiss();
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

// create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

// show it
                    alertDialog.show();

                }


            }
        });

    }


    class PostShare_async extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String response_string;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            //Log.d("pre execute", "Executando onPreExecute ingredients");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(Post_Share.this, "Loading", "Please Wait--------.", true, false);
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

                HttpPost post = new HttpPost("http://technocratsappware.com/androidapps/post_post.php");
                List<NameValuePair> params = new ArrayList<NameValuePair>();

               // System.out.println("imagestring=="+Ppostimgbitmap.toString());

                params.add(new BasicNameValuePair("user_name",Puser_name));
                params.add(new BasicNameValuePair("post_imgage",Ppostimgbitmap));
                params.add(new BasicNameValuePair("post_decrpt",Ppost_msg));
                params.add(new BasicNameValuePair("email_address",Puser_email));


                //response mate niche no code

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);
                HttpEntity resEntity = responsePOST.getEntity();
                if (resEntity != null) {
                    String resp = EntityUtils.toString(resEntity);
                    res = resp;

                    //System.out.println("response got from server----- "+resp);


                }}catch(Exception e){
                e.printStackTrace();

            }



//            progressDialog.dismiss();
            return res;

        }



        @Override
        protected void onPostExecute(String result)
        {

           // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res == null || res.equals("")) {



                Toast.makeText(Post_Share.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                Log.i("RESPONSE", res);

                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.



                response_string=obj.getString("msg");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("Done Post added!")){
                    Intent home_pg = new Intent(Post_Share.this,Home_screen_navigation.class);
                    startActivity(home_pg);
                    finish();
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), "Successfully Posted!", Snackbar.LENGTH_LONG)
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
                            .make(findViewById(android.R.id.content), "Post can't added,try after some time!", Snackbar.LENGTH_LONG)
                            .setAction("ok", new View.OnClickListener() {
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

                    img_post.setImageBitmap(photo);
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
            public void onClick(DialogInterface dialog, int item) { //pick from camera
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
                } else { //pick from file
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
                finish();
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
