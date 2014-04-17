package com.hutao.timetracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * Created by hutao on 2014-4-3.
 */
public class FragmentA extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private ListPreference listPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        SharedPreferences preferences;

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pre_scr);
        listPreference = (ListPreference)findPreference("color");
        preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        preferences.registerOnSharedPreferenceChangeListener(this);
        listPreference.setSummary(preferences.getString("color","White"));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences,String key) {
        listPreference.setSummary(preferences.getString(key,"White"));

    }

}
