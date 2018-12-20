package com.ivanpacheco.DBLib.Tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ivanpacheco.DBLib.util.DBElement;
import com.ivanpacheco.DBLib.util.UtilDB;

/**
 * Created by ivanpacheco on 31/01/18.
 * Class that abstract a simple sqlite table without complex attributes
 */

public class SimpleTable implements Table {
    private DBElement table;
    private HashMap<String, DBElement> columns;
    private List<DBElement> constraints;
    private HashMap<String, List<DBElement>> columnsType;
    private HashMap<String, List<DBElement>> columnsProperties;

    private Boolean isTemporal;

    public SimpleTable(String resTable,
                       List<String> resTblTemporary,
                       List<String> resTblFields,
                       List<String> resTblConstraints,
                       List<String> resFldsTypes,
                       List<String> resFldsProperties){

        this.table = UtilDB.processDBResource(resTable);

        List<DBElement> idNames = new ArrayList<>(Collections.singletonList(this.table));
        List<DBElement> temporary = UtilDB.getItemFromLstResource(resTblTemporary, idNames);

        this.isTemporal = (temporary != null && temporary.size()>0);
        this.columns = UtilDB.getHasMapItemFromLstResource(resTblFields, idNames);
        this.constraints = UtilDB.getItemFromLstResource(resTblConstraints, idNames);

        columnsType = new HashMap<>();
        columnsProperties = new HashMap<>();

        for(Map.Entry<String, DBElement> field : columns.entrySet()){
            idNames.add(field.getValue());
            columnsType.put(field.getValue().id, UtilDB.getItemFromLstResource(resFldsTypes, idNames));
            columnsProperties.put(field.getValue().id, UtilDB.getItemFromLstResource(resFldsProperties, idNames));
            idNames.remove(idNames.size()-1);
        }
    }

    @Override
    public String getTableName() {
        return this.table.value;
    }

    @Override
    public String getTableId() {
        return this.table.id;
    }

    @Override
    public String getCreateQuery(){
        StringBuilder sql = new StringBuilder("CREATE ");
        if(this.isTemporal)
            sql.append("TEMPORARY ");
        sql.append("TABLE IF NOT EXISTS ").append(this.table.value).append("(");

        for(Map.Entry<String, DBElement> column : columns.entrySet()){
            sql.append("'").append(column.getValue().value).append("' ");
            for(DBElement type : columnsType.get(column.getValue().id)){
                sql.append(type.value).append(" ");
            }
            for(DBElement property : columnsProperties.get(column.getValue().id)){
                sql.append(property.value).append(" ");
            }
            sql.append(", ");
        }
        for(DBElement constraint : constraints){
            sql.append(constraint.value).append(", ");
        }
        return sql.substring(0, sql.length()-2) + ")";
    }

    @Override
    public String getDropQuery() {
        return "DROP TABLE IF EXISTS "+this.table.value;
    }

    @Override
    public String getClearTableQuery() {
        return "DELETE FROM " + this.table.value;
    }

    @Override
    public String getColumnName(String columnId) {
        return this.columns.get(columnId).value;
    }

    @Override
    public HashMap<String, DBElement> getColumns() {
        return this.columns;
    }
}
