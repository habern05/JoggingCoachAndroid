package at.haraldbernhard.joggingcoachandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harald Bernhard on 28.06.2016.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = DbHelper.class.getSimpleName();


    // Name and Version of Database
    private static final String DATABASE_NAME = "joggingcoach.db";
    private static final int DATABASE_VERSION = 1;

    //USER
    //Name and Attribute of Table "user"
    public static final String _ID = "_id";
    public static final String TABLE_NAME_USER = "user";

    public static final String USERNAME = "username";
    public static final String AGE = "age";
    public static final String SIZE = "size";
    public static final String WEIGHT = "weight";

    //Create Table
    private static final String TABLE_USER_CREATE =
            "CREATE TABLE "
                    + TABLE_NAME_USER + " (" + _ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + USERNAME + " TEXT, "
                    + AGE + " INTEGER, "
                    + SIZE + " INTEGER, "
                    + WEIGHT + " INTEGER);";

    //Delete Table
    public static final String TABLE_USER_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME_USER;


    //TRAINING
    //NAME and Attribute of Table "training"
    public static final String TABLE_NAME_TRAINING = "training";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String DISTANCE = "distance";
    public static final String CALORIE = "calorie";
    public static final String SPEEDA = "speeda";

    //SQL-Statement Create Table Training
    private static final String TABLE_TRAINING_CREATE =
            "CREATE TABLE "
                    + TABLE_NAME_TRAINING + " (" + _ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DATE + " TEXT, "
                    + TIME + " TEXT, "
                    + DISTANCE + " TEXT, "
                    + CALORIE + " TEXT, "
                    + SPEEDA + " TEXT);";



    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_USER_CREATE);
        db.execSQL(TABLE_TRAINING_CREATE);
    }

    //Delete Table
    public static final String TABLE_TRAINING_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME_TRAINING;


    public DbHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }





    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(TABLE_USER_DROP);
        db.execSQL(TABLE_TRAINING_DROP);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    //CRUD for USER

    public int getUserId(String name){
        //Open Connection
        SQLiteDatabase db = getReadableDatabase();
        String[] column = {_ID};
        Cursor cursor = db.query(TABLE_NAME_USER, column, USERNAME+" = '"+name+"'", null, null, null, null);
        cursor.moveToFirst();
        if(cursor !=null && cursor.getCount()>0){
            return cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
        }
        else {
            return 0;
        }
    }

    public int getUserWeight(String weight){
        //Open Connection
        SQLiteDatabase db = getReadableDatabase();
        String[] column = {WEIGHT};
        Cursor cursor = db.query(TABLE_NAME_USER, column, WEIGHT+" = '"+weight+"'", null, null, null, null);
        cursor.moveToFirst();
        if(cursor.getColumnIndexOrThrow(WEIGHT)>0){
            return cursor.getColumnIndexOrThrow(_ID);
        }
        else {
            return 0;
        }
    }

    public void insertUser(User user){
        long rowId = -1;
        try{
            // Open Connection
            SQLiteDatabase db = getWritableDatabase();
            //Save Values
            ContentValues values = new ContentValues();
            values.put(USERNAME, user.getUsername());
            values.put(AGE, user.getAge());
            values.put(SIZE, user.getSize());
            values.put(WEIGHT, user.getWeight());
            //Insert into Table
            rowId = db.insert(TABLE_NAME_USER, null, values);
        }

        catch (SQLiteException e){
            Log.e(TAG, "insert() ", e);
        }
        finally {
            Log.d(TAG, "insert(): rowId= " + rowId);
        }
    }

    /*
    public String getUsername(String name){
        //Open Connection
        SQLiteDatabase db = getWritableDatabase();
        //Select Name From Avatar
        String[] column = {USERNAME};
        Cursor cursor = db.query(TABLE_NAME_USER, column, USERNAME+" = '"+name+"'", null, null, null, null );
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()){
            int index1=cursor.getColumnIndex(USERNAME);
            String username = cursor.getString(index1);
            buffer.append(name);
        }
        return buffer.toString();
    }
    */

    //CRUD for TRAINING

    public void insertTraining(String date, String time, String distance, String calorie, String speedA){
        long rowId = -1;
        try {
            //Open Connection
            SQLiteDatabase db = getWritableDatabase();
            //Save Values
            ContentValues values = new ContentValues();
            values.put(DATE, date);
            values.put(TIME, time);
            values.put(DISTANCE, distance);
            values.put(CALORIE, calorie);
            values.put(SPEEDA, speedA);
            //Insert Into Table
            rowId = db.insert(TABLE_NAME_TRAINING, null, values);
        } catch (SQLiteException e) {
            Log.e(TAG, "insert() ", e);
        } finally {
            Log.d(TAG, "insert(): rowId= " + rowId);
        }
    }

    public String getLastTrainingDate(){

        String result;
        //Open Connection
        SQLiteDatabase db = getReadableDatabase();
        //Build SQL-Statement
        String[] column = {DATE};
        Cursor cursor = db.query    (TABLE_NAME_TRAINING,   // The table to query
                column,                 // The columns to return
                null,                   // The columns for the WHERE clause
                null,                   // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                DATE+" DESC"            // The sort order
        );
        cursor.moveToFirst();
        if(cursor != null && cursor.getCount()>0) {
            result =  cursor.getString(cursor.getColumnIndexOrThrow(DATE));
        }
        else {
            result = "";
        }
        return result;
    }


    public Cursor getAllTraining(){
        SQLiteDatabase db = getReadableDatabase();
        String[] column = {_ID, DATE, TIME, DISTANCE, CALORIE, SPEEDA};
        Cursor cursor = db.query(TABLE_NAME_TRAINING, column, null, null, null, null, DATE+" DESC");
        return cursor;
    }
}
