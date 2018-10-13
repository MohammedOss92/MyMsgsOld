package com.sarrawi.mymessages;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.sarrawi.mymessages.database.DatabaseHelper;
import com.sarrawi.mymessages.model.Msg;

import java.util.List;

public class PagerMessage extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    List<Msg> msg_list;
    int titleID;
    boolean sourceISFav;
    int msgId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i=getIntent();
        titleID=i.getExtras().getInt("titleID");
        int pos=i.getExtras().getInt("pos");
        msgId=i.getExtras().getInt("msgID");
        // in case of fav the origPos sent from FavMessages as favOrder
        // but in case of normal message it sent as origpos
        sourceISFav=i.getExtras().getBoolean("sourceIsFav");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.


        DatabaseHelper ss=new DatabaseHelper(getApplicationContext());
//        if(sourceISFav)
//        {
//            //we have to keep it ASC in Fav because of pager position
//            //because we use its order while in normal messages it is not favorder but it is origposition
//            msg_list = ss.getFavMessagesOrderedASC() ;
//        }
//        else {
            msg_list = ss.getMessagesNotOrdered(titleID);
//        }
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(pos);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pager_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }
            TextView tvMsg,tvTitle;
            ImageView fav;
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber,String msg,Msg m) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString("Resaleh", msg);
            //args.putParcelable("MsgObject", (Parcelable) m);
            args.putParcelable("MsgObject",  m);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pager_message, container, false);
            final DatabaseHelper data=new DatabaseHelper(getActivity());
            tvMsg=(TextView)rootView.findViewById(R.id.tvMsg);
            tvTitle=(TextView)rootView.findViewById(R.id.tvTitle);
            fav = (ImageView)rootView.findViewById(R.id.imgFavaa);

            String msg=getArguments().getString("Resaleh");
            final Msg m=getArguments().getParcelable("MsgObject");
            final DatabaseHelper s=new DatabaseHelper(getActivity());
            String titleDesc=s.getMsgTitleByTitleID(m.getID_Type());
            tvTitle.setText(titleDesc);
            tvMsg.setText(msg);

            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(data.fav())
                }
            });

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Msg m=(Msg) msg_list.get(position);
            return PlaceholderFragment.newInstance(position + 1,m.getMsg_Name(), m);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return msg_list.size();
        }
    }
}
