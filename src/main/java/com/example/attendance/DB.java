package com.example.attendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DB extends SQLiteOpenHelper {

    private static final String DB_NAME = "yoklamaa";
    private static final int VERSION =1;

    private static final String TABLO_KISILER = "user";
    private static final String ROW_ID = "id";
    private static final String ROW_AD = "adsoyad";
    private static final String ROW_ONO = "ogrencino";
    private static final String ROW_DEV = "devamsızlık";

    public DB(Context context){
        super(context,DB_NAME,null,VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLO_KISILER + "("
                + ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ROW_AD + " TEXT NOT NULL, "
                + ROW_ONO + " TEXT NOT NULL, "
                + ROW_DEV + " TEXT NOT NULL)");

        // db.execSQL("INSERT INTO user (adsoyad,ogrencino,devamsızlık)VALUES ('Emine KOŞAK',150101062,0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLO_KISILER);
        onCreate(db);
    }


    public void insert(SQLiteDatabase db,String adsoyad,String ogrencino,String devamsızlık) throws SQLException{
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(ROW_AD, adsoyad);
            cv.put(ROW_ONO,ogrencino );
            cv.put(ROW_DEV,devamsızlık);
            db.insert(TABLO_KISILER, null,cv);
        }catch (Exception e){

        }
        db.close();
    }


    public List<String> VeriListele() throws SQLException{
        List<String> veriler = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String[] stunlar = {ROW_ID,ROW_AD,ROW_ONO,ROW_DEV};
            Cursor cursor = db.query(TABLO_KISILER, stunlar,null,null,null,null,null);
            while (cursor.moveToNext()){
                veriler.add(cursor.getInt(0) + " - " + cursor.getString(1) + " - " + cursor.getString(2) + " - " + cursor.getString(3));
            }
        }catch (Exception e){

        }
        db.close();
        return veriler;
    }

    public void delete(SQLiteDatabase db,int id) throws SQLException{
        SQLiteDatabase database = this.getWritableDatabase();
        db.delete("user","id="+id,null);
    }

}
