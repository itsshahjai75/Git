package com.emoji.me_emoji.keyboard;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.ClipDescription;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.provider.Telephony;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.design.widget.Snackbar;
import android.support.v13.view.inputmethod.EditorInfoCompat;
import android.support.v13.view.inputmethod.InputConnectionCompat;
import android.support.v13.view.inputmethod.InputContentInfoCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.os.BuildCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputBinding;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.emoji.me_emoji.EmojiGridActivity;
import com.emoji.me_emoji.LoginActivity;
import com.emoji.me_emoji.Profile;
import com.emoji.me_emoji.R;
import com.emoji.me_emoji.SplashActivity;
import com.emoji.me_emoji.dataset.EmojiDataset;
import com.emoji.me_emoji.keyboard.emojicon.EmojiIconGallaryFragment;
import com.emoji.me_emoji.keyboard.emojicon.EmojiIconImageFragment;
import com.emoji.me_emoji.keyboard.emojicon.EmojiIconTempFragment;
import com.emoji.me_emoji.keyboard.emojicon.EmojiconGridFragment;
import com.emoji.me_emoji.keyboard.emojicon.EmojiconsFragment;
import com.emoji.me_emoji.keyboard.emojicon.emoji.Emojicon;
import com.emoji.me_emoji.utils.ConnectionDetector;
import com.emoji.me_emoji.utils.Constants;
import com.emoji.me_emoji.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Developer on 2/21/2017.
 */

public class SimpleIME extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener, ViewPager.OnPageChangeListener, View.OnAttachStateChangeListener {

    private String TAG = SimpleIME.class.getSimpleName();

    private View view;
    private Keyboard keyboard;

    private boolean caps = false;


    public static Emojicon[] RECENT_DATA = new Emojicon[]{};
    public static LinearLayout lin_emoji_popup_buttons;
    //public OnEmojiconBackspaceClickedListener mOnEmojiconBackspaceClickedListener;
    public int mEmojiTabLastSelectedIndex = -1;
    public View[] mEmojiTabs;

    public static FrameLayout image_emoji_layout;
    public static LinearLayout emoji_layout;
    public static FragmentManager fm;
    EmijiView emijiView;


    public ArrayList<EmojiDataset> emojiDatasets = new ArrayList<>();

    private static final String AUTHORITY = "com.emoji.me_emoji.keyboard.inputcontent";
    private static final String MIME_TYPE_GIF = "image/gif";
    private static final String MIME_TYPE_PNG = "image/png";
    private static final String MIME_TYPE_WEBP = "image/webp";

    private File mPngFile;
    private File mGifFile;
    private File mWebpFile;
    private Button mGifButton;
    private Button mPngButton;
    private Button mWebpButton;


    File imagesDir;

