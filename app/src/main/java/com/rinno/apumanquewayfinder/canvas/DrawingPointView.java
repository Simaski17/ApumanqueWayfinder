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
import android.util.AttributeSet;
import android.view.View;

import com.rinno.apumanquewayfinder.models.Nodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simaski on 31-01-17.
 */

public class DrawingPointView extends View {//Clase canvas para dibujar punto de inicio con animacion fade-in fade-out
    Path path;
    Paint paint2;
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

        /*Agregamos los X e Y por donde va a ser dibujada la ruta*/
        for(int i =0; i < puntos.size(); i++){
            coordx.add((float) puntos.get(i).getLocationX());
            coordy.add((float) puntos.get(i).getLocationY());
        }

        /*
        * Si contador es igual a 0 se dibuja en el punto de inicio de la ruta optima.
        *
        * Si no se dibuja el punto desde la segunda escalera encontrada en la ruta.
        * */
        if(cont == 0) {
            contador  = cont;
                path.moveTo(coordx.get(cont), coordy.get(cont));
        }else{
              contador = cont +1;
                path.moveTo(coordx.get(cont+1), coordy.get(cont+1));
        }

        /*ObjectAnimator fade-in fade-out con tiempo de 0.6 segundos */
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(this, "alpha",  1f, .3f);
        fadeOut.setDuration(600);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(this, "alpha", .3f, 1f);
        fadeIn.setDuration(600);

        mAnimationSet.end();
        mAnimationSet.play(fadeIn).after(fadeOut);

        /*Inicia la animacion y la realiza de manera constante*/
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
    }
}
