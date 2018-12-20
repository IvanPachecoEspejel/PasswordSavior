package com.ivanpacheco.DBLib.Tables;

import com.ivanpacheco.DBLib.util.DBElement;

import java.util.HashMap;

/**
 * Created by ivanpacheco on 31/01/18.
 * Interface for many types of Tables
 */

public interface Table {
    String getTableName();
    String getTableId();
    String getCreateQuery();
    String getDropQuery();
    String getClearTableQuery();
    String getColumnName(String columnId);

    HashMap<String, DBElement> getColumns();
}