    boolean gifSupported = false;
    boolean pngSupported = false;
    boolean webpSupported = false;

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }


    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);

    }

    @Override
    public void onFinishInputView(boolean finishingInput) {
        super.onFinishInputView(finishingInput);
    }

    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {

    }


    @Override
    public void onCreate() {
        super.onCreate();
        imagesDir = new File(getFilesDir(), "images");
        imagesDir.mkdirs();
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        String[] mimeTypes = EditorInfoCompat.getContentMimeTypes(attribute);

        gifSupported = false;
        pngSupported = false;
        webpSupported = false;
        for (String mimeType : mimeTypes) {
            if (ClipDescription.compareMimeTypes(mimeType, MIME_TYPE_GIF)) {
                gifSupported = true;
            }
            if (ClipDescription.compareMimeTypes(mimeType, MIME_TYPE_PNG)) {
                pngSupported = true;
            }
            if (ClipDescription.compareMimeTypes(mimeType, MIME_TYPE_WEBP)) {
                webpSupported = true;
            }
        }

        Log.e(TAG, "gifSupported--" + gifSupported);
        Log.e(TAG, "pngSupported--" + pngSupported);
        Log.e(TAG, "webpSupported--" + webpSupported);
    }

    @Override
    public View onCreateInputView() {

        view = getLayoutInflater().inflate(R.layout.emoji_keyboard, null);
        Log.i("simple ime service", "on create view");

        final ViewPager emojisPager = (ViewPager) view.findViewById(R.id.emojis_pager);
        image_emoji_layout = (FrameLayout) view.findViewById(R.id.image_emoji_layout);
        emoji_layout = (LinearLayout) view.findViewById(R.id.emoji_layout);

        emijiView = new EmijiView();
        fm = emijiView.getSupportFragmentManager();

        load_quarty_keyboard();
        view.findViewById(R.id.img_chat_popup_back).setOnTouchListener(new EmojiconsFragment.RepeatListener(1000, 50, new View.OnClickListener() {
            //        	R.id.emojis_backspace
            @Override
            public void onClick(View v) {

            }
        }));

        view.findViewById(R.id.img_chat_popup_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Utils.getPrefs(getApplicationContext(), Constants.PREF_USERID) != null) && (Utils.getPrefs(getApplicationContext(), Constants.PREF_USERID).toString().length() > 0)) {
                    load_emoji_view();
                } else {
                    load_setting_view();
                }

            }
        });


        view.findViewById(R.id.img_chat_popup_addpicture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_quarty_keyboard();
            }
        });

        view.findViewById(R.id.img_chat_popup_captureimage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_setting_view();
            }
        });

        return view;
    }

    public void load_emoji_view() {
        Log.d("SimpelIME", "gallary fragment");
        image_emoji_layout.setVisibility(View.VISIBLE);
        emoji_layout.setVisibility(View.GONE);

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        GridView myView = (GridView) inflater.inflate(R.layout.emoji_gallary_grid, null);
        image_emoji_layout.removeAllViews();
        image_emoji_layout.addView(myView);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        GridView imageGrid = (GridView) myView.findViewById(R.id.gallary_gridview);
        ArrayList<String> bitmapList = new ArrayList<String>();

        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // get Internet status
        boolean isInternetPresent = false;
        isInternetPresent = cd.isConnectingToInternet();

        Log.i("isInternetPresent:", " " + isInternetPresent);
        if (isInternetPresent) {

            ArrayList<String> stringArrayList = new ArrayList<>();

            stringArrayList.add("http://www.personal.psu.edu/users/r/j/rjq5009/mushroom.png");
            stringArrayList.add("http://www.freeiconspng.com/uploads/green-humming-bird-png-4.png");
            stringArrayList.add("http://www.queness.com/resources/images/png/apple_ex.png");
            stringArrayList.add("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcT7hvjx9l0Oid9wZhcsKg2YeMCZ1FtzJlyDdvknn-SaJdrEu_rQSA");
            stringArrayList.add("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcR6LtDD_c6NiRPq5y8CYuN1N6n9HGMb1IBRXLd_-orSh6BhR822");
            stringArrayList.add("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTxZKXYIXjFzWdZQVmNqA0qfpnfVE5wO54Jfp0GikRzy3Eg_9FR");
            stringArrayList.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRQAtUkMnzqKFui2w3V9zduOpjkfp_U6LltzZXVOaNrqGjurIQh");
            stringArrayList.add("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcSjKsxA6wgqYFJARuYpqrWrSRCeOW2oktwtaVVOFBSAK0wJOVBP-Q");
            stringArrayList.add("http://www.pngall.com/wp-content/uploads/2016/04/Jaguar-PNG-HD.png");
            stringArrayList.add("http://www.freeiconspng.com/uploads/bird-png-15.png");

            emojiDatasets.clear();
            for (int i = 0; i < stringArrayList.size(); i++) {
                EmojiDataset emojiDataset = new EmojiDataset();
                emojiDataset.setUrl(stringArrayList.get(i));
//                        Bitmap bitmap=getBitmapFromURL(stringArrayList.get(i));
//                        emojiDataset.setFile(persistImage(bitmap,"image"+i));
//                        emojiDataset.setFilepath(emojiDataset.getFile().getAbsolutePath());
//                        Log.i("filepath:", " " + emojiDataset.getFilepath());
                emojiDatasets.add(emojiDataset);
            }

            Log.i("emojiDatasets.size():", " " + emojiDatasets.size());
            Log.i("emojiDatasets:", " " + emojiDatasets.toString());

            imageGrid.setAdapter(new GridviewAdapter());
            Log.i("gallaryfragment", "this.bitmapList" + emojiDatasets);

            Log.i("gallaryfragment", "this.bitmapList.size()" + emojiDatasets.size());
        } else {

            Snackbar snackbar = Snackbar.make(myView.findViewById(android.R.id.content), " Sorry! No Internet!!!", Snackbar.LENGTH_LONG);

            // Changing message text color
            snackbar.setActionTextColor(Color.BLUE);

            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();

            Toast.makeText(getApplicationContext(), "  No Internet Connection!!!.  ", Toast.LENGTH_LONG).show();

        }
    }

    public void load_quarty_keyboard() {
        image_emoji_layout.setVisibility(View.VISIBLE);
        emoji_layout.setVisibility(View.GONE);

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final KeyboardView myView = (KeyboardView) inflater.inflate(R.layout.letter_keyboard, null);

        myView.setBackgroundResource(R.drawable.bg);
        keyboard = new Keyboard(getApplicationContext(), R.xml.qwerty);
        myView.setKeyboard(keyboard);
        myView.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {

            @Override
            public void onPress(int primaryCode) {

            }

            @Override
            public void onRelease(int primaryCode) {

            }

            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                InputConnection ic = getCurrentInputConnection();
                playClick(primaryCode);
                switch (primaryCode) {
                    case Keyboard.KEYCODE_DELETE:
                        ic.deleteSurroundingText(1, 0);
                        break;
                    case Keyboard.KEYCODE_SHIFT:
                        caps = !caps;
                        keyboard.setShifted(caps);
                        myView.invalidateAllKeys();
                        break;
                    case Keyboard.KEYCODE_DONE:
                        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                        break;
                    default:
                        char code = (char) primaryCode;
                        if (Character.isLetter(code) && caps) {
                            code = Character.toUpperCase(code);
                        }
                        ic.commitText(String.valueOf(code), 1);
                }
            }

            @Override
            public void onText(CharSequence text) {


            }

            @Override
            public void swipeLeft() {

            }

            @Override
            public void swipeRight() {

            }

            @Override
            public void swipeDown() {

            }

            @Override
            public void swipeUp() {

            }

            private void playClick(int keyCode) {
                AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
                switch (keyCode) {
                    case 32:
                        am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                        break;
                    case Keyboard.KEYCODE_DONE:
                    case 10:
                        am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                        break;
                    case Keyboard.KEYCODE_DELETE:
                        am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                        break;
                    default:
                        am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
                }
            }
        });

        image_emoji_layout.removeAllViews();
        image_emoji_layout.addView(myView);
    }

    public void load_setting_view() {
        image_emoji_layout.setVisibility(View.VISIBLE);
        emoji_layout.setVisibility(View.GONE);

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout myView = (LinearLayout) inflater.inflate(R.layout.emoji_activity_main, null);
        image_emoji_layout.removeAllViews();
        image_emoji_layout.addView(myView);

        myView.findViewById(R.id.btn_change_keyboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager ime = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (ime != null) {
                    ime.showInputMethodPicker();
                }
            }
        });

        myView.findViewById(R.id.btn_goto_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Utils.getPrefs(getApplicationContext(), Constants.PREF_USERID) != null) && (Utils.getPrefs(getApplicationContext(), Constants.PREF_USERID).toString().length() > 0)) {
                    Log.i("TAG", "Profile PREF_USERID" + Utils.getPrefs(getApplicationContext(), Constants.PREF_USERID));
                    Intent intent = new Intent(getApplicationContext(), Profile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else {

                    Log.i("TAG", "MainActivity PREF_USERID" + Utils.getPrefs(getApplicationContext(), Constants.PREF_USERID));
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private File persistImage(Bitmap bitmap, File outputDir, String name) {
        File filesDir = this.getFilesDir();

        final File imageFile = new File(outputDir, name + ".png");
//        File imageFile = new File(filesDir, name + ".png");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e("LoginActivity", "Error writing bitmap", e);
        }
        return imageFile;
    }

    public static void input(EditText editText, Emojicon emojicon) {
        if (editText == null || emojicon == null) {
            return;
        }

        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start < 0) {
            editText.append(emojicon.getEmoji());
        } else {
            editText.getText().replace(Math.min(start, end), Math.max(start, end), emojicon.getEmoji(), 0, emojicon.getEmoji().length());
        }
    }


    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int i) {
        if (mEmojiTabLastSelectedIndex == i) {
            return;
        }
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                if (mEmojiTabLastSelectedIndex >= 0 && mEmojiTabLastSelectedIndex < mEmojiTabs.length) {
                    mEmojiTabs[mEmojiTabLastSelectedIndex].setSelected(false);
                }
                mEmojiTabs[i].setSelected(true);
                mEmojiTabLastSelectedIndex = i;
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }


    public class GridviewAdapter extends BaseAdapter {
        public GridviewAdapter() {
            super();

        }

        @Override
        public int getCount() {
            return emojiDatasets.size();
        }

        @Override
        public Object getItem(int position) {
            return emojiDatasets.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public class ViewHolder {
            public ImageView img_emojilist;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder view;
            final LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());

            if (convertView == null) {
                view = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.emojilist_row, null);

                view.img_emojilist = (ImageView) convertView.findViewById(R.id.img_emojilist);

                convertView.setTag(view);
            } else {
                view = (ViewHolder) convertView.getTag();
            }
            Picasso.with(getApplicationContext()).load(emojiDatasets.get(position).getUrl()).error(R.drawable.ic_vp_smileys).placeholder(R.drawable.ic_vp_smileys).into(view.img_emojilist);
            view.img_emojilist.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "image click");
