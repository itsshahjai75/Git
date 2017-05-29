package com.example.grandma1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;

public class TogetherActivity extends Activity implements AnimationListener {

	ImageView imgLogo1,imgLogo2;
	Button btnStart;

	// Animation
	Animation animTogether,animRotate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_together);
		getActionBar().hide();
		
	//	btnStart = (Button) findViewById(R.id.btnStart);

		// load the animation
		animTogether = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.together);

		// set animation listener
		animTogether.setAnimationListener(TogetherActivity.this);
		imgLogo1 = (ImageView) findViewById(R.id.imgLogo1);
		imgLogo2 = (ImageView) findViewById(R.id.imgLogo2);
						imgLogo1.startAnimation(animTogether);
						imgLogo2.setVisibility(View.INVISIBLE);
	
}


/*			
		Thread timer =new Thread(){
			public void run(){
				try{
					animTogether = AnimationUtils.loadAnimation(getApplicationContext(),
							R.anim.together);

					// set animation listener
					animTogether.setAnimationListener(TogetherActivity.this);
					imgLogo1 = (ImageView) findViewById(R.id.imgLogo1);
									imgLogo1.startAnimation(animTogether);

					sleep(5000);
					
					
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
				finally{
				

				}
			}
		};
		timer.start();*/
				
		// button click event
	//	btnStart.setOnClickListener(new View.OnClickListener() {
	//		@Override
		//	public void onClick(View v) {
				// start the animation
		
				
				
				
				
				
				// start the animation
		

				// start the animation
				
				
			//}
	//	});

	

	private void sleep(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// Take any action after completing the animation

		// check for zoom in animation
		if (animation == animTogether) {
			animRotate = AnimationUtils.loadAnimation(getApplicationContext(),
					R.anim.rotate);



			animRotate.setAnimationListener(TogetherActivity.this);



			imgLogo2.setVisibility(View.VISIBLE);
			imgLogo2.startAnimation(animRotate);
			}
		if(animation == animRotate){
			  Intent intent = new Intent(this, MainActivity.class);
			  startActivity(intent);

		}

		}

	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}


	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

}