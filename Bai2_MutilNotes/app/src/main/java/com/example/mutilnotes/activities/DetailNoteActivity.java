package com.example.mutilnotes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mutilnotes.R;
import com.example.mutilnotes.dao.NoteRepo;
import com.example.mutilnotes.models.NoteModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class DetailNoteActivity extends AppCompatActivity {

    private EditText titleTxt;
    private EditText contentTxt;
    private int noteId;

    LocalDateTime timer = null;
    private final int[] year = new int[1];
    private final int[] month = new int[1];
    private final int[] day = new int[1];
    private int[] hour = new int[1] ;
    private int[] minute = new int[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);
        titleTxt = findViewById(R.id.titleInput);
        contentTxt = findViewById(R.id.contentInput);
        ConstraintLayout backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailNoteActivity.this, MainActivity.class));
            }
        });
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("note_id")) {
            noteId = intent.getIntExtra("note_id", -1);
            NoteModel noteModel = NoteRepo.getInstance().selectById(this, noteId);
            initModel(noteModel);
            initSaveNote(noteModel);
        }
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

    private void initModel(NoteModel noteModel) {
        titleTxt.setText(noteModel.getTitle());
        contentTxt.setText(noteModel.getContent());
    }

    private void initSaveNote(NoteModel noteModel) {
        ConstraintLayout saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!titleTxt.getText().toString().isEmpty() &&
                        !contentTxt.getText().toString().isEmpty() &&
                        titleTxt.getText() != null && contentTxt.getText() != null
                ) {
                    if(!titleTxt.getText().toString().equals(noteModel.getTitle()) ||
                            !contentTxt.getText().toString().equals(noteModel.getContent())
                    ) {
                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                                "EEEE, dd MM yyyy HH:mm",
                                new Locale("vi", "VN")
                        );
                        String formattedDateTime = now.format(formatter);
                        NoteRepo.getInstance().update(DetailNoteActivity.this,
                                new NoteModel(
                                        noteId,
                                        titleTxt.getText().toString(),
                                        contentTxt.getText().toString(),
                                        timer == null ? noteModel.getTimer() : timer.toString(),
                                        formattedDateTime
                                ));
                    }
                }
                startActivity(new Intent(DetailNoteActivity.this, MainActivity.class));
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        year[0] = calendar.get(Calendar.YEAR);
        month[0] = calendar.get(Calendar.MONTH);
        day[0] = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(DetailNoteActivity.this,
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(DetailNoteActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        hour[0] = selectedHour;
                        minute[0] = selectedMinute;
                        LocalTime selectedTime = LocalTime.of(hour[0], minute[0]);
                        timer = LocalDateTime.of(date, selectedTime);
                        Toast.makeText(DetailNoteActivity.this, "Time selected: " + timer.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, hour[0], minute[0], false);

        timePickerDialog.show();
    }
}