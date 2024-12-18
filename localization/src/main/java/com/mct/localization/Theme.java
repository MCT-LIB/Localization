package com.mct.localization;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;

import java.util.Arrays;
import java.util.Optional;

public enum Theme {

    // @formatter:off
    Red         (548932,    R.style.M3Theme_Red ,        R.string.localization_theme_red),
    Pink        (173845,    R.style.M3Theme_Pink,        R.string.localization_theme_pink),
    Purple      (786219,    R.style.M3Theme_Purple,      R.string.localization_theme_purple),
    DeepPurple  (320514,    R.style.M3Theme_DeepPurple,  R.string.localization_theme_deep_purple),
    Indigo      (628772,    R.style.M3Theme_Indigo,      R.string.localization_theme_indigo),
    Blue        (455887,    R.style.M3Theme_Blue,        R.string.localization_theme_blue),
    LightBlue   (912367,    R.style.M3Theme_LightBlue,   R.string.localization_theme_light_blue),
    Cyan        (206491,    R.style.M3Theme_Cyan,        R.string.localization_theme_cyan),
    Teal        (841123,    R.style.M3Theme_Teal,        R.string.localization_theme_teal),
    Green       (739258,    R.style.M3Theme_Green,       R.string.localization_theme_green),
    LightGreen  (587996,    R.style.M3Theme_LightGreen,  R.string.localization_theme_light_green),
    Lime        (384689,    R.style.M3Theme_Lime,        R.string.localization_theme_lime),
    Yellow      (971502,    R.style.M3Theme_Yellow,      R.string.localization_theme_yellow),
    Amber       (693410,    R.style.M3Theme_Amber,       R.string.localization_theme_amber),
    Orange      (210753,    R.style.M3Theme_Orange,      R.string.localization_theme_orange),
    DeepOrange  (865481,    R.style.M3Theme_DeepOrange,  R.string.localization_theme_deep_orange);
    // @formatter:on

    public static Optional<Theme> find(int value) {
        return Arrays.stream(values()).filter(t -> t.value == value).findFirst();
    }

    private final int value;
    private final int style;
    private final int name;

    Theme(Integer value, @StyleRes int style, @StringRes int name) {
        this.value = value;
        this.style = style;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public int getStyle() {
        return style;
    }

    public int getName() {
        return name;
    }

    public int getColor(@NonNull Context context, int attr) {
        return getColor(context, attr, Utils.isContextConfigurationLight(context));
    }

    public int getColor(@NonNull Context context, int attr, boolean isLight) {
        return retrieveColor(context, style, attr, isLight);
    }

    @NonNull
    public int[] getColors(@NonNull Context context, int[] attrs) {
        return getColors(context, attrs, Utils.isContextConfigurationLight(context));
    }

    @NonNull
    public int[] getColors(@NonNull Context context, int[] attrs, boolean isLight) {
        return retrieveColors(context, style, attrs, isLight);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Theme Utils
    ///////////////////////////////////////////////////////////////////////////

    // cache field for theme
    private static volatile Resources.Theme sThemeLight;
    private static volatile Resources.Theme sThemeDark;

    private static int retrieveColor(@NonNull Context context, int style, int attr, boolean isLight) {
        createThemeIfNeed(context);
        int color = -1;
        try {
            Resources.Theme theme = isLight ? sThemeLight : sThemeDark;
            TypedValue tv = new TypedValue();
            theme.applyStyle(style, true);
            theme.resolveAttribute(attr, tv, true);
            color = tv.data;
        } catch (Exception ignored) {
        }
        return color;
    }

    @NonNull
    private static int[] retrieveColors(@NonNull Context context, int style, int[] attrs, boolean isLight) {
        if (attrs == null || attrs.length == 0) {
            return new int[0];
        }
        createThemeIfNeed(context);
        int[] colors = new int[attrs.length];
        TypedArray ta = null;
        try {
            Resources.Theme theme = isLight ? sThemeLight : sThemeDark;
            theme.applyStyle(style, true);
            ta = theme.obtainStyledAttributes(attrs);
            for (int i = 0; i < attrs.length; i++) {
                colors[i] = ta.getColor(i, -1);
            }
        } catch (Exception ignored) {
        } finally {
            if (ta != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ta.close();
                } else {
                    ta.recycle();
                }
            }
        }
        return colors;
    }

    private static void createThemeIfNeed(@NonNull Context context) {
        if (sThemeLight == null) {
            sThemeLight = Utils.createTheme(context, true);
        }
        if (sThemeDark == null) {
            sThemeDark = Utils.createTheme(context, false);
        }
    }
}
