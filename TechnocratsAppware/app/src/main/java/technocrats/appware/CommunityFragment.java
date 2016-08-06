package technocrats.appware;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class CommunityFragment extends Fragment {
	
	public CommunityFragment(){
		
	}
	
	Context context;
	String text;
	Button fb_btn,twiter_btn,gpluse_btn,whatsapp_btn;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_community, container, false);
       
        
        
        fb_btn = (Button) rootView.findViewById(R.id.btn_fb);
        twiter_btn=(Button) rootView.findViewById(R.id.btn_twitter);
        gpluse_btn =(Button) rootView.findViewById(R.id.btn_googlepluse);
        whatsapp_btn = (Button) rootView.findViewById(R.id.btn_whtsapp);


        fb_btn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("Test", "onClickListener ist gestartet");

                String facebookUrl = "https://www.facebook.com/technocratsappware";
                try {
                        int versionCode = getActivity().getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
                        if (versionCode >= 3002850) {
                            Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                            startActivity(new Intent(Intent.ACTION_VIEW, uri));;
                        } else {
                            // open the Facebook app using the old method (fb://profile/id or fb://pro
                        }
                } catch (PackageManager.NameNotFoundException e) {
                    // Facebook is not installed. Open the browser
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
                }
            }
        });

        
       twiter_btn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try
			{
			    // Check if the Twitter app is installed on the phone.
			    getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);

			    Intent intent = new Intent(Intent.ACTION_VIEW);
			    intent.setClassName("com.twitter.android", "com.twitter.android.ProfileActivity");
			    // Don't forget to put the "L" at the end of the id.
			    intent.putExtra("user_id", 2408517542L);
			    startActivity(intent);
			}
			catch (NameNotFoundException e)
			{
			    // If Twitter app is not installed, start browser.
			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/Jay8Technoctars")));
			}
		}
	});
	
        gpluse_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/111339892437607769944/posts")));
				
				
			
			}
		});
        
        
        whatsapp_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				
				 Intent waIntent = new Intent(Intent.ACTION_SEND);
				    waIntent.setType("text/plain");
				            String text = "Hi am using your Android app May I talk with you??,Thank You.";
				    waIntent.setPackage("com.whatsapp");
				    if (waIntent != null) {
				        waIntent.putExtra(Intent.EXTRA_TEXT, text);//
				        startActivity(Intent.createChooser(waIntent, "Find Technocrats Appware in your Whatsapp Contact List."));
				    } else {
				        Toast.makeText(getActivity(), "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
				    }
				
				
				
				
				
			}
		});
        
        return rootView;
	}
	
	
	
	
	
	
	
	
}
