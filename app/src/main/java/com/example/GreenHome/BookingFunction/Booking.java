package com.example.GreenHome.BookingFunction;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.GreenHome.R;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
//import com.shuhart.stepview.StepView;

public class Booking extends AppCompatActivity {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    Button checkInfor;
    public static LocalDate selectedDate;
    public static List<String> days;
    public static List<String> fakedays;
    ImageButton Adult_d, Adult_u, child_d, child_u,back;
    EditText num_adult, num_child;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        findIDs();
        checkInformation();
        changeCount();
    }

    private void changeCount() {
        //
        int sl_bedroom = Integer.parseInt(num_adult.getText().toString());
        if (sl_bedroom < 1) {
            Adult_d.setVisibility(View.INVISIBLE);
            Adult_u.setVisibility(View.VISIBLE);
        } else if (sl_bedroom >= 1) {
            Adult_d.setVisibility(View.VISIBLE);
            Adult_u.setVisibility(View.VISIBLE);
        }
        Adult_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sl = Integer.parseInt(num_adult.getText().toString());
                int sl_new;
                sl_new = sl - 1;
                num_adult.setText(sl_new + "");
                if (sl_new < 1) {
                    Adult_d.setVisibility(View.INVISIBLE);
                    Adult_u.setVisibility(View.VISIBLE);
                } else if (sl_bedroom >= 1) {
                    Adult_d.setVisibility(View.VISIBLE);
                    Adult_u.setVisibility(View.VISIBLE);
                }
            }
        });
        Adult_u.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sl = Integer.parseInt(num_adult.getText().toString());
                int sl_new;
                sl_new = sl + 1;
                num_adult.setText(sl_new + "");
                if (sl_new < 1) {
                    Adult_d.setVisibility(View.INVISIBLE);
                    Adult_u.setVisibility(View.VISIBLE);
                } else if (sl_new >= 1) {
                    Adult_d.setVisibility(View.VISIBLE);
                    Adult_u.setVisibility(View.VISIBLE);
                }
            }
        });
        //
        int sl_child = Integer.parseInt(num_child.getText().toString());
        if (sl_child < 1) {
            child_d.setVisibility(View.INVISIBLE);
            child_u.setVisibility(View.VISIBLE);
        } else if (sl_child >= 1) {
            child_d.setVisibility(View.VISIBLE);
            child_u.setVisibility(View.VISIBLE);
        }
        child_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sl = Integer.parseInt(num_child.getText().toString());
                int sl_new;
                sl_new = sl - 1;
                num_child.setText(sl_new + "");
                if (sl_new < 1) {
                    child_d.setVisibility(View.INVISIBLE);
                    child_u.setVisibility(View.VISIBLE);
                } else if (sl_new >= 1) {
                    child_d.setVisibility(View.VISIBLE);
                    child_u.setVisibility(View.VISIBLE);
                }
            }
        });
        child_u.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sl = Integer.parseInt(num_child.getText().toString());
                int sl_new;
                sl_new = sl + 1;
                num_child.setText(sl_new + "");
                if (sl_new < 1) {
                    child_d.setVisibility(View.INVISIBLE);
                    child_u.setVisibility(View.VISIBLE);
                } else if (sl_new >= 1) {
                    child_d.setVisibility(View.VISIBLE);
                    child_u.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void checkInformation() {
        checkInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Booking.this, ConfirmBooking.class);
                intent.putExtra("adult",num_adult.getText().toString());
                intent.putExtra("child",num_child.getText().toString());
                startActivity(intent);

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void findIDs() {
        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();
        days = new ArrayList<>();
        fakedays = new ArrayList<>();
        checkInfor = findViewById(R.id.btn_checkinfor);
        Adult_d = findViewById(R.id.num_d_Adults);
        Adult_u = findViewById(R.id.num_u_Adults);
        child_d = findViewById(R.id.num_d_children);
        child_u = findViewById(R.id.num_u_children);
        num_adult = findViewById(R.id.num_count_Adults);
        num_child = findViewById(R.id.num_count_children);
        back = findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("");
            } else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonthAction(View view) {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction(View view) {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

}



