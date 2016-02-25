package com.example.qiao.crimeaction;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private boolean msubtitle ;
    private static final int RESULT_CRIME = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.crimes_title);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        msubtitle = false;
        //当接收到createMenu的回调方法使用
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_list_crime,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                crime c = new crime();
                crimelab.getScrimelab(getActivity()).addCrime(c);
                Intent i = new Intent(getActivity(),CrimePagerActivity.class);
                i.putExtra(CrimeFragment.ExTRA_CRIME_ID, c.getmId());
                startActivityForResult(i, 0);
                break;

            case R.id.menu_title_show_subtitle:
                ActionBar actionBar= ((AppCompatActivity) getActivity()).getSupportActionBar();
                if (actionBar.getSubtitle()==null){
                    actionBar.setSubtitle(R.string.subtitle);
                    msubtitle=true;
                    item.setTitle(R.string.hide_subtitle);
                }else {
                    actionBar.setSubtitle(null);
                    msubtitle=false;
                    item.setTitle(R.string.show_subtitle);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (msubtitle==true){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.subtitle);
        }

        return view;
    }
}
