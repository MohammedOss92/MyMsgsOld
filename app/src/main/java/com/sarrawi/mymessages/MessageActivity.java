package com.sarrawi.mymessages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;

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
import com.sarrawi.mymessages.adapter.MsgAdapter;
import com.sarrawi.mymessages.database.DatabaseHelper;
import com.sarrawi.mymessages.model.Msg;
import com.sarrawi.mymessages.model.MsgTypes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    DrawerLayout drawer;
    RecyclerView Rv_Msg;
    private MsgAdapter msgAdapter;
    private List<Msg> msg_list;
    private DatabaseHelper mDBHelper;
    int titleID;
    ////////////////////////////////
    private SharedPreferences preferences;
    public static final String TAG = "Armstring";
    private String txtView1Size;
    private Typeface font;
    private int textSize;
    private Typeface font1,font2,font3,font4,font5,font6,font7;
    /////////////////////////////
    AdView mAdView;
    AdRequest AdRequest;
    private static final String App_ID = "ca-app-pub-1895204889916566~1424391069";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbaraa);
        setSupportActionBar(toolbar);

        Intent i=getIntent();
        titleID=i.getExtras().getInt("titleID");

        Rv_Msg=(RecyclerView)findViewById(R.id.Msg);
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
        msg_list = mDBHelper.getAllMsg(titleID);
//        mProductList = mDBHelper.getListProduct();
        msgAdapter = new MsgAdapter(msg_list, MessageActivity.this,font,textSize);
        Rv_Msg.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        Rv_Msg.setLayoutManager(mLayoutManager);
        Rv_Msg.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        Rv_Msg.setItemAnimator(new DefaultItemAnimator());
        Rv_Msg.setAdapter(msgAdapter);
        Rv_Msg.setNestedScrollingEnabled(false);
        msgAdapter.notifyDataSetChanged();

        font1=Typeface.createFromAsset(getAssets(),"fonts/a.otf");
        font2=Typeface.createFromAsset(getAssets(),"fonts/ab.otf");
        font3=Typeface.createFromAsset(getAssets(),"fonts/ac.otf");
        font4=Typeface.createFromAsset(getAssets(),"fonts/ad.otf");
        font5=Typeface.createFromAsset(getAssets(),"fonts/ae.otf");
        font6=Typeface.createFromAsset(getAssets(),"fonts/af.otf");
        font7=Typeface.createFromAsset(getAssets(),"fonts/ag.otf");
        preferences = PreferenceManager.getDefaultSharedPreferences(MessageActivity.this);


        AdsView();
        prepareAd();
//        navigationdrawaer();
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
            Log.w("MessageActivity","DB copied");
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_msg, menu);

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            Intent i = new Intent(MessageActivity.this,FavActivity.class);
//            startActivity(i);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void AdsView() {
        mAdView = (AdView) findViewById(R.id.adView_msg);
        mAdView.loadAd(new AdRequest.Builder().build());
    }

    public void  prepareAd(){


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
            msgAdapter = new MsgAdapter(msg_list, MessageActivity.this, font1, textSize);
            Rv_Msg.setAdapter(msgAdapter);
        }
        else if(fontName.matches("ab.otf")){
            Log.d(TAG, "specifyFont: 2");
            font = font2;

            font2=Typeface.createFromAsset(getAssets(),"fonts/ab.otf");
            msgAdapter = new MsgAdapter(msg_list, MessageActivity.this, font2, textSize);
            Rv_Msg.setAdapter(msgAdapter);
        }
        else if(fontName.matches("ac.otf")) {
            Log.d(TAG, "specifyFont: 3");
            font = font3;

            font3=Typeface.createFromAsset(getAssets(),"fonts/ac.otf");
            msgAdapter = new MsgAdapter(msg_list, MessageActivity.this, font3, textSize);
            Rv_Msg.setAdapter(msgAdapter);
        }
        else if(fontName.matches("ad.otf")) {
            Log.d(TAG, "specifyFont: 4");
            font = font4;
            font4=Typeface.createFromAsset(getAssets(),"fonts/ad.otf");
            msgAdapter = new MsgAdapter(msg_list, MessageActivity.this, font4, textSize);
            Rv_Msg.setAdapter(msgAdapter);
        }
        else if(fontName.matches("ae.otf")) {
            Log.d(TAG, "specifyFont: 5");
            font = font5;

            font5=Typeface.createFromAsset(getAssets(),"fonts/ae.otf");
            msgAdapter = new MsgAdapter(msg_list, MessageActivity.this, font5, textSize);
            Rv_Msg.setAdapter(msgAdapter);
        }
        else if(fontName.matches("af.otf")) {
            Log.d(TAG, "specifyFont: 6");
            font = font6;

            font6=Typeface.createFromAsset(getAssets(),"fonts/af.otf");
            msgAdapter = new MsgAdapter(msg_list, MessageActivity.this, font6, textSize);
            Rv_Msg.setAdapter(msgAdapter);
        }
        else if(fontName.matches("ag.otf")) {
            Log.d(TAG, "specifyFont: 7");
            font = font7;


            font7=Typeface.createFromAsset(getAssets(),"fonts/ag.otf");
            msgAdapter = new MsgAdapter(msg_list, MessageActivity.this, font7, textSize);
            Rv_Msg.setAdapter(msgAdapter);
        }
    }
    private void specifyFontSize(){
        txtView1Size = preferences.getString("TEXT_SIZE","14");
        textSize = Integer.parseInt(txtView1Size);
        msgAdapter = new MsgAdapter(msg_list, MessageActivity.this, font, textSize);
        Rv_Msg.setAdapter(msgAdapter);
    }
//    private void navigationdrawaer() {
//
//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        NavigationView rightNavigationView = (NavigationView) findViewById(R.id.nav_right_view);
//        rightNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(MenuItem item) {
//                // Handle Right navigation view item clicks here.
//                int id = item.getItemId();
//
//                if (id == R.id.nav_azkar) {
////                    Intent i =new Intent(MessageActivity.this,MessageActivity.class);
////                    startActivity(i);
//
//                }
//                else if (id == R.id.nav_fav) {
//                    Intent i =new Intent(MessageActivity.this,FavActivity.class);
//                    startActivity(i);
//
//                }
////                else if (id == R.id.nav_seit) {
////                    Intent i =new Intent(MessageActivity.this,SettingsActivity.class);
////                    startActivity(i);
////
////                }
//
//
//                drawer.closeDrawer(GravityCompat.END); /*Important Line*/
//                return true;
//            }
//        });
//    }
}
