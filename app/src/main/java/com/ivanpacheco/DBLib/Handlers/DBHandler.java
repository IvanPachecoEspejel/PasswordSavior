package com.ivanpacheco.DBLib.Handlers;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.ivanpacheco.DBLib.util.DBElement;
import com.ivanpacheco.DBLib.util.FieldValue;
import com.ivanpacheco.DBLib.util.UtilDB;
import com.ivanpacheco.passwordsavior.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.ivanpacheco.DBLib.Tables.SimpleTable;
import com.ivanpacheco.DBLib.Tables.TableIPE;

/**
 * Created by ivanpacheco on 31/01/18.
 * Handler to manage a local data base
 */

public class DBHandler extends SQLiteOpenHelper{

    private String LOG_TAG = "DBLib.DBHandler";

    private Resources res;
    private HashMap<String, TableIPE> tables;
    private SQLiteDatabase db;

    public DBHandler(Context ctx){
        super(ctx,
                ctx.getResources().getString(R.string.db_name),
                null,
                ctx.getResources().getInteger(R.integer.db_version));

        res = ctx.getResources();
        tables = new HashMap<>();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.db = sqLiteDatabase;

        List<String> resTbl = Arrays.asList(res.getStringArray(R.array.db_tables_name));
        List<String> resTblTemporary = Arrays.asList(res.getStringArray(R.array.db_temporary_tables));
        List<String> resTblFields = Arrays.asList(res.getStringArray(R.array.db_tables_fields));
        List<String> resTblConstraints = Arrays.asList(res.getStringArray(R.array.db_table_constraint));
        List<String> resFldsTypes = Arrays.asList(res.getStringArray(R.array.db_fields_types));
        List<String> resFldsProperties = Arrays.asList(res.getStringArray(R.array.db_fields_properties));

        TableIPE auxTbl;
        DBElement res;
        try{
            this.db.beginTransaction();

            for(String tableName : resTbl){
                res = UtilDB.processDBResource(tableName);
                auxTbl = new SimpleTable(res.value,resTblTemporary, resTblFields, resTblConstraints, resFldsTypes, resFldsProperties);
                this.db.execSQL(auxTbl.getCreateQuery());
                tables.put(res.id, auxTbl);
            }
            this.db.setTransactionSuccessful();

        }catch(Exception e){
            Log.e(LOG_TAG, e.getMessage(), e);
        }finally {
            this.db.endTransaction();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long insert(String tblId, List<FieldValue> parameters){
        long rowId = -1;
        try{
            this.db.beginTransaction();

            String sql = tables.get(tblId).getInsertStatement(parameters);
            SQLiteStatement statement = this.db.compileStatement(sql);
            for(int idx = 0; idx <= parameters.size(); idx ++){
                statement.bindString(idx, parameters.get(idx).value);
            }
            rowId = statement.executeInsert();

            this.db.setTransactionSuccessful();
        }catch(Exception e){
            Log.e(LOG_TAG, e.getMessage(), e);
        }finally {
            db.endTransaction();
        }
        return rowId;
    }

    public long update(String tblId, List<FieldValue> parameters){
        long rowId = -1;
        try{
            this.db.beginTransaction();

            String sql = tables.get(tblId).getInsertStatement(parameters);
            SQLiteStatement statement = this.db.compileStatement(sql);
            for(int idx = 0; idx <= parameters.size(); idx ++){
                statement.bindString(idx, parameters.get(idx).value);
            }
            rowId = statement.executeInsert();

            this.db.setTransactionSuccessful();
        }catch(Exception e){
            Log.e(LOG_TAG, e.getMessage(), e);
        }finally {
            db.endTransaction();
        }
        return rowId;
    }

    public void delete(){

    }
}
