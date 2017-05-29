package technocrats.appware;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class WhatsHotFragment extends Fragment {
	
	public WhatsHotFragment(){}
	
	
	
	Dialog mDialog;
	
	Button btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_whats_hot, container, false);
         
        btn_1 = (Button) rootView.findViewById(R.id.btn_1);
        btn_2 = (Button) rootView.findViewById(R.id.btn_2);
        btn_3 = (Button) rootView.findViewById(R.id.btn_3);
        btn_4 = (Button) rootView.findViewById(R.id.btn_4);
        btn_5 = (Button) rootView.findViewById(R.id.btn_5);
        btn_6 = (Button) rootView.findViewById(R.id.btn_6);
        btn_7 = (Button) rootView.findViewById(R.id.btn_7);
        
        
        

        
        btn_2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				/* Creating a alert dialog builder */
			     		
				

				
					
						mDialog=new Dialog(getActivity());
						            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						            mDialog.setContentView(R.layout.dialog_custome);
						            TextView dia_title,dia_msg;
						            dia_title=(TextView) mDialog.findViewById(R.id.dia_title);
						            dia_msg=(TextView) mDialog.findViewById(R.id.dia_msg);
						          
						            
						            dia_title.setText("Developer Team:");
						            dia_msg.setText("1)Viren Kheni\n2)Sunil Patil\n3)Vishal Savaliya \n4)Rahul Vekariya\n***About This App***\nThis is a very famous applicatoin now a days for Restaurants and Hotels.Very cost effecting and time saving application for them.Also very user friendly to see Whole Menu and all. ");
						            
						            mDialog.show();
	
						
						
						
						
						
						
						
						
						
						
						
			}
		});
        
        
        
  btn_3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				
				
				
				mDialog=new Dialog(getActivity());
	            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	            mDialog.setContentView(R.layout.dialog_custome);
	            TextView dia_title,dia_msg;
	            dia_title=(TextView) mDialog.findViewById(R.id.dia_title);
	            dia_msg=(TextView) mDialog.findViewById(R.id.dia_msg);
	          
	            
	            dia_title.setText("Developer Team:");
	            dia_msg.setText("1)Sagar Gondaliya\n2)Vaibhav Lunagariya\n3)Kalpesh Patel \n4)Arjun Borda\n***About App***\nThis Application will show the specific location of medical store that have user reqred Medicine not all Medical stores,also user can see live stocks,discounts and all other details of medical store by this app.");
	            
	            mDialog.show();

			     		
				
			}
		});
  
  
  
  
  
  btn_4.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		
		     		/** Setting the content for the alert dialog */
		     		mDialog=new Dialog(getActivity());
		            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		            mDialog.setContentView(R.layout.dialog_custome);
		            TextView dia_title,dia_msg;
		            dia_title=(TextView) mDialog.findViewById(R.id.dia_title);
		            dia_msg=(TextView) mDialog.findViewById(R.id.dia_msg);
		          
		            
		            dia_title.setText("Developer Team:");
		            dia_msg.setText("1)Manisha Prajapati\n2)Swati Bhakhar\n3)Bhoomika Bathawar  \n4)Hima Sojitra\n***About App***\nNow a days very hard to find out some professionalalist like tailor,carpentor,electrician in some area so this app will help full to find out every catagory professional people small to big class.");
		            
		            mDialog.show();
        
		}
	});

        
  btn_5.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		
		     		/** Setting the content for the alert dialog */
		     		mDialog=new Dialog(getActivity());
		            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		            mDialog.setContentView(R.layout.dialog_custome);
		            TextView dia_title,dia_msg;
		            dia_title=(TextView) mDialog.findViewById(R.id.dia_title);
		            dia_msg=(TextView) mDialog.findViewById(R.id.dia_msg);
		          
		            
		            dia_title.setText("Developer Team:");
		            dia_msg.setText("1)Nilesh Chotaliya .D\n2)Shah Jay\n***About App***\nThis Application will save Friend's College life's memory and some funny,emotional and happy moments of those days also Alumni group with events and post Shareing features many more.");
		            
		            mDialog.show();
      
		}
	});  
        
        return rootView;
    }
}
