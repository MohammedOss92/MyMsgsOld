package com.sarrawi.mymessages.adapter;

import android.content.Context;
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

import com.sarrawi.mymessages.R;
import com.sarrawi.mymessages.model.Msg;

import java.util.List;

public class MsgFavAdapter extends RecyclerView.Adapter<MsgFavAdapter.MyViewHolder> {
    private List<Msg> msg_list;
    Context con;
    private int fontSize;
    private Typeface font;


    public MsgFavAdapter(List<Msg> msg_list, Context context)
    {
        this.con = context;
        this.msg_list=msg_list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.msg_design, parent, false);


        return new MyViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,final int position) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(con);
        final Msg m = msg_list.get(position);
        holder.txtMsg.setText(m.getMsg_Name());
    }

    @Override
    public int getItemCount() {
        return msg_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtMsg;
        ImageView fav;
        RelativeLayout cardview;

        public MyViewHolder(View view) {
            super(view);
            txtMsg=(TextView)view.findViewById(R.id.txt_msg);
            fav = (ImageView)view.findViewById(R.id.img_fav);
            cardview = (RelativeLayout) view.findViewById(R.id.card_view);
        }
    }
}
