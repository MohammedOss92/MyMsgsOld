package com.sarrawi.mymessages;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sarrawi.mymessages.adapter.MsgAdapter;
import com.sarrawi.mymessages.adapter.MsgFavAdapter;
import com.sarrawi.mymessages.adapter.MsgTypesAdapter;
import com.sarrawi.mymessages.database.DatabaseHelper;
import com.sarrawi.mymessages.model.Fav;
import com.sarrawi.mymessages.model.Msg;
import com.sarrawi.mymessages.model.MsgTypes;

import java.util.List;

public class FavActivity extends AppCompatActivity {
    RecyclerView RVMsgFav;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    DrawerLayout drawer;
    private MsgFavAdapter msgFavAdapter;
    private List<Msg> msg_list;
    private DatabaseHelper mDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RVMsgFav=(RecyclerView)findViewById(R.id.Msg_Types);
        mDBHelper = new DatabaseHelper(this);

        msg_list = mDBHelper.getFavMessages();
        //Init adapter
        msgFavAdapter= new MsgFavAdapter(msg_list, FavActivity.this);
        RVMsgFav.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        RVMsgFav.setLayoutManager(mLayoutManager);
        RVMsgFav.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        RVMsgFav.setItemAnimator(new DefaultItemAnimator());
        RVMsgFav.setAdapter(msgFavAdapter);
        RVMsgFav.setNestedScrollingEnabled(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        msgFavAdapter.notifyDataSetChanged();
    }


}
