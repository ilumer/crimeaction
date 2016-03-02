package com.example.qiao.crimeaction;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrimeFragment extends Fragment{
    private crime mCrime;
    private EditText mTitleFiled;
    private Button mDateButton;
    private CheckBox mSolvedCheckedBox;
    private ImageView mimageview;
    private Button mSerachSuspect;
    private String PhoneNumber = null;

    public static final String ExTRA_CRIME_ID =
            "com.example.qiao.crime_id";

    public static final int REQUEST_DATE = 0;

    public static final int REQUEST_PHOTO = 1;

    public static final int REQUEST_CONTACT = 2;

    public static String DIALOG_DATE = "date";

    public static final String DIALOG_IMAGE = "image";

    public static String TAG =
            "com.example.qiao.crimeaction.CrimeFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   UUID uuid = (UUID) getActivity().getIntent().getSerializableExtra(CrimeFragment.ExTRA_CRIME_ID);
        //UUID crimeId = (UUID) getArguments().getSerializable(CrimeFragment.ExTRA_CRIME_ID);
        //mCrime = crimelab.getScrimelab(getActivity()).getCrime(crimeId);
        mCrime = (crime) getArguments().getSerializable(CrimeFragment.ExTRA_CRIME_ID);
        setHasOptionsMenu(true);
        getActivity().setResult(CrimeListFragment.RESULT_CRIME);
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
                    //检查是否有应用可以响应隐式intent

                    File photofile = createFile();
                    if (photofile != null) {
                        mCrime.setImageLocation(photofile.toString());
                        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photofile));
                    }
                    startActivityForResult(i, REQUEST_PHOTO);
                }
            }
        });
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
        mSolvedCheckedBox.setChecked(mCrime.getmSloved());
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
                if (v == view.findViewById(R.id.ImageviewShow)) {
                    if (mCrime.getMimageLocation() == null) {
                        return;
                    } else {
                        FragmentManager fragmentManager = getActivity().getFragmentManager();
                        String path = mCrime.getMimageLocation();
                        BigImageFragment.newInstance(path)
                                .show(fragmentManager, DIALOG_IMAGE);
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

        mSerachSuspect = (Button) view.findViewById(R.id.serach_suspect);
        final Button mSendText = (Button) view.findViewById(R.id.sendtext);
        mSendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                if (i.resolveActivity(getActivity()
                .getPackageManager())!=null) {
                    i = Intent.createChooser(i, getString(R.string.crime_report));
                    startActivity(i);
                }
            }
        });
        mSerachSuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                if (i.resolveActivity(getActivity().getPackageManager())!=null)
                    startActivityForResult(i,REQUEST_CONTACT);
            }
        });
        if (mCrime.getSuspect()!=null){
            mSendText.setText(mCrime.getSuspect());
        }

        Button mCallButton = (Button) view.findViewById(R.id.callButton);
        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                if (PhoneNumber!=null) {
                    Uri uri = Uri.parse("tel:"+PhoneNumber);
                    if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                        i.setData(uri);
                        startActivity(i);
                    }
                } else {
                    Toast.makeText(getActivity(),"please choose suspect",Toast.LENGTH_SHORT).show();
                }
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    public static Fragment newInstance(crime c){
        Bundle args = new Bundle();
        args.putSerializable(CrimeFragment.ExTRA_CRIME_ID,c);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /*public void returnResult(){
        getActivity().setResult(Activity.RESULT_OK, null);
    }*/

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
            } else if (requestCode == REQUEST_CONTACT){
                Uri uri = data.getData();
                Log.e(TAG, data.toString());
                //String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor cursor = getActivity().getContentResolver()
                        .query(uri, null, null, null, null);
                cursor.moveToFirst();
                String suspect = cursor.getString(cursor.
                        getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                PhoneNumber = cursor.getString(cursor.
                        getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                mCrime.setSuspect(suspect);
                mSerachSuspect.setText(suspect);
                cursor.close();
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
                if (mCrime.getMimageLocation()!=null)
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
        mCrime.setmSloved(mSolvedCheckedBox.isChecked());
        mCrime.setmDate(new Date(mDateButton.getText().toString()));
        mCrime.setmTitle(mTitleFiled.toString());
        mCrime.setImageLocation(createFile().toString());
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

    private String getCrimeReport(){
        String solvedString = null;
        if (mCrime.getmSloved()){
            solvedString = getString(R.string.crime_report_solved);
        }else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE,MMM dd";
        String dateString = DateFormat.format(dateFormat,mCrime.getmDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null){
            suspect = getString(R.string.crime_report_no_suspect);
        }else {
            suspect = getString(R.string.crime_report_suspect,suspect);
        }

        String report = getString(R.string.crime_report,
                mCrime.getmTitle(),dateString,solvedString
        ,suspect);
        return report;
    }
}
