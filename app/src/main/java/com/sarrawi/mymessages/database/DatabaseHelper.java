package com.sarrawi.mymessages.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sarrawi.mymessages.model.Msg;
import com.sarrawi.mymessages.model.MsgTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NgocTri on 11/7/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "msg.db";
    public static final String DBLOCATION = "/data/data/com.sarrawi.mymessages/databases/";
    public static Context mContext;
    private SQLiteDatabase mDatabase;
    private static DatabaseHelper sInstance;


    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if(mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if(mDatabase!=null) {
            mDatabase.close();
        }
    }



    public ArrayList<MsgTypes> getMessageTypes() {

        ArrayList<MsgTypes> contactList = new ArrayList<MsgTypes>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + "MssageType";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("cursor", cursor.toString());
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MsgTypes row = new MsgTypes();

                row.setId(Integer.parseInt(cursor.getString(0)));
                row.setName(cursor.getString(1));

                // Adding contact to list
                contactList.add(row);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return contactList;
    }
    public ArrayList<Msg> getAllMsg(int typeID) {

        ArrayList<Msg> contactList = new ArrayList<Msg>();
        // Select All Query
        String selectQuery = "SELECT * FROM Messages where ID_Type='" + typeID + "' ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("cursor", cursor.toString());
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Msg row = new Msg();
                row.setID_Msg(Integer.parseInt(cursor.getString(0)));
                row.setID_Type(Integer.parseInt(cursor.getString(2)));
                row.setMsg_Name(cursor.getString(1));


                // Adding contact to list
                contactList.add(row);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return contactList;
    }

    public ArrayList<Msg> getAllMsgnotID() {

        ArrayList<Msg> contactList = new ArrayList<Msg>();
        // Select All Query
        String selectQuery = "SELECT * FROM Messages where ID_Type ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("cursor", cursor.toString());
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Msg row = new Msg();
                row.setID_Msg(Integer.parseInt(cursor.getString(0)));
                row.setID_Type(Integer.parseInt(cursor.getString(2)));
                row.setMsg_Name(cursor.getString(1));


                // Adding contact to list
                contactList.add(row);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return contactList;
    }

    public ArrayList<Msg> getAllPrayer(String text) {
        text = "%" + text + "%";
        ArrayList<Msg> contactList = new ArrayList<Msg>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + "Messages" + " WHERE ID_Type and Message_Filter  LIKE '" + text + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("cursor", cursor.toString());
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Msg row = new Msg();
             /*   Log.e (" cursor.getString (1)", cursor.getString (1) + "");// send
                Log.e (" cursor.getString type", cursor.getString (2) + "");// type
                Log.e (" cursor.getString date", cursor.getString (3) + "");// date
                Log.e (" cursor.getString (4)", cursor.getString (4) + "");// time
*/
                row.setID_Msg(Integer.parseInt(cursor.getString(0)));
                row.setMsg_Name(cursor.getString(1));
                row.setID_Type(Integer.parseInt(cursor.getString(2)));
                row.setMsg_Filter(cursor.getString(4));
                row.setFav(Integer.parseInt(cursor.getString(3)));

                // Adding contact to list
                contactList.add(row);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return contactList;
    }

    public List<Msg> getMessagesFiltered(int typeID, String filterValue) {
        Msg u;

        List<Msg> myList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT *   FROM Messages   where ID_Type='" + typeID + "' and Message_Filter like'%" + filterValue + "%' ", null);

        if (c.moveToFirst()) {

            do {
                u = new Msg();
                u.setID_Msg(c.getInt(0));
                u.setMsg_Name(c.getString(1));
                u.setMsg_Filter(c.getString(4));
                u.setID_Type(c.getInt(2));
                u.setFav(c.getInt(3));

                myList.add(u);
            }
            while (c.moveToNext());
        }
        c.close();
        db.close();
        return myList;
    }

    public String getMsgTitleByTitleID(int msgType) {

        String result = "";
        SQLiteDatabase db = getReadableDatabase();

        Cursor countCursor = db.rawQuery("SELECT MsgTypes from MssageType where ID=" + msgType, null);
        if (countCursor.moveToFirst()) {
            result = countCursor.getString(0);
        }
        countCursor.close();
        db.close();

        return result;
    }

    public List<Msg> getMessagesNotOrdered(int typeID) {
        Msg u;

        List<Msg> myList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT *   FROM Messages   where ID_Type='" + typeID + "' ", null);

        if (c.moveToFirst()) {

            do {
                u = new Msg();
                u.setID_Msg(c.getInt(0));
                u.setMsg_Name(c.getString(1));
                u.setID_Type(c.getInt(2));

                u.setFav(c.getInt(3));
                myList.add(u);
            }
            while (c.moveToNext());
        }
        c.close();
        db.close();
        return myList;
    }


    public ArrayList<Msg> getFaVourit() {
        Msg m;
        ArrayList<Msg> Data = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
//        db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, 0);
        String SelectQuery = "Select * from Messages where FAV= 1";
        Cursor cursor = db.rawQuery(SelectQuery, null);
        Log.e("cursor", cursor.toString());
//        Cursor cursor = db.rawQuery("Select * from vmsg where FAV= 1", null);
        if (cursor.moveToFirst()) {

            do {
                Msg row = new Msg();
                row.setID_Msg(Integer.parseInt(cursor.getString(0)));
                row.setID_Type(Integer.parseInt(cursor.getString(2)));
                row.setMsg_Name(cursor.getString(1));
                row.setFav(Integer.parseInt(cursor.getString(3)));


                Data.add(row);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return Data;
    }
    public void favorite(int ID,String Name,int Type,int Fav) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        values.put("IDMsg",ID);
        values.put("MessageName",Name);
        values.put("ID_Type",Type);
        values.put("Fav",Fav);
        // Inserting Row
        db.update("Messages", values, "IDMsg=" + ID, null);
//        db.update("Messages", values, "ID_Type=" + Type, null);

        db.close();
        // Closing database connection
    }


    public int getIFMsgIsFav(Msg m) {
        int result = 0;
        SQLiteDatabase db = getWritableDatabase();
        Cursor countCursor = db.rawQuery("SELECT fav  from messages where IDMsg=" + m.getID_Msg(), null);
        if (countCursor.moveToFirst()) {
            result = countCursor.getInt(0);
        }
        db.close();
        return result;
    }

    public List<Msg> getFavMessages() {
        Msg u;

        List<Msg> myList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT *   FROM Messages   where fav=1", null);

        if (c.moveToFirst()) {

            do {
                u = new Msg();
                u.setID_Msg(c.getInt(0));
                u.setMsg_Name(c.getString(1));
                u.setID_Type(c.getInt(2));
                u.setFav(c.getInt(3));

                myList.add(u);
            }
            while (c.moveToNext());
        }
        c.close();
        db.close();
        return myList;
    }

    public void changeFav(Msg msg, int intFav) {


        SQLiteDatabase db = getWritableDatabase();
        String sql = "select max(Fav) from messages";
        int intMaxOrderOfFav = 0;
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) ;
        {
            intMaxOrderOfFav = c.getInt(0);
        }
        intMaxOrderOfFav = intMaxOrderOfFav + 1;
        if (intFav == 0) {
            sql = "update Messages set fav=" + intFav + " ,Fav=0 where IDMsg=" + msg.getID_Msg();
            db.execSQL(sql);
            sql = "delete from  favs where IDMsg=" + msg.getID_Msg();
//            db.execSQL(sql);
        } else {

            sql = "update Messages set fav=" + intFav + " ,Fav=1 where IDMsg=" + msg.getID_Msg();
            db.execSQL(sql);

//            sql = "insert into favs values(" + msg.getMsgID() + ")";
//            db.execSQL(sql);
        }

        c.close();
        db.close();
    }



