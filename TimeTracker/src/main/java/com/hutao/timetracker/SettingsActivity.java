package com.hutao.timetracker;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import java.util.List;

/**
 * Created by hutao on 14-4-1.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().add(android.R.id.content,new FragmentA()).commit();
    }
}
