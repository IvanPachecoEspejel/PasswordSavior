package com.ivanpacheco.DBLib.Tables;

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

public class SimpleTable implements TableIPE {
    private DBElement table;
    private HashMap<String, DBElement> fields;
    private List<DBElement> constraints;
    private HashMap<String, List<DBElement>> fieldsType;
    private HashMap<String, List<DBElement>> fieldsProperties;

    private Boolean isTemporal;

    public SimpleTable(String table,
                       List<String> resTblTemporary,
                       List<String> resTblFields,
                       List<String> resTblConstraints,
                       List<String> resFldsTypes,
                       List<String> resFldsProperties){

        this.table = UtilDB.processDBResource(table);

        List<DBElement> idNames = Collections.singletonList(this.table);
        List<DBElement> temporary = UtilDB.getItemFromLstResource(resTblTemporary, idNames);

        this.isTemporal = (temporary != null && temporary.size()>0);
        this.fields = UtilDB.getHasMapItemFromLstResource(resTblFields, idNames);
        this.constraints = UtilDB.getItemFromLstResource(resTblConstraints, idNames);

        fieldsType = new HashMap<>();
        fieldsProperties = new HashMap<>();

        for(Map.Entry<String, DBElement> field : fields.entrySet()){
            idNames.add(field.getValue());
            fieldsType.put(field.getValue().id, UtilDB.getItemFromLstResource(resFldsTypes, idNames));
            fieldsProperties.put(field.getValue().id, UtilDB.getItemFromLstResource(resFldsProperties, idNames));
            idNames.remove(idNames.size()-1);
        }
    }

    @Override
    public String getTableName() {
        return this.table.value;
    }

    @Override
    public String getCreateQuery(){
        StringBuilder sql = new StringBuilder("CREATE ");
        if(this.isTemporal)
            sql.append("TEMPORARY ");
        sql.append("TABLE IF NOT EXISTS ").append(this.table.value).append("(");

        for(Map.Entry<String, DBElement> field : fields.entrySet()){
            sql.append(field.getValue().value).append(" ");
            for(DBElement type : fieldsType.get(field.getValue().id)){
                sql.append(type.value).append(" ");
            }
            for(DBElement property : fieldsProperties.get(field.getValue().id)){
                sql.append(property.value).append(" ");
            }
            sql.append(", ");
        }
        for(DBElement constraint : constraints){

            sql.append(constraint).append(", ");
        }
        return sql.substring(0, sql.length()-2) + ")";
    }

    @Override
    public String getDropQuery() {
        StringBuilder sql = new StringBuilder("DROP TABLE IF EXISTS ");
        sql.append(this.table.value);
        return sql.toString();
    }

    @Override
    public String getClearTableQuery() {
        StringBuilder sql =  new StringBuilder("DELETE FROM ");
        sql.append(this.table.value);
        return sql.toString();
    }

}