//                    Intent i = new Intent(Intent.ACTION_SEND);
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                    Uri screenshotUri=getUriToResource(getApplicationContext(),R.drawable.ic_launcher);
//                    i.putExtra(Intent.EXTRA_STREAM, screenshotUri);
//                    i.setType("image/png");
//                    i.setPackage(Telephony.Sms.getDefaultSmsPackage(getApplicationContext()));


//working
//                    Uri screenshotUri=getUriToResource(getApplicationContext(),R.drawable.ic_launcher);
//                    passImage(screenshotUri.toString());


//                    mGifFile = getFileForResource(SimpleIME.this, R.drawable.smile, imagesDir, "image.gif");
//                    mPngFile = getFileForResource(SimpleIME.this, 0, imagesDir, "image.png");
//                    mWebpFile = getFileForResource(SimpleIME.this, R.drawable.ic_vp_smileys, imagesDir, "image.webp");
//
//                    SimpleIME.this.doCommitContent("A droid logo", MIME_TYPE_PNG, mPngFile);


//                    Bitmap bitmap = getBitmapFromURL(emojiDatasets.get(position).getUrl());
//                    Log.i(TAG, "bitmap" + bitmap);
//                    File file = persistImage(bitmap, imagesDir, Long.toString(System.currentTimeMillis()));
//                    Log.i(TAG, "file" + file);
//                    SimpleIME.this.doCommitContent("A waving flag", MIME_TYPE_PNG, file);


