package com.scy.android.tomatotaskdo.conpoment.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.scy.android.tomatotaskdo.conpoment.constants.ConstValues;

/**
 * 沈程阳
 * created by scy on 2019/1/13 16:54
 * 邮箱：1797484636@qq.com
 */
public class SpUtil {

    public static void setIsFirstVisit(Context context, String key, Boolean value) {
        SharedPreferences sp = context.getSharedPreferences("ifv", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    public static boolean getIsFirstVisit(Context context,String key) {
        SharedPreferences sp = context.getSharedPreferences("ifv", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static void setIsLogin(Context context, String key, Boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ifv", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static boolean getIsLogin(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("ifv", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static void setCurrentUser(Context context, String key, long value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ifv", Context.MODE_PRIVATE);
        sharedPreferences.edit().putLong(key, value).apply();
    }

    public static long getCurrentUser(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("ifv", Context.MODE_PRIVATE);
        return sp.getLong(key, 0);
    }

    public static void setCurrentFocusTime(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ifv", Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(key,value).apply();
    }

    public static int getCurrentFocusTime(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("ifv", Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    public static void setIsTiming(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("ifv", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    public static boolean getIsTiming(Context context,String key) {
        SharedPreferences sp = context.getSharedPreferences("ifv", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static void signOut(Context context) {
        setIsLogin(context, ConstValues.LOGIN,false);
        setIsTiming(context,ConstValues.ISTIMING, false);
    }
}
