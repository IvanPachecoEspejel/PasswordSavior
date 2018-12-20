package com.ivanpacheco.DBLib.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by ivanpacheco on 31/01/18.
 * Basic element to build a database
 */

public class DBElement {
    public String id;
    public String value;
    public List<String> references;

    public DBElement(String id, String value, String[] references){
        this.id = id;
        this.value = value;

        if(references == null || references.length == 0){
            this.references = new ArrayList<>();

        }else if(references.length == 1 ){
            this.references = Collections.singletonList(references[0]);
        }else{
            this.references = Arrays.asList(references);
        }
    }

    public DBElement(String id, String value){
        this.id = id;
        this.value = value;
        this.references = new ArrayList<>();
    }
}
