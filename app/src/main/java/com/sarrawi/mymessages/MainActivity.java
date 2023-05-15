package com.sarrawi.mymessages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.sarrawi.mymessages.adapter.MsgTypesAdapter;
import com.sarrawi.mymessages.database.DatabaseHelper;
import com.sarrawi.mymessages.model.MsgTypes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    RecyclerView RVMsgType;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    DrawerLayout drawer;
    private MsgTypesAdapter msgTypesAdapter;
    private List<MsgTypes> msgTypes_list;
    private DatabaseHelper mDBHelper;
    /////////////////////////////
    AdView mAdView;
    AdRequest AdRequest;
    private static final String App_ID = "ca-app-pub-1895204889916566~1424391069";
    private InterstitialAd mInterstitialAd;
    //////////////////////////////////
    private SharedPreferences preferences;
    public static final String TAG = "Armstring";
    private String txtView1Size;
    private Typeface font;
    private int textSize;
    private Typeface font1,font2,font3,font4,font5,font6,font7;
    //////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbaraa);
        setSupportActionBar(toolbar);
//////aaaaaaaaaaaaa
        RVMsgType=(RecyclerView)findViewById(R.id.Msg_Types);
        mDBHelper = new DatabaseHelper(this);
        //Check exists database
        File database = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);
        if(false == database.exists()) {
            mDBHelper.getReadableDatabase();
            //Copy db
            if(copyDatabase(this)) {
                Toast.makeText(this, "Copy database succes", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Copy data error", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        final MsgTypes m = new MsgTypes();
        //Get product list in db when db exists
        msgTypes_list = mDBHelper.getMessageTypes();
//        msgTypes_list = mDBHelper.getListProduct();
        msgTypesAdapter = new MsgTypesAdapter(msgTypes_list, MainActivity.this,font,textSize);
        RVMsgType.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        RVMsgType.setLayoutManager(mLayoutManager);
        RVMsgType.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        RVMsgType.setItemAnimator(new DefaultItemAnimator());
        RVMsgType.setAdapter(msgTypesAdapter);
        RVMsgType.setNestedScrollingEnabled(false);

        font1=Typeface.createFromAsset(getAssets(),"fonts/a.otf");
        font2=Typeface.createFromAsset(getAssets(),"fonts/ab.otf");
        font3=Typeface.createFromAsset(getAssets(),"fonts/ac.otf");
        font4=Typeface.createFromAsset(getAssets(),"fonts/ad.otf");
        font5=Typeface.createFromAsset(getAssets(),"fonts/ae.otf");
        font6=Typeface.createFromAsset(getAssets(),"fonts/af.otf");
        font7=Typeface.createFromAsset(getAssets(),"fonts/ag.otf");
        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        AdsView();
        prepareAd();
        showAds();
        navigationdrawaer();

    }

    private void navigationdrawaer() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView rightNavigationView = (NavigationView) findViewById(R.id.nav_right_view);
        rightNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle Right navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_azkar) {
//                    Intent i =new Intent(MainActivity.this,MainActivity.class);
//                    startActivity(i);

                }
                else if (id == R.id.nav_fav) {
                    Intent i =new Intent(MainActivity.this,FavActivity.class);
                    startActivity(i);

                }
                else if (id == R.id.nav_seit) {
                    Intent i =new Intent(MainActivity.this,SettingsActivity.class);
                    startActivity(i);

                }
                else if (id == R.id.zak) {
                    Intent i =new Intent(MainActivity.this,Text.class);
                    startActivity(i);

                }


                drawer.closeDrawer(GravityCompat.END); /*Important Line*/
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.aa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_openRight) {
            drawer.openDrawer(GravityCompat.END);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean copyDatabase(Context context) {
        try {

            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[]buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.w("MainActivity","DB copied");
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void AdsView() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        mAdView = (AdView) findViewById(R.id.adView_type);
        mAdView.loadAd(new AdRequest.Builder().build());
    }

    public void  prepareAd(){

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-1895204889916566/4403433142", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }
    private void showAds() {
        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            public void run() {
                Log.i("hello", "world");
                prepareAd();
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(MainActivity.this);
                        } else {
                            Log.d("TAG"," Interstitial not loaded");
                        }
                        prepareAd();
                    }
                });

            }
        }, 4, 4, TimeUnit.MINUTES);
    }

    @Override
    protected void onStart() {
        super.onStart();
        specifyFont();
        specifyFontSize();
        if(mAdView != null) {
            mAdView.loadAd(new AdRequest.Builder().build());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        specifyFont();
        specifyFontSize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        specifyFont();
        specifyFontSize();
    }

    @Override
    protected void onPause() {
        super.onPause();
        specifyFont();
        specifyFontSize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        specifyFont();
        specifyFontSize();
//        showAds();
//        prepareAd();
//        AdsView();
        if(mAdView != null) {
            mAdView.loadAd(new AdRequest.Builder().build());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        specifyFont();
        specifyFontSize();
    }

    private void specifyFont(){
        String fontName = preferences.getString("LIST_OF_FONTS", "Chunkfive.otf");
        Log.d(TAG, "specifyFont: " + fontName);
        if(fontName.matches("a.otf")){
            Log.d(TAG, "specifyFont: 1");
            font = font1;
            font1=Typeface.createFromAsset(getAssets(),"fonts/a.otf");
            msgTypesAdapter = new MsgTypesAdapter(msgTypes_list, MainActivity.this, font1, textSize);
            RVMsgType.setAdapter(msgTypesAdapter);
        }
        else if(fontName.matches("ab.otf")){
            Log.d(TAG, "specifyFont: 2");
            font = font2;

            font2=Typeface.createFromAsset(getAssets(),"fonts/ab.otf");
            msgTypesAdapter = new MsgTypesAdapter(msgTypes_list, MainActivity.this, font2, textSize);
            RVMsgType.setAdapter(msgTypesAdapter);
        }
        else if(fontName.matches("ac.otf")) {
            Log.d(TAG, "specifyFont: 3");
            font = font3;

            font3=Typeface.createFromAsset(getAssets(),"fonts/ac.otf");
            msgTypesAdapter = new MsgTypesAdapter(msgTypes_list, MainActivity.this, font3, textSize);
            RVMsgType.setAdapter(msgTypesAdapter);
        }
        else if(fontName.matches("ad.otf")) {
            Log.d(TAG, "specifyFont: 4");
            font = font4;
            font4=Typeface.createFromAsset(getAssets(),"fonts/ad.otf");
            msgTypesAdapter = new MsgTypesAdapter(msgTypes_list, MainActivity.this, font4, textSize);
            RVMsgType.setAdapter(msgTypesAdapter);
        }
        else if(fontName.matches("ae.otf")) {
            Log.d(TAG, "specifyFont: 5");
            font = font5;

            font5=Typeface.createFromAsset(getAssets(),"fonts/ae.otf");
            msgTypesAdapter = new MsgTypesAdapter(msgTypes_list, MainActivity.this, font5, textSize);
            RVMsgType.setAdapter(msgTypesAdapter);
        }
        else if(fontName.matches("af.otf")) {
            Log.d(TAG, "specifyFont: 6");
            font = font6;

            font6=Typeface.createFromAsset(getAssets(),"fonts/af.otf");
            msgTypesAdapter = new MsgTypesAdapter(msgTypes_list, MainActivity.this, font6, textSize);
            RVMsgType.setAdapter(msgTypesAdapter);
        }
        else if(fontName.matches("ag.otf")) {
            Log.d(TAG, "specifyFont: 7");
            font = font7;


            font7=Typeface.createFromAsset(getAssets(),"fonts/ag.otf");
            msgTypesAdapter = new MsgTypesAdapter(msgTypes_list, MainActivity.this, font7, textSize);
            RVMsgType.setAdapter(msgTypesAdapter);
        }
    }
    private void specifyFontSize(){
        txtView1Size = preferences.getString("TEXT_SIZE","14");
        textSize = Integer.parseInt(txtView1Size);
        msgTypesAdapter = new MsgTypesAdapter(msgTypes_list, MainActivity.this, font, textSize);
        RVMsgType.setAdapter(msgTypesAdapter);
    }

}
