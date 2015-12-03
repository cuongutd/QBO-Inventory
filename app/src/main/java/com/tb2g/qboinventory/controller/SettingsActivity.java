package com.tb2g.qboinventory.controller;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
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

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            preference.setSummary(stringValue);
            return true;
        }
    };

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
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
                readSettingFromFile();
            }
        });

    }

    private void readSettingFromFile(){

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();

        File sdcard = Environment.getExternalStorageDirectory();
        //Get the text file
        File file = new File(sdcard,"qbo.txt");

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
            Toast.makeText(this, "Load setting completed!", Toast.LENGTH_LONG).show();
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
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);
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
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            bindPreferenceSummaryToValue(findPreference("SHARED_PREF_CONSUMMER_KEY"));
            bindPreferenceSummaryToValue(findPreference("SHARED_PREF_CONSUMMER_SECRET"));
            bindPreferenceSummaryToValue(findPreference("SHARED_PREF_ACCESS_TOKEN"));
            bindPreferenceSummaryToValue(findPreference("SHARED_PREF_ACCESS_TOKEN_SECRET"));
            bindPreferenceSummaryToValue(findPreference("SHARED_PREF_UPCDB_KEY"));
            bindPreferenceSummaryToValue(findPreference("SHARED_PREF_QBO_COMPANY_ID"));
            bindPreferenceSummaryToValue(findPreference("SHARED_PREF_SAMPLE_ITEM_NAME"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

}
