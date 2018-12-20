package com.ivanpacheco.DBLib.Handlers;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ivanpacheco.passwordsavior.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ivanpacheco.DBLib.Tables.SimpleTable;
import com.ivanpacheco.DBLib.Tables.Table;

import javax.crypto.Cipher;

/**
 * Created by ivanpacheco on 31/01/18.
 * Handler to manage a local data base
 */

public class DBHandler extends SQLiteOpenHelper{

    private final String LOG_TAG = this.getClass().getName();

    private static HashMap<String, Table> tables = null;

    public DBHandler(Context ctx){
        super(ctx,
                ctx.getResources().getString(R.string.db_name),
                null,
                ctx.getResources().getInteger(R.integer.db_version));

        loadTables(ctx);
    }

    public static void initDB(Context ctx){
        new DBHandler(ctx.getApplicationContext()).getReadableDatabase();
    }

    private static void loadTables(Context ctx){
        if(tables != null){
            return;
        }
        Resources res = ctx.getResources();
        tables = new HashMap<>();

        List<String> resTbl = new ArrayList<>(Arrays.asList(res.getStringArray(R.array.db_tables_name)));
        List<String> resTblTemporary = new ArrayList<>(Arrays.asList(res.getStringArray(R.array.db_temporary_tables)));
        List<String> resTblFields = new ArrayList<>(Arrays.asList(res.getStringArray(R.array.db_tables_fields)));
        List<String> resTblConstraints = new ArrayList<>(Arrays.asList(res.getStringArray(R.array.db_table_constraint)));
        List<String> resFldsTypes = new ArrayList<>(Arrays.asList(res.getStringArray(R.array.db_fields_types)));
        List<String> resFldsProperties = new ArrayList<>(Arrays.asList(res.getStringArray(R.array.db_fields_properties)));

        Table auxTbl;

        for(String tableName : resTbl){
            auxTbl = new SimpleTable(tableName,resTblTemporary, resTblFields, resTblConstraints, resFldsTypes, resFldsProperties);
            tables.put(auxTbl.getTableId(), auxTbl);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "Creating database: "+db.getPath());

        try{
            db.beginTransaction();

            for(Map.Entry<String, Table> table : tables.entrySet()){
                db.execSQL(table.getValue().getCreateQuery());
            }
            db.setTransactionSuccessful();

        }catch(Exception e){
            Log.e(LOG_TAG, e.getMessage(), e);
            throw e;
        }finally {
            db.endTransaction();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.d(LOG_TAG, "Updating database: "+db.getPath());
        try{
            db.beginTransaction();
            for(Map.Entry<String, Table> table : tables.entrySet()){
                db.execSQL(table.getValue().getDropQuery());
            }
            db.setTransactionSuccessful();
            this.onCreate(db);
        }
        catch(Exception e){
            Log.e(LOG_TAG, e.getMessage(), e);
            throw e;
        }finally {
            db.endTransaction();
        }

    }

    @Override
    public void onOpen(SQLiteDatabase sqLiteDatabase){
        Log.d(LOG_TAG, "Database: "+sqLiteDatabase.getPath()+" open");
    }

    public SQLiteDatabase getReadableDatabase(){
        return super.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase(){
        return super.getWritableDatabase();
    }

    public Table getTableById(String tblId){
        return tables.get(tblId);
    }

}
