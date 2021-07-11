package com.example.suyog.newapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Suyog on 18-10-2018.
 */

public class SaveSharedPreference {
    final static String uid="uid";
    final static String utype="utype",fkOfDist="fkOfDist";

    static SharedPreferences getSharedPreferences(Context ctx)
    {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName,String uType)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(uid, userName);
        editor.putString(utype,uType);
        editor.apply();
     }

    public static void setFkOfDist(Context ctx,String dID)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(fkOfDist,dID);
        editor.apply();
    }

    public static String getUserName(Context ctx)
    {
       return getSharedPreferences(ctx).getString(uid,"");
    }


    public static String getUtype(Context ctx)
    {
        return getSharedPreferences(ctx).getString(utype, "");
    }

    public static String getFkOfDist(Context ctx)
    {
        return getSharedPreferences(ctx).getString(fkOfDist, "");
    }

    public static void clearUserName(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.apply();
    }
}
