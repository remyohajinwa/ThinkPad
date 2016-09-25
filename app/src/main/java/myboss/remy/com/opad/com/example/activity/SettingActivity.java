package myboss.remy.com.opad.com.example.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import myboss.remy.com.opad.R;

/**
 * Created by Chimere on 9/20/2016.
 */
public class SettingActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
