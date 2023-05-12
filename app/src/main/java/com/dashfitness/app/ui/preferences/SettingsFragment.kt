package com.dashfitness.app.ui.preferences

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.dashfitness.app.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val caloriesPreference = findPreference<SwitchPreferenceCompat>(getString(R.string.estimate_calories_preference))
        caloriesPreference?.setOnPreferenceClickListener {
            showHidePreferences()
            true
        }
        val metricPreference = findPreference<SwitchPreferenceCompat>(getString(R.string.metric_preference))
        metricPreference?.setOnPreferenceClickListener {
            showHidePreferences()
            true
        }
        showHidePreferences()
    }

    private fun showHidePreferences() {
        val feetHeightPreference = findPreference<EditTextPreference>(getString(R.string.height_feet_preference))
        val inchesHeightPreference = findPreference<EditTextPreference>(getString(R.string.height_inches_preference))
        val cmHeightPreference = findPreference<EditTextPreference>(getString(R.string.height_cm_preference))
        val weightLbsPreference = findPreference<EditTextPreference>(getString(R.string.weight_lbs_preference))
        val weightKiloPreference = findPreference<EditTextPreference>(getString(R.string.weight_kilo_preference))
        val agePreference = findPreference<EditTextPreference>(getString(R.string.age_preference))
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        if (!preferences.getBoolean(getString(R.string.estimate_calories_preference), false)) {
            feetHeightPreference?.isVisible = false
            inchesHeightPreference?.isVisible = false
            cmHeightPreference?.isVisible = false
            weightLbsPreference?.isVisible = false
            weightKiloPreference?.isVisible = false
            agePreference?.isVisible = false
        } else {
            if (preferences.getBoolean(getString(R.string.metric_preference), false)) {
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