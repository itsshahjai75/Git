package technocrats.appware;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;


public class HomeFragment extends Fragment  {


	
	public HomeFragment(){}


	private FloatingActionButton fab1;
	private FloatingActionButton fab2;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

		final FloatingActionMenu menu1 = (FloatingActionMenu)rootView.findViewById(R.id.menu1);

		menu1.setOnMenuButtonClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (menu1.isOpened()) {
					//Toast.makeText(getActivity(), menu1.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
				}

				menu1.toggle(true);
			}
		});
		menu1.setClosedOnTouchOutside(true);


		fab1 = (FloatingActionButton)rootView.findViewById(R.id.fab1);
		fab2 = (FloatingActionButton)rootView.findViewById(R.id.fab2);

		//fab1.setEnabled(false);

		fab1.setOnClickListener(clickListener);
		fab2.setOnClickListener(clickListener);

        return rootView;
    }


	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			String text = "";

			switch (v.getId()) {
				case R.id.fab1:
					text = fab1.getLabelText();
					break;
				case R.id.fab2:
					Intent a = new Intent(getActivity(),Contactus.class);
					startActivity(a);
					text = fab2.getLabelText();
					//fab2.setVisibility(View.GONE);
					break;

			}

			Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
		}
	};
}
