package com.example.qiao.crimeaction;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.util.ArrayList;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private ArrayList<crime> mCrimes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        mCrimes = crimelab.getScrimelab(this).getListCrimes();

        FragmentManager fragmentManager = getFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                crime mcrime = mCrimes.get(position);
                return CrimeFragment.newInstance(mcrime.getmId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        //setOnpagechangedlistener 应该去除了
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                crime c = mCrimes.get(position);
                if (c.getmTitle()!=null)
                   setTitle(c.getmTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        UUID uuid = (UUID)getIntent().getSerializableExtra(CrimeFragment.ExTRA_CRIME_ID);
        for (int i =0;i<mCrimes.size();i++){
            if (mCrimes.get(i).getmId()==uuid){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
