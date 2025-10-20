package com.dicoding.courseschedule.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var DARK_MODE: String
    private lateinit var NOTIFICATION: String
    private lateinit var themePreference: ListPreference
    private lateinit var notificationPreference: SwitchPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        //TODO 10 : Update theme based on value in ListPreference (DONE)
        init()
        themePreference.setOnPreferenceChangeListener { _, newValue ->
            when (newValue) {
                "off" -> updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
                "on" -> updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
                "auto" -> updateTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                else -> false
            }
        }
        //TODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference (DONE)
        notificationPreference.setOnPreferenceChangeListener { _, newValue ->
            val isEnabled = newValue as Boolean
            val dailyReminder = DailyReminder()
            if (isEnabled) {
                dailyReminder.setDailyReminder(requireContext())
            } else {
                dailyReminder.cancelAlarm(requireContext())
            }
            true
        }
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        themePreference.value = when (nightMode) {
            AppCompatDelegate.MODE_NIGHT_NO -> "off"
            AppCompatDelegate.MODE_NIGHT_YES -> "on"
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> "auto"
            else -> "auto"
        }
        requireActivity().recreate()
        return true
    }

    private fun init(){
        DARK_MODE = resources.getString(R.string.pref_key_dark)
        NOTIFICATION = resources.getString(R.string.pref_key_notify)

        themePreference = findPreference<ListPreference>(DARK_MODE) as ListPreference
        notificationPreference = findPreference<SwitchPreference>(NOTIFICATION) as SwitchPreference
    }
}