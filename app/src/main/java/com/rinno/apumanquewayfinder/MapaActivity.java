package com.rinno.apumanquewayfinder;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rinno.apumanquewayfinder.algoritmo.Astar;
import com.rinno.apumanquewayfinder.algoritmo.Graph;
import com.rinno.apumanquewayfinder.canvas.DrawingPointView;
import com.rinno.apumanquewayfinder.canvas.DrawingView;
import com.rinno.apumanquewayfinder.models.Edges;
import com.rinno.apumanquewayfinder.models.Message;
import com.rinno.apumanquewayfinder.models.Nodes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapaActivity extends AppCompatActivity {


    //Listas donde manejamos la adicion de los vertices y edges del grafo
    public List<Graph.Vertex<String>> vertices = new ArrayList<Graph.Vertex<String>>();
    public List<Graph.Edge<String>> edges = new ArrayList<Graph.Edge<String>>();
    @BindView(R.id.btEscalera1)
    ImageView btEscalera1;
    @BindView(R.id.btEscalera2)
    ImageView btEscalera2;
    @BindView(R.id.btEscalera3)
    ImageView btEscalera3;
    @BindView(R.id.rlPisos)
    RelativeLayout rlPisos;
    @BindView(R.id.btEscalera4)
    ImageView btEscalera4;
    private ArrayList arregloA = new ArrayList();
    private ArrayList arregloB = new ArrayList();
    private ArrayList arregloCosto = new ArrayList();
    private ArrayList arregloIdRuta = new ArrayList();
    private ArrayList arregloLocationX = new ArrayList();
    private ArrayList arregloLocationY = new ArrayList();
    private ArrayList arregloStair = new ArrayList();
    private ArrayList arregloType = new ArrayList();
    private ArrayList arregloRutaFinal = new ArrayList();
    private ArrayList arregloFloor = new ArrayList();
    private ArrayList arregloFloorEnd = new ArrayList();

    private List<String> stockList = new ArrayList<String>();
    List<Nodes> puntos = new ArrayList<Nodes>();
    private int start;
    private int end;
    int idCont;
    int contadorPiso = 0;

    //custom drawing view
    private DrawingView drawView;
    private DrawingPointView drawViewPoint;

    //Button btPrueba;
    Bundle datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        ButterKnife.bind(this);


        datos = this.getIntent().getExtras();
        idCont = 0;


        //ESCALERAS COORD XY
//        btEscalera1.setEnabled(false);
//        btEscalera2.setEnabled(false);
//        btEscalera2.setEnabled(false);
        btEscalera1.setX(720);
        btEscalera1.setY(240);
        btEscalera2.setX(1340);
        btEscalera2.setY(240);
        btEscalera3.setX(720);
        btEscalera3.setY(780);
        btEscalera4.setX(1340);
        btEscalera4.setY(780);

        //get drawing view
        drawView = (DrawingView) findViewById(R.id.drawing);
        drawViewPoint = (DrawingPointView) findViewById(R.id.drawingPoint);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (int i = 1; i < dataSnapshot.child("Nodes").getChildrenCount(); i++) {

                    Nodes nod = dataSnapshot.child("Nodes").child(String.valueOf(i)).getValue(Nodes.class);
                    Graph.Vertex<String> a = new Graph.Vertex<String>(nod.getId());
                    arregloIdRuta.add(nod.getId());
                    arregloLocationX.add(Float.parseFloat(String.valueOf(nod.getLocationX())));
                    arregloLocationY.add(Float.parseFloat(String.valueOf(nod.getLocationY())));
                    arregloType.add(String.valueOf(nod.getType()));
                    arregloFloor.add(String.valueOf(nod.getFloor()));
                    vertices.add(a);
                }

                for (int i = 1; i < dataSnapshot.child("Edges").getChildrenCount(); i++) {

                    Edges edg = dataSnapshot.child("Edges").child(String.valueOf(i)).getValue(Edges.class);
                    Graph.Vertex<String> a = new Graph.Vertex<String>(edg.getSource());
                    Graph.Vertex<String> b = new Graph.Vertex<String>(edg.getEnd());

                    for (int l = 0; l < vertices.size(); l++) {
                        if (vertices.get(l).equals(a)) {
                            arregloA.add(l);
                        }
                    }

                    for (int l = 0; l < vertices.size(); l++) {
                        if (vertices.get(l).equals(b)) {
                            arregloB.add(l);
                        }
                    }
                    arregloCosto.add(edg.getCost());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "ERROR: " + databaseError);
            }
        });

        drawView.setVisibility(View.GONE);
        drawViewPoint.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                idCont = 0;
                drawView.setVisibility(View.GONE);
                drawViewPoint.setVisibility(View.GONE);

                start = datos.getInt("start");
                end = datos.getInt("end");

                calcularRuta(start, end);

                if (puntos.size() > 0) {
                    contadorPiso = Integer.parseInt((String) arregloFloorEnd.get(0));
                    cambiarFondoPiso();
                    drawView.setVisibility(View.VISIBLE);
                    drawViewPoint.setVisibility(View.VISIBLE);

                    drawView.init(puntos, arregloRutaFinal, arregloStair, idCont);
                    drawViewPoint.init(puntos, idCont);
                }
            }
        }, 2000);


        rlPisos.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {

                //stringBuilder.setLength(0);
                //si la acción que se recibe es de movimiento
                if (arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    //stringBuilder.append("Moviendo, X:" + arg1.getX() + ", Y:" + arg1.getY() );
                    Log.e("TAG", "Moviendo, X:" + arg1.getX() + ", Y:" + arg1.getY());
                } else {
                    Log.e("TAG", "Detenido, X:" + arg1.getX() + ", Y:" + arg1.getY());
                }
                //Se muestra en pantalla
                //textView.setText( stringBuilder.toString() );
                return true;
            }


        });


    }

    @OnClick({R.id.btEscalera1, R.id.btEscalera2, R.id.btEscalera3, R.id.btEscalera4, R.id.rlPisos})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btEscalera1:
                cambiarFondoPiso();
                stairEvent();
                break;
            case R.id.btEscalera2:
                cambiarFondoPiso();
                stairEvent();
                break;
            case R.id.btEscalera3:
                cambiarFondoPiso();
                stairEvent();
                break;
            case R.id.rlPisos:
                //Button b;
                break;
        }
    }


    public void stairEvent() {
        drawView.setVisibility(View.GONE);
        drawViewPoint.setVisibility(View.GONE);

        drawView.setVisibility(View.VISIBLE);
        drawViewPoint.setVisibility(View.VISIBLE);

        drawView.init(puntos, arregloRutaFinal, arregloStair, idCont);
        drawViewPoint.init(puntos, idCont);
    }

    public void cambiarFondoPiso() {
        if (contadorPiso == 1) {
            rlPisos.setBackgroundDrawable(getResources().getDrawable(R.drawable.mapauno));
        } else if (contadorPiso == 2) {
            rlPisos.setBackgroundDrawable(getResources().getDrawable(R.drawable.mapados));
        } else {
            rlPisos.setBackgroundDrawable(getResources().getDrawable(R.drawable.mapatres));
        }
    }

    public void calcularRuta(int a, int b) {
        Set<String> linkedHashSet = new LinkedHashSet<String>();
        //////////////////////////////////////////AGREGA EDGES RESPECTO A CADA UNO DE LOS VERTICES///////////////////////////////////////////////////////////////////////////////////////
        for (int i = 0; i < arregloA.size(); i++) {
            Graph.Edge<String> ed = new Graph.Edge<String>((int) arregloCosto.get(i), vertices.get((int) arregloA.get(i)), vertices.get((int) arregloB.get(i)));
            edges.add(ed);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Log.e("TAG", "VERTICES: " + edges.size());

        ////////////////////////////////////////////
        Graph<String> graph = new Graph<String>(Graph.TYPE.UNDIRECTED, vertices, edges);
        Astar astar = new Astar();
        arregloRutaFinal = new ArrayList();
        arregloRutaFinal.clear();
        arregloStair = new ArrayList();
        arregloStair.clear();

        //////GENERAR RUTA FINAL//////////////////////////////
        stockList = astar.aStar(graph, vertices.get(a), vertices.get(b));
        //Log.e("TAG", "Stocklist " + stockList);
        String ss = "";
        for (int i = 0; i < stockList.size(); i++) {
            ss = String.valueOf(stockList.get(i));
            String[] sp = ss.split(",");
            arregloRutaFinal.add(sp[0]);
            arregloRutaFinal.add(sp[1]);
        }
        linkedHashSet.addAll(arregloRutaFinal);
        arregloRutaFinal.clear();
        arregloRutaFinal.addAll(linkedHashSet);
        Log.e("TAG", "RUTA FINAL " + arregloRutaFinal);
        ///////////////////////////////////////////////////////////////////

        //////DETERMINAR ESCALERAS ENCONTRADAS EN LA RUTA OPTIMA y AGREGA PUNTOS DONDE SE DIBUJARA LA RUTA/////////////////////////////
        puntos = new ArrayList<>();
        for (int i = 0; i < arregloRutaFinal.size(); i++) {
            for (int j = 0; j < arregloIdRuta.size(); j++) {
                if (arregloRutaFinal.get(i).equals(arregloIdRuta.get(j))) {
                    arregloFloorEnd.add(arregloFloor.get(j));
                    Log.e("TAG", "ARREGLO PISO FINAL: " + arregloFloorEnd);
                    if (arregloType.get(j).equals("stair")) {
                        arregloStair.add(i);
                    }
                    puntos.add(new Nodes((float) arregloLocationX.get(j), (float) arregloLocationY.get(j)));
                }
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Log.e("TAG", "ARREGLO STAIR" + arregloStair);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        arregloA.clear();
        arregloB.clear();
        vertices.clear();
        arregloCosto.clear();
        contadorPiso = 0;
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessage(Message event) {
        idCont = event.getCont();
        if (idCont > 0) {
            btEscalera1.setEnabled(true);
//            btEscalera1.setImageResource(R.drawable.escaleraamarilla);
            btEscalera2.setEnabled(true);
            btEscalera3.setEnabled(true);
            contadorPiso = Integer.parseInt((String) arregloFloorEnd.get(idCont + 1));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        arregloA.clear();
        arregloB.clear();
        vertices.clear();
        arregloCosto.clear();
        contadorPiso = 0;
    }
}

