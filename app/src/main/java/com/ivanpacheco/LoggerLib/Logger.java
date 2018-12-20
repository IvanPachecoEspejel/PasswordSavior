package com.ivanpacheco.LoggerLib;

import android.util.Log;

import com.ivanpacheco.passwordsavior.BuildConfig;

import java.io.Serializable;

/**
 * Created by ivanpacheco on 19/02/18.
 *
 */

public class Logger implements Serializable {

    private final String LOG_TAG;

    public Logger(String LOG_TAG){
        this.LOG_TAG = LOG_TAG;
    }

    public void i(String message){
        if(checkDebug()){
            Log.i(LOG_TAG, message);
        }
    }

    public void i(String message, Throwable exeption){
        if(checkDebug()){
            Log.i(LOG_TAG, message, exeption);
        }
    }

    public void e(String message){
        Log.e(LOG_TAG, message);
    }

    public void e(String message, Throwable exeption){
        Log.e(LOG_TAG, message, exeption);
    }

    private boolean checkDebug(){
        return BuildConfig.DEBUG;
    }
}

