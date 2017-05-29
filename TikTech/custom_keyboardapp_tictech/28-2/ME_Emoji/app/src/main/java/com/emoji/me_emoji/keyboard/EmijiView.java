package com.emoji.me_emoji.keyboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.emoji.me_emoji.R;
import com.emoji.me_emoji.keyboard.emojicon.EmojiconGridFragment;
import com.emoji.me_emoji.keyboard.emojicon.EmojiconsFragment;
import com.emoji.me_emoji.keyboard.emojicon.emoji.Emojicon;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EmijiView extends FragmentActivity implements View.OnTouchListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener, EmojiconGridFragment.OnEmojiconClickedListener {

    // this should app glob for recent emoji
    public final ArrayList<Emojicon> mEmojicons = new ArrayList<Emojicon>();
    /**
     * Checking emoji_keyboard height and emoji_keyboard visibility
     */
    int previousHeightDiffrence = 0;
    private EditText messageEd;
    private LinearLayout emojiIconsCover;
    private LinearLayout footer_layout_main;
    private ImageView emoticonsButton;
    private int keyboardHeight;
    private boolean isKeyBoardVisible;
    private boolean isEmojiVisible;
    private LinearLayout parentLayout;
    private ImageView sendButton;
    private String mChatMessage;
    private Handler mShowEmojiHandler = new Handler();
    private TextView messageTx;


    static Uri editedImageUri;

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public final int MY_REQUEST_CODE=500;
    public final int MY_REQUEST_CODE_STORAGE=600;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emoji_activity_chat_fragment);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        messageEd = (EditText) findViewById(R.id.edit_chat_message);
        messageTx = (TextView) findViewById(R.id.txt_sentMessage);
        parentLayout = (LinearLayout) findViewById(R.id.chatfragment_parent);
        emojiIconsCover = (LinearLayout) findViewById(R.id.footer_for_emojiicons);
        sendButton = (ImageView) findViewById(R.id.btn_chat_send);

        messageEd.setOnTouchListener(this);
        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mChatMessage = messageEd.getText().toString();
                mChatMessage = mChatMessage.trim();
                if (!mChatMessage.equals("")) {
                    sendMessage(mChatMessage);
                    messageEd.setText("");
                }
                if (isEmojiVisible)
                    changeEmojiLayout();
            }
        });

        emoticonsButton = (ImageView) findViewById(R.id.btn_chat_emoji);
        emoticonsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEmojiLayout();
            }
        });

        checkKeyboardHeight(parentLayout);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_emiji_view, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void sendMessage(String message) {
        messageTx.setText(message);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.edit_chat_message:
                if (isEmojiVisible) {
                    emoticonsButton
                            .setBackgroundResource(R.drawable.ic_vp_smileys);
                    emojiIconsCover
                            .setVisibility(LinearLayout.GONE);
                    isEmojiVisible = false;
                }
                break;

//            case R.id.list_chat:
//                if (isEmojiVisible) {
//                    emoticonsButton
//                            .setBackgroundResource(R.drawable.ic_vp_smileys);
//                    emojiIconsCover
//                            .setVisibility(LinearLayout.GONE);
//                    isEmojiVisible = false;
//                }
//                break;

            default:
                break;
        }
        return false;
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(messageEd);
    }

    @Override
    public void onEmojiconCallClicked(View v) {
        EmojiconsFragment.emojishow();
    }

    @Override
    public void onEmojiconEmailClicked(View v) {
        Toast.makeText(this,"email clicked",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onEmojiconFileTransferClicked(View v) {
        Toast.makeText(this,"file transfer clicked",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onEmojiconGallaryClicked(View v) {
        EmojiconsFragment.gallaryfragment();
    }

    @Override
    public void onEmojiconCameraClicked(View v) {
        captureImage();
    }

    @Override
    public void onEmojiconAudioClicked(View v) {
        EmojiconsFragment.tempfragment();
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(messageEd, emojicon);

        if (!mEmojicons.contains(emojicon))
            mEmojicons.add(emojicon);
    }


    private void showKeyboard(View view) {
        InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(view, 0);
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
                .getWindowToken(), 0);
    }

    protected void changeEmojiLayout() {

        Toast.makeText(this,"change layout",Toast.LENGTH_LONG).show();

        Log.i("emoji","isEmojiVisible"+isEmojiVisible);
        Log.i("emoji","isKeyBoardVisible"+isKeyBoardVisible);

        final InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//		emoji_keyboard.showSoftInput(message, 0);
        if (isEmojiVisible && !isKeyBoardVisible) {
            emoticonsButton
                    .setBackgroundResource(R.drawable.ic_vp_smileys);
            emojiIconsCover
                    .setVisibility(LinearLayout.GONE);
            isEmojiVisible = false;
            mShowEmojiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    messageEd.requestFocus();
                    keyboard.showSoftInput(messageEd, 0);
                    checkKeyboardHeight(parentLayout);
                }
            }, 100);

        } else if (isEmojiVisible && isKeyBoardVisible) {

        } else if (!isEmojiVisible && isKeyBoardVisible) {
            hideKeyboard();

            mShowEmojiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    emoticonsButton
                            .setBackgroundResource(R.drawable.ic_vp_keypad);

                    emojiIconsCover
                            .setVisibility(LinearLayout.VISIBLE);
                    isEmojiVisible = true;
                }
            }, 100);
        } else if (!isEmojiVisible && !isKeyBoardVisible) {
            emoticonsButton
                    .setBackgroundResource(R.drawable.ic_vp_keypad);

            emojiIconsCover
                    .setVisibility(LinearLayout.VISIBLE);
            isEmojiVisible = true;
        }
    }

    private void checkKeyboardHeight(final View parentLayout) {

        parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        parentLayout.getWindowVisibleDisplayFrame(r);

                        int screenHeight = parentLayout.getRootView()
                                .getHeight();
                        int heightDifference = screenHeight - (r.bottom);

                        if (previousHeightDiffrence - heightDifference > 50) {
//							popupWindow.dismiss(); TODO
                            emoticonsButton.setBackgroundResource(R.drawable.ic_vp_smileys);
                            emojiIconsCover.setVisibility(LinearLayout.GONE);
                        }
                        previousHeightDiffrence = heightDifference;
                        if (heightDifference > 100) {
                            isKeyBoardVisible = true;
                            changeKeyboardHeight(heightDifference);
                        } else {
                            isKeyBoardVisible = false;
                        }
                    }
                });

    }

    /**
     * change height of emoticons emoji_keyboard according to height of actual
     * emoji_keyboard
     *
     * @param height minimum height by which we can make sure actual emoji_keyboard is
     *               open or not
     */
    private void changeKeyboardHeight(int height) {

        if (height > 100) {
            keyboardHeight = height;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, keyboardHeight);
            emojiIconsCover.setLayoutParams(params);
        }

    }



    /**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        if (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Capturing Camera Image will lauch camera app requrest image capture
     */
    public void captureImage() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        editedImageUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, editedImageUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }




    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", editedImageUri);
    }


    /**
     * Recording video
     */
    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        editedImageUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, editedImageUri); // set the image file
        // name

        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }

    /**
     * Receiving activity result method will be called after closing the camera
     * */



    /**
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            // hide video preview
            //videoPreview.setVisibility(View.GONE);

//            img_propic.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(editedImageUri.getPath(),
                    options);

//            img_propic.setImageBitmap(bitmap);


            if (bitmap!=null){

                EmojiconsFragment.camerafragment(bitmap);
//                image_emoji_layout.setVisibility(View.VISIBLE);
//
//                Fragment fr;
//
//                fr = new EmojiIconImageFragment();
//
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("bitmap", bitmap);
//                fr.setArguments(bundle);
//
//                FragmentManager fm = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fm.beginTransaction();
//                fragmentTransaction.replace(R.id.image_emoji_fragment, fr);
//                fragmentTransaction.commit();
            }

        } catch (NullPointerException e) {
            //onBackPressed();
            e.printStackTrace();
        }
    }

    /**
     * Previewing recorded video
     */
    private void previewVideo() {
        try {
            // hide image preview
            //imgPreview.setVisibility(View.GONE);

            //videoPreview.setVisibility(View.VISIBLE);
            //videoPreview.setVideoPath(fileUri.getPath());
            //// start playing
            //videoPreview.start();
        } catch (Exception e) {
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

    private static Uri getOutputMediaFileUri(int type){
        File fileToReturn =  getOutputMediaFile(type);
        return  fileToReturn!=null? Uri.fromFile(fileToReturn):
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

    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Add Photo!");
        builder.setCancelable(false);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {

                    captureImage();
                    /*************************** Camera Intent End ************************/

                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                   /* onBackPressed();*/
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Log.d("onactivityresult==",Integer.toString(requestCode)+Integer.toString(resultCode)+data.toString());
		/*if (data == null || resultCode == Activity.RESULT_CANCELED) {
			Log.w("result canceled","========================");
			onBackPressed();
		}*/

        //if (resultCode == RESULT_OK) {



        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE){

            if(resultCode==RESULT_CANCELED) {
               /* onBackPressed();*/
            }else {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            }


        } else if (requestCode == 2) {

            if(data!=null) {
                editedImageUri = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(editedImageUri, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

//                img_propic.setImageBitmap(thumbnail);


                /*Intent edit_pic = new Intent(EditPhotoActivity.this,EditPhotoActivity.class);
				edit_pic.putExtra("filepath",selectedImage.toString());
				startActivity(edit_pic);
				overridePendingTransition(R.anim.left_right_in,
						R.anim.left_right_out);*/

                Log.w("image from gallery.", editedImageUri.toString() + "thubnail==");
            }else{
                /*onBackPressed();*/
            }
        } /*else if (requestCode == 3) {

				  *//* Show the image! *//*
            editedImageUri = data.getParcelableExtra(AdobeImageIntent.EXTRA_OUTPUT_URI);
            img_propic.setImageURI(editedImageUri);

        } *//*else if((requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE ||requestCode == 2 ||requestCode == 3)
					&& resultCode == Activity.RESULT_CANCELED){

				Log.w("canceled result got","========================");
				onBackPressed();
			}else if(resultCode==RESULT_CANCELED || resultCode!=RESULT_OK){
				onBackPressed();
			}*/


        //}
    }




    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
