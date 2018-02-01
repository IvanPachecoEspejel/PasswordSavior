package com.ivanpacheco.DBLib.util;

import java.util.List;

/**
 * Created by ivanpacheco on 31/01/18.
 * Basic element to build a database
 */

public class DBElement {
    public String id;
    public String value;
    public List<String> references;

    public DBElement(String id, String value, List<String> references){
        this.id = id;
        this.value = value;
        this.references = references;
    }
}