//                    Log.d(TAG, "Image passed: " + emojiDatasets.get(position).getUrl());
//                    if (pngSupported){
//                        Bitmap bitmap = scaleDown(getBitmapFromURL(emojiDatasets.get(position).getUrl()),70,true);
//                        Log.i(TAG, "bitmap" + bitmap);
//                        File file = persistImage(bitmap, imagesDir, Long.toString(System.currentTimeMillis()));
//                        Log.i(TAG, "file" + file);
//                        SimpleIME.this.doCommitContent("A waving flag", MIME_TYPE_PNG, file);
//                    }else {
//
//                        Bitmap bitmap = getBitmapFromURL(emojiDatasets.get(position).getUrl());
//                        Log.i(TAG, "bitmap" + bitmap);
//                        Uri uri = getImageUri(getApplicationContext(), bitmap);
//                        Intent intent = null;
//                        intent = createIntent(getApplicationContext(), getCurrentInputBinding(), uri.toString());
//                        try {
//                            getApplicationContext().startActivity(intent);
//                        } catch (ActivityNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                    }


                    Log.d(TAG, "Image passed: " + emojiDatasets.get(position).getUrl());
                    if (pngSupported) {
                        Bitmap bitmap = getBitmapFromURL(emojiDatasets.get(position).getUrl());
                        Log.i(TAG, "bitmap" + bitmap);
                        File file = persistImage(bitmap, imagesDir, Long.toString(System.currentTimeMillis()));
                        Log.i(TAG, "file" + file);
                        SimpleIME.this.doCommitContent("A waving flag", MIME_TYPE_PNG, file);

                    } else {

//                        Bitmap bitmap = getBitmapFromURL(emojiDatasets.get(position).getUrl());
//                        Log.i(TAG, "bitmap" + bitmap);
//                        Uri uri = getImageUri(getApplicationContext(), bitmap);


                        Intent intent = null;
                        File file=saveGIF(emojiDatasets.get(position).getUrl(),".png");
                        intent = createIntent(getApplicationContext(), getCurrentInputBinding(), file);
                        try {
                            getApplicationContext().startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                        }


                    }


                }
            });
            return convertView;
        }
    }

    public File saveGIF(String res,String mimetype)

    {
        try
        {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "me_emoji" + System.currentTimeMillis() + mimetype);

            long startTime = System.currentTimeMillis();

            Log.d(TAG, "on do in background, url open connection");

//            InputStream is = getResources().openRawResource(res);
            InputStream is = new URL(res).openStream();
            Log.d(TAG, "on do in background, url get input stream");
            BufferedInputStream bis = new BufferedInputStream(is);
            Log.d(TAG, "on do in background, create buffered input stream");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Log.d(TAG, "on do in background, create buffered array output stream");

            byte[] img = new byte[1024];

            int current = 0;

            Log.d(TAG, "on do in background, write byte to baos");
            while ((current = bis.read()) != -1) {
                baos.write(current);
            }


            Log.d(TAG, "on do in background, done write");

            Log.d(TAG, "on do in background, create fos");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());

            Log.d(TAG, "on do in background, write to fos");
            fos.flush();

            fos.close();
            is.close();
            Log.d(TAG, "on do in background, done write to fos");
            return file;
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }


    public File getfilefromurl(@NonNull String stringurl, @NonNull String filename) {

        File file = new File("");
        URL url;
        try {
            url = new URL(stringurl);
            URLConnection connection = url.openConnection();
            InputStream in = connection.getInputStream();
            FileOutputStream fos = null;
            fos = new FileOutputStream(new File(filename));
            byte[] buf = new byte[1024];
            while (true) {
                int len = in.read(buf);
                if (len == -1) {
                    break;
                }
                fos.write(buf, 0, len);
            }
            in.close();
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;


    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public Intent createIntent(Context context, InputBinding inputBinding, File uri) {
        String[] packageNames = context.getPackageManager().getPackagesForUid(inputBinding.getUid());
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        sharingIntent.setType("*/*");
        sharingIntent.setPackage(packageNames[0]);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(uri));
        return sharingIntent;
    }

    private void doCommitContent(@NonNull String description, @NonNull String mimeType,
                                 @NonNull File file) {
        final EditorInfo editorInfo = getCurrentInputEditorInfo();

        // Validate packageName again just in case.
        if (!validatePackageName(editorInfo)) {
            return;
        }

        final Uri contentUri = FileProvider.getUriForFile(this, AUTHORITY, file);

        final int flag;
        if (Build.VERSION.SDK_INT >= 25) {

            flag = InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION;
        } else {

            flag = 0;
            try {
                // TODO: Use revokeUriPermission to revoke as needed.
                grantUriPermission(
                        editorInfo.packageName, contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (Exception e) {
                Log.e(TAG, "grantUriPermission failed packageName=" + editorInfo.packageName
                        + " contentUri=" + contentUri, e);
            }
        }

        final InputContentInfoCompat inputContentInfoCompat = new InputContentInfoCompat(
                contentUri,
                new ClipDescription(description, new String[]{mimeType}),
                null /* linkUrl */);
        InputConnectionCompat.commitContent(
                getCurrentInputConnection(), getCurrentInputEditorInfo(), inputContentInfoCompat,
                flag, null);
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    private boolean validatePackageName(@Nullable EditorInfo editorInfo) {
        if (editorInfo == null) {
            return false;
        }
        final String packageName = editorInfo.packageName;
        if (packageName == null) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }

        final InputBinding inputBinding = getCurrentInputBinding();
        if (inputBinding == null) {

            Log.e(TAG, "inputBinding should not be null here. "
                    + "You are likely to be hitting b.android.com/225029");
            return false;
        }
        final int packageUid = inputBinding.getUid();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final AppOpsManager appOpsManager =
                    (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            try {
                appOpsManager.checkPackage(packageUid, packageName);
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        final PackageManager packageManager = getPackageManager();
        final String possiblePackageNames[] = packageManager.getPackagesForUid(packageUid);
        for (final String possiblePackageName : possiblePackageNames) {
            if (packageName.equals(possiblePackageName)) {
                return true;
            }
        }
        return false;
    }


    private static File getFileForResource(
            @NonNull Context context, int res, @NonNull File outputDir,
            @NonNull String filename) {
        final File outputFile = new File(outputDir, filename);
        final byte[] buffer = new byte[4096];
        InputStream resourceReader = null;
        try {
            try {
                resourceReader = context.getResources().openRawResource(res);
                OutputStream dataWriter = null;
                try {
                    dataWriter = new FileOutputStream(outputFile);
                    while (true) {
                        final int numRead = resourceReader.read(buffer);
                        if (numRead <= 0) {
                            break;
                        }
                        dataWriter.write(buffer, 0, numRead);
                    }
                    return outputFile;
                } finally {
                    if (dataWriter != null) {
                        dataWriter.flush();
                        dataWriter.close();
                    }
                }
            } finally {
                if (resourceReader != null) {
                    resourceReader.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }


}