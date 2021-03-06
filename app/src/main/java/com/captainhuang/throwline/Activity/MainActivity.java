package com.captainhuang.throwline.Activity;

import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.captainhuang.throwline.R;
import com.captainhuang.throwline.Views.TwoPointThrowView;
import com.captainhuang.throwline.utils.ParabolaAlgorithm;
import com.captainhuang.throwline.utils.Util;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button btn;
    private TwoPointThrowView tThrowView;
    private EditText editText;
    private int count = 500;
    private float a = 0f, b = 0f, c = 0f;
    Keyframe[] keyframes;
    int[] endLocation = new int[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initView() {
        count = (int) (Util.getScreenWidth(this) - 2 * Util.dip2px(this, 25));
        keyframes = new Keyframe[count];
        textView = findViewById(R.id.tv_1);
        editText = findViewById(R.id.end_point);
        btn = findViewById(R.id.anim_start);
        tThrowView = new TwoPointThrowView(this);
        tThrowView = findViewById(R.id.t_throw);
        Point sPoint = new Point(0, Util.getScreenHeight(this) / 2);
        Point ePoint = new Point((int) (Util.getScreenWidth(this) - 2 * Util.dip2px(this, 25)), Util.getScreenHeight(this) / 2 + 50);
        Point hPoint = new Point(Util.getScreenWidth(this) / 2, sPoint.y < ePoint.y ? sPoint.y : ePoint.y - 150);
//        float[][] points = {{sPoint.x, sPoint.y}, {hPoint.x, hPoint.y}, {ePoint.x, ePoint.y}};
        float[][] points = {{sPoint.x, sPoint.y}, {ePoint.x, ePoint.y}};
        /*float[] abc = ParabolaAlgorithm.calculate(points);
        a = abc[0];
        b = abc[1];
        c = abc[2];*/

        a = (float) 6.825007E-4;
        float[] bc = ParabolaAlgorithm.calculate(a, points);
        b = bc[0];
        c = bc[1];
        Log.i("abcis: ", a + " " + b + " " + c);
    }


    private float getY(int i) {
        return a * i * i + b * i + c;
    }

    private void initListener() {
        btn.setOnClickListener(v -> {
                String[] str = editText.getText().toString().split(",");
                endLocation[0] = Integer.parseInt(str[0]);
                endLocation[1] = Integer.parseInt(str[1]);
//            startThrow();
            tThrowView.startThrow(this.getResources().getDrawable(R.drawable.game_gift_heart), new int[]{0, 0}, endLocation);
        });
    }

    private void startThrow() {

        final float keyStep = 1f / (float) count;
        float key = keyStep;

        for (int i = 0; i < count; i++) {
            keyframes[i] = Keyframe.ofFloat(key, i + 1);
            Log.i("keyframesX" + i, "" + keyframes[i]);
            key += keyStep;
        }
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofKeyframe("translationX", keyframes);

        key = keyStep;
        for (int i = 0; i < count; i++) {
            Log.i("keyframesY" + i, "" + keyframes[i]);
            keyframes[i] = Keyframe.ofFloat(key, getY(i + 1));
            key += keyStep;
        }
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofKeyframe("translationY", keyframes);
        ObjectAnimator anim = ObjectAnimator.ofFloat(textView, "rotation", 0f, 360f);
        ObjectAnimator yxThrow = ObjectAnimator.ofPropertyValuesHolder(textView, pvhX, pvhY);
        yxThrow.setInterpolator(new OvershootInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(2000);
        animatorSet.play(yxThrow).with(anim);
        animatorSet.start();

    }

}
