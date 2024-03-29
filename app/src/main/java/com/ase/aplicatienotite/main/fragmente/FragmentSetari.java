package com.ase.aplicatienotite.main.fragmente;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import com.ase.aplicatienotite.R;

public class FragmentSetari extends PreferenceFragmentCompat{
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
