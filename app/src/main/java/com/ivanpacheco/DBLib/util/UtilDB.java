package com.ivanpacheco.DBLib.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ivanpacheco on 31/01/18.
 * Util for create Local data bases
 */

public class UtilDB {

    public static String DLMTR = "@";

    public static DBElement processDBResource(String dbResource){
        String[] res = dbResource.split("\\.");
        String[] references = null;
        if(res.length > 2){
            references = Arrays.copyOfRange(res, 0, res.length -2);
        }
        return new DBElement(res[res.length-2], res[res.length-1], references);
    }

    public static List<DBElement> getItemFromLstResource(List<String> lstResource, List<DBElement> idNames){
        DBElement resItem;
        List<DBElement> result = new ArrayList<>();
        int idx = 0;

        for(int idxItem = 0; idxItem < lstResource.size(); idxItem++){
            resItem = processDBResource(lstResource.get(idxItem));

            if(idNames.size() == resItem.references.size()){
                for(idx = 0; idx < idNames.size(); idx++){
                    if(!cmpReference(idNames.get(idx), resItem.references.get(idx))){
                        break;
                    }
                }
                if(idx == idNames.size()){
                    result.add(resItem);
                    lstResource.remove(idxItem);
                    idxItem--;
                }
            }
        }
        return result;
    }

    public static HashMap<String, DBElement> getHasMapItemFromLstResource(List<String> lstResource, List<DBElement> idNames){
        DBElement resItem;
        HashMap<String, DBElement> result = new HashMap<>();
        int idx = 0;

        for(int idxItem = 0; idxItem < lstResource.size(); idxItem++){
            resItem = processDBResource(lstResource.get(idxItem));

            if(idNames.size() == resItem.references.size()){
                for(idx = 0; idx < idNames.size(); idx++){
                    if(!cmpReference(idNames.get(idx), resItem.references.get(idx))){
                        break;
                    }
                }
                if(idx == idNames.size()){
                    result.put(resItem.id, resItem);
                    lstResource.remove(idxItem);
                    idxItem--;
                }
            }
        }
        return result;
    }


    private static boolean cmpReference(DBElement idName, String reference){
        return idName.id.equals(reference) || idName.value.equals(reference);
    }

    public static String processDBString(String dbString, HashMap<String, DBElement> columns){
        if(dbString == null || dbString.length() <= 0)
            return "";
        String[] parts = dbString.split(DLMTR);
        StringBuilder sb = new StringBuilder();
        int endId;
        for(String part : parts){
            if(part.isEmpty()){
                continue;
            }
            if(part.indexOf(" ") == -1){
                sb.append(columns.get(part).value);
            }else{
                sb.append(columns.get(part.substring(0, part.indexOf(" "))).value)
                        .append(part.substring(part.indexOf(" ")+1, part.length()));
            }
        }
        return sb.toString();
    }
}
