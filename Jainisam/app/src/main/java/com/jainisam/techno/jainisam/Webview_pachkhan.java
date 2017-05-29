package com.jainisam.techno.jainisam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

public class Webview_pachkhan extends AppCompatActivity {

    WebView web_pdf;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_pachkhan);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        web_pdf=(WebView)this.findViewById(R.id.webpdf);



        String url = "https://docs.google.com/gview?embedded=true&url=http://technocratsappware.com/androidapps/pachkhaan.pdf";
        web_pdf.getSettings().setJavaScriptEnabled(true);
        web_pdf.getSettings().setUserAgentString("Desktop");
        web_pdf.getSettings().setBuiltInZoomControls(true);
        web_pdf.loadUrl(url);


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


        finish();
    }

}
