package com.example.user.to_do_project_1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by user on 01-12-2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String create_Table = "CREATE TABLE " + Constants.TABLE_NAME + "(" + Constants.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Constants.KEY_TITLE + " TEXT, " + Constants.KEY_DESCRIPTION + " TEXT, " + Constants.KEY_DATE + " TEXT, " + Constants.KEY_STATUS + " INTEGER ) " ;

        sqLiteDatabase.execSQL(create_Table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public int insert(Task task)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //Putting values in the ContentValue Object.
        values.put(Constants.KEY_TITLE,task.mtitle);
        values.put(Constants.KEY_DESCRIPTION,task.mdescription);
        values.put(Constants.KEY_DATE,task.mdate);
        values.put(Constants.KEY_STATUS,task.mstatus);

        // Inserting Row
        long student_Id = database.insert(Constants.TABLE_NAME, null, values);
        database.close(); // Closing database connection
        return (int) student_Id;
    }

    //Method to delete entry from database.
    public void delete(int entry_Id) {

        SQLiteDatabase database = this.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        database.delete(Constants.TABLE_NAME, Constants.KEY_ID + "= ?", new String[] { String.valueOf(entry_Id) });
        database.close(); // Closing database connection
    }

    //Method to update the Entries of the database.
    public void update(Task entry) {

        //Creating reference of writable db.
        SQLiteDatabase database = this.getWritableDatabase();

        //Creating object of ContentValues.
        ContentValues values = new ContentValues();

        //Putting values in the contentValues object.
        values.put(Constants.KEY_TITLE,entry.mtitle);
        values.put(Constants.KEY_DESCRIPTION,entry.mdescription);
        values.put(Constants.KEY_DATE,entry.mdate);
        values.put(Constants.KEY_STATUS,entry.mstatus);

        // It's a good practice to use parameter ?, instead of concatenate string
        database.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "= ?", new String[] { String.valueOf(entry.mid) });
        database.close(); // Closing database connection
    }

    //Method to retrive all Entries of DB.
    public ArrayList<Task> getEntryList() {
        //Open connection to read only
        SQLiteDatabase database = this.getReadableDatabase();

        //Select query to retrive elements.
        String selectQuery =  "SELECT  " +
                Constants.KEY_ID + "," +
                Constants.KEY_TITLE + "," +
                Constants.KEY_DESCRIPTION+","+
                Constants.KEY_DATE+","+
                Constants.KEY_STATUS+
                " FROM " + Constants.TABLE_NAME;

        //Student student = new Student();
        ArrayList<Task> entry_list = new ArrayList<>();

        Cursor cursor = database.rawQuery(selectQuery,null);
        // looping through all rows and adding to list

        //Creating Entry objects and putting them into ArrayList.
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.mid=cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID));
                task.mtitle=cursor.getString(cursor.getColumnIndex(Constants.KEY_TITLE));
                task.mdescription=cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION));
                task.mdate=cursor.getString(cursor.getColumnIndex(Constants.KEY_DATE));
                task.mstatus=cursor.getInt(cursor.getColumnIndex(Constants.KEY_STATUS));
                entry_list.add(task);

            } while (cursor.moveToNext());
        }

        //Sorting the ArrayList by sort() method in Collections class.
        //We already compared the objects in the containing class only in compareTo() method.
        Collections.sort(entry_list);

        //Closing the cursor and db objects.
        cursor.close();
        database.close();
        return entry_list;    //returning ArrayList.

    }

    //Method to get Entry object by ID.
    public Task getStudentById(int Id){
        //Getting reference of Readable DB.
        SQLiteDatabase database = this.getReadableDatabase();

        //Select query.
        String selectQuery =  "SELECT  " +
                Constants.KEY_ID + "," +
                Constants.KEY_TITLE + "," +
                Constants.KEY_DESCRIPTION+","+
                Constants.KEY_DATE+","+
                Constants.KEY_STATUS+
                " FROM " + Constants.TABLE_NAME
                + " WHERE " +
                Constants.KEY_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        int iCount =0;
        Task task=new Task();       //Creating object of Entry class.

        //Creating cursor for selectQuery.
        Cursor cursor = database.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        //Getting the values.
        if (cursor.moveToFirst()) {
            do {

                task.mid=cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID));
                task.mtitle=cursor.getString(cursor.getColumnIndex(Constants.KEY_TITLE));
                task.mdescription=cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION));
                task.mdate=cursor.getString(cursor.getColumnIndex(Constants.KEY_DATE));
                task.mstatus=cursor.getInt(cursor.getColumnIndex(Constants.KEY_STATUS));

            } while (cursor.moveToNext());
        }

        //closing objects of db and cursor.
        cursor.close();
        database.close();
        return task;   //returning entry.
    }

    //Method to retrive Entries whose status is 1 from DB.
    public ArrayList<Task> getCompletedEntryList() {
        //Open connection to read only
        SQLiteDatabase database = this.getReadableDatabase();

        //Select query to retrive elements.
        String selectQuery =  "SELECT  " +
                Constants.KEY_ID + "," +
                Constants.KEY_TITLE + "," +
                Constants.KEY_DESCRIPTION+","+
                Constants.KEY_DATE+","+
                Constants.KEY_STATUS+
                " FROM " + Constants.TABLE_NAME;

        //Student student = new Student();
        ArrayList<Task> entryList = new ArrayList<Task>();

        Cursor cursor = database.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        //Creating Entry objects and putting them into ArrayList.
        if (cursor.moveToFirst()) {
            do {
                if(cursor.getInt(cursor.getColumnIndex(Constants.KEY_STATUS))==1) {
                    Task task = new Task();
                    task.mid = cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID));
                    task.mtitle = cursor.getString(cursor.getColumnIndex(Constants.KEY_TITLE));
                    task.mdescription = cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION));
                    task.mdate = cursor.getString(cursor.getColumnIndex(Constants.KEY_DATE));
                    task.mstatus = cursor.getInt(cursor.getColumnIndex(Constants.KEY_STATUS));
                    entryList.add(task);
                }

            } while (cursor.moveToNext());
        }

        //Sorting the ArrayList by sort() method in Collections class.
        //We already compared the objects in the containing class only in compareTo() method.
        Collections.sort(entryList);

        //Closing the cursor and db objects.
        cursor.close();
        database.close();
        return entryList;   //returning ArrayList.

    }
}
