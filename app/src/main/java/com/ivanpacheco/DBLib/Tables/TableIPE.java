package com.ivanpacheco.DBLib.Tables;

import com.ivanpacheco.DBLib.util.FieldValue;

import java.util.List;

/**
 * Created by ivanpacheco on 31/01/18.
 * Interface for many types of Tables
 */

public interface TableIPE {
    String getCreateQuery();
    String getInsertStatement(List<FieldValue> parameters);
    String getUpdateStatement(List<FieldValue> parameters);
}
