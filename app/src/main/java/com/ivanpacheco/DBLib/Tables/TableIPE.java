package com.ivanpacheco.DBLib.Tables;

/**
 * Created by ivanpacheco on 31/01/18.
 * Interface for many types of Tables
 */

public interface TableIPE {
    String getTableName();
    String getCreateQuery();
    String getDropQuery();
    String getClearTableQuery();
    String getColumnName(String columnId);
}
