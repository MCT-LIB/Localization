package com.mct.localization;

import java.util.Optional;

/**
 * Use:
 * <pre>
 *  public class App extends Application {
 *
 *      static {
 *          LocalizationConfig.setDefaultTheme(Theme.LightBlue);
 *          LocalizationConfig.setDefaultDarkMode(DarkMode.Auto);
 *          LocalizationConfig.setDefaultLanguage(Language.System);
 *      }
 *  }
 * </pre>
 */
public class LocalizationConfig {

    static final Theme DEFAULT_THEME = Theme.LightBlue;
    static final DarkMode DEFAULT_DARK_MODE = DarkMode.Auto;
    static final Language DEFAULT_LANGUAGE = Language.System;

    private static Theme defaultTheme = DEFAULT_THEME;
    private static DarkMode defaultDarkMode = DEFAULT_DARK_MODE;
    private static Language defaultLanguage = DEFAULT_LANGUAGE;

    public static Theme getDefaultTheme() {
        return defaultTheme;
    }

    public static void setDefaultTheme(Theme theme) {
        defaultTheme = Optional.ofNullable(theme).orElse(DEFAULT_THEME);
    }

    public static DarkMode getDefaultDarkMode() {
        return defaultDarkMode;
    }

    public static void setDefaultDarkMode(DarkMode darkMode) {
        defaultDarkMode = Optional.ofNullable(darkMode).orElse(DEFAULT_DARK_MODE);
    }

    public static Language getDefaultLanguage() {
        return defaultLanguage;
    }

    public static void setDefaultLanguage(Language language) {
        defaultLanguage = Optional.ofNullable(language).orElse(DEFAULT_LANGUAGE);
    }

    private LocalizationConfig() {
        //no instance
    }
}
