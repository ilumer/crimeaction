package com.example.qiao.crimeaction;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by qiao on 2016/2/18.
 */

public class crimelab {
    private static crimelab Scrimelab;
    private Context mAppContext;
    private ArrayList<crime> mcrimes;

    public crimelab(Context AppContext) {
        this.mAppContext = AppContext;
        mcrimes = new ArrayList<>();
        for (int i =0;i<100;i++){
            crime c = new crime();
            c.setmTitle("crime"+i);
            c.setmSloved(i%2==0);
            mcrimes.add(c);
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
}