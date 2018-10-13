package com.sarrawi.mymessages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
}
