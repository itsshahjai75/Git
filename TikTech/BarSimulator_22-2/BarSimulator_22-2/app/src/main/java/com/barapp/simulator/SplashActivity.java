package com.barapp.simulator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;

import com.barapp.simulator.utils.Const;
import com.barapp.simulator.utils.Utils;
import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity {

    public final int MY_REQUEST_CODE=500;
    public final int MY_REQUEST_CODE_STORAGE=600;

    SharedPreferences shared_pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_splash);

        shared_pref = getSharedPreferences("MyPref" , Context.MODE_PRIVATE);
        getSupportActionBar().hide();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(Utils.getPrefs(SplashActivity.this, Const.ISLOGIN).equalsIgnoreCase("yes")){
                    if(Utils.getPrefs(SplashActivity.this, Const.TYPE).equalsIgnoreCase("0")){
                        startActivity(new Intent(SplashActivity.this , OwnerMenuActivity.class));
                        finish();
                    }else if(Utils.getPrefs(SplashActivity.this, Const.TYPE).equalsIgnoreCase("1")){
                        startActivity(new Intent(SplashActivity.this , UserMenuActivity.class));
                        finish();
                    }
                }else{
                    startActivity(new Intent(SplashActivity.this , LoginActivity.class));
                    finish();
                }

              /*if(Utils.getPrefs(SplashActivity.this , Const.ISLOGIN) != null && Utils.getPrefs(SplashActivity.this ,Const.ISLOGIN).equalsIgnoreCase("yes")){
                      if(Utils.getPrefs(SplashActivity.this , Const.TYPE).equals("0")){
                          startActivity(new Intent(SplashActivity.this , OwnerMenuActivity.class));
                      }else{
                          startActivity(new Intent(SplashActivity.this , UserMenuActivity.class));
                      }
                }else{
                    startActivity(new Intent(SplashActivity.this , LoginActivity.class));
                }*/

            }
        } , 6000);


        // To generate key hash
		/*try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"com.barapp.simulator", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
//				Log.d("KeyHash:",
//						Base64.encodeToString(md.digest(), Base64.DEFAULT));
				System.out.println("::KeyHash :  "+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}*/

    }













}
