package com.landsoft.jsonsearchgooglemap.Preference;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;

import com.landsoft.jsonsearchgooglemap.MainActivity;
import com.landsoft.jsonsearchgooglemap.R;

import static com.landsoft.jsonsearchgooglemap.Constants.Contant.RESULT_CODE;
import static com.landsoft.jsonsearchgooglemap.Constants.Contant.SETTING_PREFERENCE;

/**
 * Created by TRANTUAN on 01-Dec-17.
 */

public class SettingPreference extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Intent intent = new Intent();
        intent.putExtra(SETTING_PREFERENCE,true);
        setResult(RESULT_CODE,intent);
        finish();
    }
}
