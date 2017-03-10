package com.rinno.apumanquewayfinder;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.FirebaseDatabase;
import com.rinno.apumanquewayfinder.algoritmo.Astar;
import com.rinno.apumanquewayfinder.algoritmo.Graph;
import com.rinno.apumanquewayfinder.bd.FirebaseConexion;
import com.rinno.apumanquewayfinder.canvas.DrawingPointEndView;
import com.rinno.apumanquewayfinder.canvas.DrawingPointView;
import com.rinno.apumanquewayfinder.canvas.DrawingRectView;
import com.rinno.apumanquewayfinder.canvas.DrawingView;
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

import static com.rinno.apumanquewayfinder.models.Globales.alto;
import static com.rinno.apumanquewayfinder.models.Globales.ancho;

public class MainActivity extends AppCompatActivity {

    //Listas donde manejamos la adicion de los vertices y edges del grafo
    public List<Graph.Edge<String>> edges = new ArrayList<Graph.Edge<String>>();
    private ArrayList arregloStair = new ArrayList();
    private ArrayList arregloRutaFinal = new ArrayList();
    private ArrayList arregloFloorEnd = new ArrayList();
    private int[] rectDib = new int[10];
    private String[] arrayServices = new String[8];
    private String img;

    private List<String> stockList = new ArrayList<String>();
    private List<Nodes> puntos = new ArrayList<Nodes>();
    public Map<Integer, Nodes> services = new HashMap<Integer, Nodes>();

    private int start;
    private int end;
    private int idCont;
    private int rect;
    private String idStair;
    private int contadorPiso = 0;
    private ImageView imageOne;
    public Temporizador temporizador;

    //drawing view canvas
    private DrawingView drawView;
    private DrawingPointView drawViewPoint;
    private DrawingPointEndView drawViewPointEnd;
    private DrawingRectView drawViewRect;

    static boolean calledAlready = false;
    private SimpleDraweeView draweeView;
    private SimpleDraweeView dvImgStore;

    FirebaseConexion fc;

    private RelativeLayout layout;


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
    @BindView(R.id.rlFondoMapa)
    RelativeLayout rlFondoMapa;
    @Nullable
    @BindView(R.id.rlPisos)
    RelativeLayout rlPisos;
    @BindView(R.id.llPisoUno)
    LinearLayout llPisoUno;
    @BindView(R.id.llPisoDos)
    LinearLayout llPisoDos;
    @BindView(R.id.llPisoTres)
    LinearLayout llPisoTres;
    @BindView(R.id.llInformacionAndBanos)
    LinearLayout llInformacionAndBanos;
    @BindView(R.id.llBanosDamaAndAscensor)
    LinearLayout llBanosDamaAndAscensor;
    @BindView(R.id.llBanosCaballerosAndEstacionamiento)
    LinearLayout llBanosCaballerosAndEstacionamiento;
    @BindView(R.id.llCajerosAndDescuentos)
    LinearLayout llCajerosAndDescuentos;
    @BindView(R.id.llAscensor)
    LinearLayout llAscensor;
    @BindView(R.id.llCochesSillas)
    LinearLayout llCochesSillas;
    @BindView(R.id.llGuardaPeques)
    LinearLayout llGuardaPeques;
    @BindView(R.id.llDescuentosPromociones)
    LinearLayout llDescuentosPromociones;
    @BindView(R.id.tvInformacionAndBanos)
    TextView tvInformacionAndBanos;
    @BindView(R.id.tvBanosDamaAndAscensor)
    TextView tvBanosDamaAndAscensor;
    @BindView(R.id.tvBanosCaballerosAndEstacionamiento)
    TextView tvBanosCaballerosAndEstacionamiento;
    @BindView(R.id.tvCajerosAndDescuentos)
    TextView tvCajerosAndDescuentos;
    @BindView(R.id.tvAscensor)
    TextView tvAscensor;
    @BindView(R.id.tvCochesSillas)
    TextView tvCochesSillas;
    @BindView(R.id.tvGuardaPeques)
    TextView tvGuardaPeques;
    @BindView(R.id.tvDescuentosPromociones)
    TextView tvDescuentosPromociones;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*INICIALIZACION LIB FRESCO PARA MANEJO DE IMAGENES*/
        Fresco.initialize(this);

        /*CONTENEDOR PRINCIPAL DE LA ACTIVIDAD*/
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        layout = (RelativeLayout) findViewById(R.id.rlFondoMapaServicios);

        imageOne = new ImageView(this);

