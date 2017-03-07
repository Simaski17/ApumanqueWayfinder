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
 * Created by simaski on 02-03-17.
 */

public class DrawingPointEndView extends View { //Clase canvas para dibujar punto final en la linea.
    Path path;
    Paint paint2;
    float length;
    private int xx = 0;
    private int yy = 0;

    public DrawingPointEndView(Context context)
    {
        super(context);
    }

    public DrawingPointEndView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public DrawingPointEndView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }


    public void init(int[] rectDib)
    {
        /*Se reciben el X e Y donde se dibujara el punto final*/
        xx =  rectDib[4];
        yy =  rectDib[5];


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
        c.drawCircle(xx, yy, 10, paint2);
    }
}
