package com.sarrawi.mymessages.adapter;

import android.content.Context;
import android.content.DialogInterface;
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
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.sarrawi.mymessages.ActivityText;
import com.sarrawi.mymessages.PagerMessage;
import com.sarrawi.mymessages.R;
import com.sarrawi.mymessages.Utils.Utils;
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
        ImageView share;
        ImageView copy;
        ImageView shared;
        TextView tvTitle;

        public MyViewHolder(View view) {
            super(view);
            txtMsg=(TextView)view.findViewById(R.id.txt_msg);
            fav = (ImageView)view.findViewById(R.id.img_fav);
            cardview = (RelativeLayout) view.findViewById(R.id.card_view);
            share = (ImageView) view.findViewById(R.id.share);
            copy = (ImageView) view.findViewById(R.id.copy);
            shared = (ImageView) view.findViewById(R.id.shared);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
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
        final DatabaseHelper s = new DatabaseHelper(con);

        int titleId = m.getID_Type();
        String titleDesc = s.getMsgTitleByTitleID(titleId);
        holder.tvTitle.setText(titleDesc);

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(con,PagerMessage.class);

                final Msg m=msg_list.get(position);

                i.putExtra("titleID",m.getID_Type());
                i.putExtra("pos",position);
                i.putExtra("msgID",m.getID_Msg());
                i.putExtra("filter",m.getMsg_Name());
                i.putExtra("sourceIsFav",m.getFav());
                con.startActivity(i);
            }
        });

        holder.shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("text/plain");
                String text = holder.txtMsg.getText().toString();
                waIntent.setPackage("com.whatsapp");
                if (waIntent != null) {
                    waIntent.putExtra(Intent.EXTRA_TEXT, text);//
                    con.startActivity(Intent.createChooser(waIntent, "Share with"));
                } else {
                    Toast.makeText(con, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder popDialog = new AlertDialog.Builder(con);
                popDialog.setTitle("هل ترغب بتعديل النص");

                popDialog.setNegativeButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(con, ActivityText.class);
                        String data = holder.txtMsg.getText().toString();
                        i.putExtra("na", holder.txtMsg.getText().toString());
                        con.startActivity(i);
                    }
                });

                popDialog.setPositiveButton("لا", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Utils.IntenteShare(con, "مسجاتي", "مسجاتي", holder.txtMsg.getText().toString());
                    }
                });
                //Utils.IntenteShare(c, "مسجاتي", "مسجاتي", holder.tvMsg.getText().toString());

                popDialog.show();
            }
        });


        holder.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringYouExtracted = holder.txtMsg.getText().toString();
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) con.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(stringYouExtracted);
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) con.getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData
                            .newPlainText(stringYouExtracted, stringYouExtracted);
                    clipboard.setPrimaryClip(clip);
                }
                Toast.makeText(con, "تم نسخ النص", Toast.LENGTH_SHORT).show();
            }
        });



        final  DatabaseHelper d=new DatabaseHelper(con);
        if (d.getIFMsgIsFav(m) == 0) {
            holder.fav.setImageResource(R.mipmap.nf);

        } else {
            holder.fav.setImageResource(R.mipmap.f);
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
                    Toast.makeText(con, "تم الإضافة إلى المفضلة", Toast.LENGTH_LONG).show();
//                    notifyDataSetChanged();
                } else {

                    holder.fav.setImageResource(R.mipmap.nf);
                    d.changeFav(m, 0);
//                    holder.fav.setBackgroundColor(Color.rgb(255, 0, 0));
//                    holder.fav.setImageResource(R.mipmap.f);
//                    DatabaseHelper.getInstance(con).fav(m.getID_Msg(),m.getMsg_Name(),m.getID_Type(),1);
//                    msg_list.get(position).setFav(1);
//                    m.setFav(1);

                    Toast.makeText(con, "تم الإزالة من المفضلة", Toast.LENGTH_LONG).show();

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
