package com.codetoart.android.qrcodescannerandroid;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class utilities {
    public static final String USERDATA = "MyVariables";
    public static int cuid = 1;

    private static void saveMap(String key, Map<String,String> inputMap){
        SharedPreferences pSharedPref = MangoDB.getAppContext().getSharedPreferences(USERDATA, Context.MODE_PRIVATE);
        if (pSharedPref != null){
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove(key).commit();
            editor.putString(key, jsonString);
            editor.commit();
        }
    }

    public static void save(String key, String value)
    {
        SharedPreferences pSharedPref = MangoDB.getAppContext().getSharedPreferences(USERDATA, Context.MODE_PRIVATE);
        if (pSharedPref != null){
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove(key).commit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public static String load(String key)
    {
        SharedPreferences pSharedPref = MangoDB.getAppContext().getSharedPreferences(USERDATA, Context.MODE_PRIVATE);
        if (pSharedPref != null){
            return pSharedPref.getString(key, "");

        }
        return "";
    }

    public static boolean contains(String key)
    {
        SharedPreferences pSharedPref = MangoDB.getAppContext().getSharedPreferences(USERDATA, Context.MODE_PRIVATE);
        if (pSharedPref != null){
            return pSharedPref.contains(key);

        }
        return false;
    }

    private static Map<String,String> loadMap(String key){
        Map<String,String> outputMap = new HashMap<String,String>();
        SharedPreferences pSharedPref = MangoDB.getAppContext().getSharedPreferences(USERDATA, Context.MODE_PRIVATE);
        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString(key, (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String k = keysItr.next();
                    String v = (String) jsonObject.get(k);
                    outputMap.put(k,v);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }
}
