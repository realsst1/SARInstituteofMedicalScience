package com.example.shreyesh.sarinstituteofmedicalscience;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static java.util.Locale.US;

public class BookAppointmentActivity extends AppCompatActivity {

    private Toolbar bookAppointmentToolbar;
    private EditText appointmentDate, appointmentTime;
    private Button confirm;
    private String userid, username;
    private DatabaseReference doctorRef, appointmentRef, aRef;
    private FirebaseAuth firebaseAuth;
    private TextView doctorsName;
    private String currentUserID;
    private String[] days;
    private boolean res;
    private Calendar myCalender = Calendar.getInstance();
    private Calendar mcurrentTime = Calendar.getInstance();
    private int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    private int minute = mcurrentTime.get(Calendar.MINUTE);
    private TimePickerDialog mTimePicker;
    private int c = 0;
    private Date actualFrom, actualTo;
    private String from, to, chosenTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        userid = getIntent().getStringExtra("userid");
        username = getIntent().getStringExtra("username");

        //Initialize views
        bookAppointmentToolbar = (Toolbar) findViewById(R.id.bookAppointmentToolbar);
        setSupportActionBar(bookAppointmentToolbar);
        getSupportActionBar().setTitle("Book Appointment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        appointmentDate = (EditText) findViewById(R.id.appointmentDate);
        appointmentTime = (EditText) findViewById(R.id.appointmentTime);
        confirm = (Button) findViewById(R.id.confirmAppointment);
        doctorsName = (TextView) findViewById(R.id.doctorNameAppointment);

        doctorsName.setText("Dr. " + username);

        doctorRef = FirebaseDatabase.getInstance().getReference().child("doctors");
        appointmentRef = FirebaseDatabase.getInstance().getReference().child("appointments");
        aRef = appointmentRef.child(currentUserID);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myCalender.set(Calendar.YEAR, i);
                myCalender.set(Calendar.MONTH, i1);
                myCalender.set(Calendar.DAY_OF_MONTH, i2);
                updateLabel();
            }
        };

        appointmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(BookAppointmentActivity.this, date, myCalender.get(Calendar.YEAR), myCalender.get(Calendar.MONTH), myCalender.get(
                        Calendar.DAY_OF_MONTH)).show();
            }
        });


        appointmentTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mTimePicker = new TimePickerDialog(BookAppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String am_pm = "AM";
                        if (selectedHour > 12) {
                            selectedHour = selectedHour - 12;
                            am_pm = "PM";
                        }
                        appointmentTime.setText(selectedHour + ":" + selectedMinute + " " + am_pm);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String aDate = appointmentDate.getText().toString();
                final String aTime = appointmentTime.getText().toString();
                appointmentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        res = true;
                        c = 0;
                        if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                            final Date date1 = new Date(aDate);
                            final Calendar current = Calendar.getInstance();
                            current.set(Calendar.HOUR, 0);
                            current.set(Calendar.MINUTE, 0);
                            current.set(Calendar.SECOND, 0);
                            current.set(Calendar.MILLISECOND, 0);
                            final Date currentDate = current.getTime();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date1);
                            int day = calendar.get(Calendar.DAY_OF_WEEK);
                            final String dayOfWeek = days[day - 1];
                            System.out.println("Day of week " + dayOfWeek);
                            if (date1.before(currentDate)) {
                                res = false;
                                Toast.makeText(BookAppointmentActivity.this, "Cannot select past date", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            for (DataSnapshot d1 : dataSnapshot.getChildren()) {
                                for (DataSnapshot d : d1.getChildren()) {
                                    final String at = d.child("time").getValue().toString();
                                    final String ad = d.child("date").getValue().toString();
                                    final String id = d.child("doctorid").getValue().toString();
                                    System.out.println(at);
                                    System.out.println(ad);
                                    System.out.println(id);

                                    final SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
                                    final SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");

                                    if (at.equals(aTime) && ad.equals(aDate) && id.equals(userid)) {
                                        res = false;
                                        Toast.makeText(BookAppointmentActivity.this, "Not Available at this time", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    doctorRef.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.child(dayOfWeek.toLowerCase()).getValue().toString().equals("Not Available")) {
                                                res = false;
                                                if (c == 0)
                                                    Toast.makeText(BookAppointmentActivity.this, "Doctor not available on this Day of the Week", Toast.LENGTH_SHORT).show();
                                                c = 1;
                                                return;
                                            } else {
                                                if (c == 0) {
                                                    System.out.println("This is ");
                                                    String time[] = dataSnapshot.child(dayOfWeek.toLowerCase()).getValue().toString().split("-");
                                                    try {
                                                        actualFrom = parseFormat.parse(time[0].substring(0, time[0].length() - 2) + ":00 " + time[0].substring(time[0].length() - 2));
                                                        actualTo = parseFormat.parse(time[1].substring(0, time[1].length() - 2) + ":00 " + time[1].substring(time[1].length() - 2));
                                                        from = displayFormat.format(actualFrom);
                                                        to = displayFormat.format(actualTo);
                                                        from = from.substring(0, from.indexOf(":"));
                                                        to = to.substring(0, to.indexOf(":"));
                                                        Date d = parseFormat.parse(aTime);
                                                        chosenTime = displayFormat.format(d);
                                                        chosenTime = chosenTime.substring(0, chosenTime.indexOf(":"));

                                                    } catch (Exception e) {

                                                    }
                                                    System.out.println(from);
                                                    System.out.println(to);
                                                    if (!(Integer.parseInt(chosenTime) >= Integer.parseInt(from) && Integer.parseInt(chosenTime) <= Integer.parseInt(to))) {
                                                        Toast.makeText(BookAppointmentActivity.this, "Time Outside Doctor's Hours", Toast.LENGTH_SHORT).show();
                                                        c = 1;
                                                        return;
                                                    }
                                                    HashMap<String, String> appointmentMap = new HashMap<>();
                                                    appointmentMap.put("date", aDate);
                                                    appointmentMap.put("time", aTime);
                                                    appointmentMap.put("doctor", username);
                                                    appointmentMap.put("doctorid", userid);
                                                    aRef.push().setValue(appointmentMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(BookAppointmentActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(BookAppointmentActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                    c = 1;
                                                    return;
                                                }
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    if (res == false)
                                        break;
                                }
                                if (res == false)
                                    return;
                            }
                        } else {
                            final Date date1 = new Date(aDate);
                            final Calendar current = Calendar.getInstance();
                            current.set(Calendar.HOUR, 0);
                            current.set(Calendar.MINUTE, 0);
                            current.set(Calendar.SECOND, 0);
                            current.set(Calendar.MILLISECOND, 0);
                            final Date currentDate = current.getTime();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date1);
                            int day = calendar.get(Calendar.DAY_OF_WEEK);
                            final String dayOfWeek = days[day - 1];
                            if (date1.before(currentDate)) {
                                Toast.makeText(BookAppointmentActivity.this, "Cannot select past date", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            doctorRef.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.child(dayOfWeek.toLowerCase()).getValue().toString().equals("Not Available")) {
                                        Toast.makeText(BookAppointmentActivity.this, "Doctor not available on this Day of the Week", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    HashMap<String, String> appointmentMap = new HashMap<>();
                                    appointmentMap.put("date", aDate);
                                    appointmentMap.put("time", aTime);
                                    appointmentMap.put("doctor", username);
                                    appointmentMap.put("doctorid", userid);
                                    aRef.push().setValue(appointmentMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(BookAppointmentActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(BookAppointmentActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, US);
        appointmentDate.setText(simpleDateFormat.format(myCalender.getTime()));
    }

}
