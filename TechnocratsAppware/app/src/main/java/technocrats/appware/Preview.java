package technocrats.appware;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Preview extends AppCompatActivity {
	
	TextView tv_name,tv_mobile,tv_course,tv_student,tv_fee,tv_grp_member,tv_email,tv_add,tv_clg;
	 String partner_names,name,mobile,course,student_no,fee,email,add,clg;
	 Button btn_submit;
	 
	 
	 AlertDialog.Builder builder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		
		
		 if (android.os.Build.VERSION.SDK_INT > 14) { 
	            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	            StrictMode.setThreadPolicy(policy);
	        }

		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);


		tv_name = (TextView)this.findViewById(R.id.tv_name);
		tv_mobile = (TextView)this.findViewById(R.id.tv_mobile);
		tv_course = (TextView)this.findViewById(R.id.tv_course);
		tv_student = (TextView)this.findViewById(R.id.tv_grp_size);
		tv_fee = (TextView)this.findViewById(R.id.tv_pre_fee);
		tv_grp_member = (TextView)this.findViewById(R.id.tv_grp_members);
		tv_email = (TextView)this.findViewById(R.id.tv_email);
		tv_add = (TextView)this.findViewById(R.id.tv_address);
		
		tv_clg = (TextView)this.findViewById(R.id.tv_clg);
		btn_submit=(Button)this.findViewById(R.id.btn_reg);
		
		
		 name= getIntent().getStringExtra("p_name");
		 mobile = getIntent().getStringExtra("p_mobile");
		 course= getIntent().getStringExtra("p_course");
		 student_no= getIntent().getStringExtra("p_stu_no");
		 fee= getIntent().getStringExtra("p_fee");
		
		int stu_count= Integer.parseInt(student_no);
		if(stu_count>1){
			tv_grp_member.setVisibility(View.VISIBLE);
		 partner_names=getIntent().getStringExtra("p_student");
		}else{
			tv_grp_member.setVisibility(View.GONE);
			// partner_names=getIntent().getStringExtra("p_student");
				
		}
		
		
		 email= getIntent().getStringExtra("p_email");
		 add= getIntent().getStringExtra("p_add");
		 clg= getIntent().getStringExtra("p_clg");
		
		
		
		
		tv_name.setText(name);
		tv_mobile.setText(mobile);
		tv_course.setText(course);
		tv_student.setText("No Of Students: "+student_no);
		tv_fee.setText(fee);
		tv_grp_member.setText(partner_names);
		tv_email.setText(email);
		tv_add.setText(add);
		tv_clg.setText(clg);
		
		
		System.out.println("\nName:"+name+
        		"\nMo.No:"+mobile+
        		"\nCourse:"+course+
        		"\nNo.of Students:"+stu_count+
        		
        		"\nPartner's Name"+partner_names+
        		"\nEmail Add:"+email+
        		"\nRes.Add"+add+
        		"\nCollege Name:"+clg+
        		"\n"+fee);
		
		

       builder = new AlertDialog.Builder(this);
		
	btn_submit.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			

             
             new gmailsend().execute();
            
 		
			
		}
	});	
		
		
	}
	
	
//===========================================
	//================================================
	//=====================================================
	
	class gmailsend extends AsyncTask<Object, Void, String> {

        private final static String TAG = "LoginActivity.EfetuaLogin";

        protected ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()
        {
        	

        	//System.out.println("On PRe Execute----done-------");
            super.onPreExecute();
            //Log.d(TAG, "Executando onPreExecute de EfetuaLogin");
            //inicia di�logo de progresso, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(Preview.this, "Loading", "Please Wait--------.", true, false);
        }


        @SuppressWarnings("unchecked")
        @Override
        protected String doInBackground(Object... parametros) {
        	
        	//System.out.println("On do in back ground----done-------"); 	
        	
        	
        	
            //Log.d(TAG, "Executando doInBackground de EfetuaLogin");
			
            
			
			 GMailSender mailsender = new GMailSender("your email jenathi mail send krvo che", "Yourpassword write here");
            String[] toArr = {"shah.jai75@gmail.com","jay@technocratsappware.com"+name};
            mailsender.set_to(toArr);
            mailsender.set_from("technocratsappware@gmail.com");
            mailsender.set_subject("This is an email sent using my Mail JavaMail wrapper from an Android device.");
            mailsender.setBody("Email body.\n\n\n" +
            		"hi" +"\nThis is Detials of Student Online Regestration Form." +
            		"\n\n"+name+
            		"\n"+email+
            		"\n"+course+
            		"\nNo.of Students:"+student_no+
            		"\nPartner's name:"+partner_names+
            		"\n"+email+
            		"\n"+add+
            		"\n"+clg+
            		"\n"+tv_fee.getText().toString()+
            		"\n\n.........Auto Generated Mail.........");

            try {
                //mailsender.addAttachment("/sdcard/filelocation");

                if (mailsender.send()) {
                    Toast.makeText(Preview.this,
                            "Email was sent successfully.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Preview.this, "Email was not sent.",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                
                Log.e("MailApp", "Could not send email", e);
            }
			
            
            
            
            
            
            
            
            return null;

            
 




        }       	
        	
        	
        @Override
        protected void onPostExecute(String result)
        {
        	
        	System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

         

           // Log.d(TAG, "Login passou persistindo info de login local no device");

            progressDialog.dismiss();
  
        
            
            
            
 			//builder.setMessage("   Please give Feedback Us!!! ")
 			builder.setIcon(R.drawable.ic_launcher)
 					.setTitle("---Thank You---")
 			       .setMessage("Your Registration Done."+"\nWe will call you within 24Hr for Verification.")
 					.setCancelable(false)
 			       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
 			           public void onClick(DialogInterface dialog, int id) {
 			        	  
 			        	   Intent feedbackform = new Intent(Preview.this, Homescreen.class);
 							startActivity(feedbackform);
 							
 							finish();
// 							Count++;
 							
 			        	
 			        	    }
 			       });
 			       
 			       
 			   
 			      
 			       builder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
 			           public void onClick(DialogInterface dialog, int id) {
 			        	   Intent intent = new Intent(Intent.ACTION_MAIN);
 			        	    intent.addCategory(Intent.CATEGORY_HOME);
 			        	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 			        	   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 			        	    startActivity(intent);
 			        	   finish();
 			        	   System.exit(0);
 			          			           }
 			       });
 			
 			/*
 			       builder.setNeutralButton(" No  ", new DialogInterface.OnClickListener() {
 			    	   		public void onClick(DialogInterface dialog, int id) {
                 	
                  
                     dialog.cancel();

                 }
             });
*/ 			       
 			       
 	 
 			
 			
 			
 			AlertDialog alert = builder.create();
 			alert.show();
 			    
 			
 			
            
            
        
        
        }

 }	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
}
