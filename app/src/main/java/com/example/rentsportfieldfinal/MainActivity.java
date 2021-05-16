package com.example.rentsportfieldfinal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import static androidx.core.content.ContextCompat.getSystemService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView sintetic, sala, tenis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isNetworkAvailable()) {
            sintetic = (ImageView) findViewById(R.id.imgViewSintetic);
            sala = (ImageView) findViewById(R.id.imgViewSalaSport);
            tenis = (ImageView) findViewById(R.id.imgViewTenis);
            sintetic.setOnClickListener(this);
            tenis.setOnClickListener(this);
            sala.setOnClickListener(this);
        }
        else
        {
            AlertDialog.Builder a_builder = new AlertDialog.Builder(this);
            a_builder.setMessage("Trebuie să vă conectați la internet!")
                    .setCancelable(false)
                    .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }) ;
            AlertDialog alert = a_builder.create();
            alert.setTitle("CSO Cugir");
            alert.show();
        }
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(this, Rent.class);
        switch (view.getId()) {
            case R.id.imgViewSintetic:
                i.putExtra("option","sintetic");
                break;
            case R.id.imgViewSalaSport:
                i.putExtra("option","sala");
                break;
            case R.id.imgViewTenis:
                i.putExtra("option","tenis");
        }
        startActivity(i);
    }

    public boolean isNetworkAvailable() {
        try
        {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (manager!=null){
                networkInfo = manager.getActiveNetworkInfo();
            }
            return networkInfo != null;
        } catch (NullPointerException e){
            return false;
        }
    }

}