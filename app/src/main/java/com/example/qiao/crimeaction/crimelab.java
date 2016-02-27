package com.example.qiao.crimeaction;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by qiao on 2016/2/18.
 */

public class crimelab {
    private static crimelab Scrimelab;
    private Context mAppContext;
    private ArrayList<crime> mcrimes;
    private static final String FILENAME = "crimes.json";
    private static final String TAG = "crimelab";
    private CrimeJSONserializer crimeJSONserializer;

    public crimelab(Context AppContext) {
        this.mAppContext = AppContext;
        crimeJSONserializer = new CrimeJSONserializer(mAppContext,FILENAME);

        String filelocation = AppContext.getFilesDir().toString();
        try{
            mcrimes = crimeJSONserializer.loadcrimes();
        }catch (Exception e){
            mcrimes = new ArrayList<>();
            Log.e(TAG,e.getMessage());
        }

    }

    public static crimelab getScrimelab(Context s){
        if (Scrimelab==null){
            Scrimelab = new crimelab(s.getApplicationContext());
        }
        return Scrimelab;
    }

    public ArrayList<crime> getListCrimes(){
        return mcrimes;
    }

    public crime getCrime(UUID id){
        for (crime c:mcrimes){
            if (c.getmId().equals(id)){
                return c;
            }
        }
        return null;
    }

    public void addCrime(crime c){
        mcrimes.add(c);
    }

    public boolean savecrimes(){
        try{
            crimeJSONserializer.saveCrimes(getListCrimes());
            Log.d(TAG,"crimes saved to file");
            return true;
        }catch (IOException e){
            Log.e(TAG,e.getMessage());
            return false;
        }
        //保存当前的数据
    }

    public void deleteCrime(crime c){
        mcrimes.remove(c);
    }
}
