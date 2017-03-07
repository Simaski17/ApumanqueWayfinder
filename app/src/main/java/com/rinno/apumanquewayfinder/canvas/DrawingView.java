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

public class DrawingView extends View { //Clase canvas para dibujar linea y punto de inicio. Se realiza animacion de linea.
    Path path;
    Paint paint;
    Paint paint2;
    float length;
    public String stair;
    ObjectAnimator animator;

    public ArrayList<Float> coordx = new ArrayList<Float>();
    public ArrayList<Float> coordy = new ArrayList<Float>();
    ArrayList arreglotemporal = new ArrayList();
    ArrayList arreglosegmentado = new ArrayList();
    ArrayList arreglorecorrido = new ArrayList();
    int contador;
    int drawRect;


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


    public void init(List<Nodes> puntos, ArrayList arregloRuta, ArrayList arregloStair, int cont, final int[] rectDib)
    {

        /*inicializa las variables a utilizar cada vez que sedibuja la ruta*/
        drawRect = 0;
        stair = "stair";
        arreglosegmentado.clear();
        coordx = new ArrayList<>();
        coordy = new ArrayList<>();

        /*Objeto animador. Encargado de hacer la animacion de la linea con un tiempo de duracion de 5 segundos*/
        animator = ObjectAnimator.ofFloat(this, "phase", 1.0f, 0.0f);
        animator.setDuration(5000);

        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        paint2 = new Paint();
        paint2.setColor(Color.BLUE);

        path = new Path();

        /*Agregamos los X e Y por donde va a ser dibujada la ruta*/
        for(int i =0; i < puntos.size(); i++){
            coordx.add((float) puntos.get(i).getLocationX());
            coordy.add((float) puntos.get(i).getLocationY());
        }


        /*Si se detecta una escalera en la ruta optima se utiliza la funcion segmentar ruta donde se divide la ruta en dos.
        * Si no agrega la ruta al arreglo temporal*/
        if(arregloStair.size() > 0) {
            segmentarRuta(arregloRuta, arregloStair);
        }else{
                for(int i =0; i < arregloRuta.size(); i++){
                    arreglotemporal.add(arregloRuta.get(i));
                }
            arreglosegmentado.add(arreglotemporal);
            arreglotemporal = new ArrayList();
        }

        /*Primer if determina si la variable cont es igual a 0 y si el arreglo de la escalera es igual a 0. Si esto ocurre no hay cambio de piso
        * por lo tanto dibuja la ruta en el piso donde se haya seleccionado los puntos.
        *
        * Segundo if si la variable cont es igual a 0 pero el arreglo escalera es mayor a 0 ocurre que habra cambio de piso y la ruta se dibujara
        * solo hasta donde llegue la primera escalera y se que dara ahi esperando hasta que ocurra un click sobre la imagen de la escalera
        * o el icono de continuar ruta.
        *
        * Tercer if si la variable cont es diferente de 0 y el arreglo escalera es mayor que 0 se dibujara la ruta desde la segunda escalera hasta
        * el punto final de la ruta seleccionada.*/
        if(cont == 0 && arregloStair.size() == 0) {
            drawRect = 1;
            contador  = cont;
            for (int i = 0; i < arregloRuta.size(); i++) {
                arreglorecorrido = (ArrayList) arreglosegmentado.get(0);
                path.moveTo(coordx.get(cont), coordy.get(cont));//Inicia el dibujado en el canvas desde este punto especifico
                for (int k = cont + 1; k < arreglorecorrido.size(); k++) {
                    path.lineTo(coordx.get(k), coordy.get(k));//mueve la linea de el punto inicial hasta el puntio final que se le indique
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
            drawRect = 1;
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

        /*Habilita la escalera y la imagen de continuar ruta dependiendo el numero de escalera donde se segmenta la ruta*/
        if(arreglorecorrido.get(arreglorecorrido.size() -1).equals("8") || arreglorecorrido.get(arreglorecorrido.size() -1).equals("55") || arreglorecorrido.get(arreglorecorrido.size() -1).equals("393")){
            stair = "uno";
        }else if(arreglorecorrido.get(arreglorecorrido.size() -1).equals("5") || arreglorecorrido.get(arreglorecorrido.size() -1).equals("56") || arreglorecorrido.get(arreglorecorrido.size() -1).equals("395")){
            stair = "dos";
        }else if(arreglorecorrido.get(arreglorecorrido.size() -1).equals("7") || arreglorecorrido.get(arreglorecorrido.size() -1).equals("57") || arreglorecorrido.get(arreglorecorrido.size() -1).equals("394")){
            stair = "tres";
        }else if(arreglorecorrido.get(arreglorecorrido.size() -1).equals("6") || arreglorecorrido.get(arreglorecorrido.size() -1).equals("58") || arreglorecorrido.get(arreglorecorrido.size() -1).equals("396")){
            stair = "cuatro";
        }else{
            stair = "stair";
        }


        // Measure the path
        PathMeasure measure = new PathMeasure(path, false);
        length = measure.getLength();

        float[] intervals = new float[]{length, length};

        /*Inicia la animacion*/
        animator.start();

        final int finalCont = cont; //Variable que es igual al cont y determina a que piso debe cambiar cuando se detecta que hay cambio de piso.
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                /**
                 * En los 3 if arriba se utiliza la variable drawrect si la variable es igual a 1 se envia un evento por eventbus
                 * donde se le informa a la actividad principal que debe iluminar la tienda bien sea por imagen o por codigo con canvas.
                 */
                if(drawRect == 1){
                    EventBus.getDefault().postSticky(new Message(4));
                }


                Toast.makeText(getContext(), "Final "+stair, Toast.LENGTH_SHORT).show();

                /*
                * Evento que se envia por eventbus donde se informa a la actividad principal a que piso debe cambiar.
                * La variable finalCont se encarga de llevar el menssaje del piso al cual debe cambiar.
                */
                EventBus.getDefault().postSticky(new Message(finalCont, stair));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    /*LLama a la funcion que crea el efecto de dibujado de la linea*/
    public void setPhase(float phase)
    {
        //Log.d("pathview","setPhase called with:" + String.valueOf(phase));
        paint.setPathEffect(createPathEffect(length, phase, 0.0f));
        invalidate();//will calll onDraw

    }

    /*Efecto creado para dar animacion a la linea. Si se elimina la linea aparece dibujada automaticamente sin hacer
    * el efecto que se va dibujando punto a punto en un intervalo de tiempo*/
    private PathEffect createPathEffect(float pathLength, float phase, float offset)
    {
        return new DashPathEffect(new float[] {
                pathLength, pathLength
        },
                Math.max(phase * pathLength, offset));
    }

    /*Funcion que se ejecuta si se encuentra una escalera en la ruta a dibujar
    * divide la ruta en dos segmentos. Cada uno dibujados por separado.*/
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

    @Override
    public void onDraw(Canvas c)
    {
        super.onDraw(c);
        c.drawPath(path, paint);
        c.drawCircle(coordx.get(contador), coordy.get(contador), 10, paint2);
    }

}