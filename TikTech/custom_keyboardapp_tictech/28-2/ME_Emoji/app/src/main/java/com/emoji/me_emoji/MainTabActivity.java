package com.emoji.me_emoji;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.emoji.me_emoji.Fragment.EmojiGridFragment;
import com.emoji.me_emoji.Fragment.ProfileFragment;

import static java.security.AccessController.getContext;

/**
 * Created by Testing on 28-Feb-17.
 */

public class MainTabActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tablayout);
        getSupportActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);
        Pager adapter = new Pager(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        //Adding the tabs using addTab() method

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public class Pager extends FragmentStatePagerAdapter {

        public Pager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    EmojiGridFragment emojiGridFragment = new EmojiGridFragment();
                    return emojiGridFragment;
                case 1:
                    ProfileFragment profileFragment = new ProfileFragment();
                    return profileFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            //return the number of tabs you want in your tabLayout
            return 2;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            //this is where you set the titles
            switch (position) {
                case 0:
                    return "Emojies";
                case 1:
                    return "Profile";
            }
            return null;
        }
    }
}