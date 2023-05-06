package com.dashfitness.app.ui.preferences

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.dashfitness.app.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val caloriesPreference = findPreference<SwitchPreferenceCompat>("estimate_calories")
        caloriesPreference?.setOnPreferenceClickListener {
            showHidePreferences()
            true
        }
        val metricPreference = findPreference<SwitchPreferenceCompat>("metric")
        metricPreference?.setOnPreferenceClickListener {
            showHidePreferences()
            true
        }
        showHidePreferences()
    }

    private fun showHidePreferences() {
        val feetHeightPreference = findPreference<EditTextPreference>("height_feet")
        val inchesHeightPreference = findPreference<EditTextPreference>("height_inches")
        val cmHeightPreference = findPreference<EditTextPreference>("height_cm")
        val weightLbsPreference = findPreference<EditTextPreference>("weight_lbs")
        val weightKiloPreference = findPreference<EditTextPreference>("weight_kilo")
        val agePreference = findPreference<EditTextPreference>("age")
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        if (!preferences.getBoolean("estimate_calories", false)) {
            feetHeightPreference?.isVisible = false
            inchesHeightPreference?.isVisible = false
            cmHeightPreference?.isVisible = false
            weightLbsPreference?.isVisible = false
            weightKiloPreference?.isVisible = false
            agePreference?.isVisible = false
        } else {
            if (preferences.getBoolean("metric", false)) {
                cmHeightPreference?.isVisible = true
                weightKiloPreference?.isVisible = true
                feetHeightPreference?.isVisible = false
                inchesHeightPreference?.isVisible = false
                cmHeightPreference?.isVisible = false
                weightLbsPreference?.isVisible = false
            } else {
                feetHeightPreference?.isVisible = true
                inchesHeightPreference?.isVisible = true
                cmHeightPreference?.isVisible = true
                weightLbsPreference?.isVisible = true
                cmHeightPreference?.isVisible = false
                weightKiloPreference?.isVisible = false
            }
            agePreference?.isVisible = true
        }
    }
}