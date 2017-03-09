package com.rinno.apumanquewayfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainScreenActivity extends AppCompatActivity {


    @BindView(R.id.imageView)
    ImageView imageView;
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
}
