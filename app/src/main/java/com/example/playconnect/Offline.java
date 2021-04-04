package com.example.playconnect;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx. fragment. app. Fragment;
import androidx. fragment. app.FragmentPagerAdapter;
import androidx. fragment. app. FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.playconnect.ui.main.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;


public class Offline extends AppCompatActivity implements MapsActivity.SendMessage {


    /*@Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
*/
    /*@Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }*/
    final  FragmentManager fragmentManager= new FragmentManager() {
        @NonNull
        @Override
        public FragmentTransaction beginTransaction() {
            return null;
        }

        @Override
        public boolean executePendingTransactions() {
            return false;
        }

        @Nullable
        @Override
        public Fragment findFragmentById(int id) {
            return null;
        }

        @Nullable
        @Override
        public Fragment findFragmentByTag(@Nullable String tag) {
            return null;
        }

        @Override
        public void popBackStack() {

        }

        @Override
        public boolean popBackStackImmediate() {
            return false;
        }

        @Override
        public void popBackStack(@Nullable String name, int flags) {

        }

        @Override
        public boolean popBackStackImmediate(@Nullable String name, int flags) {
            return false;
        }

        @Override
        public void popBackStack(int id, int flags) {

        }

        @Override
        public boolean popBackStackImmediate(int id, int flags) {
            return false;
        }

        @Override
        public int getBackStackEntryCount() {
            return 0;
        }

        @NonNull
        @Override
        public BackStackEntry getBackStackEntryAt(int index) {
            return null;
        }

        @Override
        public void addOnBackStackChangedListener(@NonNull OnBackStackChangedListener listener) {

        }

        @Override
        public void removeOnBackStackChangedListener(@NonNull OnBackStackChangedListener listener) {

        }

        @Override
        public void putFragment(@NonNull Bundle bundle, @NonNull String key, @NonNull Fragment fragment) {

        }

        @Nullable
        @Override
        public Fragment getFragment(@NonNull Bundle bundle, @NonNull String key) {
            return null;
        }

        @NonNull
        @Override
        public List<Fragment> getFragments() {
            return null;
        }

        @Nullable
        @Override
        public Fragment.SavedState saveFragmentInstanceState(@NonNull Fragment f) {
            return null;
        }

        @Override
        public boolean isDestroyed() {
            return false;
        }

        @Override
        public void registerFragmentLifecycleCallbacks(@NonNull FragmentLifecycleCallbacks cb, boolean recursive) {

        }

        @Override
        public void unregisterFragmentLifecycleCallbacks(@NonNull FragmentLifecycleCallbacks cb) {

        }

        @Nullable
        @Override
        public Fragment getPrimaryNavigationFragment() {
            return null;
        }

        @Override
        public void dump(@NonNull String prefix, @Nullable FileDescriptor fd, @NonNull PrintWriter writer, @Nullable String[] args) {

        }

        @Override
        public boolean isStateSaved() {
            return false;
        }
    };
    String currentFragment="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


    }
    @Override
    public void onBackPressed() {
        FragmentManager fm= new FragmentManager() {
            @NonNull
            @Override
            public FragmentTransaction beginTransaction() {
                return null;
            }

            @Override
            public boolean executePendingTransactions() {
                return false;
            }

            @Nullable
            @Override
            public Fragment findFragmentById(int id) {
                return null;
            }

            @Nullable
            @Override
            public Fragment findFragmentByTag(@Nullable String tag) {
                return null;
            }

            @Override
            public void popBackStack() {

            }

            @Override
            public boolean popBackStackImmediate() {
                return false;
            }

            @Override
            public void popBackStack(@Nullable String name, int flags) {

            }

            @Override
            public boolean popBackStackImmediate(@Nullable String name, int flags) {
                return false;
            }

            @Override
            public void popBackStack(int id, int flags) {

            }

            @Override
            public boolean popBackStackImmediate(int id, int flags) {
                return false;
            }

            @Override
            public int getBackStackEntryCount() {
                return 0;
            }

            @NonNull
            @Override
            public BackStackEntry getBackStackEntryAt(int index) {
                return null;
            }

            @Override
            public void addOnBackStackChangedListener(@NonNull OnBackStackChangedListener listener) {

            }

            @Override
            public void removeOnBackStackChangedListener(@NonNull OnBackStackChangedListener listener) {

            }

            @Override
            public void putFragment(@NonNull Bundle bundle, @NonNull String key, @NonNull Fragment fragment) {

            }

            @Nullable
            @Override
            public Fragment getFragment(@NonNull Bundle bundle, @NonNull String key) {
                return null;
            }

            @NonNull
            @Override
            public List<Fragment> getFragments() {
                return null;
            }

            @Nullable
            @Override
            public Fragment.SavedState saveFragmentInstanceState(@NonNull Fragment f) {
                return null;
            }

            @Override
            public boolean isDestroyed() {
                return false;
            }

            @Override
            public void registerFragmentLifecycleCallbacks(@NonNull FragmentLifecycleCallbacks cb, boolean recursive) {

            }

            @Override
            public void unregisterFragmentLifecycleCallbacks(@NonNull FragmentLifecycleCallbacks cb) {

            }

            @Nullable
            @Override
            public Fragment getPrimaryNavigationFragment() {
                return null;
            }

            @Override
            public void dump(@NonNull String prefix, @Nullable FileDescriptor fd, @NonNull PrintWriter writer, @Nullable String[] args) {

            }

            @Override
            public boolean isStateSaved() {
                return false;
            }
        };
        if(fm.getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
        else if(fm.getBackStackEntryCount() == 1) {
            moveTaskToBack(false);
        }
        else {
            fm.popBackStack();
        }
    }
    @Override
    public void sendData(String message) {
        String tag = "android:switcher:" + R.id.view_pager + ":" + 1;
        //ScheduleMatch parent= (ScheduleMatch)getSupportFragmentManager().findFragmentByTag(tag);
        Bundle bundle=new Bundle();
        bundle.putString("Address",message);
        Log.i("dfggggggggggggg",message);
        Submit_details f= (Submit_details) getSupportFragmentManager().findFragmentById(R.id.submit_details) ;
        f.displayReceivedData(message);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.logout: {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            }

            default: return  false;
        }
    }
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        public PlaceholderFragment() {
        }
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_offline, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
           // textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new FindMatch();

                    break;
                case 1:
                    fragment = new ScheduleMatch();

                    break;
                case 2:
                    fragment = new History();
                    break;

            }
            return fragment;
        }
        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Find Match";
                case 1:
                    return "Schedule Match";
                case 2:
                    return "History";

            }
            return null;
        }
    }

}
