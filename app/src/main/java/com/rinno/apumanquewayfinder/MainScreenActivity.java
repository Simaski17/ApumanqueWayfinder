package com.rinno.apumanquewayfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainScreenActivity extends AppCompatActivity {


    @BindView(R.id.btirMapa)
    LinearLayout btirMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        ButterKnife.bind(this);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


    }


    @OnClick(R.id.btirMapa)
    public void onClick() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(new CalligraphyContextWrapper(context, R.attr.fontPath));
    }


}

//    public void cambiarServicios(String nameServices, int img) {
//
//        layout.removeView(imageOne);
//        layout.removeView(imageThree);
//        layout.removeView(imageTwo);
//        layout.removeView(imageFour);
//        Log.e("TAG","TIPO TAMANIO: "+fc.arregloType.size());
//        services.clear();
//        for (int i = 0; i < fc.arregloType.size(); i++){
//            if(fc.arregloType.get(i).equals(nameServices) && fc.arregloFloor.get(i).equals(String.valueOf(contadorPiso))){
//                services.put(i, new Nodes((float) fc.arregloLocationX.get(i),(float) fc.arregloLocationY.get(i)));
//                Log.e("TAG","VALOR DE X: "+i +"**********"+fc.arregloLocationX.get(i));
//                Log.e("TAG","VALOR DE Y: "+i +"**********"+fc.arregloLocationY.get(i));
//            }
//
//        }
//
//
//        String uri;
//        int imageResource;
//        Drawable myDrawable;
//        Log.d("IMG", ""+img);
//        Log.d("SERVICES SIZE",""+services.size());
//        int index = 0;
//
//        for (Map.Entry<Integer, Nodes> servicesf :  services.entrySet()) {
//            switch (services.size()){
//
//                case 1:
//                    uri = "@drawable/img"+img;
//                    imageResource = getResources().getIdentifier(uri, null, getPackageName());
//                    myDrawable = getResources().getDrawable(imageResource);
//
//                    if(index == 0){
//                        imageOne = new ImageView(this);
//                        imageOne.setLayoutParams(new ViewGroup.LayoutParams(60, 60));
//                        imageOne.setImageDrawable(myDrawable);
//                        imageOne.setX(servicesf.getValue().getLocationX() - 30);
//                        imageOne.setY(servicesf.getValue().getLocationY() - 60);
//
//                        // Adds the view to the layout
//                        layout.addView(imageOne);
//                        break;
//                    }
//                case 2:
//                    uri = "@drawable/img"+img;
//                    imageResource = getResources().getIdentifier(uri, null, getPackageName());
//                    myDrawable = getResources().getDrawable(imageResource);
//                    if(index == 0){
//                        imageOne = new ImageView(this);
//                        imageOne.setLayoutParams(new ViewGroup.LayoutParams(60, 60));
//                        imageOne.setImageDrawable(myDrawable);
//                        imageOne.setX(servicesf.getValue().getLocationX() - 30);
//                        imageOne.setY(servicesf.getValue().getLocationY() - 60);
//
//                        // Adds the view to the layout
//                        layout.addView(imageOne);
//                        index++;
//                        break;
//                    }else if (index == 1){
//                        imageTwo = new ImageView(this);
//                        imageTwo.setLayoutParams(new ViewGroup.LayoutParams(60, 60));
//                        imageTwo.setImageDrawable(myDrawable);
//                        imageTwo.setX(servicesf.getValue().getLocationX() - 30);
//                        imageTwo.setY(servicesf.getValue().getLocationY() - 60);
//
//                        // Adds the view to the layout
//                        layout.addView(imageTwo);
//                        break;
//                    }
//                    break;
//                case 3:
//                    uri = "@drawable/img"+img;
//                    imageResource = getResources().getIdentifier(uri, null, getPackageName());
//                    myDrawable = getResources().getDrawable(imageResource);
//                    if(index == 0){
//                        imageOne.setLayoutParams(new ViewGroup.LayoutParams(60, 60));
//                        imageOne.setImageDrawable(myDrawable);
//                        imageOne.setX(servicesf.getValue().getLocationX() - 30);
//                        imageOne.setY(servicesf.getValue().getLocationY() - 60);
//
//                        // Adds the view to the layout
//                        layout.addView(imageOne);
//                        index++;
//                        break;
//                    }else if(index == 1){
//                        imageTwo = new ImageView(this);
//                        imageTwo.setLayoutParams(new ViewGroup.LayoutParams(60, 60));
//                        imageTwo.setImageDrawable(myDrawable);
//                        imageTwo.setX(servicesf.getValue().getLocationX() - 30);
//                        imageTwo.setY(servicesf.getValue().getLocationY() - 60);
//
//                        // Adds the view to the layout
//                        layout.addView(imageTwo);
//                        index++;
//                        break;
//                    }else if (index == 2){
//                        imageThree = new ImageView(this);
//                        imageThree.setLayoutParams(new ViewGroup.LayoutParams(60, 60));
//                        imageThree.setImageDrawable(myDrawable);
//                        imageThree.setX(servicesf.getValue().getLocationX() - 30);
//                        imageThree.setY(servicesf.getValue().getLocationY() - 60);
//
//                        // Adds the view to the layout
//                        layout.addView(imageThree);
//                        index++;
//                        break;
//                    }
//                    break;
//                case 4:
//                    uri = "@drawable/img"+img;
//                    imageResource = getResources().getIdentifier(uri, null, getPackageName());
//                    myDrawable = getResources().getDrawable(imageResource);
//                    if(index == 0){
//                        imageOne = new ImageView(this);
//                        imageOne.setLayoutParams(new ViewGroup.LayoutParams(60, 60));
//                        imageOne.setImageDrawable(myDrawable);
//                        imageOne.setX(servicesf.getValue().getLocationX() - 30);
//                        imageOne.setY(servicesf.getValue().getLocationY() - 60);
//
//                        // Adds the view to the layout
//                        layout.addView(imageOne);
//                        index++;
//                        break;
//                    }else if(index == 1){
//                        imageTwo.setLayoutParams(new ViewGroup.LayoutParams(60, 60));
//                        imageTwo.setImageDrawable(myDrawable);
//                        imageTwo.setX(servicesf.getValue().getLocationX() - 30);
//                        imageTwo.setY(servicesf.getValue().getLocationY() - 60);
//
//                        // Adds the view to the layout
//                        layout.addView(imageTwo);
//                        index++;
//                        break;
//                    }else if(index == 2){
//
//                        imageThree.setLayoutParams(new ViewGroup.LayoutParams(60, 60));
//                        imageThree.setImageDrawable(myDrawable);
//                        imageThree.setX(servicesf.getValue().getLocationX() - 30);
//                        imageThree.setY(servicesf.getValue().getLocationY() - 60);
//
//                        // Adds the view to the layout
//                        layout.addView(imageThree);
//                        index++;
//                        break;
//                    }else if (index == 3){
//                        imageFour = new ImageView(this);
//                        imageFour.setLayoutParams(new ViewGroup.LayoutParams(60, 60));
//                        imageFour.setImageDrawable(myDrawable);
//                        imageFour.setX(servicesf.getValue().getLocationX() - 30);
//                        imageFour.setY(servicesf.getValue().getLocationY() - 60);
//
//                        // Adds the view to the layout
//                        layout.addView(imageFour);
//                        index++;
//                        break;
//                    }
//                    break;
//            }
//        }
//    }

