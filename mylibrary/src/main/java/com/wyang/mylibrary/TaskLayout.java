package com.wyang.mylibrary;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
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
        params.leftToLeft = getId();
        params.rightToRight = getId();
        params.bottomToBottom = getId();
        params.bottomMargin = dp2px(37);
        //修改这里处理图标或文件被截取的问题
        params.leftMargin = dp2px(10);
        params.rightMargin = dp2px(10);
    }

    public void setMax(int max) {
        mProgressBar.setMax(max);
    }

    public void setProgress(int progress) {
        mProgressBar.setProgress(progress);
        for (int i = 0; i < iconArr.size(); i++) {
            int stage = iconArr.keyAt(i);
            ImageView icon = iconArr.valueAt(i);
            int state = states.get(stage);

            if (stage > progress)
                continue;

            if (state == RECEIVED)
                continue;

            states.put(stage, ACTIVE);

            if (onStageListener != null) {
                onStageListener.setIcon(icon, ACTIVE);
            }
        }
    }

    private SparseArray<Space> baseLine = new SparseArray<>();
    private SparseArray<ImageView> iconArr = new SparseArray<>();
    private SparseArray<TextView> moneyArr = new SparseArray<>();
    private SparseArray<TextView> stageArr = new SparseArray<>();
    //状态记录
    private SparseIntArray states = new SparseIntArray();

    public void addStage(final int stage, int money, boolean isReceive) {
        //保存状态
        states.put(stage, isReceive ? RECEIVED : NORMAL);

        float per = stage * 1.0f / mProgressBar.getMax();

        Space space = new Space(getContext());
        space.setId(View.generateViewId());
        addView(space);
        baseLine.append(stage, space);
        LayoutParams params = (LayoutParams) space.getLayoutParams();
        if (per == 0) {
            params.leftToLeft = mProgressBar.getId();
            params.rightToLeft = mProgressBar.getId();
            params.horizontalBias = 0;
        } else {
            params.leftToLeft = mProgressBar.getId();
            params.rightToRight = mProgressBar.getId();
            params.horizontalBias = per;
        }
        space.setLayoutParams(params);

        //初始化icon
        ImageView icon = new ImageView(getContext());
        icon.setAdjustViewBounds(true);
        icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = states.get(stage);
                //已领取或未到领取状态
                if (state == RECEIVED || stage > mProgressBar.getProgress())
                    return;

                if (onStageListener != null)
                    onStageListener.onClick(stage);
            }
        });
        if (onStageListener != null) {
            if (stage > mProgressBar.getProgress()) {
                //未到领取状态
                onStageListener.setIcon(icon, NORMAL);
            } else if (!isReceive) {
                //领取状态
                onStageListener.setIcon(icon, ACTIVE);
            } else {
                //已领取状态不显示gif
                onStageListener.setIcon(icon, RECEIVED);
            }
        }
        addView(icon);
        iconArr.append(stage, icon);

        LayoutParams params1 = (LayoutParams) icon.getLayoutParams();
        params1.width = dp2px(15);
        params1.height = -2;
        params1.leftToLeft = space.getId();
        params1.rightToRight = space.getId();
        params1.bottomToBottom = mProgressBar.getId();
        params1.bottomMargin = dp2px(24);
        icon.setLayoutParams(params1);

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
        tvMoney.setLayoutParams(params2);

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
        tvStage.setLayoutParams(params3);

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

    public void setReceived(int stage) {
        states.put(stage, RECEIVED);
        if (onStageListener != null)
            onStageListener.setIcon(iconArr.get(stage), RECEIVED);
    }

    private int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private int px2dp(float px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    private OnStageListener onStageListener;

    public void setOnStageListener(OnStageListener onStageListener) {
        this.onStageListener = onStageListener;
    }

    public interface OnStageListener {
        void setIcon(ImageView iv, @State int state);

        void onClick(int stage);
    }

    public final static int NORMAL = 1;
    public final static int ACTIVE = 2;
    public final static int RECEIVED = 3;

    @IntDef({NORMAL, ACTIVE, RECEIVED})
    public @interface State {
    }
}
