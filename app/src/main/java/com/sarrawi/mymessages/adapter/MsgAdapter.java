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
import android.widget.Toast;

import com.sarrawi.mymessages.PagerMessage;
import com.sarrawi.mymessages.R;
import com.sarrawi.mymessages.database.DatabaseHelper;
import com.sarrawi.mymessages.model.Msg;
import com.sarrawi.mymessages.model.MsgTypes;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MyViewHolder>  {
    private List<Msg> msg_list;
    Context con;
    private int fontSize;
    private Typeface font;

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

    public MsgAdapter(List<Msg> msg_list, Context context)
    {
        this.con = context;
        this.msg_list=msg_list;
    }

    @NonNull
    @Override
    public MsgAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_design,parent,false);

        return new MsgAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MsgAdapter.MyViewHolder holder, final int position) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(con);

        final Msg m = msg_list.get(position);
        holder.txtMsg.setText(m.getMsg_Name());

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(con,PagerMessage.class);

                final Msg m=msg_list.get(position);

                i.putExtra("titleID",m.getID_Type());
                i.putExtra("pos",position);
                i.putExtra("msgID",m.getID_Msg());
//                i.putExtra("origPos",m.getOrigPos());
                i.putExtra("sourceIsFav",false);
                con.startActivity(i);
            }
        });
        final  DatabaseHelper d=new DatabaseHelper(con);
        if (d.getIFMsgIsFav(m) == 0) {
            holder.fav.setBackgroundResource(R.mipmap.nf);

        } else {
            holder.fav.setBackgroundResource(R.mipmap.f);
        }

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (d.getIFMsgIsFav(m) == 0) {
                    holder.fav.setImageResource(R.mipmap.f);
                    d.changeFav(m, 1);
//                    holder.fav.setBackgroundColor(Color.rgb(76, 255, 0));
//                    holder.fav.setImageResource(R.mipmap.nf);
//                    DatabaseHelper.getInstance(con).fav(m.getID_Msg(),m.getMsg_Name(),m.getID_Type(),0);
//                    msg_list.get(position).setFav(0);
//                    m.setFav(0);
//                    Toast.makeText(con, "تم الإزالة من المفضلة", Toast.LENGTH_LONG).show();
//                    notifyDataSetChanged();
                } else {

                    holder.fav.setImageResource(R.mipmap.nf);
                    d.changeFav(m, 0);
//                    holder.fav.setBackgroundColor(Color.rgb(255, 0, 0));
//                    holder.fav.setImageResource(R.mipmap.f);
//                    DatabaseHelper.getInstance(con).fav(m.getID_Msg(),m.getMsg_Name(),m.getID_Type(),1);
//                    msg_list.get(position).setFav(1);
//                    m.setFav(1);
//                    Toast.makeText(con, "تم الإضافة إلى المفضلة", Toast.LENGTH_LONG).show();
//                    notifyDataSetChanged();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return msg_list.size();
    }


}
