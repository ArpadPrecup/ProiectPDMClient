package com.example.rentsportfieldfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Final extends AppCompatActivity {
    String strSelectedHour, strSelectedDay,strSelectedMonth,strSelectedYear,option,phoneNumber,name, strSelectedCourt;
    TextView txtViewData, txtViewHour,txtViewOption;
    Button btnFinalRent;
    Bundle intentData;
    DatabaseReference ref, refTenis, countRef;
    EditText edtTextName, edtTextPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        intentData = getIntent().getExtras();
        strSelectedDay = intentData.getString("day");
        strSelectedMonth = intentData.getString("month");
        strSelectedYear = intentData.getString("year");
        strSelectedHour = intentData.getString("hour");
        option = intentData.getString("option");
        if (option.equals("tenis"))
            strSelectedCourt = intentData.getString("tenisCourt");
        txtViewOption = (TextView)findViewById(R.id.txtViewFinalOption);
        txtViewHour = (TextView)findViewById(R.id.txtViewFinalHour);
        txtViewData = (TextView)findViewById(R.id.txtViewFinalData);
        btnFinalRent = (Button)findViewById(R.id.btnFinish);
        edtTextName = (EditText)findViewById(R.id.edtTextName);
        edtTextPhone = (EditText)findViewById(R.id.edtTextPhoneNumber);
        String showSelectedDay = strSelectedDay;
        String showSelectedMonth = strSelectedMonth;
        if (Integer.parseInt(strSelectedDay) < 10)
            showSelectedDay = "0" + strSelectedDay;
        if (Integer.parseInt(strSelectedMonth) < 10)
            showSelectedMonth = "0" + strSelectedMonth;
        String dataToShow = showSelectedDay + "."+showSelectedMonth+"."+strSelectedYear;
        txtViewData.setText(dataToShow);
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
        txtViewHour.setText(startHour+" - " +finishHour);
        if (!option.equals("tenis"))
            txtViewOption.setText(option);
        else
        {
            String opt = "Terenul nr. " + strSelectedCourt + " " + option;
            txtViewOption.setText(opt);
        }
        ref = FirebaseDatabase.getInstance().getReference().child(option).child(strSelectedYear).child(strSelectedMonth).child(strSelectedDay).child(strSelectedHour);
        countRef = FirebaseDatabase.getInstance().getReference().child(option).child(strSelectedYear).child(strSelectedMonth).child("count");
        btnFinalRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = edtTextPhone.getText().toString().trim();
                name = edtTextName.getText().toString().trim();
                if (name.equals(""))
                {
                    AlertDialog.Builder a_builder = new AlertDialog.Builder(Final.this);
                    a_builder.setMessage("Introduceți numele!")
                            .setCancelable(false)
                            .setNegativeButton("Am înțeles",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }) ;
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("CSO Cugir");
                    alert.show();
                }
                else
                if (phoneNumber.equals(""))
                {
                    AlertDialog.Builder a_builder = new AlertDialog.Builder(Final.this);
                    a_builder.setMessage("Introduceți numărul de telefon!")
                            .setCancelable(false)
                            .setNegativeButton("Am înțeles",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }) ;
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("CSO Cugir");
                    alert.show();
                }
                else {
                    if (option.equals("tenis"))
                    {
                        refTenis = ref.child(strSelectedCourt);
                        refTenis.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    AlertDialog.Builder a_builder = new AlertDialog.Builder(Final.this);
                                    a_builder.setMessage("Cineva tocmai a închiriat înaintea dumneavoastră acest teren! Selectați din nou o oră")
                                            .setCancelable(false)
                                            .setNegativeButton("Aleg altă oră", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent j = new Intent(Final.this, FreeHours.class);
                                                    startActivity(j);
                                                }
                                            });
                                    AlertDialog alert = a_builder.create();
                                    alert.setTitle("CSO Cugir");
                                    alert.show();
                                }
                                else
                                {
                                    //Log.i("EE", "Key " + snapshot.getKey() +  " copii " + snapshot.getChildrenCount());
                                    Order order = new Order(name, phoneNumber);

                                    countRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            System.out.println(snapshot.getValue() + "eee " + snapshot.getKey());
                                            if (snapshot.getValue() == null)
                                            {
                                                countRef.setValue("1");
                                            }
                                            else
                                            {
                                                Long count = Long.parseLong(String.valueOf(snapshot.getValue()));
                                                countRef.setValue(Long.toString(count + 1));
                                            }

                                            System.out.println("gata count");
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    
                                    refTenis.setValue(order);
                                    //refTenis.child("Nume").setValue(name);
                                    //refTenis.child("Telefon").setValue(phoneNumber);
                                    AlertDialog.Builder a_builder = new AlertDialog.Builder(Final.this);
                                    a_builder.setMessage("Ați închiriat cu succes!")
                                            .setCancelable(false)
                                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent j = new Intent(Final.this, MainActivity.class);
                                                    startActivity(j);
                                                }
                                            });
                                    AlertDialog alert = a_builder.create();
                                    alert.setTitle("CSO Cugir");
                                    alert.show();

                                }


                                }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else {
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    AlertDialog.Builder a_builder = new AlertDialog.Builder(Final.this);
                                    a_builder.setMessage("Cineva tocmai a închiriat înaintea dumneavoastră! Ce doriți să faceți?")
                                            .setCancelable(false)
                                            .setNegativeButton("Aleg altă oră", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent j = new Intent(Final.this, FreeHours.class);
                                                    startActivity(j);
                                                }
                                            });
                                    AlertDialog alert = a_builder.create();
                                    alert.setTitle("CSO Cugir");
                                    alert.show();
                                } else {
                                    Order order = new Order(name, phoneNumber);
                                    //ref.child("Nume").setValue(name);
                                    //ref.child("Telefon").setValue(phoneNumber);
                                    countRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            System.out.println(snapshot.getValue() + "eee " + snapshot.getKey());
                                            if (snapshot.getValue() == null)
                                            {
                                                countRef.setValue("1");
                                            }
                                            else
                                            {
                                                Long count = Long.parseLong(String.valueOf(snapshot.getValue()));
                                                countRef.setValue(Long.toString(count + 1));
                                            }

                                            System.out.println("gata count");
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                        //TimeUnit.SECONDS.sleep(2);
                                        ref.setValue(order);
                                        System.out.println("gata try1");
                                        AlertDialog.Builder a_builder = new AlertDialog.Builder(Final.this);
                                        a_builder.setMessage("Ați închiriat cu succes!")
                                                .setCancelable(false)
                                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        System.out.println("gata try");
                                                        Intent j = new Intent(Final.this, MainActivity.class);
                                                        startActivity(j);
                                                    }
                                                });
                                        AlertDialog alert = a_builder.create();
                                        alert.setTitle("CSO Cugir");
                                        alert.show();


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
            }
        });

    }
}