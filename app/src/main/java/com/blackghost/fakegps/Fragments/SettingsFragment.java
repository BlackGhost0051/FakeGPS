package com.blackghost.fakegps.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
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
}
