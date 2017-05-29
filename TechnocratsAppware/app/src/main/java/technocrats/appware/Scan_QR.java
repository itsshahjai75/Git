package technocrats.appware;

import technocrats.appware.QRCodeReaderView.OnQRCodeReadListener;
import technocrats.appware.QRCodeReaderView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Scan_QR extends AppCompatActivity implements technocrats.appware.QRCodeReaderView.OnQRCodeReadListener {

    private TextView myTextView;
	private QRCodeReaderView mydecoderview;
	private ImageView line_image;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan__qr);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(this);
        
        myTextView = (TextView) findViewById(R.id.exampleTextView);
        
        line_image = (ImageView) findViewById(R.id.red_line_image);
        
        TranslateAnimation mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.5f);
       mAnimation.setDuration(1000);
       mAnimation.setRepeatCount(-1);
       mAnimation.setRepeatMode(Animation.REVERSE);
       mAnimation.setInterpolator(new LinearInterpolator());
       line_image.setAnimation(mAnimation);
        
    }

    
    // Called when a QR is decoded
    public void onQRCodeRead(String text, PointF[] points) {
		myTextView.setText(text);
	}

	
	// Called when your device have no camera
	public void cameraNotFound() {
		
	}

	// Called when there's no QR codes in the camera preview image
	public void QRCodeNotFoundOnCamImage() {
		
	}
    
	@Override
	protected void onResume() {
		super.onResume();
		mydecoderview.getCameraManager().startPreview();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mydecoderview.getCameraManager().stopPreview();
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
}
