package com.emoji.me_emoji;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.emoji.me_emoji.dataset.EmojiDataset;
import com.emoji.me_emoji.utils.RuntimePermissionsActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SplashActivity extends RuntimePermissionsActivity {

    private static final int REQUEST_PERMISSIONS = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SplashActivity.super.requestAppPermissions(new
                        String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, R.string.runtime_permissions_txt
                , REQUEST_PERMISSIONS);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashActivity.this , LoginActivity.class));
                finish();
            }
        } , 5000);

    }

    @Override
    public void onPermissionsGranted(int requestCode) {
       // Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
    }


}
