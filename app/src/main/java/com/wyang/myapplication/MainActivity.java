package com.wyang.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.wyang.mylibrary.TaskLayout;

/**
 * Created by weiyang on 2019-09-24.
 */
public class MainActivity extends AppCompatActivity {
    private TaskLayout mTaskLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTaskLayout = findViewById(R.id.mTaskLayout);
        mTaskLayout.setOnStageListener(new TaskLayout.OnStageListener() {
            @Override
            public void setIcon(ImageView iv, int state) {
                if (state == TaskLayout.NORMAL) {
                    iv.setImageResource(R.drawable.icon_normal);
                } else if (state == TaskLayout.ACTIVE) {
                    Glide.with(MainActivity.this)
                            .load(R.drawable.icon_active)
                            .into(iv);
                } else if (state == TaskLayout.RECEIVED) {
                    iv.setImageResource(R.drawable.icon_received);
                }
            }

            @Override
            public void onClick(final int stage) {
                //调用接口领取
                Toast.makeText(MainActivity.this, "领取中..", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "领取成功!", Toast.LENGTH_SHORT).show();
                        mTaskLayout.setReceived(stage);
                        mTaskLayout.setProgress(stage + 3);
                    }
                }, 3000);
            }
        });

        mTaskLayout.setProgress(3);
        mTaskLayout.setMax(24);

        mTaskLayout.addStage(3, 88, false);
        mTaskLayout.addStage(6, 188, false);
        mTaskLayout.addStage(9, 388, false);
        mTaskLayout.addStage(12, 588, false);
        mTaskLayout.addStage(15, 588, false);
        mTaskLayout.addStage(18, 588, false);
        mTaskLayout.addStage(21, 588, false);
    }
}
