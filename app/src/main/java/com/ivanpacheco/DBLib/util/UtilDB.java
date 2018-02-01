package com.ivanpacheco.DBLib.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ivanpacheco on 31/01/18.
 * Util for create Local data bases
 */

public class UtilDB {
    public static DBElement processDBResource(String dbResource){
        String[] res = dbResource.split(".");
        return new DBElement(res[res.length-2], res[res.length-1], new ArrayList<String>());
    }

    public static List<DBElement> getItemFromLstResource(List<String> lstResource, List<DBElement> idNames){
        DBElement resItem;
        List<DBElement> result = new ArrayList<>();

        for(int idxItem = 0; idxItem <= lstResource.size(); idxItem++){
            resItem = processDBResource(lstResource.get(idxItem));

            if(idNames.size() == resItem.references.size()){
                for(int idx = 0; idx < idNames.size(); idx++){
                    if(!cmpReference(idNames.get(idx), resItem.references.get(idx))){
                        break;
                    }
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

        for(int idxItem = 0; idxItem <= lstResource.size(); idxItem++){
            resItem = processDBResource(lstResource.get(idxItem));

            if(idNames.size() == resItem.references.size()){
                for(int idx = 0; idx < idNames.size(); idx++){
                    if(!cmpReference(idNames.get(idx), resItem.references.get(idx))){
                        break;
                    }
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
}
