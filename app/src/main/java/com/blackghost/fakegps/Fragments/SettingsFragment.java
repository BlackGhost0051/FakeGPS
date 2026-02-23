package com.blackghost.fakegps.Fragments;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.blackghost.fakegps.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getContext().getTheme().applyStyle(R.style.preferences_style, true);

        ListPreference languagePref = findPreference("language");
        if (languagePref != null) {
            languagePref.setOnPreferenceChangeListener((preference, newValue) -> {
                String languageCode = (String) newValue;
                setAppLocale(languageCode);
                return true;
            });
        }

        SwitchPreference darkThemePref = findPreference("dark_theme");
        if (darkThemePref != null) {
            darkThemePref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isDark = (boolean) newValue;
                AppCompatDelegate.setDefaultNightMode(
                        isDark ? AppCompatDelegate.MODE_NIGHT_YES
                               : AppCompatDelegate.MODE_NIGHT_NO
                );
                return true;
            });
        }
    }

    private void setAppLocale(String languageCode) {
        LocaleListCompat appLocale;

        if ("system".equals(languageCode)) {
            appLocale = LocaleListCompat.getEmptyLocaleList();
        } else {
            appLocale = LocaleListCompat.forLanguageTags(languageCode);
        }

        AppCompatDelegate.setApplicationLocales(appLocale);
    }
}
