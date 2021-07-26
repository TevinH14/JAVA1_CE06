package com.example.hamiltontevin_ce06;

import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class CounterTask extends AsyncTask<String,Integer,Float> {

    //tag for log
    static  final private String TAG = "funday";

    // sleep on async task
    static final private long SLEEP = 200;


    final private OnFinished mFinishedInterface;

    // interface
    interface OnFinished {
        void onPre();
        void onPost(Float aFloat);
        void onPro(Integer... values);
        void onCan(Float aFloat);
    }

    //C-tor
    public CounterTask(OnFinished _finished){
        mFinishedInterface = _finished;
    }


    @Override
    protected void onPreExecute() {
        mFinishedInterface.onPre();
    }


    @Override
    protected Float doInBackground(String... strings) {

        if(strings == null || strings.length <= 0 || strings[0].trim().isEmpty()){
            return 0.0f;
        }
        long startTime = System.currentTimeMillis();
        int mins = Integer.parseInt(strings[0]);
        int sec = Integer.parseInt(strings[1]);

        long minutes = TimeUnit.MINUTES.toMillis(mins);
        long seconds = TimeUnit.SECONDS.toMillis(sec);

        long endTime = startTime + minutes + seconds;

        long currentTime = System.currentTimeMillis();

        // block until count is satisfied
        while(startTime <= endTime&& !isCancelled()){


            // suspend the thread for a period of time
            try{
                Thread.sleep(SLEEP);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            publishProgress((int) ((endTime - startTime)/ 1000));
            Log.i(TAG,"current sec " +((endTime - startTime)/ 1000));
            startTime = System.currentTimeMillis();

        }
        // Return real elapsed time
        return (System.currentTimeMillis() - currentTime) / 1000.0f;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        mFinishedInterface.onPro(values);
    }

    @Override
    protected void onCancelled(Float aFloat) {
        mFinishedInterface.onCan(aFloat);
    }

    @Override
    protected void onPostExecute(Float aFloat) {
        mFinishedInterface.onPost(aFloat);
    }
}
