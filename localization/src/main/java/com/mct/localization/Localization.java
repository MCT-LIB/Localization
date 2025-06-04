package com.mct.localization;

import static com.mct.localization.LocalizationConfig.getDefaultDarkMode;
import static com.mct.localization.LocalizationConfig.getDefaultLanguage;
import static com.mct.localization.LocalizationConfig.getDefaultTheme;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.util.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * Note: add <code>android:configChanges="locale|layoutDirection|screenLayout|screenSize"</code>
 * to your Activity in AndroidManifest.xml
 * <p>
 * Use:
 * <pre>
 *  // apply base context in Application
 *  public class App extends Application {
 *
 *      protected void attachBaseContext(Context base) {
 *          super.attachBaseContext(Localization.attachBaseContext(this, base));
 *      }
 *  }
 *
 *  // apply base context in Activity
 *  public class MyActivity extends AppCompatActivity {
 *
 *      protected void attachBaseContext(Context base) {
 *          super.attachBaseContext(Localization.attachBaseContext(this, base));
 *      }
 *  }
 *
 *  // change theme, dark mode, language
 *  if (Localization.{@link Localization#setTheme(Theme)}) {
 *      {@link Activity#recreate()}
 *  }
 *  if (Localization.{@link Localization#setDarkMode(DarkMode)}) {
 *      {@link Activity#recreate()}
 *  }
 *  if (Localization.{@link Localization#setLanguage(Language)}) {
 *      {@link Activity#recreate()}
 *  }
 * </pre>
 *
 * @see LocalizationConfig
 */
public class Localization {

    private static final String TAG = "Localization";

    ///////////////////////////////////////////////////////////////////////////
    // Init
    ///////////////////////////////////////////////////////////////////////////

    public static Context attachBaseContext(@NonNull Activity activity, @NonNull Context base) {
        Log.e(TAG, "attachBaseContext: " + activity.getClass().getName());
        Localization.init(base);
        updateResources(base);
        updateAppCompatDelegates();
        return base;
    }

    public static Context attachBaseContext(@NonNull Application application, @NonNull Context base) {
        Log.e(TAG, "attachBaseContext: " + application.getClass().getName());
        Localization.init(base);
        ApplicationHolder.init(application);
        updateAppCompatDelegates();
        application.registerActivityLifecycleCallbacks(new LifecycleCallbacks() {
            @Override
            public void onActivityPreCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                updateResources(activity.getApplication(), Localization.getTheme());
                updateResources(activity, Localization.getTheme());
            }
        });
        return base;
    }

    public static Context attachBaseContextWithoutApplyTheme(@NonNull Application application, @NonNull Context base) {
        Log.e(TAG, "attachBaseContext: " + application.getClass().getName());
        Localization.init(base);
        ApplicationHolder.init(application);
        updateAppCompatDelegates();
        return base;
    }

    public static void applyLocalizationContext(@NonNull Context context) {
        Localization.init(context);
        updateResources(context);
    }

    @NonNull
    public static Context createLocalizationContext(@NonNull Context context) {
        Localization.init(context);
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, 0);
        updateResources(contextThemeWrapper);
        return contextThemeWrapper;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Utils
    ///////////////////////////////////////////////////////////////////////////

    private static void init(Context context) {
        if (Preferences.getInstance() != null) {
            return;
        }
        // Init preferences
        Preferences.init(context);

        // Validate enums when debug
        if ((context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            validateEnums(Theme.values(), Theme::getValue);
            validateEnums(DarkMode.values(), DarkMode::getValue);
            validateEnums(Language.values(), Language::getValue);
        }
    }

    private static <T extends Enum<T>> void validateEnums(@NonNull T[] enums, @NonNull Function<T, Integer> valueGetter) {
        int size = enums.length;
        List<Integer> values = new ArrayList<>(size);
        for (T e : enums) {
            int value = valueGetter.apply(e);
            if (values.contains(value)) {
                throw new IllegalArgumentException("Duplicate value: " + value + " for enum: " + e);
            }
            values.add(value);
        }
    }

    private static void updateAppCompatDelegates() {
        AppCompatDelegate.setDefaultNightMode(Localization.getDarkMode().getRealNightMode());
        AppCompatDelegate.setApplicationLocales(Localization.getLanguage().getLocaleListCompat());
    }

    private static void updateResources(@NonNull Context context) {
        updateResources(context, Localization.getTheme(), Localization.getDarkMode(), Localization.getLanguage());
    }

    private static void updateResources(@NonNull Context context, Theme theme) {
        updateResources(context, theme, null, null);
    }

    private static void updateResources(@NonNull Context context, DarkMode darkMode) {
        updateResources(context, null, darkMode, null);
    }

    private static void updateResources(@NonNull Context context, Language language) {
        updateResources(context, null, null, language);
    }

    private static void updateResources(@NonNull Context context, Theme theme, DarkMode darkMode, Language language) {
        if (theme != null) {
            // Set the new theme to the context
            context.setTheme(theme.getStyle());
        }
        if (darkMode != null || language != null) {
            // Get the current resources and configuration
            Resources resources = context.getResources();
            Configuration configuration = resources.getConfiguration();
            if (darkMode != null) {
                // Update resources based on the new dark mode
                int uiNightMode = darkMode.getUiNightMode();
                configuration.uiMode = (configuration.uiMode & ~Configuration.UI_MODE_NIGHT_MASK) | uiNightMode;
            }
            if (language != null) {
                // Update resources based on the new language
                configuration.setLocale(language.getLocale());
            }
            // Apply the updated configuration
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Theme
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @return true if theme changed
     */
    public static boolean setTheme(@NonNull Theme theme) {
        if (getTheme().getStyle() != theme.getStyle()) {
            Preferences.getInstance().setTheme(theme);
            updateResources(ApplicationHolder.getApplication(), theme);
            return true;
        }
        return false;
    }

    /**
     * @return current theme
     */
    public static Theme getTheme() {
        return Preferences.getInstance().getTheme();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Dark Mode
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @return true if dark mode changed
     */
    public static boolean setDarkMode(@NonNull DarkMode darkMode) {
        Preferences.getInstance().setDarkMode(darkMode);
        if (AppCompatDelegate.getDefaultNightMode() != darkMode.getRealNightMode()) {
            AppCompatDelegate.setDefaultNightMode(darkMode.getRealNightMode());
            updateResources(ApplicationHolder.getApplication(), darkMode);
            return true;
        }
        return false;
    }

    /**
     * @return current dark mode
     */
    public static DarkMode getDarkMode() {
        return Preferences.getInstance().getDarkMode();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Language
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @return true if language changed
     */
    public static boolean setLanguage(@NonNull Language language) {
        Preferences.getInstance().setLanguage(language);
        if (!AppCompatDelegate.getApplicationLocales().equals(language.getLocaleListCompat())) {
            AppCompatDelegate.setApplicationLocales(language.getLocaleListCompat());
            updateResources(ApplicationHolder.getApplication(), language);
            return true;
        }
        return false;
    }

    /**
     * @return current language
     */
    public static Language getLanguage() {
        return Preferences.getInstance().getLanguage();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Preferences
    ///////////////////////////////////////////////////////////////////////////

    private static class Preferences {

        private static final String PREF_NAME = "localization_prefs";
        private static final String KEY_THEME = "k_theme";
        private static final String KEY_DARK_MODE = "k_dark_mode";
        private static final String KEY_LANGUAGE = "k_language";
        private static Preferences sInstance;
        private final SharedPreferences preferences;

        private Preferences(@NonNull Context context) {
            preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }

        private static void init(Context context) {
            sInstance = new Preferences(context);
        }

        private static Preferences getInstance() {
            return sInstance;
        }

        private SharedPreferences.Editor editor() {
            return preferences.edit();
        }

        /* ----------------------------------- Theme -------------------------------------------- */

        public Theme getTheme() {
            return Theme.find(preferences.getInt(KEY_THEME, -1)).orElse(getDefaultTheme());
        }

        public void setTheme(@NonNull Theme theme) {
            editor().putInt(KEY_THEME, theme.getValue()).apply();
        }

        /* ----------------------------------- DarkMode ---------------------------------------- */

        public DarkMode getDarkMode() {
            return DarkMode.find(preferences.getInt(KEY_DARK_MODE, -1)).orElse(getDefaultDarkMode());
        }

        public void setDarkMode(@NonNull DarkMode darkMode) {
            editor().putInt(KEY_DARK_MODE, darkMode.getValue()).apply();
        }

        /* ----------------------------------- Language ----------------------------------------- */

        public Language getLanguage() {
            return Language.find(preferences.getInt(KEY_LANGUAGE, -1)).orElse(getDefaultLanguage());
        }

        public void setLanguage(@NonNull Language language) {
            editor().putInt(KEY_LANGUAGE, language.getValue()).apply();
        }

    }

    private Localization() {
        //no instance
    }

}
