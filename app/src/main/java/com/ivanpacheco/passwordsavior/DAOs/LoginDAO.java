package com.ivanpacheco.passwordsavior.DAOs;

import android.content.Context;

import com.ivanpacheco.DBLib.GenericDAO;
import com.ivanpacheco.DBLib.Handlers.DBHandler;

/**
 * Created by ivanpacheco on 1/02/18.
 * class that insert update or delete a credential
 */

public class LoginDAO extends GenericDAO {

    private static String TABLE_ID = "1";

    private static String ID = "1";
    private static String USER_NAME_ID = "2";
    private static String PASSWORD_ID = "3";
    private static String LAST_UPDATE_ID = "4";
    private static String CREATED_ID = "5";


    public LoginDAO(Context context) {
        super(context, TABLE_ID);
    }
}
