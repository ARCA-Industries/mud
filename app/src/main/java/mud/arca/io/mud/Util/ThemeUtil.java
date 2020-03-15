package mud.arca.io.mud.Util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

public class ThemeUtil {

    public static void loadAndSetThemeFromPreferences(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        setTheme(prefs.getString("theme", ""));
    }

    public static void setTheme(String theme) {
        switch (theme) {
            case "day":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "night":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
            case "auto":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

}
