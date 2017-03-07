package com.rinno.apumanquewayfinder.canvas;

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

public class DrawingRectView extends View {//Clase canvas para dibujar rectangulo tienda.
    Path path;
    Paint paint2;
    float length;
    private int Xi = 0;
    private int Yi = 0;
    private int Xf = 0;
    private int Yf = 0;

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
        /*Se reciben el X inicio y X final mas el Y inicio y el Y final donde se dibujara el rectangulo de la tienda*/
        Xi =   rectDib[0];
        Yi =   rectDib[1];
        Xf =  rectDib[2];
        Yf =  rectDib[3];

        paint2 = new Paint();
        paint2.setColor(Color.BLUE);

        path = new Path();


        PathMeasure measure = new PathMeasure(path, false);
        length = measure.getLength();

    }

    @Override
    public void onDraw(Canvas c)
    {
        super.onDraw(c);
        c.drawPath(path, paint2);
        c.drawRect(Xi, Yi, Xf, Yf,paint2);
    }
}
