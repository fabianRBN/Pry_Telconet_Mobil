package com.example.telconet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "user.db";
    public static final String TABLE_NAME = "user_table";
    public static final String COL_USERNAME= "Username";
    public static final String COL_PASSWORD= "Password";

    public DatabaseHelper(Context context) {
        super(context, "login", null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE login(Username text primary key, Password text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS login");
        onCreate(db);
    }

    public boolean insertData(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Username", username);
        contentValues.put("Password ", password);
        long result = db.insert("login", null, contentValues);


        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM login", null);

        return res;
    }

    public boolean deleteData(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("login", "Username = "+ '"'+username +'"', new String[]{});

        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean login(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM login WHERE Username =" + '"'+username +'"'+ "AND  Password = "+'"'+password+'"' , new String[]{});

        return (res.getCount() == 0)?false:true;

        //return true;
    }
}
