package com.ivanpacheco.DBLib.util;

import java.util.HashMap;

/**
 * Created by ivanpacheco on 1/02/18.
 * Returns a condition whit the fields passed
 */

public class Condition {
    private String condition;

    public Condition(String condition){
        this.condition = condition;
    }


    public String getCondition(HashMap<String, DBElement> fields){
        String[] parts = condition.split("\\?");
        return "";
    }
}
