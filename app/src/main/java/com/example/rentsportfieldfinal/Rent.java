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
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

public class Rent extends AppCompatActivity {
    TextView afis;
    CalendarView calendar;
    Button btnCheckFreeHours;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);
        calendar = (CalendarView)findViewById(R.id.cldViewCalendar);
        btnCheckFreeHours = (Button)findViewById(R.id.btnCheckFreeHours);
        afis = (TextView)findViewById(R.id.txtViewOption);
        Bundle data = getIntent().getExtras();
        final String option = data.getString("option").trim();
        //Log.i("Optiunea si option",data.getString("option") + " si option " + option);
        if (option.equals("sala")) {
            afis.setText("În ce dată doriti să închiriați sala?");
        }
        else if(option.equals("tenis"))
        {
        afis.setText("În ce dată doriți să închiriați terenul de " + option + "?");
        }
        else
        {
            afis.setText("În ce dată doriți să închiriați terenul  " + option + "?");
        }

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, final int selected_year, final int selected_month, final int selected_day) {
                final String strSelectedYear = Integer.toString(selected_year);
                final String strSelectedMonth = Integer.toString(selected_month + 1);
                final String strSelectedDay = Integer.toString(selected_day);
                btnCheckFreeHours.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        String currentYear = Integer.toString(calendar.get(Calendar.YEAR));
                        String currentMonth = Integer.toString(calendar.get(Calendar.MONTH) + 1);
                        String currentDay = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
                        String currentHour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
                        //Log.i("Data de azi:", currentYear + currentMonth + currentDay + " " + currentHour);
                        if (Integer.parseInt(currentYear) > Integer.parseInt(strSelectedYear) || (Integer.parseInt(currentYear) == Integer.parseInt(strSelectedYear)
                                && Integer.parseInt(currentMonth) > Integer.parseInt(strSelectedMonth)) || (Integer.parseInt(currentYear) == Integer.parseInt(strSelectedYear)
                                && Integer.parseInt(currentMonth) == Integer.parseInt(strSelectedMonth) && Integer.parseInt(currentDay) > Integer.parseInt(strSelectedDay)))
                        {
                            AlertDialog.Builder a_builder = new AlertDialog.Builder(Rent.this);
                            a_builder.setMessage("Dată aleasă greșit. Mai încercați!")
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
                        {
                            Intent i = new Intent(getApplicationContext(), FreeHours.class);
                            i.putExtra("year",strSelectedYear);
                            i.putExtra("month", strSelectedMonth);
                            i.putExtra("day",strSelectedDay);
                            i.putExtra("option",option);
                            i.putExtra("hourOfCheck",currentHour);
                            //Log.i("Data de la buton", strSelectedYear + " a " + strSelectedMonth + " b " + strSelectedDay);
                            startActivity(i);
                        }

                    }
                });
            }
        });


}
}