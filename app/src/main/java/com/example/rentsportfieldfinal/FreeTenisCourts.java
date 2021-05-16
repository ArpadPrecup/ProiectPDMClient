package com.example.rentsportfieldfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.LinkedHashMap;
import java.util.Map;

public class FreeTenisCourts extends AppCompatActivity implements View.OnClickListener{
    LinearLayout allLayout, logoLayout;
    TextView question;
    String strSelectedHour, strSelectedDay,strSelectedMonth,strSelectedYear,option,phoneNumber,name;
    Bundle intentData;
    DatabaseReference ref;
    Resources res;
    Map<String, Boolean> courts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_tenis_courts);
        allLayout = (LinearLayout)findViewById(R.id.lLayoutFreeCourts);
        logoLayout = (LinearLayout)findViewById(R.id.lLayoutTitle);
        question = (TextView)findViewById(R.id.txtViewTenisQuestion);
        courts = new LinkedHashMap<String, Boolean>();
        intentData = getIntent().getExtras();
        strSelectedDay = intentData.getString("day");
        strSelectedMonth = intentData.getString("month");
        strSelectedYear = intentData.getString("year");
        strSelectedHour = intentData.getString("hour");
        option = intentData.getString("option");
        String showSelectedDay = strSelectedDay;
        String showSelectedMonth = strSelectedMonth;
        if (Integer.parseInt(strSelectedDay) < 10)
            showSelectedDay = "0" + strSelectedDay;
        if (Integer.parseInt(strSelectedMonth) < 10)
            showSelectedMonth = "0" + strSelectedMonth;
        String dataToShow = showSelectedDay + "."+showSelectedMonth+"."+strSelectedYear;
        int h = Integer.parseInt((String)strSelectedHour);
        String startHour, finishHour;
        if (h < 10)
            startHour = ("0"+strSelectedHour+":00");
        else
            startHour = (strSelectedHour+":00");
        h+=1;
        if (h < 10)
            finishHour = ("0"+ h +":00");
        else
            finishHour= (h +":00");
        question.setText("Ce teren doriți să închiriați pe " + dataToShow + " în intervalul " + startHour + "-" + finishHour);
        for (int l = 1; l<5 ; l+=1)
            courts.put(Integer.toString(l),true);
        ref = FirebaseDatabase.getInstance().getReference().child(option).child(strSelectedYear).child(strSelectedMonth).child(strSelectedDay).child(strSelectedHour);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int courtsAvaible = 4 - (int)(snapshot.getChildrenCount()) + 2;
                allLayout.setWeightSum(6);
                /*LinearLayout.LayoutParams lParams1 = (LinearLayout.LayoutParams) lLayoutScrView.getLayoutParams();
                lParams1.height = 0;
                lParams1.weight = 4f;
                lLayoutScrView.setLayoutParams(lParams1);*/
                LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) logoLayout.getLayoutParams();
                lParams.weight = 1f;
                logoLayout.setLayoutParams(lParams);
               /* LinearLayout.LayoutParams qParams = (LinearLayout.LayoutParams) question.getLayoutParams();
                qParams.weight = 0.5f;
                question.setLayoutParams(qParams);*/
                for (DataSnapshot d: snapshot.getChildren()) {
                    courts.put(d.getKey(), false);
                }
                res = getResources();
                for (Map.Entry<String,Boolean> entry : courts.entrySet())
                {
                    if (entry.getValue() == true) {
                        String msg = "";
                        //Log.i("Key, value", entry.getKey() + " valoare " + entry.getValue());
                        /*ImageView image = new ImageView(FreeTenisCourts.this);
                        msg = option + entry.getKey();
                        Log.i("Poza", msg + "nr ala " + courtsAvaible);
                        int resId = res.getIdentifier(msg, "drawable", getPackageName());
                        image.setImageResource(resId);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.2f);
                        lp.setMargins(5, 10, 5, 10);
                        //image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,1f));
                        image.setLayoutParams(lp);

                        image.setScaleType(ImageView.ScaleType.FIT_XY);
                        image.setBackgroundResource(R.drawable.rounded_shape);
                        image.setClipToOutline(true);
                        allLayout.addView(image);*/
                        msg = option + entry.getKey();
                        int resId = res.getIdentifier(msg, "drawable", getPackageName());
                        RoundedImageView riv = new RoundedImageView(FreeTenisCourts.this);
                        riv.setTag(msg);
                        riv.setId(Integer.parseInt(entry.getKey()));
                        riv.setScaleType(ImageView.ScaleType.FIT_XY);
                        riv.setCornerRadius((float) 15);
                        riv.setBorderWidth((float) 2);
                        riv.setBorderColor(Color.BLACK);
                        riv.mutateBackground(true);
                        riv.setImageResource(resId);
                        //riv.setBackground(backgroundDrawable);
                        riv.setOval(false);
                       // riv.setTileModeX(Shader.TileMode.REPEAT);
                        //riv.setTileModeY(Shader.TileMode.REPEAT);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.2f);
                        lp.setMargins(2, 5, 2, 0);
                        riv.setLayoutParams(lp);
                        riv.setOnClickListener(FreeTenisCourts.this);
                        allLayout.addView(riv);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onClick(View view) {
            Log.i("optiunea de teren", view.getTag() + " " + view.getId());
        Intent i = new Intent(this,Final.class);
        i.putExtra("tenisCourt", Integer.toString(view.getId()));
        i.putExtra("option", option);
        i.putExtra("year", strSelectedYear);
        i.putExtra("month", strSelectedMonth);
        i.putExtra("day", strSelectedDay);
        i.putExtra("hour", strSelectedHour);
        Log.i("Extraurile", option + " " + strSelectedYear + " " + strSelectedMonth + " " + strSelectedDay +" " + strSelectedHour + " " + view.getId());
        startActivity(i);
    }
}