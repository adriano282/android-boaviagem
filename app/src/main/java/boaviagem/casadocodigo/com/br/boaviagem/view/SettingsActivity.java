package boaviagem.casadocodigo.com.br.boaviagem.view;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import boaviagem.casadocodigo.com.br.boaviagem.R;


public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
