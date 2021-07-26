package com.example.hamiltontevin_ce06;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements CounterTask.OnFinished {

    private Button start_btn;
    private  Button stop_btn;
    private EditText mMins;
    private EditText mSecs;
    private CounterTask counterTask = null;
    private final String zero = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start_btn = findViewById(R.id.btn_Start);
        stop_btn = findViewById(R.id.btn_stop);
        mMins = findViewById(R.id.et_mins);
        mSecs = findViewById(R.id.et_sec);


        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counterTask = new CounterTask(MainActivity.this);
                if(mSecs.getText().toString().matches("") && mMins.getText().toString().matches("")) {
                    String msg = getString(R.string.noneValidTime);
                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                }else if( mSecs.getText().toString().contains("0") && mMins.getText().toString().contains("0")){
                    String msg =getString(R.string.noneValidTime);
                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                } else{

                    if( mMins.getText().toString().matches("")){
                        counterTask.execute(zero,mSecs.getText().toString());
                    }else if(mSecs.getText().toString().matches("")){
                        counterTask.execute(mMins.getText().toString(),zero);
                    } else{
                        counterTask.execute(mMins.getText().toString(),mSecs.getText().toString());
                    }

                    Toast.makeText(MainActivity.this,R.string.timerStarted, Toast.LENGTH_SHORT).show();

                    start_btn.setEnabled(false);
                    stop_btn.setEnabled(true);
                }
            }
        });
        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counterTask.cancel(true);
                mSecs.setText(R.string.defultTime);
                mMins.setText(R.string.defultTime);
                start_btn.setEnabled(true);
                stop_btn.setEnabled(false);
                mMins.clearFocus();
                mSecs.clearFocus();
            }
        });
    }


    @Override
    public void onPre() {
        Toast.makeText(this, R.string.onPreExecute, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPost(Float aFloat) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.timerStopped);
        builder.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
        mMins.setText(R.string.defultTime);
        mSecs.setText(R.string.defultTime);
        start_btn.setEnabled(true);
        stop_btn.setEnabled(false);
    }

    @Override
    public void onPro(Integer... values) {
        int time = values[0];
        int mins = time / 60;
        int secs = time % 60;
        String minString = Integer.toString(mins);
        String secString = Integer.toString(secs);

        if(time > 60){
            mMins.setText(minString);
            mSecs.setText(secString);
        }
        else {
            mMins.setText(R.string.defultTime);
            mSecs.setText(secString);
        }
    }

    @Override
    public void onCan(Float aFloat) {

        float fNum = aFloat;
        int numInt = (int)fNum;
        int mins = 0;
        int secs;

        if (numInt >= 60) {
            mins = numInt / 60;
            secs = numInt % 60;
        }else{
            secs = numInt % 60;
        }
        String minString =Integer.toString(mins);
        String secString = Integer.toString(secs);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.timerStopped);
        builder.setMessage(getString(R.string.timeElapsed,minString,secString));
        builder.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

}
