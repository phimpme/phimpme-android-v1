package com.drupal;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Common class.
 */
public class Common {

  public static final int SEND_COOKIE = 1;
  public static final int SAVE_COOKIE = 2;
  public static final int FAILURE = 0;
  public static final int SUCCESS = 1;
  public static final int NO_AUTH = 2;
  public static final int JSON_PARSE_ERROR = 3;
  public static final int SERVER_PARSE_ERROR = 4;
  public static boolean drupappAuthenticated = false;

  /**
   * Get a preference.
   * 
   * @param ctxt
   *        The current context.
   * @param pref
   *        The name of the preference
   * @param DefaultValue
   *        The default value
   * 
   * @return The value of the preference.
   */
  public static String getPref(Context ctxt, String pref, String DefaultValue) {
    SharedPreferences sharedPreferences = ctxt.getSharedPreferences("drupoid", Context.MODE_PRIVATE);
    return sharedPreferences.getString(pref, DefaultValue);
  }

  /**
   * Delete a preference.
   * 
   * @param ctxt
   *        The current context.
   * @param pref
   *        The name of the preference
   */
  public static void delPref(Context ctxt, String pref) {
    SharedPreferences sharedPreferences = ctxt.getSharedPreferences("drupoid", Context.MODE_PRIVATE);
    sharedPreferences.edit().remove(pref).apply();
  }

  /**
   * Set a preference.
   * 
   * @param ctxt
   *        The current context.
   * @param pref
   *        The name of the preference
   * @param value
   *        The value
   */
  public static void setPref(Context ctxt, String pref, String value) {
    SharedPreferences sharedPreferences = ctxt.getSharedPreferences("drupoid", Context.MODE_PRIVATE);
    Editor editor = sharedPreferences.edit();
    editor.putString(pref, value);
    editor.apply();
  }
}