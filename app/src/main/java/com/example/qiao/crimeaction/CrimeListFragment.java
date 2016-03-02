package com.example.qiao.crimeaction;

import android.annotation.TargetApi;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by qiao on 2016/2/18.
 */

public class CrimeListFragment extends ListFragment{
    private ArrayList<crime> mCrimes;
    private static final String TAG = "CrimeListFragment";
    private boolean msubtitle ;
    private ListView myListview;
    private CrimeAdapter myCrimeAdapter;
    private HashMap<Integer,Boolean> mStorageState = new HashMap<>();

    public static final int RESULT_CRIME = 1;

    public static final String EXTRA_CRIME_MAP =
            "com.example.qiao.crime_map";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.crimes_title);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        msubtitle = false;
        if (savedInstanceState!=null){
            mStorageState = (HashMap<Integer,Boolean>) savedInstanceState.get(EXTRA_CRIME_MAP);
        }
        //当接收到createMenu的回调方法使用
        mCrimes=crimelab.getScrimelab(getActivity()).getListCrimes();

        //setListAdapter(adapter);
        }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        crime c = (crime) myListview.getAdapter().getItem(position);
        //Log.e(TAG,c.getmTitle()+"was clicked");
        //start CrimePagerActivity
        Log.e(TAG,c.getmSloved()+"what");
        if (mStorageState.get(position)!=null) {
            c.setmSloved(mStorageState.get(position));
            crimelab.getScrimelab(getActivity()).getListCrimes()
                    .get(position).setmSloved(mStorageState.get(position));
        }
        Intent i = new Intent(getActivity(),CrimePagerActivity.class);
        i.putExtra(CrimeFragment.ExTRA_CRIME_ID, c);
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
            final int temp = position;
            Log.e(TAG,c.toString());
            IsSolvedCheckBox.setChecked(c.getmSloved());
            IsSolvedCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()){
                        mStorageState.put(temp,true);
                    }else {
                        mStorageState.put(temp,false);
                    }
                }
            });
            return convertView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
        for (int postion:mStorageState.keySet()){
            if (mStorageState.get(postion)!=mCrimes.get(postion).getmSloved()){
                if (mStorageState.get(postion)!=null)
                    mCrimes.get(postion).setmSloved(mStorageState.get(postion));
            }
        }
        ((CrimeAdapter) myListview.getAdapter()).notifyDataSetChanged();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==RESULT_CRIME){
            myCrimeAdapter.notifyDataSetChanged();
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
                i.putExtra(CrimeFragment.ExTRA_CRIME_ID, c);
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
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.my_list_view,null);
        myListview = (ListView) parent.findViewById(android.R.id.list);
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB) {
            registerForContextMenu(myListview);
            //现在还有3.0的手机吗？
        }  else{
            myListview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            myListview.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    //Called when action mode is first created.
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.crime_list_item_context,menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    //Called to report a user click on an action button
                    switch (item.getItemId()){
                        case R.id.menu_item_delete_crime:
                            CrimeAdapter adapter = (CrimeAdapter) myListview.getAdapter();
                            crimelab tempcrimelab = crimelab.getScrimelab(getActivity());
                            for (int i = adapter.getCount()-1;i>=0;i--){
                                if (myListview.isItemChecked(i)){
                                    tempcrimelab.deleteCrime(adapter.getItem(i));
                                }
                            }
                            mode.finish();
                            adapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }
        TextView textView = (TextView) parent.findViewById(R.id.show_empty);
        Button mButton = (Button) parent.findViewById(R.id.add_a_new_crime);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crime c = new crime();
                crimelab.getScrimelab(getActivity()).addCrime(c);
                Intent i = new Intent(getActivity(),CrimePagerActivity.class);
                i.putExtra(CrimeFragment.ExTRA_CRIME_ID, c);
                startActivityForResult(i, 0);
            }
        });
        myCrimeAdapter = new CrimeAdapter(mCrimes);
        myListview.setAdapter(myCrimeAdapter);
        myListview.setEmptyView(textView);
        if (msubtitle==true){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.subtitle);
        }
        return parent;
    }

    //这个是为了兼容性而做的
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
        //生成一个上下文菜单
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                item.getMenuInfo();
        int Position = info.position;
        //get item position
        CrimeAdapter adapter = (CrimeAdapter) myListview.getAdapter();
        crime c = adapter.getItem(Position);
        //get item
        switch (item.getItemId()){
            case R.id.menu_item_delete_crime:
                crimelab.getScrimelab(getActivity()).deleteCrime(c);
                adapter.notifyDataSetChanged();
                return true;
            //从model和view中删除crime
        }
        return super.onContextItemSelected(item);
        //响应上下文选择
    }

    @Override
    public void onPause() {
        super.onPause();
        crimelab.getScrimelab(getActivity()).savecrimes();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_CRIME_MAP,mStorageState);
    }
}
