package com.example.devigoogleplaces.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class Prefs<date> {
    static final String PREF_SPLASH_SCREEN = "splash_screen";
    // static final String DATE = "date";
    static final String PREF_USER_FIRST_NAME = "user_first_name";
    static final String PREF_USER_LAST_NAME = "user_last_name";
    static final String PREF_USER_NAME = "user_name";
    static final String PREF_USER_PWD = "user_pwd";
    static final String PREF_USER_EMAIL = "user_email";
    static final String PREF_USER_MOBILE = "user_mobile";
    static final String PREF_USER_PIC_URL = "userPic_url";
    static final String PREF_USER_ID = "user_id";
    static final String PREF_ORDER_ID = "order_id";
    static final String PREF_ADDRESS_ID = "address_id";
    static final String PREF_ADDRESS = "address";
    static final String PREF_TOTAL = "total";
    static final String PREF_ACC_EXISTS = "acc_exists";
    static final String PREF_GCM_TOKEN = "gcm_token";
    static final String PREF_DATE = "date";
    static final String PREF_LAT = "latitude";
    static final String PREF_LANG = "longitude";
    static final String PREF_RFERAL = "referal";
    static final String PREF_STORE_LAT = "latitude";
    static final String PREF_STORE_LANG = "longitude";
    static final String PREF_CITY_ID = "city_id";

    /* Android Info Generic Usecase*/


    static SharedPreferences getPrefs(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setCityId(Context ctx, String s) {
        getPrefs(ctx).edit().putString(PREF_CITY_ID, s).commit();
    }

    public static String getCityId(Context ctx) {
        return getPrefs(ctx).getString(PREF_CITY_ID, "");
    }

    public static void setUserFirstName(Context ctx, String s) {
        getPrefs(ctx).edit().putString(PREF_USER_FIRST_NAME, s).commit();
    }

    public static void setUserLastName(Context ctx, String s) {
        getPrefs(ctx).edit().putString(PREF_USER_LAST_NAME, s).commit();
    }

    public static void setUserName(Context ctx, String s) {
        getPrefs(ctx).edit().putString(PREF_USER_NAME, s).commit();
    }


    public static String getUserFirstName(Context ctx) {
        return getPrefs(ctx).getString(PREF_USER_FIRST_NAME, "");
    }

    public static String getUserLastName(Context ctx) {
        return getPrefs(ctx).getString(PREF_USER_LAST_NAME, "");
    }

    public static String getUserName(Context ctx) {
        return getPrefs(ctx).getString(PREF_USER_NAME, "");
    }

    public static void setUserEmail(Context ctx, String s) {
        getPrefs(ctx).edit().putString(PREF_USER_EMAIL, s).commit();
    }


    public static String getUserEmail(Context ctx) {
        return getPrefs(ctx).getString(PREF_USER_EMAIL, "");
    }

    public static void setMobileNumber(Context ctx, String s) {
        getPrefs(ctx).edit().putString(PREF_USER_MOBILE, s).commit();
    }

    public static String getUserLat(Context ctx) {
        return getPrefs(ctx).getString(PREF_LAT, "");
    }

    public static void setUserLat(Context ctx, String s) {
        getPrefs(ctx).edit().putString(PREF_LAT, s).commit();
    }

    public static String getUserLang(Context ctx) {
        return getPrefs(ctx).getString(PREF_LANG, "");
    }

    public static void setUserLang(Context ctx, String s) {
        getPrefs(ctx).edit().putString(PREF_LANG, s).commit();
    }

    public static String getReferalCode(Context ctx) {
        return getPrefs(ctx).getString(PREF_RFERAL, "");
    }

    public static void setReferalCode(Context ctx, String s) {
        getPrefs(ctx).edit().putString(PREF_RFERAL, s).commit();
    }

    public static String getMobileNumber(Context ctx) {
        return getPrefs(ctx).getString(PREF_USER_MOBILE, "");
    }


    public static void setUserProfileUrl(Context ctx, String s) {
        getPrefs(ctx).edit().putString(PREF_USER_PIC_URL, s).commit();
    }

    public static String getUserProfileUrl(Context ctx) {
        return getPrefs(ctx).getString(PREF_USER_PIC_URL, "");
    }

    public static void setUserPwd(Context ctx, String s) {
        getPrefs(ctx).edit().putString(PREF_USER_PWD, s).commit();
    }

    public static String getUserPwd(Context ctx) {
        return getPrefs(ctx).getString(PREF_USER_PWD, "");
    }
    /* Global server user id */
    public static void setUserId(Context ctx, String s) {
        getPrefs(ctx).edit().putString(PREF_USER_ID, s).commit();
    }

    public static String getUserId(Context ctx) {
        return getPrefs(ctx).getString(PREF_USER_ID, null);
    }
    public static boolean hasUserIdGenerated(Context ctx){
        return !TextUtils.isEmpty(getUserId(ctx));
    }
    /* Android Info Generic Use case */


    public static void setGCMToken(Context ctx, String s) {
        getPrefs(ctx).edit().putString(PREF_GCM_TOKEN, s).commit();
    }

    public static String getGCMToken(Context ctx) {
        return getPrefs(ctx).getString(PREF_GCM_TOKEN, null);
    }

    public static void setAccExists(Context ctx, String username, boolean y) {
        if(username == null) return;
        getPrefs(ctx).edit().putBoolean(username, y).commit();
    }

    public static boolean getAccExists(Context ctx, String username) {
        if(username == null) return false;
        return getPrefs(ctx).getBoolean(username, false);
    }

    public static void setSplashScreenPref(Context ctx, String s) {
        getPrefs(ctx).edit().putString(PREF_SPLASH_SCREEN, s).commit();
    }


    public static String getSplashScreenPref(Context ctx) {
        return getPrefs(ctx).getString(PREF_SPLASH_SCREEN, "");
    }




/*public static boolean getSplashScreenPref(Context ctx) {
        return getPrefs(ctx).getString(PREF_SPLASH_SCREEN);
    }

    public static void (Context ctx, String s) {
        getPrefs(ctx).edit().putString(PREF_SPLASH_SCREEN, s).commit();
    }*/

    public static boolean getUserNotificationPreference(final Context context) {
        return getPrefs(context).getBoolean("notification_alerts", true);
    }

    public static void saveDataWithKeyAndValue(Context context, String key, String value){
        SharedPreferences.Editor editor = context.getSharedPreferences("aina",context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDataFromKey(Context context, String key){
        SharedPreferences prefs = context.getSharedPreferences("aina",context.MODE_PRIVATE);
        String restoredText = prefs.getString(key, null);
        return restoredText;

    }

    public static void setAddressId(Context ctx, String s) {
        getPrefs(ctx).edit().putString(PREF_ADDRESS_ID, s).commit();
    }


    public static String getAddressId(Context ctx) {
        return getPrefs(ctx).getString(PREF_ADDRESS_ID, "");
    }

    public static void setAddress(Context ctx, String s) {
        getPrefs(ctx).edit().putString(PREF_ADDRESS, s).commit();
    }


    public static String getAddress(Context ctx) {
        return getPrefs(ctx).getString(PREF_ADDRESS, "");
    }

    public static void setTotal(Context ctx, String s) {
        getPrefs(ctx).edit().putString(PREF_TOTAL, s).commit();
    }


    public static String getTotal(Context ctx) {
        return getPrefs(ctx).getString(PREF_TOTAL, "");
    }

    public static void setOrderId(Context ctx, String s) {
        getPrefs(ctx).edit().putString(PREF_ORDER_ID, s).commit();
    }


    public static String getOrderId(Context ctx) {
        return getPrefs(ctx).getString(PREF_ORDER_ID, "");
    }
    public static void setPrefDate(Context ctx, String s) {
        getPrefs(ctx).edit().putString(PREF_DATE, s).commit();
    }


    public static String getPrefDate(Context ctx) {
        return getPrefs(ctx).getString(PREF_DATE, "");
    }


    public static void setStoreLat(Context ctx, String s) {
        getPrefs(ctx).edit().putString(PREF_STORE_LAT, s).commit();
    }


    public static String getSoreLat(Context ctx) {
        return getPrefs(ctx).getString(PREF_STORE_LAT, "");
    }

    public static void setStoreLang(Context ctx, String s) {
        getPrefs(ctx).edit().putString(PREF_STORE_LANG, s).commit();
    }


    public static String getSoreLang(Context ctx) {
        return getPrefs(ctx).getString(PREF_STORE_LANG, "");
    }

    //PREF_DATE
}