package com.example.btqt4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Main extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button captureButton = findViewById(R.id.captureButton);
        Button viewPhotosButton = findViewById(R.id.viewPhotosButton);
        Button scheduleNotificationButton = findViewById(R.id.scheduleNotificationButton);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mở màn hình chụp ảnh
                Intent intent = new Intent(Main.this, MainActivity.class);
                startActivity(intent);
            }
        });

        viewPhotosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển đến ViewPhotosActivity
                Intent intent = new Intent(Main.this, ViewPhotoActivity.class);
                startActivity(intent);
            }
        });

        scheduleNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển đến ViewPhotosActivity
                Intent intent = new Intent(Main.this, AlarmActivity.class);
                startActivity(intent);
            }
        });
    }
}
