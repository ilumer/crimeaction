package com.example.qiao.crimeaction;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiao on 2016/2/18.
 */

public class CrimeListFragment extends ListFragment{
    private ArrayList<crime> mCrimes;
    private static final String TAG = "CrimeListFragment";
    private static final int RESULT_CRIME = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.crimes_title);
        mCrimes=crimelab.getScrimelab(getActivity()).getListCrimes();
        //ArrayAdapter<crime> adapter = new ArrayAdapter<crime>(getActivity(),R.layout.support_simple_spinner_dropdown_item,mCrimes);
        CrimeAdapter adapter = new CrimeAdapter(mCrimes);
        setListAdapter(adapter);
        }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        crime c = (crime) getListAdapter().getItem(position);
        //Log.e(TAG,c.getmTitle()+"was clicked");
        //start CrimePagerActivity
        Intent i = new Intent(getActivity(),CrimePagerActivity.class);
        i.putExtra(CrimeFragment.ExTRA_CRIME_ID, c.getmId());
        startActivityForResult(i, RESULT_CRIME);
    }

    private class CrimeAdapter extends ArrayAdapter<crime>{
        public CrimeAdapter(List<crime> objects) {
            super(getActivity(), 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_view_crime,null);
            }
            crime c = getItem(position);
            TextView titleTextView = (TextView) convertView.findViewById(R.id.list_item_title);
            titleTextView.setText(c.getmTitle());
            TextView dateTextView = (TextView) convertView.findViewById(R.id.list_item_date);
            dateTextView.setText(c.getmDate().toString());
            CheckBox IsSolvedCheckBox =
                    (CheckBox) convertView.findViewById(R.id.list_item_Issolved);
            IsSolvedCheckBox.setChecked(c.getmSloved());
            return convertView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==RESULT_CRIME){

        }
    }
}
