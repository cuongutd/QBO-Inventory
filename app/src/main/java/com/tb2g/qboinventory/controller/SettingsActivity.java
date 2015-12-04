package com.tb2g.qboinventory.controller;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tb2g.qboinventory.R;
import com.tb2g.qboinventory.util.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class SettingsActivity extends AppCompatPreferenceActivity {

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            preference.setSummary(stringValue);
            return true;
        }
    };

    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        setContentView(R.layout.pref_button);
        addPreferencesFromResource(R.xml.pref_general);

        bindPreferenceSummaryToValue(findPreference("SHARED_PREF_CONSUMMER_KEY"));
        bindPreferenceSummaryToValue(findPreference("SHARED_PREF_CONSUMMER_SECRET"));
        bindPreferenceSummaryToValue(findPreference("SHARED_PREF_ACCESS_TOKEN"));
        bindPreferenceSummaryToValue(findPreference("SHARED_PREF_ACCESS_TOKEN_SECRET"));
        bindPreferenceSummaryToValue(findPreference("SHARED_PREF_UPCDB_KEY"));
        bindPreferenceSummaryToValue(findPreference("SHARED_PREF_QBO_COMPANY_ID"));
        bindPreferenceSummaryToValue(findPreference("SHARED_PREF_SAMPLE_ITEM_NAME"));

        Button button = (Button)findViewById(R.id.prefbutton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readSettingFromFile(v);
            }
        });

    }

    private void readSettingFromFile(View v){

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(v.getContext());
        SharedPreferences.Editor editor = sharedPref.edit();

        File sdcard = Environment.getExternalStorageDirectory();
        //Get the text file
        File file = new File(sdcard,getString(R.string.qbo_key_filename));

        try {

            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;

            while ((line = br.readLine()) != null) {
                Toast.makeText(this, line.split(":")[0], Toast.LENGTH_SHORT).show();
                Toast.makeText(this, line.split(":")[1], Toast.LENGTH_SHORT).show();
                if ("consumerKey".equals(line.split(":")[0]))
                    editor.putString(Constants.SHARED_PREF_CONSUMMER_KEY, line.split(":")[1]);
                else if("consumerSecret".equals(line.split(":")[0]))
                    editor.putString(Constants.SHARED_PREF_CONSUMMER_SECRET, line.split(":")[1]);
                else if("accessToken".equals(line.split(":")[0]))
                    editor.putString(Constants.SHARED_PREF_ACCESS_TOKEN, line.split(":")[1]);
                else if("accessTokenSecret".equals(line.split(":")[0]))
                    editor.putString(Constants.SHARED_PREF_ACCESS_TOKEN_SECRET, line.split(":")[1]);
                else if("upcDatabaseKey".equals(line.split(":")[0]))
                    editor.putString(Constants.SHARED_PREF_UPCDB_KEY, line.split(":")[1]);
                else if("QBOCompanyId".equals(line.split(":")[0]))
                    editor.putString(Constants.SHARED_PREF_QBO_COMPANY_ID, line.split(":")[1]);
                else if("sampleItemName".equals(line.split(":")[0]))
                    editor.putString(Constants.SHARED_PREF_SAMPLE_ITEM_NAME, line.split(":")[1]);
            }
            br.close();
            editor.commit();
            Toast.makeText(this, R.string.setting_load_completed_msg, Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

    }


    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        //loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    public static class GeneralPreferenceFragment extends PreferenceFragment {
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            addPreferencesFromResource(R.xml.pref_general);
//            setHasOptionsMenu(true);
//
//            bindPreferenceSummaryToValue(findPreference("SHARED_PREF_CONSUMMER_KEY"));
//            bindPreferenceSummaryToValue(findPreference("SHARED_PREF_CONSUMMER_SECRET"));
//            bindPreferenceSummaryToValue(findPreference("SHARED_PREF_ACCESS_TOKEN"));
//            bindPreferenceSummaryToValue(findPreference("SHARED_PREF_ACCESS_TOKEN_SECRET"));
//            bindPreferenceSummaryToValue(findPreference("SHARED_PREF_UPCDB_KEY"));
//            bindPreferenceSummaryToValue(findPreference("SHARED_PREF_QBO_COMPANY_ID"));
//            bindPreferenceSummaryToValue(findPreference("SHARED_PREF_SAMPLE_ITEM_NAME"));
//        }
//
//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//            int id = item.getItemId();
//            if (id == android.R.id.home) {
//                startActivity(new Intent(getActivity(), SettingsActivity.class));
//                return true;
//            }
//            return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
