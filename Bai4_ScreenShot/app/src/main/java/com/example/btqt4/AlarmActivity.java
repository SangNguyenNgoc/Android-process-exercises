package com.example.btqt4;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        // Ánh xạ các thành phần giao diện
        DatePicker datePicker = findViewById(R.id.datePicker);
        TimePicker timePicker = findViewById(R.id.timePicker);
        Button buttonSetAlarm = findViewById(R.id.buttonSetAlarm);

        // Gọi hàm để đặt báo thức khi nhấn nút
        buttonSetAlarm.setOnClickListener(view -> {
            // Lấy ngày từ DatePicker
            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int day = datePicker.getDayOfMonth();

            // Lấy giờ và phút từ TimePicker
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            // Gọi hàm để đặt báo thức
            setAlarmManager(year, month, day, hour, minute);
        });
    }

    @SuppressLint("ScheduleExactAlarm")
    private void setAlarmManager(int year, int month, int day, int hour, int minute) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Intent để khởi động BroadcastReceiver hoặc Service
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setAction(AlarmReceiver.ACTION_CAPTURE_SELFIE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        // Lấy thời gian hiện tại và cài đặt thời gian báo thức
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, 0);

        // Đặt báo thức
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


        Toast.makeText(this, "Báo thức đã được đặt thành công!", Toast.LENGTH_SHORT).show();
    }
}
