package com.example.qiao.crimeaction;

import android.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(CrimeFragment.ExTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }
}
