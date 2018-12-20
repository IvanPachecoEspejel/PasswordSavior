package com.ivanpacheco.DBLib;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ivanpacheco.DBLib.Handlers.DBHandler;
import com.ivanpacheco.DBLib.Tables.Table;
import com.ivanpacheco.DBLib.util.UtilDB;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by ivanpacheco on 2/02/18.
 * Basic class to make CRUD
 */

public class GenericDAO {

    private DBHandler dbh;
    private Table table;
    private String LOG_TAG;

    public GenericDAO(Context context, String tblId){

        this.dbh = new DBHandler(context.getApplicationContext());
        this.table = dbh.getTableById(tblId);
        this.LOG_TAG = this.getClass().getName();
    }

    public Cursor query(String[] columnsIds) throws Exception{
        return this.query(false, columnsIds, "", null, "", "", "", "");
    }

    public Cursor query(String[] columnsIds, String whereClause, String[] whereArgs) throws Exception{
        return this.query(false, columnsIds, whereClause, whereArgs, "", "", "", "");
    }

    public Cursor query(boolean distinct, String[] columnsIds, String whereClause, String[] whereArgs, String groupBy, String having, String orderBy, String limit)throws Exception{
        SQLiteDatabase db = dbh.getReadableDatabase();
        String[] columns = new String[columnsIds.length];
        for(int idxCol = 0; idxCol < columnsIds.length; idxCol++){
            columns[idxCol] = table.getColumnName(columnsIds[idxCol]);
        }
        whereClause = UtilDB.processDBString(whereClause, table.getColumns());
        Cursor c = null;
        try{
            c = db.query(distinct, table.getTableName(), columns, whereClause, whereArgs, groupBy, having, orderBy, limit);
            if(c.getCount() == 0){
                c.close();
                c= null;
            }
            return c;
        }catch(Exception e){
            Log.e(LOG_TAG, e.getMessage(), e);
            throw e;
        }
    }

    public long insert(HashMap<String, String> values)throws Exception{
        long rowId;
        ContentValues cv = new ContentValues(values.size());
        for(Map.Entry<String, String> value : values.entrySet()){
            cv.put(table.getColumnName(value.getKey()), value.getValue());
        }
        rowId = rawInsert(cv);
        return rowId;
    }

    public int update(HashMap<String, String> values, String whereClause, String[] whereArgs)throws Exception{
        int rowId;
        ContentValues cv = new ContentValues(values.size());
        for(Map.Entry<String, String> value : values.entrySet()){
            cv.put(table.getColumnName(value.getKey()), value.getValue());
        }
        rowId = rawUpdate(cv, whereClause, whereArgs);
        return rowId;
    }

    public int delete(String whereClause, String[] whereArgs){
        int rowId;
        SQLiteDatabase db = dbh.getWritableDatabase();
        try{
            db.beginTransaction();

            rowId = db.delete(table.getTableName(), whereClause, whereArgs);

            db.setTransactionSuccessful();
            return rowId;
        }catch(Exception e){
            Log.e(LOG_TAG, e.getMessage(), e);
            throw e;
        }finally {
            db.endTransaction();
        }
    }

    private long rawInsert(ContentValues values) throws Exception{
        long rowId;
        SQLiteDatabase db = dbh.getWritableDatabase();
        try{
            db.beginTransaction();

            rowId = db.insert(table.getTableName(),null, values);

            db.setTransactionSuccessful();
            return rowId;
        }catch(Exception e){
            Log.e(LOG_TAG, e.getMessage(), e);
            throw e;
        }finally {
            db.endTransaction();
        }
    }

    private int rawUpdate(ContentValues values, String whereClause, String[] whereArgs)throws Exception{
        int rowId;
        SQLiteDatabase db = dbh.getWritableDatabase();
        try{
            db.beginTransaction();

            rowId = db.update(table.getTableName(), values, whereClause, whereArgs);

            db.setTransactionSuccessful();
            return rowId;
        }catch(Exception e){
            Log.e(LOG_TAG, e.getMessage(), e);
            throw e;
        }finally {
            db.endTransaction();
        }
    }
}
