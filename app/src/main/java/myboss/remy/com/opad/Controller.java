package myboss.remy.com.opad;

import android.app.Application;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import myboss.remy.com.opad.com.example.database.DbHelper;
import myboss.remy.com.opad.com.example.database.MyContentProvider;

/**
 * Created by Chimere on 9/18/2016.
 */
public class Controller extends Application {
    public static DbHelper dbHelper;
    public static SharedPreferences sharedPreferences;
    public static SQLiteDatabase db;

    public static final String HIDE_LOCKED = "hideLocked";
    public static final String PASSWORD = "password";
    public static final String LAST_AUTHORITY = "lastAuth";
    public static final String DEFAULT_SORT = "defaultSort";
    public static final String DEFAULT_CATEFORY_OPTION = "defaultCategoryOption";
    public static final String TIME_OPTION = "timeOption";

    public static long  PUBLIC_CATEGORYID = 1;
    public static Uri  LASTCREATED_CATEGORYID  = MyContentProvider.CONTENT_URI_CATEGORY;
    public static long  LASTSELECTED_CATEGORYID ;


    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();
    }

    public static int getDefaultCategoryOption() {
        return Integer.parseInt(sharedPreferences.getString(DEFAULT_CATEFORY_OPTION, "0"));
    }

    public static boolean showLocked() {
        return !sharedPreferences.getBoolean(HIDE_LOCKED, false);
    }

    public static boolean isAuthority() {
        return TextUtils.isEmpty(sharedPreferences.getString(PASSWORD, null)) || (System.currentTimeMillis() -sharedPreferences.getLong(LAST_AUTHORITY, 0)) < 5 * 60* 1000;
    }

    public static void clearAuth() {
        sharedPreferences.edit().remove(LAST_AUTHORITY).commit();
    }

    public static boolean doAuthority(String s) {
        String pass = sharedPreferences.getString(PASSWORD, null);
        boolean isAuthority = TextUtils.isEmpty(pass) || pass.equals(s);
        if (isAuthority)
            sharedPreferences.edit().putLong(LAST_AUTHORITY, System.currentTimeMillis()).commit();
        return isAuthority;
    }

    public static int getDefaultSort() {
        return Integer.parseInt(sharedPreferences.getString(DEFAULT_SORT, "0"));
    }

    public static int getTimeOption() {
        return Integer.parseInt(sharedPreferences.getString(TIME_OPTION, "0"));
    }
}
