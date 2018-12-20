package com.ivanpacheco.LoginValidatiorLib;

import com.ivanpacheco.LoginValidatiorLib.util.UtilLV;

/**
 * Created by ivanpacheco on 14/02/18.
 *
 */

public class LoginValidator {

    private static final int MAX_INSECURE = 4;

    public static int getInsecurityPassword(String password) {
        int insecurity = MAX_INSECURE;
        if(UtilLV.containsLowerCase(password)){
            insecurity--;
        }
        if(UtilLV.containsNumbers(password)){
            insecurity--;
        }
        if(UtilLV.containsUpperCase(password)){
            insecurity--;
        }
        if(UtilLV.containsSpecialCharacters(password)){
            insecurity--;
        }
        return insecurity;
    }

    public static int getMaxInsecure(String password) {
        return MAX_INSECURE;
    }

    public static boolean isValidPassword(String password) {
        return !UtilLV.hasBlanksCharacters(password);
    }
}
