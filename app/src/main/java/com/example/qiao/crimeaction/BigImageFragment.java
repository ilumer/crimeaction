package com.example.qiao.crimeaction;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * Created by qiao on 2016/2/29.
 */
public class BigImageFragment extends DialogFragment{
    public static final String EXTRA_IMAGE_PATH =
            "com.example.qiao.crimeaction.imagePath";

    public static BigImageFragment newInstance(String imagePath){
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_IMAGE_PATH, imagePath);


        BigImageFragment bigImageFragment = new BigImageFragment();
        bigImageFragment.setArguments(bundle);
        bigImageFragment.setStyle(DialogFragment.STYLE_NO_TITLE,0);
        return bigImageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.imageviewdialog,container,false);
        ImageView imageView = (ImageView) view.findViewById(R.id.Bigimageview);
        String path = (String) getArguments().getSerializable(EXTRA_IMAGE_PATH);
        if (path!=null){
            Log.e("location",path);
            Bitmap temp = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(temp);
            //imageView.setImageAlpha(R.drawable.camera);
        }

        return view;
    }
}

