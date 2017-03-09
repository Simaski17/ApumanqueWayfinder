package com.rinno.apumanquewayfinder.bd;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rinno.apumanquewayfinder.algoritmo.Graph;
import com.rinno.apumanquewayfinder.models.Edges;
import com.rinno.apumanquewayfinder.models.Nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rinno.apumanquewayfinder.models.Globales.alto;
import static com.rinno.apumanquewayfinder.models.Globales.ancho;

/**
 * Created by simaski on 07-03-17.
 */

public class FirebaseConexion {

    DatabaseReference referenceNodes;
    DatabaseReference referenceEdges;

    public ArrayList arregloA = new ArrayList();
    public ArrayList arregloB = new ArrayList();
    public ArrayList arregloCosto = new ArrayList();
    public ArrayList arregloIdRuta = new ArrayList();
    public ArrayList arregloLocationX = new ArrayList();
    public ArrayList arregloLocationY = new ArrayList();
    public ArrayList arregloType = new ArrayList();
    public ArrayList arregloFloor = new ArrayList();

    public Map<String, Nodes> coordRect = new HashMap<String, Nodes>();

    public List<Graph.Vertex<String>> vertices = new ArrayList<Graph.Vertex<String>>();

    public void init(){

       /*INSTANCIAS DE BD HACIENDO REFERENCIA A LOS NODOS EN LOS CUALES ESTAREMOS TRABAJANDO EN LA APP*/
        referenceNodes = FirebaseDatabase.getInstance().getReference().child("Nodes");
        referenceEdges = FirebaseDatabase.getInstance().getReference().child("Edges");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /*HABILITAMOS LA SINCRONIZACION DE LOS NODOS A UTILIZAR  PARA MANTENER DATOS ACTUALIZADOS TANTO EN MEMORIA COMO EN DISCO
        * SIEMPRE Y CUANDO TENGAMOS CONEXION A INTERNET SINO SOLO SE TENDRAN LOS DATOS EN DISCO*/
        referenceNodes.keepSynced(true);
        referenceEdges.keepSynced(true);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


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
                //Log.e("TAG","VERTICES DESDE OTRA CLASE: "+vertices.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "ERROR: " + databaseError);
            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


         /*
         *CONEXION DEL NODO (Edges) DESDE FIREBASE. CAPTURA DE DATOS A UTILIZAR POR LA APP
         */
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
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
}
