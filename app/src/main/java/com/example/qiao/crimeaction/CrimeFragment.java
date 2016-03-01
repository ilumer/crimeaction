package com.example.qiao.crimeaction;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment{
    private crime mCrime;
    private EditText mTitleFiled;
    private Button mDateButton;
    private CheckBox mSolvedCheckedBox;
    private ImageView mimageview;

    public static final String ExTRA_CRIME_ID =
            "com.example.qiao.crime_id";

    public static final int REQUEST_DATE = 0;

    public static final int REQUEST_PHOTO = 1;

    public static String DIALOG_DATE = "date";

    public static final String DIALOG_IMAGE = "image";

    public static String TAG =
            "com.example.qiao.crimeaction.CrimeFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   UUID uuid = (UUID) getActivity().getIntent().getSerializableExtra(CrimeFragment.ExTRA_CRIME_ID);
        UUID crimeId = (UUID) getArguments().getSerializable(CrimeFragment.ExTRA_CRIME_ID);
        mCrime = crimelab.getScrimelab(getActivity()).getCrime(crimeId);
        setHasOptionsMenu(true);
    }

    @TargetApi(11)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    //实现视图层的创建
        final View view = inflater.inflate(R.layout.fragment_crime,container,false);
        //false 是否将生成的视图传递给父视图
        mTitleFiled = (EditText) view.findViewById(R.id.Crime_title);
        mDateButton = (Button) view.findViewById(R.id.showTime);
        ImageButton mimageButton = (ImageButton) view.findViewById(R.id.Imagebutton);
        mimageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getActivity()
                        .getPackageManager()) != null) {

                    File photofile = createFile();
                    if (photofile != null) {
                        mCrime.setImageLocation(photofile.toString());
                        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photofile));
                    }
                    startActivityForResult(i, REQUEST_PHOTO);
                }
            }
        });
        //mDateButton.setText(GetSuitableDateformat());
        upDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getmDate());
                datePickerFragment.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
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

        mimageview = (ImageView) view.findViewById(R.id.ImageviewShow);
        if (mCrime.getMimageLocation()!=null){
            Bitmap image = pictureUtils.decodeSampledBitmapFromFile
                    (mCrime.getMimageLocation(),100,100);
            mimageview.setImageBitmap(image);
        }
        mimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v== view.findViewById(R.id.ImageviewShow)){
                    if (mCrime.getMimageLocation()==null){
                        return;
                    }else {
                        FragmentManager fragmentManager = getActivity().getFragmentManager();
                        String path = mCrime.getMimageLocation();
                        BigImageFragment.newInstance(path)
                                .show(fragmentManager,DIALOG_IMAGE);
                    }
                }
            }
        });
        mimageview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                getActivity().startActionMode(LongerimageCallback);
                return true;
            }
        });
        setHasOptionsMenu(true);
        return view;
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
            if (requestCode==REQUEST_DATE){
                Date date = (Date) data
                        .getSerializableExtra(DatePickerFragment.EXTRA_DATE);

                mCrime.setmDate(date);
                /*if (date==null){
                    Log.e("hello","hello");
                }*/
                //mDateButton.setText(date.toString());
                upDate();
            } else if (requestCode==REQUEST_PHOTO){
                File imagefile = new File(mCrime.getMimageLocation());
                Bitmap image = pictureUtils.decodeSampledBitmapFromFile
                        (imagefile.toString(), 100, 100);
                mimageview.setImageBitmap(image);
            }

    }

    public void upDate(){
        mDateButton.setText(mCrime.getmDate().toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                crimelab.getScrimelab(getActivity()).deleteCrime(mCrime);
                new File(mCrime.getMimageLocation()).delete();
                NavUtils.navigateUpFromSameTask(getActivity());
                //startActivity(new Intent(getActivity(),CrimePagerActivity.class));
                //startActivity(new Intent(getActivity(),CrimeListActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        crimelab.getScrimelab(getActivity()).savecrimes();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.setting, menu);
    }

    private File createFile(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = getActivity().getExternalFilesDir(null);

        File image =null;
        try{
            image = File.createTempFile(imageFileName,".jpg",storageDir);
        }catch (IOException e){
            Log.e(TAG,e.getMessage());
        }
        return image;
    }

    private Bitmap getBitmapwithFile(File file){
        Bitmap temp = null;
        if (file.exists()) {
            temp = BitmapFactory.decodeFile(file.getAbsolutePath());
        }
        return temp;
    }

    ActionMode.Callback LongerimageCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
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
            switch (item.getItemId()){
                case R.id.menu_item_delete_crime:
                    mimageview.setImageResource(android.R.color.transparent);
                    new File(mCrime.getMimageLocation()).delete();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };
}
