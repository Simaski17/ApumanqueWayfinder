package com.rinno.apumanquewayfinder.canvas;

import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by simaski on 01-03-17.
 */

public class DrawingRectView extends View {
    Path path;
    Paint paint;
    Paint paint2;
    float length;
    int contador;
    private int Xi = 0;
    private int Yi = 0;
    private int Xf = 0;
    private int Yf = 0;
    private int xx = 0;
    private int yy = 0;

    final AnimatorSet mAnimationSet = new AnimatorSet();

    public DrawingRectView(Context context)
    {
        super(context);
    }

    public DrawingRectView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public DrawingRectView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }


    public void init(int[] rectDib)
    {
        Xi =   rectDib[0];
        Yi =   rectDib[1];
        Xf =  rectDib[2];
        Yf =  rectDib[3];
        xx =  rectDib[4];
        yy =  rectDib[5];
        paint2 = new Paint();
        paint2.setColor(Color.BLUE);
        //paint2.setStrokeWidth(3);

        path = new Path();


        PathMeasure measure = new PathMeasure(path, false);
        length = measure.getLength();

        //float[] intervals = new float[]{length, length};

    }

    @Override
    public void onDraw(Canvas c)
    {
        super.onDraw(c);
        c.drawPath(path, paint2);
        //c.drawCircle(xx, yy, 10, paint2);
        c.drawRect(Xi, Yi, Xf, Yf,paint2);
        //c.drawRect(903,658,963,708,paint2);
    }
}
