package com.zeneo.tmdb.DATABASE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.zeneo.tmdb.Models.Movies;

import java.util.ArrayList;
import java.util.List;

import static com.zeneo.tmdb.DATABASE.DataContract.FavoriteEntry.COLUMN_NAME_MOV_ID;

public class DataHelper {







    public List<Movies> getAllItems(SQLiteDatabase db , String table , String type){
        List<Movies> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM "+ table + " WHERE "
                +DataContract.FavoriteEntry.COLUMN_NAME_TYPE+" = "+type,null);

        if(cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MOV_ID));
                String title = cursor.getString(cursor.getColumnIndex(DataContract.FavoriteEntry.COLUMN_NAME_TITLE));
                String imgurl = cursor.getString(cursor.getColumnIndex(DataContract.FavoriteEntry.COLUMN_NAME_IMG_URL));
                list.add(new Movies(title,imgurl, Integer.parseInt(id),type));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }





    public static class FavoriteHelper extends SQLiteOpenHelper{

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Favor.db";
        private static final String SQL_CREATE_FAVORITE =
                "CREATE TABLE IF NOT EXISTS '" + DataContract.FavoriteEntry.TABLE_NAME + "' (" +
                        DataContract.FavoriteEntry._ID + " integer PRIMARY KEY," +
                        DataContract.FavoriteEntry.COLUMN_NAME_TITLE + " text," +
                        DataContract.FavoriteEntry.COLUMN_NAME_IMG_URL + " text," +
                        COLUMN_NAME_MOV_ID + " text,"+
                        DataContract.FavoriteEntry.COLUMN_NAME_TYPE + " text)";


        private static final String SQL_DELETE_FAVORITE =
                "DROP TABLE IF EXISTS " + DataContract.FavoriteEntry.TABLE_NAME;




        public FavoriteHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null , DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(SQL_DELETE_FAVORITE);
        }
        public boolean CheckIsDataAlreadyInDBorNot(String TableName, String dbfield) {
            String Query = "Select * from " + TableName + " where " + COLUMN_NAME_MOV_ID + " = '" + dbfield +"'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(Query, null);
            if(cursor.getCount() <= 0){
                cursor.close();
                return false;
            }
            cursor.close();
            return true;
        }
        public long insert( String table,String tltle,String imgurl,String id,String type){

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DataContract.FavoriteEntry.COLUMN_NAME_TITLE,tltle);
            values.put(DataContract.FavoriteEntry.COLUMN_NAME_IMG_URL,imgurl);
            values.put(COLUMN_NAME_MOV_ID,id);
            values.put(DataContract.FavoriteEntry.COLUMN_NAME_TYPE,type);

            long id1 = db.insert(table,null,values);
            db.close();
            return id1;
        }
        public boolean delete(String table,String id,String type){
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(table,COLUMN_NAME_MOV_ID + " = '"+id+"';",null) > 0;
        }

        public List<Movies> getAllItems( String table , String type){
            List<Movies> list = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM "+ table + " WHERE "
                        +DataContract.FavoriteEntry.COLUMN_NAME_TYPE+" = '"+type+"'",null);

            if(cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MOV_ID));
                    String title = cursor.getString(cursor.getColumnIndex(DataContract.FavoriteEntry.COLUMN_NAME_TITLE));
                    String imgurl = cursor.getString(cursor.getColumnIndex(DataContract.FavoriteEntry.COLUMN_NAME_IMG_URL));
                    list.add(new Movies(title,imgurl, Integer.parseInt(id),type));
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

            return list;
        }
    }
    public static class WatchListHelper extends SQLiteOpenHelper{

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "WatchList.db";
        private static final String SQL_CREATE_WATCHLIST =
                "CREATE TABLE IF NOT EXISTS '" + DataContract.WatchListEntry.TABLE_NAME + "' (" +
                        DataContract.FavoriteEntry._ID + " integer PRIMARY KEY," +
                        DataContract.FavoriteEntry.COLUMN_NAME_TITLE + " text," +
                        DataContract.FavoriteEntry.COLUMN_NAME_IMG_URL + " text," +
                        COLUMN_NAME_MOV_ID + " text,"+
                        DataContract.FavoriteEntry.COLUMN_NAME_TYPE + " text)";




        private static final String SQL_DELETE_WATCHLIST =
                "DROP TABLE IF EXISTS " + DataContract.WatchListEntry.TABLE_NAME;

        public WatchListHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null , DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SQL_CREATE_WATCHLIST);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(SQL_DELETE_WATCHLIST);
        }
        public boolean CheckIsDataAlreadyInDBorNot(String TableName, String dbfield) {
            String Query = "Select * from " + TableName + " where " + COLUMN_NAME_MOV_ID + " = '" + dbfield +"'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(Query, null);
            if(cursor.getCount() <= 0){
                cursor.close();
                return false;
            }
            cursor.close();
            return true;
        }

        public long insert(String table,String tltle,String imgurl,String id,String type){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DataContract.FavoriteEntry.COLUMN_NAME_TITLE,tltle);
            values.put(DataContract.FavoriteEntry.COLUMN_NAME_IMG_URL,imgurl);
            values.put(COLUMN_NAME_MOV_ID,id);
            values.put(DataContract.FavoriteEntry.COLUMN_NAME_TYPE,type);

            long id1 = db.insert(table,null,values);
            db.close();
            return id1;
        }
        public boolean delete(String table,String id,String type){
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(table,COLUMN_NAME_MOV_ID + " = '"+id+"';",null) > 0;
        }
        public List<Movies> getAllItems( String table , String type){
            SQLiteDatabase db = this.getReadableDatabase();
            List<Movies> list = new ArrayList<>();

            Cursor cursor = db.rawQuery("SELECT * FROM "+ table + " WHERE "
                    +DataContract.FavoriteEntry.COLUMN_NAME_TYPE+" = '"+type+"'",null);

            if(cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MOV_ID));
                    String title = cursor.getString(cursor.getColumnIndex(DataContract.FavoriteEntry.COLUMN_NAME_TITLE));
                    String imgurl = cursor.getString(cursor.getColumnIndex(DataContract.FavoriteEntry.COLUMN_NAME_IMG_URL));
                    list.add(new Movies(title,imgurl, Integer.parseInt(id),type));
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

            return list;
        }
    }



}
