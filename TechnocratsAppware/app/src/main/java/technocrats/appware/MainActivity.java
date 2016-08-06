package technocrats.appware;


import java.util.ArrayList;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;

public class MainActivity extends Activity {

	
	

	final int SPLASH_TIME_OUT = 3500;
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_main);
		
		String number = "09033228796";
		contactExists(MainActivity.this, number);
		
		
		new Handler().postDelayed(new Runnable() {
		    @Override
		    public void run() {
		        startActivity(new Intent(MainActivity.this,
		                Homescreen_new.class));
		       overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left);
		        finish();
		       
		    }
		}, SPLASH_TIME_OUT);
        
	

		
	}
	
	
	

	public boolean contactExists(Context context, String number) {
		/// number is the phone number
		Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, 
		Uri.encode(number));
		String[] mPhoneNumberProjection = { PhoneLookup._ID, PhoneLookup.NUMBER, PhoneLookup.DISPLAY_NAME };
		Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
		try {
		   if (cur.moveToFirst()) {
			   
			   System.out.println("Contact allready exist");
		      return true;
		}
		   else{
			   addcontact();
			   System.out.println("add contact method called-----");
		   }
		   
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
		if (cur != null)
		   cur.close();
		}
		return false;
		}

	
	
	
	
	private void addcontact() {
		// TODO Auto-generated method stub
		
    	
    	
      
            ArrayList<ContentProviderOperation> op_list = new ArrayList<ContentProviderOperation>(); 
            op_list.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI) 
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null) 
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null) 
                    //.withValue(RawContacts.AGGREGATION_MODE, RawContacts.AGGREGATION_MODE_DEFAULT) 
                    .build()); 

            // first and last names 
            op_list.add(ContentProviderOperation.newInsert(Data.CONTENT_URI) 
                    .withValueBackReference(Data.RAW_CONTACT_ID, 0) 
                    .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE) 
                    .withValue(StructuredName.GIVEN_NAME, "Technocrats Appware") 
                    .withValue(StructuredName.FAMILY_NAME, "Jay Shah") 
                    .build()); 

            op_list.add(ContentProviderOperation.newInsert(Data.CONTENT_URI) 
                    .withValueBackReference(Data.RAW_CONTACT_ID, 0) 
                    .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "09033228796")
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, Phone.TYPE_HOME)
                    .build());
         

            try{ 
                ContentProviderResult[] results = getContentResolver().applyBatch(ContactsContract.AUTHORITY, op_list); 
                
                System.out.println("CONTACT INSERTED--------"+results.toString());
                
                
            }catch(Exception e){ 
                e.printStackTrace(); 
            } 
        }
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
