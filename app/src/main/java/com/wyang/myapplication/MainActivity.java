package com.wyang.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.wyang.mylibrary.TaskLayout;

/**
 * Created by weiyang on 2019-09-24.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TaskLayout mTaskLayout = findViewById(R.id.mTaskLayout);

        mTaskLayout.setProgress(3);
        mTaskLayout.setMax(10);


        mTaskLayout.addStage(3, 88);
        mTaskLayout.addStage(6, 188);
        mTaskLayout.addStage(8, 888);
    }
}
