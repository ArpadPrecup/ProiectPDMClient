package com.example.rentsportfieldfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;




import java.util.LinkedHashMap;
import java.util.Map;


public class FreeHours extends AppCompatActivity implements View.OnClickListener{

    TextView questionHour;
    DatabaseReference database, newRef;
    Bundle data;
    Map <String, Boolean> hours;
    String option;
    String strSelectedYear;
    String strSelectedMonth;
    String strSelectedDay;
    String strSelectedHour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_hours);
        data = getIntent().getExtras();
        questionHour = (TextView)findViewById(R.id.txtViewQuestionHour);
        //FirebaseDatabase.getInstance().setLogLevel(Logger.Level.INFO);
        strSelectedYear = data.getString("year");
        strSelectedMonth = data.getString("month");
        strSelectedDay = data.getString("day");
        option = data.getString("option");
        String showSelectedDay = strSelectedDay;
        String showSelectedMonth = strSelectedMonth;
        if (Integer.parseInt(strSelectedDay) < 10)
            showSelectedDay = "0" + strSelectedDay;
        if (Integer.parseInt(strSelectedMonth) < 10)
            showSelectedMonth = "0" + strSelectedMonth;
        database = FirebaseDatabase.getInstance().getReference();
        hours = new LinkedHashMap<String, Boolean>();
        for (int j = 0; j<24 ;j+=1)
            hours.put(Integer.toString(j), true);
        questionHour.setText("Alegeți ora de închiriere pentru " + option + " în data de " + showSelectedDay+"."+showSelectedMonth+"."+strSelectedYear);
        newRef = database.child(option).child(strSelectedYear).child(strSelectedMonth).child(strSelectedDay);
        newRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!option.equals("tenis"))
                {
                    for (DataSnapshot d: dataSnapshot.getChildren()) {
                        hours.put(d.getKey(), false);
                        //Log.i("hAI FRAAA", d.getKey());

                    }
                }
                else
                {
                    for (DataSnapshot d: dataSnapshot.getChildren())
                    {
                        //Log.i("IA ZI MI SA VAD", d.getKey() + " SI CHILDNREN " + d.getValue());
                        Log.i(" key + Nr copii", d.getKey() + " " + d.getChildrenCount() + " ");
                        if (d.getChildrenCount() >= 4)
                        {
                            hours.put(d.getKey(), false);
                        }
                        /*for (DataSnapshot dc: d.getChildren())
                        {
                            Log.i("Key + Chidlren",dc.getKey()+ " "+dc.getValue() + " " + dc.getChildrenCount());
                        }*/
                    }
                }
                LinearLayout lLayout = (LinearLayout)findViewById(R.id.LinearLayoutFreeHours);
                for (Map.Entry<String,Boolean> entry : hours.entrySet())
                {
                    Log.i("Key, value", entry.getKey() + " valoare " + entry.getValue());
                    if (entry.getValue() == true)
                    {
                    Button newButton = new Button(FreeHours.this);
                    int h = Integer.parseInt((String)(entry.getKey()));
                    if (h < 10)
                        newButton.setText("0"+entry.getKey() + ":00");
                    else
                        newButton.setText(entry.getKey()+":00");
                    newButton.setId(2000 + Integer.parseInt(entry.getKey()));
                    newButton.setGravity(Gravity.CENTER);
                    newButton.setBackgroundResource(R.drawable.btn_design);
                    newButton.setTextColor(Color.WHITE);
                    newButton.setOnClickListener(FreeHours.this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(5,0,5,5);
                    newButton.setLayoutParams(params);
                    lLayout.addView(newButton);
                }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("Eroare","nOT GOOD MATE");
                throw databaseError.toException();

            }
        });



    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {
        strSelectedHour = Integer.toString(view.getId() - 2000);
        if (!option.equals("tenis")) {
            Intent i = new Intent(this, Final.class);
            i.putExtra("option", option);
            i.putExtra("year", strSelectedYear);
            i.putExtra("month", strSelectedMonth);
            i.putExtra("day", strSelectedDay);
            i.putExtra("hour", strSelectedHour);
            startActivity(i);
        }
        else
        {
            Intent i = new Intent(this, FreeTenisCourts.class);
            i.putExtra("option", option);
            i.putExtra("year", strSelectedYear);
            i.putExtra("month", strSelectedMonth);
            i.putExtra("day", strSelectedDay);
            i.putExtra("hour", strSelectedHour);
            startActivity(i);
        }
    }
}