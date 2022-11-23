

package com.zlfc.test.util;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;
import java.util.Set;

/**
 * Create by lance on 2020/1/2/0002
 * <p>
 * Helper class to use SharedPreferences easier.
 */
public class SpUtil {

    // File name store on internal storage.
    private static final String FILE_NAME = "fusion_config";

    // Save data.
    public static void put(Context context, String key, Object obj) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if (sp != null) {
            Editor editor = sp.edit();

            if (obj instanceof Boolean) {
                editor.putBoolean(key, (Boolean) obj);
            } else if (obj instanceof Float) {
                editor.putFloat(key, (Float) obj);
            } else if (obj instanceof Integer) {
                editor.putInt(key, (Integer) obj);
            } else if (obj instanceof Long) {
                editor.putLong(key, (Long) obj);
            } else if (obj instanceof String) {
                editor.putString(key, (String) obj);
            } else if (obj instanceof Set) {
                // A workaround to fix putStringSet bug.
                editor.remove(key);
                editor.putStringSet(key, (Set<String>) obj);
            }
            editor.apply();
        }
    }

    // Fetch data.
    public static Object get(Context context, String key, Object defaultObj) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if (sp == null)
            return null;

        if (defaultObj instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObj);
        } else if (defaultObj instanceof Float) {
            return sp.getFloat(key, (Float) defaultObj);
        } else if (defaultObj instanceof Integer) {

            return sp.getInt(key, (Integer) defaultObj);
        } else if (defaultObj instanceof Long) {
            return sp.getLong(key, (Long) defaultObj);
        } else if (defaultObj instanceof String) {
            return sp.getString(key, (String) defaultObj);
        } else if (defaultObj instanceof Set) {
            return sp.getStringSet(key, (Set<String>) defaultObj);
        }
        return null;
    }


    // Fetch data.
    public static String getString(Context context, String key) {//获得3.0参数
        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences("beizisdk_config", Context.MODE_PRIVATE);
            if (sp == null) {
                return null;
            }
            return sp.getString(key, "");
        }
        return null;
    }

    public static void putString(Context context, String key,String value) {
        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences("beizisdk_config", Context.MODE_PRIVATE);
            Editor editor = sp.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    // Delete data.
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if (sp != null) {
            Editor editor = sp.edit();
            editor.remove(key);
            editor.apply();
        }
    }

    // Return all the key-value pairs.
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if (sp != null)
            return sp.getAll();

        return null;
    }

    // Clear all data.
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if (sp != null) {
            Editor editor = sp.edit();
            editor.clear();
            editor.apply();
        }
    }

    // Check if we have specialised key.
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp != null && sp.contains(key);
    }


    public static String getUpdated(Context context, String flag) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        String updated = sharedPreferences.getString(flag, String.valueOf(System.currentTimeMillis() / 1000));
        return updated;
    }


    public static void saveUpdated(Context context, String flag, String updated) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString(flag, updated);
        editor.commit();
    }


}
