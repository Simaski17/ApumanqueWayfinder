package com.rinno.apumanquewayfinder;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rinno.apumanquewayfinder.algoritmo.Astar;
import com.rinno.apumanquewayfinder.algoritmo.Graph;
import com.rinno.apumanquewayfinder.canvas.DrawingPointEndView;
import com.rinno.apumanquewayfinder.canvas.DrawingPointView;
import com.rinno.apumanquewayfinder.canvas.DrawingRectView;
import com.rinno.apumanquewayfinder.canvas.DrawingView;
import com.rinno.apumanquewayfinder.models.Edges;
import com.rinno.apumanquewayfinder.models.Message;
import com.rinno.apumanquewayfinder.models.Nodes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class MainActivity extends AppCompatActivity {

    //Listas donde manejamos la adicion de los vertices y edges del grafo
    public List<Graph.Vertex<String>> vertices = new ArrayList<Graph.Vertex<String>>();
    public List<Graph.Edge<String>> edges = new ArrayList<Graph.Edge<String>>();
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
    private   int[ ]   rectDib = new  int[10];
    private String img;

    Map<String, Nodes> coordRect = new HashMap<String, Nodes>();

    private List<String> stockList = new ArrayList<String>();
    List<Nodes> puntos = new ArrayList<Nodes>();

    private int start;
    private int end;
    private int idCont;
    private int rect;
    private String idStair;
    private int contadorPiso = 0;
    private float ancho;
    private float alto;

    DatabaseReference referenceNodes;
    DatabaseReference referenceEdges;

    //custom drawing view
    private DrawingView drawView;
    private DrawingPointView drawViewPoint;
    private DrawingPointEndView drawViewPointEnd;
    private DrawingRectView drawViewRect;

    static boolean calledAlready = false;
    private SimpleDraweeView draweeView;
    private SimpleDraweeView dvImgStore;


    @BindView(R.id.tvInicio)
    TextView tvInicio;
    @BindView(R.id.etInicio)
    EditText etInicio;
    @BindView(R.id.tvFin)
    TextView tvFin;
    @BindView(R.id.etFin)
    EditText etFin;
    @BindView(R.id.btIr)
    Button btIr;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;

    @Nullable
    @BindView(R.id.btEscalera1)
    ImageView btEscalera1;
    @Nullable
    @BindView(R.id.btEscalera2)
    ImageView btEscalera2;
    @Nullable
    @BindView(R.id.btEscalera3)
    ImageView btEscalera3;
    @Nullable
    @BindView(R.id.btEscalera4)
    ImageView btEscalera4;
    @Nullable
    @BindView(R.id.rlPisos)
    RelativeLayout rlPisos;
    float suma;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        draweeView = (SimpleDraweeView) findViewById(R.id.ivContinuarRuta);
        draweeView.setVisibility(View.GONE);

        dvImgStore = (SimpleDraweeView) findViewById(R.id.dvImgStore);
        dvImgStore.setVisibility(View.GONE);

        ancho = (float) 965 / 1254 + (float) 0.001;
        alto = (float) 831 / 1080 + (float) 0.001;


        //get drawing view
        drawView = (DrawingView) findViewById(R.id.drawing);
        drawViewPoint = (DrawingPointView) findViewById(R.id.drawingPoint);
        drawViewPointEnd = (DrawingPointEndView) findViewById(R.id.drawingPointEnd);
        drawViewRect = (DrawingRectView) findViewById(R.id.drawingRect);
        drawView.setVisibility(View.GONE);
        drawViewPoint.setVisibility(View.GONE);
        drawViewPointEnd.setVisibility(View.GONE);
        drawViewRect.setVisibility(View.GONE);

        if (!calledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }

        referenceNodes = FirebaseDatabase.getInstance().getReference().child("Nodes");
        referenceEdges = FirebaseDatabase.getInstance().getReference().child("Edges");
        referenceNodes.keepSynced(true);
        referenceEdges.keepSynced(true);

        referenceNodes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (int i = 1; i <= dataSnapshot.getChildrenCount(); i++) {

                    Nodes nod = dataSnapshot.child(String.valueOf(i)).getValue(Nodes.class);
                    Graph.Vertex<String> a = new Graph.Vertex<String>(nod.getId());
                    arregloIdRuta.add(nod.getId());
                    arregloLocationX.add(Float.parseFloat(String.valueOf((nod.getLocationX() * ancho) +  5)));
                    arregloLocationY.add(Float.parseFloat(String.valueOf((nod.getLocationY() * alto)+ 5)));
                    arregloType.add(String.valueOf(nod.getType()));
                    arregloFloor.add(String.valueOf(nod.getFloor()));
                    coordRect.put(nod.getId(), new Nodes(nod.getId(), nod.getRectX() * ancho, nod.getRectY() * ancho , ((nod.getRectX() * ancho) + (nod.getRectW() * ancho)) ,((nod.getRectY() * ancho) + (nod.getRectH() * ancho)),
                    nod.getImg(), nod.getImgX() * ancho, nod.getImgY() * ancho, nod.getImgW(), nod.getImgH()));
                    vertices.add(a);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "ERROR: " + databaseError);
            }
        });

        referenceEdges.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (int i = 1; i < dataSnapshot.getChildrenCount(); i++) {

                    Edges edg = dataSnapshot.child(String.valueOf(i)).getValue(Edges.class);
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


        draweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarFondoPiso();
                stairEvent();
            }
        });

    }

    public void stairEvent() {
        draweeView.setVisibility(View.GONE);
        drawView.setVisibility(View.GONE);
        drawViewPoint.setVisibility(View.GONE);
        //drawViewRect.setVisibility(View.GONE);

        drawView.setVisibility(View.VISIBLE);
        drawViewPoint.setVisibility(View.VISIBLE);
        //drawViewRect.setVisibility(View.VISIBLE);

        drawView.init(puntos, arregloRutaFinal, arregloStair, idCont, rectDib);
        drawViewPoint.init(puntos, idCont);
        //drawViewRect.init();
    }

    public void seeStair() {
        btEscalera1.setVisibility(View.VISIBLE);
        btEscalera2.setVisibility(View.VISIBLE);
        btEscalera3.setVisibility(View.VISIBLE);
        btEscalera4.setVisibility(View.VISIBLE);
        btEscalera1.setEnabled(false);
        btEscalera2.setEnabled(false);
        btEscalera3.setEnabled(false);
        btEscalera4.setEnabled(false);
        btEscalera1.setX((float) (285 * ancho));
        btEscalera1.setY((float) 183 * alto);
        btEscalera2.setX((float) (975 * ancho));
        btEscalera2.setY((float) 183 * alto);
        btEscalera3.setX((float) (285 * ancho));
        btEscalera3.setY((float) 795 * alto);
        btEscalera4.setX((float) (975 * ancho));
        btEscalera4.setY((float) 795 * alto);
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


    @Optional
    @OnClick({R.id.btIr, R.id.btEscalera1, R.id.btEscalera2, R.id.btEscalera3, R.id.btEscalera4, R.id.rlPisos})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btIr:
                idCont = 0;
                dvImgStore.setVisibility(View.GONE);
                drawView.setVisibility(View.GONE);
                drawViewPoint.setVisibility(View.GONE);
                drawViewPointEnd.setVisibility(View.GONE);
                drawViewRect.setVisibility(View.GONE);

                start = Integer.parseInt(etInicio.getText().toString()) - 1;
                end = Integer.parseInt(etFin.getText().toString()) - 1;

                calcularRuta(start, end);
                seeStair();

                if (puntos.size() > 0) {
                    contadorPiso = 0;
                    contadorPiso = Integer.parseInt((String) arregloFloorEnd.get(0));
                    cambiarFondoPiso();
                    drawView.setVisibility(View.VISIBLE);
                    drawViewPoint.setVisibility(View.VISIBLE);

                    drawView.init(puntos, arregloRutaFinal, arregloStair, idCont, rectDib);
                    drawViewPoint.init(puntos, idCont);

                    etInicio.setText("");
                    etFin.setText("");

                }
                break;
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
            case R.id.btEscalera4:
                cambiarFondoPiso();
                stairEvent();
                break;
            case R.id.rlPisos:
                //Button b;
                break;
        }
    }

    public void calcularRuta(int a, int b) {
        draweeView.setVisibility(View.GONE);
        Set<String> linkedHashSet = new LinkedHashSet<String>();
        edges.clear();
        //Log.e("TAG", "EDGES EDGES: " + arregloA.size());
        //////////////////////////////////////////AGREGA EDGES RESPECTO A CADA UNO DE LOS VERTICES///////////////////////////////////////////////////////////////////////////////////////
        for (int i = 0; i < arregloA.size(); i++) {
            Graph.Edge<String> ed = new Graph.Edge<String>((int) arregloCosto.get(i), vertices.get((int) arregloA.get(i)), vertices.get((int) arregloB.get(i)));
            edges.add(ed);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        ////////////////////////////////////////////
        Graph<String> graph = new Graph<String>(Graph.TYPE.UNDIRECTED, vertices, edges);
        Astar astar = new Astar();
        arregloRutaFinal = new ArrayList();
        arregloRutaFinal.clear();
        arregloStair = new ArrayList();
        arregloStair.clear();
        arregloFloorEnd.clear();

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
        //Log.e("TAG", "RUTA FINAL " + arregloRutaFinal);
        ///////////////////////////////////////////////////////////////////

        //////////////////////////COORD DIBUJO RECTANGULO///////////////////////////////////////////////////////////////////////////////////////////
        for (Map.Entry<String, Nodes> jugador : coordRect.entrySet()){
            float xx = (float) arregloLocationX.get(Integer.parseInt(String.valueOf(arregloRutaFinal.get(arregloRutaFinal.size() - 1))) - 1) ;
            float yy = (float) arregloLocationY.get(Integer.parseInt(String.valueOf(arregloRutaFinal.get(arregloRutaFinal.size() - 1))) - 1);
            if(arregloRutaFinal.size() > 0) {
                //String clave = jugador.getKey();
                if (jugador.getKey().equals(arregloRutaFinal.get(arregloRutaFinal.size() - 1))) {
                    img = jugador.getValue().getImg();
                    rectDib = new int[]{(int) jugador.getValue().getRectX(),(int)  jugador.getValue().getRectY(),(int)  jugador.getValue().getRectW(),(int)  jugador.getValue().getRectH(), (int) xx, (int) yy,
                            (int) jugador.getValue().getImgX(),(int)  jugador.getValue().getImgY(),(int)  jugador.getValue().getImgW(),(int)  jugador.getValue().getImgH()};  //Array de 4 elementos
                }
            }
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //////DETERMINAR ESCALERAS ENCONTRADAS EN LA RUTA OPTIMA y AGREGA PUNTOS DONDE SE DIBUJARA LA RUTA/////////////////////////////
        puntos = new ArrayList<>();
        for (int i = 0; i < arregloRutaFinal.size(); i++) {
            for (int j = 0; j < arregloIdRuta.size(); j++) {
                if (arregloRutaFinal.get(i).equals(arregloIdRuta.get(j))) {
                    arregloFloorEnd.add(arregloFloor.get(j));
                    //Log.e("TAG", "ARREGLO PISO FINAL: " + arregloFloorEnd);
                    if (arregloType.get(j).equals("stair")) {
                        arregloStair.add(i);
                    }
                    puntos.add(new Nodes((float) arregloLocationX.get(j), (float) arregloLocationY.get(j)));
                }
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Log.e("TAG", "ARREGLO STAIR" + arregloStair);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);

        if (FirebaseDatabase.getInstance() != null) {
            FirebaseDatabase.getInstance().goOnline();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);

        if (FirebaseDatabase.getInstance() != null) {
            FirebaseDatabase.getInstance().goOffline();
        }

    }

    public void manoStair(float x, float y) {
        draweeView.setVisibility(View.VISIBLE);
        draweeView.setX(x);
        draweeView.setY(y);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessage(Message event) {
        idCont = event.getCont();
        idStair = event.getStair();
        if (idCont > 0 && idStair.equals("uno")) {
            manoStair(490 * ancho, 220 * ancho);
            btEscalera1.setEnabled(true);
            contadorPiso = Integer.parseInt((String) arregloFloorEnd.get(idCont + 2));
        } else if (idCont > 0 && idStair.equals("dos")) {
            manoStair(1060 * ancho, 220 * ancho);
            btEscalera2.setEnabled(true);
            contadorPiso = Integer.parseInt((String) arregloFloorEnd.get(idCont + 2));
        } else if (idCont > 0 && idStair.equals("tres")) {
            manoStair(490 * ancho, 730 * ancho);
            btEscalera3.setEnabled(true);
            contadorPiso = Integer.parseInt((String) arregloFloorEnd.get(idCont + 2));
        } else if (idCont > 0 && idStair.equals("cuatro")) {
            manoStair(1020 * ancho,730 * ancho);
            btEscalera4.setEnabled(true);
            contadorPiso = Integer.parseInt((String) arregloFloorEnd.get(idCont + 2));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageRect(Message event) {
        rect = event.getRect();
        if (rect == 4){
            dvImgStore.setX(rectDib[6]);
            dvImgStore.setY(rectDib[7] + 1);
            int resta = rectDib[7] + 1;
            dvImgStore.getLayoutParams().width = (int) (rectDib[8] * ancho);
            dvImgStore.getLayoutParams().height = (int) (rectDib[9] * alto);
            if(img != null) {
                Uri uri = Uri.parse(img);
                dvImgStore.setImageURI(uri);
                dvImgStore.setVisibility(View.VISIBLE);
            }
            drawViewPointEnd.setVisibility(View.VISIBLE);
            drawViewRect.setVisibility(View.VISIBLE);
            drawViewPointEnd.init(rectDib);
            drawViewRect.init(rectDib);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @OnClick(R.id.btIr)
    public void onClick() {
    }
}
