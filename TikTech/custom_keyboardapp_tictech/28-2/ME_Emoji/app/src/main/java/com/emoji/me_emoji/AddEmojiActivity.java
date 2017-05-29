package com.emoji.me_emoji;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.emoji.me_emoji.utils.Constants;
import com.emoji.me_emoji.utils.Utils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Android Developer on 2/27/2017.
 */

public class AddEmojiActivity extends AppCompatActivity {

    private String TAG = AddEmojiActivity.this.getClass().getSimpleName();

    FloatingActionButton fab_add_emoji;
    ImageView img_add_image,img_addemoji_back;

    LinearLayout ll_setmeemoji, ll_convertcartoon;
    LinearLayout ll_hr_setmeemoji;

    ImageView img_smile_pr;
    ImageView img_wink_pr, img_troll_pr, img_squint_pr, img_sad_pr, img_browup_pr, img_oops_pr;

    Uri editedImageUri;
    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "MeEmoji";

    String img_string = "";
    String sub_type = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_emoji);
        getSupportActionBar().hide();

        fab_add_emoji = (FloatingActionButton) findViewById(R.id.fab_add_emoji);
        img_add_image = (ImageView) findViewById(R.id.img_add_image);
        img_addemoji_back =(ImageView) findViewById(R.id.img_addemoji_back);

        ll_setmeemoji = (LinearLayout) findViewById(R.id.ll_setmeemoji);
        ll_convertcartoon = (LinearLayout) findViewById(R.id.ll_convertcartoon);
        ll_hr_setmeemoji = (LinearLayout) findViewById(R.id.ll_hr_setmeemoji);

        img_smile_pr = (ImageView) findViewById(R.id.img_smile_pr);
        img_wink_pr = (ImageView) findViewById(R.id.img_wink_pr);
        img_troll_pr = (ImageView) findViewById(R.id.img_troll_pr);
        img_squint_pr = (ImageView) findViewById(R.id.img_squint_pr);
        img_sad_pr = (ImageView) findViewById(R.id.img_sad_pr);
        img_browup_pr = (ImageView) findViewById(R.id.img_browup_pr);
        img_oops_pr = (ImageView) findViewById(R.id.img_oops_pr);

        selectImage(1);

        img_addemoji_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fab_add_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        img_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(2);
            }
        });

        ll_setmeemoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_hr_setmeemoji.getVisibility() == View.GONE) {
                    ll_hr_setmeemoji.setVisibility(View.VISIBLE);
                    setdefaultpreview();
                } else {
                    ll_hr_setmeemoji.setVisibility(View.GONE);
                }
            }
        });
    }

    public void setdefaultpreview() {

//        Picasso.with(getApplicationContext()).load(R.drawable.smile)
//                .placeholder(R.drawable.ic_vp_smileys).error(R.drawable.ic_vp_smileys)
//                .into(img_smile_pr);


//        GifAnimationDrawable gif;
//
//        try {
//            gif = new GifAnimationDrawable(getResources().openRawResource(R.raw.smile));
//            gif.setOneShot(false);
//            img_smile_pr.setImageDrawable(gif);
//            gif.setVisible(true, true);
//        } catch (Resources.NotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Glide.with(AddEmojiActivity.this)
                .load(R.drawable.smile)
                .asGif()
                .placeholder(R.drawable.ic_vp_smileys)
                .crossFade()
                .into(img_smile_pr);

        Glide.with(this)
                .load(R.drawable.wink)
                .asGif()
                .placeholder(R.drawable.ic_vp_smileys)
                .crossFade()
                .into(img_wink_pr);
        Glide.with(this)
                .load(R.drawable.troll)
                .asGif()
                .placeholder(R.drawable.ic_vp_smileys)
                .crossFade()
                .into(img_troll_pr);
        Glide.with(this)
                .load(R.drawable.squint)
                .asGif()
                .placeholder(R.drawable.ic_vp_smileys)
                .crossFade()
                .into(img_squint_pr);
        Glide.with(this)
                .load(R.drawable.sad)
                .asGif()
                .placeholder(R.drawable.ic_vp_smileys)
                .crossFade()
                .into(img_sad_pr);
        Glide.with(this)
                .load(R.drawable.browup)
                .asGif()
                .placeholder(R.drawable.ic_vp_smileys)
                .crossFade()
                .into(img_browup_pr);
        Glide.with(this)
                .load(R.drawable.oops)
                .asGif()
                .placeholder(R.drawable.ic_vp_smileys)
                .crossFade()
                .into(img_oops_pr);


        img_smile_pr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perform_setemoji_preview("smile");
                sub_type = "1";
            }
        });

        img_wink_pr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perform_setemoji_preview("wink");
                sub_type = "6";
            }
        });

        img_troll_pr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perform_setemoji_preview("troll");
                sub_type = "5";
            }
        });

        img_squint_pr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perform_setemoji_preview("squint");
                sub_type = "4";
            }
        });

        img_sad_pr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perform_setemoji_preview("sad");
                sub_type = "2";
            }
        });

        img_browup_pr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perform_setemoji_preview("browup");
                sub_type = "7";
            }
        });

        img_oops_pr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perform_setemoji_preview("oops");
                sub_type = "3";
            }
        });

    }

    public void perform_setemoji_preview(String preview) {


        Bitmap bitmap = ((BitmapDrawable) img_add_image.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] b = baos.toByteArray();
        img_string = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d(TAG, "img_string--" + img_string);

        if (Utils.isNetworkAvailable(AddEmojiActivity.this)) {

            new AddEmojiActivity.FaceconverterTask().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Network not Available", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Capturing Camera Image will lauch camera app requrest image capture
     */
    private void captureImage() {


		/*int cameraCount=2;
        Camera c = null;  // object that use
		Camera.CameraInfo info = new Camera.CameraInfo();
		int count = Camera.getNumberOfCameras();

		for (int i = 0; i<cameraCount; i++) {
			Camera.getCameraInfo(i, info);
			if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				try {
					c = Camera.open(i);
				} catch (RuntimeException e) {
					// Handle
				}
			}
		}*/

        editedImageUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, editedImageUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", editedImageUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        editedImageUri = savedInstanceState.getParcelable("file_uri");
    }


    /**
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            // hide video preview
            //videoPreview.setVisibility(View.GONE);

            img_add_image.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(editedImageUri.getPath(),
                    options);

            img_add_image.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            //onBackPressed();
            e.printStackTrace();
        }
    }


    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    /*public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}*/
    private static Uri getOutputMediaFileUri(int type) {
        File fileToReturn = getOutputMediaFile(type);
        return fileToReturn != null ? Uri.fromFile(fileToReturn) :
                null;
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void selectImage(final int type) {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddEmojiActivity.this);
        //builder.setTitle("Add Photo!");
        builder.setCancelable(false);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                    captureImage();
                    /*************************** Camera Intent End ************************/

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                    if (type == 1) {
                        onBackPressed();
                    }
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_CANCELED) {
                onBackPressed();
            } else {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            }

        } else if (requestCode == 2) {

            if (data != null) {
                editedImageUri = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(editedImageUri, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                img_add_image.setImageBitmap(thumbnail);
				/*Intent edit_pic = new Intent(AudioRecordFileCustomePlayer.this,AudioRecordFileCustomePlayer.class);
				edit_pic.putExtra("filepath",selectedImage.toString());
				startActivity(edit_pic);
				overridePendingTransition(R.anim.left_right_in,
						R.anim.left_right_out);*/
                Log.w("image from gallery.", editedImageUri.toString() + "thubnail==");
            } else {
                onBackPressed();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //overridePendingTransition(R.anim.left_right_in, R.anim.left_right_out);

    }


    private class FaceconverterTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(AddEmojiActivity.this, "Loading", "Please Wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action", "faceconverter")
                    .add("image", "" + img_string)
                    .add("main_type", "face")
                    .add("sub_type", "")
                    .build();

            Request request = new Request.Builder()
                    .url(Constants.API_URL)
                    .post(formBody).build();

            try {
                Response response = client.newCall(request).execute();
                res = response.body().string();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (res == null || res.equals("")) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Network Error Or Error", Toast.LENGTH_SHORT).show();
                return;
            }

            pDialog.dismiss();
            ll_hr_setmeemoji.setVisibility(View.GONE);
            try {
                JSONObject jobjres = new JSONObject(res);

                Log.d(TAG, "jobjres_preview" + jobjres);
//                String status_code = jobjres.getString("status_code");
//                if (status_code.equals("200")) {
//
//                    JSONObject jobj = jobjres.getJSONObject("user");
//                    String userid = jobj.getString("id");
//                    String emailid = jobj.getString("email");
//                    String phoneno = jobj.getString("phone_no");
//                    String username = jobj.getString("username");
//
//                    Utils.setPrefs(LoginActivity.this, Constants.PREF_USERID, userid);
//                    Utils.setPrefs(LoginActivity.this, Constants.PREF_EMAIL, emailid);
//                    Utils.setPrefs(LoginActivity.this, Constants.PREF_PHONENO, phoneno);
//                    Utils.setPrefs(LoginActivity.this, Constants.PREF_USERNAME, username);
//
//                    startActivity(new Intent(LoginActivity.this, EmojiGridActivity.class));
//                    Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(LoginActivity.this, jobjres.getString("message"), Toast.LENGTH_SHORT).show();
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    public class CircleTransform extends BitmapTransformation {

        public CircleTransform(Context context) {
            super(context);
        }

        @Override protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override public String getId() {
            return getClass().getName();
        }
    }

}
