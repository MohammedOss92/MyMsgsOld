package com.sarrawi.mymessages.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sarrawi.mymessages.MessageActivity;
import com.sarrawi.mymessages.R;
import com.sarrawi.mymessages.model.MsgTypes;

import java.util.List;

public class MsgTypesAdapter extends RecyclerView.Adapter<MsgTypesAdapter.MyViewHolder> {

    private List<MsgTypes> msgTypes_list;
    Context con;
    private int fontSize;
    private Typeface font;

    public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txtMsg_types;
            RelativeLayout card_view;

        public MyViewHolder(View view) {
            super(view);
            txtMsg_types = (TextView) view.findViewById(R.id.txt_msg_types);
            card_view = (RelativeLayout) view.findViewById(R.id.cards_view);

        }
    }

    public MsgTypesAdapter(List<MsgTypes> msgTypes_list,Context context)
    {
        this.con = context;
        this.msgTypes_list=msgTypes_list;
    }


    @NonNull
    @Override
    public MsgTypesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_types_design,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgTypesAdapter.MyViewHolder holder, final int position) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(con);

        final MsgTypes m = msgTypes_list.get(position);
        holder.txtMsg_types.setText(m.getName());

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MsgTypes m = msgTypes_list.get(position);
                Intent i = new Intent(con, MessageActivity.class);
                i.putExtra("titleID", m.getId());
                con.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return msgTypes_list.size();
    }


}
