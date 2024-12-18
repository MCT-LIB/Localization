package com.mct.localization;

import android.os.LocaleList;

import androidx.annotation.NonNull;
import androidx.core.os.LocaleListCompat;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public enum Language {
    // @formatter:off
    System  (371625, null), // Unspecified
    af      (109768, "af"), // Afrikaans
    am      (138497, "am"), // Amharic
    ar      (201546, "ar"), // Arabic
    az      (258673, "az"), // Azerbaijani
    be      (307192, "be"), // Belarusian
    bg      (316285, "bg"), // Bulgarian
    bn      (401396, "bn"), // Bengali
    bs      (412593, "bs"), // Bosnian
    ca      (470813, "ca"), // Catalan
    cs      (512094, "cs"), // Czech
    da      (549672, "da"), // Danish
    de      (601837, "de"), // German
    el      (619452, "el"), // Greek
    en      (701583, "en"), // English
    es      (713298, "es"), // Spanish (Spain)
    et      (728564, "et"), // Estonian
    eu      (781493, "eu"), // Basque
    fa      (807265, "fa"), // Farsi
    fi      (819634, "fi"), // Finnish
    fr      (825713, "fr"), // French (France)
    gl      (840167, "gl"), // Galician
    gu      (902356, "gu"), // Gujarati
    hi      (914325, "hi"), // Hindi
    hr      (967215, "hr"), // Croatian
    hu      (109453, "hu"), // Hungarian
    hy      (123598, "hy"), // Armenian
    in      (130946, "in"), // Indonesian
    is      (149572, "is"), // Icelandic
    it      (158634, "it"), // Italian
    iw      (174298, "iw"), // Hebrew
    ja      (182364, "ja"), // Japanese
    ka      (197365, "ka"), // Georgian
    kk      (203948, "kk"), // Kazakh
    km      (219753, "km"), // Khmer
    kn      (231596, "kn"), // Kannada
    ko      (248673, "ko"), // Korean
    ky      (269731, "ky"), // Kyrgyz
    lo      (287314, "lo"), // Lao
    lt      (294756, "lt"), // Lithuanian
    lv      (309472, "lv"), // Latvian
    mk      (318527, "mk"), // Macedonian
    ml      (329456, "ml"), // Malayalam
    mn      (340821, "mn"), // Mongolian
    mr      (365124, "mr"), // Marathi
    ms      (371582, "ms"), // Malay
    my      (381964, "my"), // Burmese
    nb      (397154, "nb"), // Norwegian
    ne      (408573, "ne"), // Nepali
    nl      (429571, "nl"), // Dutch
    or      (438765, "or"), // Odia
    pa      (450137, "pa"), // Punjabi
    pl      (467295, "pl"), // Polish
    pt      (479316, "pt"), // Portuguese
    ro      (509847, "ro"), // Romanian
    ru      (518236, "ru"), // Russian
    si      (534671, "si"), // Sinhala
    sk      (549821, "sk"), // Slovak
    sl      (567412, "sl"), // Slovenian
    sq      (580631, "sq"), // Albanian
    sr      (612934, "sr"), // Serbian
    sv      (629743, "sv"), // Swedish
    sw      (642835, "sw"), // Swahili
    ta      (651784, "ta"), // Tamil
    te      (670425, "te"), // Telugu
    th      (684931, "th"), // Thai
    tl      (701254, "tl"), // Filipino
    tr      (719386, "tr"), // Turkish
    uk      (726415, "uk"), // Ukrainian
    ur      (739586, "ur"), // Urdu
    uz      (751843, "uz"), // Uzbek
    vi      (769412, "vi"), // Vietnamese
    zh      (784236, "zh"), // Chinese
    zu      (790654, "zu"), // Zulu
    ;
    // @formatter:on

    public static Optional<Language> find(int value) {
        return Arrays.stream(values()).filter(o -> o.value == value).findFirst();
    }

    private static final String DIVIDER = "_";
    private final int value;
    private final String language;

    Language(Integer value, String language) {
        this.value = value;
        this.language = language;
    }

    public int getValue() {
        return value;
    }

    @NonNull
    public String getDisplayLanguage() {
        if (language == null) {
            return Utils.getString(Locale.ENGLISH, R.string.localization_system_language);
        }
        Locale locale = getLocale();
        return locale.getDisplayLanguage(Locale.ENGLISH);
    }

    @NonNull
    public String getDisplayLanguageBySelf() {
        if (language == null) {
            return Utils.getString(getLocale(), R.string.localization_system_language);
        }
        Locale locale = getLocale();
        return locale.getDisplayLanguage(locale);
    }

    @NonNull
    public Locale getLocale() {
        try {
            String[] arr = language.split(DIVIDER);
            if (arr.length == 1) {
                return new Locale(arr[0]);
            }
            if (arr.length == 2) {
                return new Locale(arr[0], arr[1]);
            }
        } catch (Throwable ignored) {
        }
        LocaleList localeList = LocaleList.getDefault();
        return !localeList.isEmpty() ? localeList.get(localeList.size() - 1) : Locale.getDefault();
    }

    @NonNull
    public LocaleListCompat getLocaleListCompat() {
        return LocaleListCompat.forLanguageTags(language);
    }

}
