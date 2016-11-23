package com.iodapp.adapter;



import com.iodapp.activities.Paeg13;
import com.iodapp.activities.Page12;
import com.iodapp.activities.Page11;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

	public PagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new Page11();
		case 1:
			// Games fragment activity
			return new Page12();
		case 2:
			return new Paeg13();
		
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
