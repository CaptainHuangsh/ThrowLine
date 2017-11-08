package com.captainhuang.throwline.Views;

import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.captainhuang.throwline.R;
import com.captainhuang.throwline.utils.ParabolaAlgorithm;
import com.captainhuang.throwline.utils.Util;

/**
 * Created by huangshaohua on 2017/11/7.
 */

public class TwoPointThrowView extends FrameLayout {

    private static final String TAG = TwoPointThrowView.class.getName();
    private ViewGroup viewGroup;
    private int count = 500;
    private float a = 0f, b = 0f, c = 0f;
    Keyframe[] keyframes;
    private Context mContext;
    private String flowName;


    public TwoPointThrowView(Context context) {
        this(context, null);
    }

    public TwoPointThrowView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TwoPointThrowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        viewGroup = createAnimLayout();
        count = (int) (Util.getScreenWidth(mContext) - 2 * Util.dip2px(mContext, 25));
        keyframes = new Keyframe[count];
        Point sPoint = new Point(0, Util.getScreenHeight(mContext) / 2);
        Point ePoint = new Point((int) (Util.getScreenWidth(mContext) - 2 * Util.dip2px(mContext, 25)), Util.getScreenHeight(mContext) / 2 + 50);
        float[][] points = {{sPoint.x, sPoint.y}, {ePoint.x, ePoint.y}};
        a = (float) 6.825007E-4;
        float[] bc = ParabolaAlgorithm.calculate(a, points);
        b = bc[0];
        c = bc[1];
        Log.i("abcis: ", a + " " + b + " " + c);
    }

    @SuppressLint("NewApi")
    public void startThrow(String flowName, int[] startLocation, int[] endLocation) {

        int viewSize = (int) Util.dip2px(mContext,25);
        final float keyStep = 1f / (float) count;
        float key = keyStep;
        View imageView = new View(mContext);
        imageView.setBackground(mContext.getResources().getDrawable(R.drawable.game_gift_shoes));
        for (int i = 0; i < count; i++) {
            keyframes[i] = Keyframe.ofFloat(key, i + 1);
            Log.i("keyframesX" + i, "" + keyframes[i]);
            key += keyStep;
        }
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofKeyframe("translationX", keyframes);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(viewSize, viewSize);
        imageView.setLayoutParams(layoutParams);
        viewGroup.addView(imageView);// 把要移动的控件添加到动画层
        key = keyStep;
        for (int i = 0; i < count; i++) {
            Log.i("keyframesY" + i, "" + keyframes[i]);
            keyframes[i] = Keyframe.ofFloat(key, getY(i + 1));
            key += keyStep;
        }
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofKeyframe("translationY", keyframes);
        ObjectAnimator anim = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        ObjectAnimator yxThrow = ObjectAnimator.ofPropertyValuesHolder(imageView, pvhX, pvhY);
        yxThrow.setInterpolator(new OvershootInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(2000);
        animatorSet.play(yxThrow).with(anim);
        animatorSet.start();
    }

    /**
     * 创建动画层
     *
     * @return
     */
    private ViewGroup createAnimLayout() {
        RelativeLayout animLayout = new RelativeLayout(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);
        this.addView(animLayout);
        return animLayout;
    }


    private float getY(int i) {
        return (float) (a * i * i + b * i + c);
    }

}
