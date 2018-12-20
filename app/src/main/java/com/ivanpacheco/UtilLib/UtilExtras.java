package com.ivanpacheco.UtilLib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ivanpacheco.EncryotorLib.Encryptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by ivanpacheco on 19/02/18.
 *
 */

public class UtilExtras {

    public static boolean getExtraBoolean(Activity activity, String extraKey) throws ClassCastException{
        Intent i = activity.getIntent();
        if(i == null)
            return false;
        Bundle extras = i.getExtras();
        if(extras == null || extras.isEmpty())
            return false;

        Object extra = extras.get(extraKey);

        if(extra == null)
            return false;

        if(extra instanceof Boolean)
            return (Boolean) extra;

        throw new ClassCastException("Extra: "+extraKey+" is not Boolean");
    }

    public static String getExtraString(Activity activity, String extraKey) throws ClassCastException{
        Intent i = activity.getIntent();
        if(i == null)
            return "";
        Bundle extras = i.getExtras();
        if(extras == null || extras.isEmpty())
            return "";

        Object extra = extras.get(extraKey);

        if(extra == null)
            return "";

        if(extra instanceof String)
            return (String) extra;

        throw new ClassCastException("Extra: "+extraKey+" is not String");
    }

    public static byte[] getExtraByteArr(Activity activity, String extraKey){
        Intent i = activity.getIntent();
        if(i == null)
            return null;
        Bundle extras = i.getExtras();
        if(extras == null || extras.isEmpty())
            return null;

        Object extra = extras.get(extraKey);

        if(extra == null)
            return null;

        if(extra instanceof byte[])
            return (byte[]) extra;

        throw new ClassCastException("Extra: "+extraKey+" is not a byte array");
    }

    public static byte[] serializeObject(Object obj) throws IOException {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos= new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
            return baos.toByteArray();
        }catch (IOException ioe){
            Log.e(Encryptor.class.getName(),"Error Serializing object: "+ioe.getMessage(),ioe);
            throw ioe;
        } finally{
            if(oos != null){
                oos.close();
            }
            if(baos != null){
                baos.close();
            }
        }
    }

    public static Object deserializeObject(byte[] objArr) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(objArr);
            ois= new ObjectInputStream(bais);
            return ois.readObject();
        }catch (IOException | ClassNotFoundException ioe){
            Log.e(Encryptor.class.getName(),"Error deserializing object: "+ioe.getMessage(),ioe);
            throw ioe;
        }finally {
            if(bais != null)
                bais.close();
            if(ois != null)
                ois.close();
        }
    }
}
