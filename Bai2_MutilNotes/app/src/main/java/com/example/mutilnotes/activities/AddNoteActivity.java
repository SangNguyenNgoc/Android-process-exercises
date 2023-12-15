package com.example.mutilnotes.activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.NotificationCompat;

import com.example.mutilnotes.R;
import com.example.mutilnotes.dao.NoteRepo;
import com.example.mutilnotes.models.NoteModel;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {

    LocalDateTime timer = null;
    private final int[] year = new int[1];
    private final int[] month = new int[1];
    private final int[] day = new int[1];
    private int[] hour = new int[1] ;
    private int[] minute = new int[1];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnew_activity);
        ConstraintLayout backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddNoteActivity.this, MainActivity.class));
            }
        });
        initSaveNote();
        initSetTimer();
        initCurrentTime();
    }

    private void initCurrentTime() {
        TextView currentTime = findViewById(R.id.currentTime);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "EEEE, dd MMMM yyyy HH:mm a",
                new Locale("vi", "VN")
        );
        String formattedDateTime = now.format(formatter);
        currentTime.setText(formattedDateTime);
    }

    private void initSetTimer() {
        ConstraintLayout timeBtn = findViewById(R.id.timeBtn);
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        year[0] = calendar.get(Calendar.YEAR);
        month[0] = calendar.get(Calendar.MONTH);
        day[0] = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddNoteActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        year[0] = selectedYear;
                        month[0] = selectedMonth;
                        day[0] = selectedDay;
                        LocalDate selectedDate = LocalDate.of(year[0], month[0] + 1, day[0]);
                        showTimePickerDialog(selectedDate);
                    }
                }, year[0], month[0], day[0]);
        datePickerDialog.show();
    }

    private void showTimePickerDialog(LocalDate date) {

        Calendar calendar = Calendar.getInstance();
        hour = new int[]{calendar.get(Calendar.HOUR_OF_DAY)};
        minute = new int[]{calendar.get(Calendar.MINUTE)};

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddNoteActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        hour[0] = selectedHour;
                        minute[0] = selectedMinute;
                        LocalTime selectedTime = LocalTime.of(hour[0], minute[0]);
                        timer = LocalDateTime.of(date, selectedTime);
                        Toast.makeText(AddNoteActivity.this, "Time selected: " + timer.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, hour[0], minute[0], false);

        timePickerDialog.show();
    }

    private void initSaveNote() {
        EditText titleTxt = findViewById(R.id.titleInput);
        EditText contentTxt = findViewById(R.id.contentInput);

        ConstraintLayout saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!titleTxt.getText().toString().isEmpty() &&
                        !contentTxt.getText().toString().isEmpty() &&
                        titleTxt.getText() !=null && contentTxt.getText() != null
                ) {
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                            "EEEE, dd MM yyyy HH:mm",
                            new Locale("vi", "VN")
                    );
                    String formattedDateTime = now.format(formatter);
                    long id = NoteRepo.getInstance().insert(AddNoteActivity.this,
                            new NoteModel(
                                    0,
                                    titleTxt.getText().toString(),
                                    contentTxt.getText().toString(),
                                    timer == null ? "Empty" : timer.toString(),
                                    formattedDateTime
                            ));
                    if(id != -1 && timer != null) {
                        scheduleNotification(timer, (int) id, titleTxt.getText().toString(), contentTxt.getText().toString());
                    }
                }
                startActivity(new Intent(AddNoteActivity.this, MainActivity.class));
            }
        });
    }

    public void scheduleNotification(LocalDateTime timer, int noteId, String title, String content) {
        LocalDateTime now = LocalDateTime.now();
        long delay = ChronoUnit.MILLIS.between(now, timer);
        if (delay <= 0) {
            return;
        }
        Toast.makeText(this, "Đã lưu ghi chú", Toast.LENGTH_SHORT).show();
        Intent notificationIntent = new Intent(AddNoteActivity.this, NotificationReceiver.class);
        notificationIntent.putExtra("note_id", noteId);
        notificationIntent.putExtra("title", title);
        notificationIntent.putExtra("content", content);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getBroadcast(AddNoteActivity.this, noteId, notificationIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);
        }
    }


}
