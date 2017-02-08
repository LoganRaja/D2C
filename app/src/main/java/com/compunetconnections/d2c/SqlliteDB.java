package com.compunetconnections.d2c;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by connectors on 1/7/2016.
 */
public class SqlliteDB extends SQLiteOpenHelper {
    public SqlliteDB(Context context)
    {
        super(context, "D2Cdatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("cr  eate table pin_table" + "(id integer primary key AUTOINCREMENT, pin text,email text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }
    public void insert(String pin,String email){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("pin",pin);
        contentValues.put("email", email);
        db.insert("pin_table", null, contentValues);
        select();
    }
    public Cursor select(){
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM pin_table",null);
        return cursor;
    }
    public void updatepin(String pin,String email){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("pin",pin);
        db.update("pin_table", contentValues, "email = ?", new String[]{email});
        select();
    }
}
