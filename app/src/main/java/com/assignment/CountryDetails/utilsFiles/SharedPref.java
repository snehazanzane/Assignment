package com.assignment.CountryDetails.utilsFiles;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * The type Shared pref.
 */
public class SharedPref {
    /**
     * The My obj.
     */
    static SharedPref myObj;
    /**
     * The share pref key.
     */
    String assignmentSharePrefKey = "";
    /**
     * The Context.
     */
    Context context;
    /**
     * The Sharedpreferences parapay.
     */
    SharedPreferences sharedpreferencesApp;
    /**
     * The Sharedpreferenceseditor parapay.
     */
    SharedPreferences.Editor sharedPreferencesEditorApp;

    /**
     * Instantiates a new Shared pref.
     *
     * @param context the context
     */
    SharedPref(Context context) {
        this.context = context;
        assignmentSharePrefKey = "assignmentSharePrefKey";
        sharedpreferencesApp = context.getSharedPreferences(assignmentSharePrefKey, Context.MODE_PRIVATE);
        sharedPreferencesEditorApp = sharedpreferencesApp.edit();
    }

    /**
     * Gets instance.
     *
     * @param context the context
     * @return the instance
     */
    public static SharedPref getInstance(Context context) {
        if (myObj == null) {
            myObj = new SharedPref(context);
        }
        return myObj;
    }

    /**
     * Gets shared pref.
     *
     * @param key the key
     * @return the shared pref
     */
    public String getSharedPref(String key) {
        return sharedpreferencesApp.getString(key, "");
    }

    /**
     * Put shared pref.
     *
     * @param key   the key
     * @param value the value
     */
    public void putSharedPref(String key, String value) {
        sharedPreferencesEditorApp.putString(key, value);
        sharedPreferencesEditorApp.commit();
        sharedPreferencesEditorApp.apply();
    }

    /**
     * Save string to shared pref.
     *
     * @param key   the key
     * @param value the value
     */
    public void saveStringToSharedPref(String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(assignmentSharePrefKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Gets string from shared pref.
     *
     * @param key the key
     * @return the string from shared pref
     */
    public String getStringFromSharedPref(String key) {
        SharedPreferences pref = context.getSharedPreferences(assignmentSharePrefKey, Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }
    
}
