package com.sarrawi.mymessages;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sarrawi.mymessages.adapter.MsgAdapter;
import com.sarrawi.mymessages.adapter.MsgTypesAdapter;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        msgAdapter = new MsgAdapter(msg_list, MessageActivity.this);
        Rv_Msg.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        Rv_Msg.setLayoutManager(mLayoutManager);
        Rv_Msg.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        Rv_Msg.setItemAnimator(new DefaultItemAnimator());
        Rv_Msg.setAdapter(msgAdapter);
        Rv_Msg.setNestedScrollingEnabled(false);
        msgAdapter.notifyDataSetChanged();

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
    public void onResume() {
        super.onResume();
        msgAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_msg, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) MessageActivity.this.getSystemService(Context.SEARCH_SERVICE);


        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();

            final EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchEditText.setTextColor(getResources().getColor(R.color.colorBlack));
            searchEditText.setBackgroundColor(getResources().getColor(R.color.colorWhite));

            int searchImgId = android.support.v7.appcompat.R.id.search_button;
            ImageView v = (ImageView) searchView.findViewById(searchImgId);
            v.setImageResource(R.mipmap.search);

            int closeImgId = android.support.v7.appcompat.R.id.search_close_btn;
            ImageView close_button = (ImageView) searchView.findViewById(closeImgId);
            close_button.setColorFilter(Color.WHITE);

            close_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView.onActionViewCollapsed();
                    //Collapse the search widget
                    searchItem.collapseActionView();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);


                    msg_list.clear();
                    msg_list.addAll(DatabaseHelper.getInstance(MessageActivity.this).getAllMsgnotID());
                    msgAdapter.notifyDataSetChanged();


                }
            });

        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MessageActivity.this.getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                  /*  mProductList.addAll(DataBase.getInstance(MainActivity.this).getAllPrayer());
                    adapter.notifyDataSetChanged();*/
                    msg_list.clear();
                    msg_list.addAll(DatabaseHelper.getInstance(MessageActivity.this).getAllPrayer(newText));
                    msgAdapter.notifyDataSetChanged();
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", "newText" + query);
                    msg_list.clear();
                    msg_list.addAll(DatabaseHelper.getInstance(MessageActivity.this).getAllPrayer(query));
                    msgAdapter.notifyDataSetChanged();

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
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
//            Intent i = new Intent(MainActivity.this,FavActivity.class);
//            startActivity(i);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
