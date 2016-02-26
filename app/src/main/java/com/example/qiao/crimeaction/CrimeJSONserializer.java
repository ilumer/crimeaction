package com.example.qiao.crimeaction;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by qiao on 2016/2/26.
 */
public class CrimeJSONserializer {
    private Context mcontext;
    private String mFilename;
    final static String TAG = "CrimeJsonserializer";

    public CrimeJSONserializer(Context context, String mFilename) {
        this.mcontext = context;
        this.mFilename = mFilename;
    }

    public void saveCrimes(ArrayList<crime> crimes)
                            throws IOException {
        JSONArray array = new JSONArray();
        for (crime c : crimes){
            try{
                array.put(c.toJSON());
            }catch (JSONException e){
                Log.e(TAG,e.getMessage());
            }
        }
        //将arraylist添加到array中

        //将文件写入手机中
        Writer writer = null;
        try {
            OutputStream out = mcontext.openFileOutput(mFilename, mcontext.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        }finally {
            if (writer!=null)
                writer.close();
        }
    }

    public ArrayList<crime> loadcrimes() throws IOException,JSONException{
        ArrayList<crime> crimes = new ArrayList<>();
        BufferedReader reader  = null;
        try{
            InputStream inputStream = mcontext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String Line =null;
            while ((Line=reader.readLine())!=null){
                jsonString.append(Line);
            }
            JSONArray jsonArray = (JSONArray) new JSONTokener(jsonString.toString())
                    .nextValue();

            for (int i = 0; i<jsonArray.length();i++){
                crimes.add(new crime(jsonArray.getJSONObject(i)));
            }
        }catch (FileNotFoundException e){
            Log.e("what","what");
        }finally {
            if (reader!=null){
                reader.close();
            }
        }
        return crimes;
    }
}
