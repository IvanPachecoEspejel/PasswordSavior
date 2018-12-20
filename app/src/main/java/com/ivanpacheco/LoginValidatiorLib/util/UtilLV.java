package com.ivanpacheco.LoginValidatiorLib.util;

import java.util.regex.Pattern;

/**
 * Created by ivanpacheco on 14/02/18.
 *
 */

public class UtilLV {

    public static String HAS_UPPERCASE = ".*\\p{Upper}+.*";
    public static String HAS_LOWERCASE = ".*\\p{Lower}+.*";
    public static String HAS_NUMBERS = ".*\\p{Digit}+.*";
    public static String HAS_SPECIAL_CHARACTERS = ".*[!|@|#|$|%|&|/|(|)|=|?|¡|¿|*|+|\\[|\\]|{|}|\\-|_|.|:|,|;|<|>]+.*";
    public static String HAS_BLANK_CHARACTERS = ".*\\s+.*";

    public static boolean containsUpperCase(String str){
        return Pattern.matches(HAS_UPPERCASE, str);
    }

    public static boolean containsLowerCase(String str){
        return Pattern.matches(HAS_LOWERCASE, str);
    }

    public static boolean containsNumbers(String str){
        return Pattern.matches(HAS_NUMBERS, str);
    }

    public static boolean containsSpecialCharacters(String str){
        return Pattern.matches(HAS_SPECIAL_CHARACTERS, str);
    }

    public static boolean hasBlanksCharacters(String str){
        return Pattern.matches(HAS_BLANK_CHARACTERS,str);
    }

}
