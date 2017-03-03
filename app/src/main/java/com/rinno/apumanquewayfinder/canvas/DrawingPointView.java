package com.rinno.apumanquewayfinder.canvas;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import com.rinno.apumanquewayfinder.models.Nodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simaski on 31-01-17.
 */

public class DrawingPointView extends View {
    Path path;
    Paint paint2;
    float length;
    int contador;

    public ArrayList<Float> coordx = new ArrayList<Float>();
    public ArrayList<Float> coordy = new ArrayList<Float>();
    final AnimatorSet mAnimationSet = new AnimatorSet();

    public DrawingPointView(Context context)
    {
        super(context);
    }

    public DrawingPointView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public DrawingPointView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }


    public void init(List<Nodes> puntos, int cont)
    {

        coordx = new ArrayList<>();
        coordy = new ArrayList<>();

        paint2 = new Paint();
        paint2.setColor(Color.MAGENTA);

        path = new Path();


        for(int i =0; i < puntos.size(); i++){
            coordx.add((float) puntos.get(i).getLocationX());
            coordy.add((float) puntos.get(i).getLocationY());
        }

        if(cont == 0) {
            contador  = cont;
                path.moveTo(coordx.get(cont), coordy.get(cont));
        }else{
              contador = cont +1;
                path.moveTo(coordx.get(cont+1), coordy.get(cont+1));
        }

        PathMeasure measure = new PathMeasure(path, false);
        length = measure.getLength();

        float[] intervals = new float[]{length, length};



        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(this, "alpha",  1f, .3f);
        fadeOut.setDuration(600);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(this, "alpha", .3f, 1f);
        fadeIn.setDuration(600);

        mAnimationSet.end();
        mAnimationSet.play(fadeIn).after(fadeOut);

        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimationSet.start();
            }
        });
        mAnimationSet.start();

    }

    @Override
    public void onDraw(Canvas c)
    {
        super.onDraw(c);
        c.drawPath(path, paint2);
        c.drawCircle(coordx.get(contador), coordy.get(contador), 20, paint2);
        //c.drawCircle(965, 783, 20, paint2);
    }
}
