package com.rinno.apumanquewayfinder.canvas;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.rinno.apumanquewayfinder.models.Message;
import com.rinno.apumanquewayfinder.models.Nodes;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simaski on 31-01-17.
 */

public class DrawingView extends View {
    Path path;
    Paint paint;
    Paint paint2;
    float length;
    ObjectAnimator animator;

    public ArrayList<Float> coordx = new ArrayList<Float>();
    public ArrayList<Float> coordy = new ArrayList<Float>();
    ArrayList arreglotemporal = new ArrayList();
    ArrayList arreglosegmentado = new ArrayList();
    ArrayList arreglorecorrido = new ArrayList();
    int contador;


    public DrawingView(Context context)
    {
        super(context);
    }

    public DrawingView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }


    public void init(List<Nodes> puntos, ArrayList arregloRuta, ArrayList arregloStair, int cont)
    {
        arreglosegmentado.clear();
        coordx = new ArrayList<>();
        coordy = new ArrayList<>();

        animator = ObjectAnimator.ofFloat(this, "phase", 1.0f, 0.0f);
        animator.setDuration(5000);

        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        paint2 = new Paint();
        paint2.setColor(Color.BLUE);

        path = new Path();

        for(int i =0; i < puntos.size(); i++){
            coordx.add((float) puntos.get(i).getLocationX());
            coordy.add((float) puntos.get(i).getLocationY());
        }


        if(arregloStair.size() > 0) {
            segmentarRuta(arregloRuta, arregloStair);
        }else{
                for(int i =0; i < arregloRuta.size(); i++){
                    arreglotemporal.add(arregloRuta.get(i));
                }
            arreglosegmentado.add(arreglotemporal);
            arreglotemporal = new ArrayList();
        }

        Log.e("TAG","ArregloTemporal: "+arreglosegmentado);
        //Log.e("TAG","CONTADOR: "+cont);

        if(cont == 0 && arregloStair.size() == 0) {
            contador  = cont;
            for (int i = 0; i < arregloRuta.size(); i++) {
                arreglorecorrido = (ArrayList) arreglosegmentado.get(0);
                path.moveTo(coordx.get(cont), coordy.get(cont));
                for (int k = cont + 1; k < arreglorecorrido.size(); k++) {
                    path.lineTo(coordx.get(k), coordy.get(k));
                }
            }
        }else if(cont == 0) {
            contador  = cont;
            for (int i = 0; i < arregloRuta.size(); i++) {
                arreglorecorrido = (ArrayList) arreglosegmentado.get(0);
                path.moveTo(coordx.get(cont), coordy.get(cont));
                for (int k = cont + 1; k < arreglorecorrido.size(); k++) {
                    path.lineTo(coordx.get(k), coordy.get(k));
                    cont = k;
                }
            }
        }else{
            contador = cont +1;
            for (int i = 0; i < arregloRuta.size(); i++) {
                arreglorecorrido = (ArrayList) arreglosegmentado.get(1);
                path.moveTo(coordx.get(cont+1), coordy.get(cont+1));
                for (int k = 1; k < arreglorecorrido.size(); k++) {
                    int suma = cont + k;
                    path.lineTo(coordx.get(cont+k +1), coordy.get(cont+k+1));
                }
            }
        }

        // Measure the path
        PathMeasure measure = new PathMeasure(path, false);
        length = measure.getLength();

        float[] intervals = new float[]{length, length};

        animator.start();

        final int finalCont = cont;
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //Log.e("TAG","POS FINAL X: "+coordx.get(finalCont));
                Toast.makeText(getContext(), "Final", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().postSticky(new Message(finalCont));
                //Log.e("TAG", "FINAL CONT: " + finalCont);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    public void segmentarRuta(ArrayList arregloRuta, ArrayList arregloStair)
    {
        for(int i =0; i < arregloRuta.size(); i++){
            int j  = 0;
            arreglotemporal.add(arregloRuta.get(i));
            if(j <= arregloStair.size()) {
                if (i == (int) arregloStair.get(j) || i == arregloRuta.size() - 1) {
                    arreglosegmentado.add(arreglotemporal);
                    arreglotemporal = new ArrayList();
                }
            }
            j++;
        }
    }

    //is called by animtor object
    public void setPhase(float phase)
    {
        //Log.d("pathview","setPhase called with:" + String.valueOf(phase));
        paint.setPathEffect(createPathEffect(length, phase, 0.0f));
        invalidate();//will calll onDraw

    }

    private PathEffect createPathEffect(float pathLength, float phase, float offset)
    {
        return new DashPathEffect(new float[] {
                pathLength, pathLength
        },
                Math.max(phase * pathLength, offset));
    }

    @Override
    public void onDraw(Canvas c)
    {
        super.onDraw(c);
        c.drawPath(path, paint);
        c.drawCircle(coordx.get(contador), coordy.get(contador), 10, paint2);
    }

}