//
//    public ArrayList<Product> getAllPrayer(String text) {
//        text = "%" + text + "%";
//        ArrayList<Product> contactList = new ArrayList<Product>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + "azkar_type" + " WHERE Name_filter  LIKE '" + text + "'";
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        Log.e("cursor", cursor.toString());
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Product row = new Product();
//             /*   Log.e (" cursor.getString (1)", cursor.getString (1) + "");// send
//                Log.e (" cursor.getString type", cursor.getString (2) + "");// type
//                Log.e (" cursor.getString date", cursor.getString (3) + "");// date
//                Log.e (" cursor.getString (4)", cursor.getString (4) + "");// time
//*/
//                row.setId(Integer.parseInt(cursor.getString(0)));
//                row.setName(cursor.getString(1));
//                row.setName_filter(cursor.getString(3));
//                row.setFav(Integer.parseInt(cursor.getString(2)));
//
//                // Adding contact to list
//                contactList.add(row);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//        // return contact list
//        return contactList;
//    }
//
//    public String getMsgTitleByTitleID(int msgType) {
//
//        String result = "";
//        SQLiteDatabase db = getReadableDatabase();
//
//        Cursor countCursor = db.rawQuery("SELECT Name from azkar_type where ID=" + msgType, null);
//        if (countCursor.moveToFirst()) {
//            result = countCursor.getString(0);
//        }
//        countCursor.close();
//        db.close();
//
//        return result;
//    }

    public static DatabaseHelper getInstance(Context contexts) {
        mContext= contexts;
        if (sInstance == null) {
            sInstance = new DatabaseHelper(contexts.getApplicationContext());
        }
        return sInstance;
    }


}
