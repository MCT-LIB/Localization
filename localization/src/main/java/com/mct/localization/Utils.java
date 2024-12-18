package com.mct.localization;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatDelegate.NightMode;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

abstract class Utils {

    static Resources.Theme createTheme(@NonNull Context context, boolean isLight) {
        Configuration cf = new Configuration(context.getResources().getConfiguration());
        int uiNightMode = isLight ? Configuration.UI_MODE_NIGHT_NO : Configuration.UI_MODE_NIGHT_YES;
        cf.uiMode = (cf.uiMode & ~Configuration.UI_MODE_NIGHT_MASK) | uiNightMode;

        return context.createConfigurationContext(cf).getResources().newTheme();
    }

    static boolean isContextConfigurationLight(@NonNull Context context) {
        return getNightMode(context.getResources()) == AppCompatDelegate.MODE_NIGHT_NO;
    }

    @NightMode
    static int getSystemNightMode() {
        return getNightMode(Resources.getSystem());
    }

    @NightMode
    static int getNightMode(@NonNull Resources resources) {
        Configuration cf = resources.getConfiguration();
        return (cf.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
                ? AppCompatDelegate.MODE_NIGHT_YES
                : AppCompatDelegate.MODE_NIGHT_NO;
    }

    @NonNull
    static String getString(@NonNull Locale local, @StringRes int resId) {
        Configuration configuration = new Configuration(ApplicationHolder.getApplication().getResources().getConfiguration());
        configuration.setLocale(local);
        Context context = ApplicationHolder.getApplication().createConfigurationContext(configuration);
        return context.getString(resId);
    }


    static Enum<?> findEnumByValue(Class<Enum<?>> clazz, int value, Enum<?> fallback) {
        Enum<?>[] values = clazz.getEnumConstants();
        if (values == null) {
            return fallback;
        }
        try {
            Field valueField = getEnumValueField(clazz);
            for (Enum<?> valueEnum : values) {
                Integer valueInt = (Integer) valueField.get(valueEnum);
                if (Objects.equals(valueInt, value)) {
                    return valueEnum;
                }
            }
        } catch (IllegalAccessException | NoSuchFieldException ignored) {

        }
        return fallback;
    }

    static void checkValueDuplicationEnums(Class<Enum<?>> clazz) {
        // we check this only in debug mode
        if (!isDebug()) {
            return;
        }
        Enum<?>[] values = clazz.getEnumConstants();
        if (values == null) {
            return;
        }
        try {
            Field valueField = getEnumValueField(clazz);
            Set<Integer> valuesSet = new HashSet<>();
            for (Enum<?> value : values) {
                Integer valueInt = (Integer) valueField.get(value);
                if (valuesSet.add(valueInt)) {
                    continue;
                }
                throw new IllegalStateException("Enum " + clazz.getSimpleName() + " has duplicated value " + valueInt);
            }
        } catch (IllegalAccessException | NoSuchFieldException ignored) {
        }
    }

    private static final Map<Class<?>, Field> enumFields = new HashMap<>();

    @NonNull
    private static Field getEnumValueField(Class<?> clazz) throws NoSuchFieldException {
        Field field = enumFields.get(clazz);
        if (field != null) {
            return field;
        }
        field = clazz.getDeclaredField("value");
        field.setAccessible(true);
        enumFields.put(clazz, field);
        return field;
    }

    private static Boolean sIsDebug;

    private static boolean isDebug() {
        if (sIsDebug == null) {
            try {
                sIsDebug = (ApplicationHolder.getApplication().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            } catch (Exception e) {
                sIsDebug = true;
            }
        }
        return sIsDebug;
    }

    private Utils() {
        //no instance
    }
}
