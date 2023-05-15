package com.sarrawi.mymessages;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.sarrawi.mymessages.adapter.MsgFavAdapter;
import com.sarrawi.mymessages.database.DatabaseHelper;
import com.sarrawi.mymessages.model.Msg;

import java.util.List;

public class FavActivity extends AppCompatActivity {
    RecyclerView RVMsgFav;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    DrawerLayout drawer;
    private MsgFavAdapter msgFavAdapter;
    private List<Msg> msg_list;
    private DatabaseHelper mDBHelper;
    /////////////////////////////
    private SharedPreferences preferences;
    public static final String TAG = "Armstring";
    private String txtView1Size;
    private Typeface font;
    private int textSize;
    private Typeface font1,font2,font3,font4,font5,font6,font7;
    /////////////
    AdView mAdView;
    AdRequest AdRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RVMsgFav=(RecyclerView)findViewById(R.id.Msg);
        mDBHelper = new DatabaseHelper(this);

        msg_list = mDBHelper.getFavMessages();
        //Init adapter
        msgFavAdapter= new MsgFavAdapter(msg_list, FavActivity.this,font,textSize);
        RVMsgFav.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        RVMsgFav.setLayoutManager(mLayoutManager);
        RVMsgFav.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        RVMsgFav.setItemAnimator(new DefaultItemAnimator());
        RVMsgFav.setAdapter(msgFavAdapter);
        RVMsgFav.setNestedScrollingEnabled(false);

        AdsView();
        prepareAd();

    }


    private void AdsView() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        mAdView = (AdView) findViewById(R.id.adView_msg);
        mAdView.loadAd(new AdRequest.Builder().build());
    }


    public void  prepareAd(){


    }

    @Override
    protected void onStart() {
        super.onStart();
//        specifyFont();
//        specifyFontSize();
        if(mAdView != null) {
            mAdView.loadAd(new AdRequest.Builder().build());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        specifyFont();
//        specifyFontSize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        specifyFont();
//        specifyFontSize();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        specifyFont();
//        specifyFontSize();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        specifyFont();
//        specifyFontSize();
//        showAds();
//        prepareAd();
//        AdsView();
        msgFavAdapter.notifyDataSetChanged();
        if(mAdView != null) {
            mAdView.loadAd(new AdRequest.Builder().build());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        specifyFont();
//        specifyFontSize();
    }

}
