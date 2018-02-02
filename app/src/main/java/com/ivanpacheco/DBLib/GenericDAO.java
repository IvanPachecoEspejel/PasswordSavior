package com.ivanpacheco.DBLib;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ivanpacheco.DBLib.Handlers.DBHandler;
import com.ivanpacheco.DBLib.Tables.TableIPE;


/**
 * Created by ivanpacheco on 2/02/18.
 * Basic class to make CRUD
 */

public class GenericDAO {

    private DBHandler dbh;
    private TableIPE table;
    private String LOG_TAG;

    public GenericDAO(Context context, String tblId){

        this.dbh = new DBHandler(context);
        this.table = dbh.getTableById(tblId);
        this.LOG_TAG = this.getClass().getName();
    }

    public long insert(ContentValues values){
        long rowId = -1;
        SQLiteDatabase db = dbh.getDB();
        try{
            db.beginTransaction();

            rowId = db.insert(table.getTableName(),null, values);

            db.setTransactionSuccessful();
        }catch(Exception e){
            Log.e(LOG_TAG, e.getMessage(), e);
        }finally {
            db.endTransaction();
        }
        return rowId;
    }

    public int update(ContentValues values, String whereClause, String[] whereArgs){
        int rowId = -1;
        SQLiteDatabase db = dbh.getDB();
        try{
            db.beginTransaction();

            rowId = db.update(table.getTableName(), values, whereClause, whereArgs);

            db.setTransactionSuccessful();
        }catch(Exception e){
            Log.e(LOG_TAG, e.getMessage(), e);
        }finally {
            db.endTransaction();
        }
        return rowId;
    }

    public int delete(String whereClause, String[] whereArgs){
        int rowId = -1;
        SQLiteDatabase db = dbh.getDB();
        try{
            db.beginTransaction();

            rowId = db.delete(table.getTableName(), whereClause, whereArgs);

            db.setTransactionSuccessful();
        }catch(Exception e){
            Log.e(LOG_TAG, e.getMessage(), e);
        }finally {
            db.endTransaction();
        }
        return rowId;
    }
}