        arrayServices = new String[]{"information","bathroomWomen", "bathroomMen", "atm",  "lift",  "babyCarrierAndWheelChair", "keeper", "parkingAtm"};

        /*INICIALIZACION LIB BUTTERKNIFE PARA INJECCION DE VISTAS*/
        ButterKnife.bind(this);

        temporizador = new Temporizador(30000,1000);
        temporizador.start();

        /*
        *CalledAlready  VARIABLE ESTATICA QUE DETERMINA SI LA PERSISTENCIA DE FIREBASE HA SIDO O NO LLAMADA ANTES
        * SI LA HA SIDO LLAMADO CONTINUA SINO REALIZA EL LLAMADO Y PUEDES TRABAJAR CON FIREBASE EN LOCAL
        * */
        if (!calledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

       /*Imagen Dinamica. Cambia con relacion a la escalera habilitada*/
        draweeView = (SimpleDraweeView) findViewById(R.id.ivContinuarRuta);
        draweeView.setVisibility(View.GONE);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /*Imagen Dinamica. Cambia con relacion a la tienda. Si la tienda no es un cuadrado perfecto se utiliza esta imagen para iluminarla*/
        dvImgStore = (SimpleDraweeView) findViewById(R.id.dvImgStore);
        dvImgStore.setVisibility(View.GONE);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        /*INICIALIZAR CANVAS PARA LOS DIBUJOS*/
        drawView = (DrawingView) findViewById(R.id.drawing); //Ruta Principal
        drawViewPoint = (DrawingPointView) findViewById(R.id.drawingPoint); //Punto de inicio de la ruta y animacion fade-in fade-out
        drawViewPointEnd = (DrawingPointEndView) findViewById(R.id.drawingPointEnd); //Punto final donde termina la ruta
        drawViewRect = (DrawingRectView) findViewById(R.id.drawingRect); //Rectangulo donde tienda sea igual cuadrado perfecto
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /*Evento click icono continuar ruta*/
        draweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarFondoPiso();
                stairEvent();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        fc = new FirebaseConexion();
        fc.init();
    }

