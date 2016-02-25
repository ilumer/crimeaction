package com.example.qiao.crimeaction;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CrimeFragment extends Fragment{
    private crime mCrime;
    private EditText mTitleFiled;
    private Button mDateButton;
    private CheckBox mSolvedCheckedBox;
    public static final String ExTRA_CRIME_ID =
            "com.example.qiao.crime_id";

    public static final int RESULT_DATE = 0;

    public static String DIALOG_DATE = "date";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   UUID uuid = (UUID) getActivity().getIntent().getSerializableExtra(CrimeFragment.ExTRA_CRIME_ID);
        UUID crimeId = (UUID) getArguments().getSerializable(CrimeFragment.ExTRA_CRIME_ID);
        mCrime = crimelab.getScrimelab(getActivity()).getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    //实现视图层的创建
        View view = inflater.inflate(R.layout.fragment_crime,container,false);
        //false 是否将生成的视图传递给父视图
        mTitleFiled = (EditText) view.findViewById(R.id.Crime_title);
        mDateButton = (Button) view.findViewById(R.id.showTime);
        //mDateButton.setText(GetSuitableDateformat());
        upDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getmDate());
                datePickerFragment.setTargetFragment(CrimeFragment.this,RESULT_DATE);
                datePickerFragment.show(fragmentManager,DIALOG_DATE);
            }
        });
        mSolvedCheckedBox = (CheckBox) view.findViewById(R.id.Issolved);
        //mSolvedCheckedBox.setChecked(mCrime.getmSloved());
        mSolvedCheckedBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSolvedCheckedBox.setChecked(isChecked);
            }
        });
        mTitleFiled.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mCrime.setmTitle(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Todo:
            }
        });
        mTitleFiled.setText(mCrime.getmTitle());

        return view;
    }

    private String GetSuitableDateformat(){
        //String format = "EEEE:MMM:d:yyyy";
        //SimpleDateFormat formats = new SimpleDateFormat(format, Locale.CHINA);
        //return formats.format(mCrime.getmDate());
        return mCrime.getmDate().toString();
    }

    public static Fragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(CrimeFragment.ExTRA_CRIME_ID,crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void returnResult(){
        getActivity().setResult(Activity.RESULT_OK, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode!=Activity.RESULT_OK)
                return;
            if (requestCode==RESULT_DATE){
                Date date = (Date) data
                        .getSerializableExtra(DatePickerFragment.EXTRA_DATE);

                mCrime.setmDate(date);
                /*if (date==null){
                    Log.e("hello","hello");
                }*/
                //mDateButton.setText(date.toString());
                upDate();
            }

    }

    public void upDate(){
        mDateButton.setText(mCrime.getmDate().toString());
    }
}
