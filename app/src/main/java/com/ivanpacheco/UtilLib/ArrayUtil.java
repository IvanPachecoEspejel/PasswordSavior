package com.ivanpacheco.UtilLib;

/**
 * Created by ivanpacheco on 20/02/18.
 */

public class ArrayUtil {

    public static byte[] reverse(byte[] arr) {
        byte[] reverse = new byte[arr.length];
        for(int i = 0; i<arr.length; i++){
            reverse[i] = arr[arr.length-i-1];
        }
        return reverse;
    }

}