    /*
    *FUNCION UTILIZADA SI EN LA RUTA HAY UN CAMBIO DE PISO Y SE LLEGA HASTA UNA ESCALERA
    */
    public void stairEvent() {
        draweeView.setVisibility(View.GONE);
        drawView.setVisibility(View.GONE);
        drawViewPoint.setVisibility(View.GONE);

        drawView.setVisibility(View.VISIBLE);
        drawViewPoint.setVisibility(View.VISIBLE);

        drawView.init(puntos, arregloRutaFinal, arregloStair, idCont, rectDib);
        drawViewPoint.init(puntos, idCont);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    * Funcion que se ejecuta al inicio del recorrido de la ruta y muestra las escaleras.
    * */
    public void seeStair() {
        btEscalera1.setVisibility(View.VISIBLE);
        btEscalera2.setVisibility(View.VISIBLE);
        btEscalera3.setVisibility(View.VISIBLE);
        btEscalera4.setVisibility(View.VISIBLE);
        btEscalera1.setEnabled(false);
        btEscalera2.setEnabled(false);
        btEscalera3.setEnabled(false);
        btEscalera4.setEnabled(false);
        btEscalera1.setX((float) 285 * ancho);
        btEscalera1.setY((float) 183 * alto);
        btEscalera2.setX((float) 985 * ancho);
        btEscalera2.setY((float) 183 * alto);
        btEscalera3.setX((float) 285 * ancho);
        btEscalera3.setY((float) 795 * alto);
        btEscalera4.setX((float) 985 * ancho);
        btEscalera4.setY((float) 795 * alto);
    }


    /*
    *Se realiza el cambio de piso de piso dependiendo del valor de la variable contadorPiso.
    */
    public void cambiarFondoPiso() {
        if (contadorPiso == 1) {
            rlPisos.setBackgroundDrawable(getResources().getDrawable(R.drawable.mapauno));
            cambiarServiciosLayout(contadorPiso);
        } else if (contadorPiso == 2) {
            rlPisos.setBackgroundDrawable(getResources().getDrawable(R.drawable.mapados));
            cambiarServiciosLayout(contadorPiso);
        } else {
            rlPisos.setBackgroundDrawable(getResources().getDrawable(R.drawable.mapatres));
            cambiarServiciosLayout(contadorPiso);
        }
    }


    @Optional
    @OnClick({R.id.btIr, R.id.btEscalera1, R.id.btEscalera2, R.id.btEscalera3, R.id.btEscalera4, R.id.rlPisos, R.id.llPisoUno, R.id.llPisoDos, R.id.llPisoTres,
            R.id.llInformacionAndBanos, R.id.llBanosDamaAndAscensor, R.id.llBanosCaballerosAndEstacionamiento, R.id.llCajerosAndDescuentos,
            R.id.llAscensor, R.id.llCochesSillas, R.id.llGuardaPeques, R.id.llDescuentosPromociones})
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

                calculateRoute(start, end);
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
            case R.id.llPisoUno:
                contadorPiso = 1;
                cambiarServiciosLayout(contadorPiso);
                cambiarFondoPiso();
                canvasVisible();
                break;
            case R.id.llPisoDos:
                contadorPiso = 2;
                cambiarServiciosLayout(contadorPiso);
                cambiarFondoPiso();
                canvasVisible();
                break;
            case R.id.llPisoTres:
                contadorPiso = 3;
                cambiarServiciosLayout(contadorPiso);
                cambiarFondoPiso();
                canvasVisible();
                break;
            case R.id.llInformacionAndBanos:
                if(contadorPiso <= 2) {
                    cambiarServicios(arrayServices[0],0);
                }else {
                    cambiarServicios(arrayServices[2],8);
                }
                canvasVisible();
                break;
            case R.id.llBanosDamaAndAscensor:
                if (contadorPiso <= 2) {
                    cambiarServicios(arrayServices[1],1);
                }else {
                    cambiarServicios(arrayServices[4],4);
                }
                canvasVisible();
                break;
            case R.id.llBanosCaballerosAndEstacionamiento:
                if (contadorPiso <= 2) {
                    cambiarServicios(arrayServices[2],2);
                }else {
                    cambiarServicios(arrayServices[7],9);
                }
                canvasVisible();
                break;
            case R.id.llCajerosAndDescuentos:
                if (contadorPiso <= 2) {
                    cambiarServicios(arrayServices[3],3);
                }else {
                    cambiarServicios(arrayServices[4],7);
                }
                canvasVisible();
                break;
            case R.id.llAscensor:
                cambiarServicios(arrayServices[4],4);
                canvasVisible();
                break;
            case R.id.llCochesSillas:
                cambiarServicios(arrayServices[5],5);
                canvasVisible();
                break;
            case R.id.llGuardaPeques:
                cambiarServicios(arrayServices[6],6);
                canvasVisible();
                break;
            case R.id.llDescuentosPromociones:
                cambiarServicios(arrayServices[6],7);
                canvasVisible();
                break;
        }
    }

    /*
    *FUNCION CALCULAR RUTA FINAL
    */
    public void calculateRoute(int a, int b) {
        draweeView.setVisibility(View.GONE);
        Set<String> linkedHashSet = new LinkedHashSet<String>();
        edges.clear();

        /*
        AGREGA EDGES RESPECTO A CADA UNO DE LOS VERTICES
        */
        for (int i = 0; i < fc.arregloA.size(); i++) {
            Graph.Edge<String> ed = new Graph.Edge<String>((int) fc.arregloCosto.get(i), fc.vertices.get((int) fc.arregloA.get(i)), fc.vertices.get((int) fc.arregloB.get(i)));
            edges.add(ed);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /*
        *PASAMOS VERTICES Y EDGES USANDO LA CLASE GRAPH PARA GENERAR GRAFO FINAL DE RUTA
        */
        Graph<String> graph = new Graph<String>(Graph.TYPE.UNDIRECTED, fc.vertices, edges);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /*
        *LIMPIAMOS LAS VARIABLES Y ARREGLOS CADA VEZ QUE GENERAMOS UNA NUEVA RUTA
        */
        Astar astar = new Astar();
        arregloRutaFinal = new ArrayList();
        arregloRutaFinal.clear();
        arregloStair = new ArrayList();
        arregloStair.clear();
        arregloFloorEnd.clear();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /*
        *TOMAMOS EL GRAFO FINAL Y CON LA CLASE ASTAR GENERAMOS SUS PESOS ENTRE LOS EDGES PARA DETERMINAR RUTA MAS CORTA ENTRE NODOS
        */
        stockList = astar.aStar(graph, fc.vertices.get(a), fc.vertices.get(b));
        //Log.e("TAG", "Stocklist " + stockList);
        //Salida por consola Stocklist [1,43, 43,6, 6,58, 58,151, 151,116, 116,115, 115,114, 114,113, 113,112, 112,111, 111,110, 110,109, 109,108]
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /*
        *TOMAMOS EL ARREGLO STOCKLIST Y GENERAMOS UN NUEVO ARREGLO ELIMINANDO ESPACIOS Y SEPARANDO POR COMAS
        */
        String ss = "";
        for (int i = 0; i < stockList.size(); i++) {
            ss = String.valueOf(stockList.get(i));
            String[] sp = ss.split(",");
            arregloRutaFinal.add(sp[0]);
            arregloRutaFinal.add(sp[1]);
        }
        //Log.e("TAG","ARREGLO RUTA FINAL: "+arregloRutaFinal);
        //Salida por consola ruta 20 a 620 ARREGLO RUTA FINAL: [20, 37, 37, 38, 38, 39, 39, 42, 42, 43, 43, 6, 6, 58, 58, 396, 396, 598, 598, 600, 600, 651, 651, 610, 610, 612, 612, 769, 769, 615, 615, 617, 617, 619, 619, 621, 621, 620]
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /*
        *UTILIZAMOS LINKEDHASHSET PARA ELIMINAR ELEMENTOS REPETIDOS Y GENERAR RUTA FINAL A DIBUJAR EN EL MAPA
        */
        linkedHashSet.addAll(arregloRutaFinal);
        arregloRutaFinal.clear();
        arregloRutaFinal.addAll(linkedHashSet);
        //Log.e("TAG", "RUTA FINAL " + arregloRutaFinal);
        //Salida por consola ruta 20 a 620 RUTA FINAL [20, 37, 38, 39, 42, 43, 6, 58, 396, 598, 600, 651, 610, 612, 769, 615, 617, 619, 621, 620]
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /*
        *RECORREMOS MAP PARA DETERMINAR COORD DE LA TIENDA A ILUMINAR Y PUNTO FINAL DE LLEGADA A DIBUJAR
        */
        for (Map.Entry<String, Nodes> cordRectTienda : fc.coordRect.entrySet()) {
            float xx = (float) fc.arregloLocationX.get(Integer.parseInt(String.valueOf(arregloRutaFinal.get(arregloRutaFinal.size() - 1))) - 1);
            float yy = (float) fc.arregloLocationY.get(Integer.parseInt(String.valueOf(arregloRutaFinal.get(arregloRutaFinal.size() - 1))) - 1);
            if (arregloRutaFinal.size() > 0) {
                if (cordRectTienda.getKey().equals(arregloRutaFinal.get(arregloRutaFinal.size() - 1))) {
                    img = cordRectTienda.getValue().getImg();
                    Log.e("TAG","IMAGEN: "+img);
                    rectDib = new int[]{(int) cordRectTienda.getValue().getRectX(), (int) cordRectTienda.getValue().getRectY(), (int) cordRectTienda.getValue().getRectW(), (int) cordRectTienda.getValue().getRectH(), (int) xx, (int) yy,
                            (int) cordRectTienda.getValue().getImgX(), (int) cordRectTienda.getValue().getImgY(), (int) cordRectTienda.getValue().getImgW(), (int) cordRectTienda.getValue().getImgH()};
                }
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /*
        *DETERMINAR ESCALERAS ENCONTRADAS EN LA RUTA OPTIMA y AGREGA COORD X e Y DONDE SE DIBUJARA LA RUTA
        */
        puntos = new ArrayList<>();
        for (int i = 0; i < arregloRutaFinal.size(); i++) {
            for (int j = 0; j < fc.arregloIdRuta.size(); j++) {
                if (arregloRutaFinal.get(i).equals(fc.arregloIdRuta.get(j))) {
                    arregloFloorEnd.add(fc.arregloFloor.get(j));
                    if (fc.arregloType.get(j).equals("stair")) {
                        arregloStair.add(i);
                    }
                    puntos.add(new Nodes((float) fc.arregloLocationX.get(j), (float) fc.arregloLocationY.get(j)));
                }
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
    ///////////////////////////////////////////////FIN FUNCION CALCULAR RUTA FINAL///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    *FUNCION DONDE SE CAMBIA LA POSICION DE LA IMAGEN CONTINUAR RUTA
    */
    public void manoStair(float x, float y) {
        draweeView.setVisibility(View.VISIBLE);
        draweeView.setX(x);
        draweeView.setY(y);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    * Funcion para cambiar imagen de los servicios segun sea el seleccionado en la pantalla del mapa
    * */
    public void cambiarServicios(String nameServices, int img) {

        layout.removeAllViews();
        services.clear();
        for (int i = 0; i < fc.arregloType.size(); i++){
            if(fc.arregloType.get(i).equals(nameServices) && fc.arregloFloor.get(i).equals(String.valueOf(contadorPiso))){
                services.put(i, new Nodes((float) fc.arregloLocationX.get(i),(float) fc.arregloLocationY.get(i)));
            }
        }
        String uri;
        int imageResource;
        Drawable myDrawable;
        for (Map.Entry<Integer, Nodes> servicesf :  services.entrySet()) {
            uri = "@drawable/img"+img;
            imageResource = getResources().getIdentifier(uri, null, getPackageName());
            myDrawable = getResources().getDrawable(imageResource);

            imageOne = new ImageView(this);
            imageOne.setLayoutParams(new ViewGroup.LayoutParams(60, 60));
            imageOne.setImageDrawable(myDrawable);
            imageOne.setX(servicesf.getValue().getLocationX() - 30);
            imageOne.setY(servicesf.getValue().getLocationY() - 60);
            layout.addView(imageOne);
        }
    }

    public void cambiarServiciosLayout(int contadorPiso){
        if (contadorPiso <= 2) {
            tvInformacionAndBanos.setText(R.string.informacion);
            tvBanosDamaAndAscensor.setText(R.string.banos_damas);
            tvBanosCaballerosAndEstacionamiento.setText(R.string.banos_caballeros);
            tvCajerosAndDescuentos.setText(R.string.cajeros);
            llAscensor.setVisibility(View.VISIBLE);
            llCochesSillas.setVisibility(View.VISIBLE);
            llGuardaPeques.setVisibility(View.VISIBLE);
            llDescuentosPromociones.setVisibility(View.VISIBLE);
        } else {
            tvInformacionAndBanos.setText(R.string.banos);
            tvBanosDamaAndAscensor.setText(R.string.ascensor);
            tvBanosCaballerosAndEstacionamiento.setText(R.string.estacionamiento);
            tvCajerosAndDescuentos.setText(R.string.descuentos);
            llAscensor.setVisibility(View.GONE);
            llCochesSillas.setVisibility(View.GONE);
            llGuardaPeques.setVisibility(View.GONE);
            llDescuentosPromociones.setVisibility(View.GONE);
        }
    }

    public void canvasVisible(){
        if (drawView.getVisibility() == View.VISIBLE) {
            drawView.setVisibility(View.GONE);
            drawViewPoint.setVisibility(View.GONE);
            drawViewPointEnd.setVisibility(View.GONE);
            drawViewRect.setVisibility(View.GONE);
        }
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

        temporizador.cancel();

    }

    /*EVENTO DE EVENTBUS PARA DETERMINAR SI HAY UN CAMBIO DE PISO Y PARA DETERMINAR QUE ESCALERA E IMAGEN HABILITAR
    * AL HACER CLICK SOBRE UNA DE ELLAS*/
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessage(Message event) {
        idCont = event.getCont();
        idStair = event.getStair();
        if (idCont > 0 && idStair.equals("uno")) {
            manoStair(600 * ancho, 280 * alto);
            btEscalera1.setEnabled(true);
            contadorPiso = Integer.parseInt((String) arregloFloorEnd.get(idCont + 2));
        } else if (idCont > 0 && idStair.equals("dos")) {
            manoStair(1300 * ancho, 280 * alto);
            btEscalera2.setEnabled(true);
            contadorPiso = Integer.parseInt((String) arregloFloorEnd.get(idCont + 2));
        } else if (idCont > 0 && idStair.equals("tres")) {
            manoStair(600 * ancho, 890 * alto);
            btEscalera3.setEnabled(true);
            contadorPiso = Integer.parseInt((String) arregloFloorEnd.get(idCont + 2));
        } else if (idCont > 0 && idStair.equals("cuatro")) {
            manoStair(1300 * ancho, 890 * alto);
            btEscalera4.setEnabled(true);
            contadorPiso = Integer.parseInt((String) arregloFloorEnd.get(idCont + 2));
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    *EVENTO DE EVENTBUS PARA DETERMINAR SI HAY UNA IMAGEN DE TIENDA A DIBUJAR Y EN QUE POSICION
    */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageRect(Message event) {
        rect = event.getRect();
        if (rect == 4) {
            dvImgStore.setX(rectDib[6]);
            dvImgStore.setY(rectDib[7] + 1);
            dvImgStore.getLayoutParams().width = (int) (rectDib[8] * ancho);
            dvImgStore.getLayoutParams().height = (int) (rectDib[9] * alto);
            if (img != null) {
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
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onUserInteraction() {
        temporizador.cancel();
        temporizador.start();
    }

    public class Temporizador extends CountDownTimer {

        public Temporizador(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
        }
    }

}
