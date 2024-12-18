package com.mct.localization;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import android.content.res.Configuration;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDelegate.NightMode;

import java.util.Arrays;
import java.util.Optional;

public enum DarkMode {

    // @formatter:off
    Auto    (742861,    MODE_NIGHT_FOLLOW_SYSTEM,   R.string.localization_mode_follow_system),
    Light   (528440,    MODE_NIGHT_NO,              R.string.localization_mode_light),
    Dark    (189737,    MODE_NIGHT_YES,             R.string.localization_mode_dark);
    // @formatter:on

    public static Optional<DarkMode> find(int value) {
        return Arrays.stream(values()).filter(o -> o.value == value).findFirst();
    }

    private final int value;
    private final int nightMode;
    private final int titleRes;

    DarkMode(Integer value, @NightMode int nightMode, @StringRes int titleRes) {
        this.value = value;
        this.nightMode = nightMode;
        this.titleRes = titleRes;
    }

    public int getValue() {
        return value;
    }

    @StringRes
    public int getTitleRes() {
        return titleRes;
    }

    @NightMode
    public int getNightMode() {
        return nightMode;
    }

    @NightMode
    public int getRealNightMode() {
        return this == DarkMode.Auto ? Utils.getSystemNightMode() : nightMode;
    }

    public boolean isLight() {
        return getRealNightMode() == MODE_NIGHT_NO;
    }

    public int getUiNightMode() {
        return isLight() ? Configuration.UI_MODE_NIGHT_NO : Configuration.UI_MODE_NIGHT_YES;
    }

}
