package com.ramyhelow.qrreader.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ramyhelow.qrreader.Model.Code;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static SQLiteDatabase sqLiteDatabase;

    public DBHelper(Context context) {
        super(context, Code.DB_NAME, null, 1);
        sqLiteDatabase = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Code.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Code.DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    public boolean insertNewCode(String time, String text) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Code.COL_TIME, time);
        contentValues.put(Code.COL_TEXT, text);
        return sqLiteDatabase.insert(Code.TABLE_NAME, null, contentValues) > 0;
    }

//    public boolean updateStudent(String oldID, String name,String average){
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(Code.COL_NAME,name);
//        contentValues.put(Code.COL_AVERAGE,average);
//        return sqLiteDatabase.update(Code.TABLE_NAME,contentValues,Student.COL_ID+"= ?",new String[]{oldID})>0;
//    }
//
//    public boolean deleteStudent(String oldID){
//        return sqLiteDatabase.delete(Code.TABLE_NAME,Code.COL_ID+"= ?",new String[]{oldID})>0;
//    }

    public ArrayList<Code> getAllCodes() {
        ArrayList<Code> codeArrayList = new ArrayList<>();

        String query = "SELECT * FROM" + Code.TABLE_NAME + "ORDER BY " + Code.COL_ID + " DESC";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Code code = new Code();
                code.setId(cursor.getString(cursor.getColumnIndex(Code.COL_ID)));
                code.setTime(cursor.getString(cursor.getColumnIndex(Code.COL_TIME)));
                code.setText(cursor.getString(cursor.getColumnIndex(Code.COL_TEXT)));

                codeArrayList.add(code);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return codeArrayList;
    }
}