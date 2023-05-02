package com.lifehelper.wallet.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.lifehelper.Constants;

public class Sqlite extends SQLiteOpenHelper {
    private static final String TABLE_WALLET="wallet";
    public Sqlite(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryWallet="CREATE TABLE IF NOT EXISTS "+TABLE_WALLET+" (" +Constants.ID+" INTEGER PRIMARY KEY,"+Constants.USER_NAME+" TEXT,"+ Constants.PASSWORD+" TEXT,"+Constants.AMOUNT+" TEXT,"+Constants.DATE_TIME+" TEXT ,"+Constants.TIMEZONE+" TEXT,"+Constants.TYPE+" TEXT)";
        db.execSQL(queryWallet);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertData(String username, String password, String amount, String datetime,String timezone,String type){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Constants.USER_NAME,username);
        values.put(Constants.PASSWORD,password);
        values.put(Constants.AMOUNT,amount);
        values.put(Constants.DATE_TIME,datetime);
        values.put(Constants.TIMEZONE,timezone);
        values.put(Constants.TYPE,type);

        long check=db.insert(TABLE_WALLET,null,values);
        if (check==-1)  //check returns -1 as data don't insert
        {
            return false;

        }

        else
        {
            return true;
        }
    }
}