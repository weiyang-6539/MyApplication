package com.wyang.mylibrary;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;

/**
 * Created by weiyang on 2019-09-24.
 */
public class TaskLayout extends ConstraintLayout {
    public TaskLayout(Context context) {
        this(context, null);
    }

    public TaskLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TaskLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.task_layout, this);
        initView();
    }

    private ProgressBar mProgressBar;

    private void initView() {
        mProgressBar = findViewById(R.id.mProgressBar);

        LayoutParams params = (LayoutParams) mProgressBar.getLayoutParams();
        params.bottomToBottom = getId();
        params.bottomMargin = dp2px(37);
    }

    public void setMax(int max) {
        mProgressBar.setMax(max);
    }

    public void setProgress(int progress) {
        mProgressBar.setProgress(progress);
    }

    private SparseArray<Space> baseLine = new SparseArray<>();
    private SparseArray<ImageView> iconArr = new SparseArray<>();
    private SparseArray<TextView> moneyArr = new SparseArray<>();
    private SparseArray<TextView> stageArr = new SparseArray<>();

    public void addStage(int stage, int money) {
        float per = stage * 1.0f / mProgressBar.getMax();

        Space space = new Space(getContext());
        space.setId(stage);
        addView(space);
        baseLine.append(stage, space);
        LayoutParams params = (LayoutParams) space.getLayoutParams();
        params.leftToLeft = getId();
        params.rightToRight = getId();
        params.horizontalBias = per;

        //初始化icon
        ImageView icon = new ImageView(getContext());
        icon.setAdjustViewBounds(true);
        icon.setImageResource(R.drawable.icon_normal);
        addView(icon);
        iconArr.append(stage, icon);

        LayoutParams params1 = (LayoutParams) icon.getLayoutParams();
        params1.width = dp2px(15);
        params1.height = -2;
        params1.leftToLeft = space.getId();
        params1.rightToRight = space.getId();
        params1.bottomToBottom = mProgressBar.getId();
        params1.bottomMargin = dp2px(24);

        //初始化金额
        TextView tvMoney = new TextView(getContext());
        tvMoney.setTextColor(0xff666666);
        tvMoney.setTextSize(14);
        tvMoney.setText(String.valueOf(money));
        addView(tvMoney);
        moneyArr.append(stage, tvMoney);

        LayoutParams params2 = (LayoutParams) tvMoney.getLayoutParams();
        params2.leftToLeft = space.getId();
        params2.rightToRight = space.getId();
        params2.bottomToBottom = mProgressBar.getId();
        params2.bottomMargin = dp2px(7);

        //初始化阶段
        TextView tvStage = new TextView(getContext());
        tvStage.setTextColor(0xff999999);
        tvStage.setTextSize(13);
        tvStage.setText(String.valueOf(stage));
        addView(tvStage);
        stageArr.append(stage, tvStage);

        LayoutParams params3 = (LayoutParams) tvStage.getLayoutParams();
        params3.leftToLeft = space.getId();
        params3.rightToRight = space.getId();
        params3.topToTop = mProgressBar.getId();
        params3.topMargin = dp2px(12);

    }

    public void removeStage(int stage) {
        Space space = baseLine.get(stage);
        ImageView icon = iconArr.get(stage);
        TextView tvMoney = moneyArr.get(stage);
        TextView tvStage = stageArr.get(stage);
        if (space != null)
            removeView(space);
        if (icon != null)
            removeView(icon);
        if (tvMoney != null)
            removeView(tvMoney);
        if (tvStage != null)
            removeView(tvStage);
    }


    private int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private int px2dp(float px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
}
