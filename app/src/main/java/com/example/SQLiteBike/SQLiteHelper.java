package com.example.SQLiteBike;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String name, String type, String chainset,
                           String cassette, String bracket, String chainlink,
                           byte[] image){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO BIKE VALUES (NULL, ?, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindString(2, type);
        statement.bindString(3, chainset);
        statement.bindString(4, cassette);
        statement.bindString(5, bracket);
        statement.bindString(6, chainlink);
        statement.bindBlob(7, image);

        statement.executeInsert();
    }

    public void updateData(String name, String type, String chainset,
                           String cassette, String bracket, String chainlink,
                           byte[] image, int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE BIKE SET name = ?, type = ?, chainset = ?, cassette = ?, " +
                "bracket = ?, chainlink = ?, image = ? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, name);
        statement.bindString(2, type);
        statement.bindString(3, chainset);
        statement.bindString(4, cassette);
        statement.bindString(5, bracket);
        statement.bindString(6, chainlink);
        statement.bindBlob(7, image);
        statement.bindDouble(8, (double)id);

        statement.execute();
        database.close();
    }

    public  void deleteData(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM BIKE WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
