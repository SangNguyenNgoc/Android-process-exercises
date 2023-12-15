package com.example.mutilnotes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mutilnotes.R;
import com.example.mutilnotes.adapters.NoteAdapter;
import com.example.mutilnotes.dao.NoteRepo;
import com.example.mutilnotes.domains.Note;
import com.example.mutilnotes.models.NoteModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.Adapter noteAdapter;
    public RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<NoteModel> noteModels = NoteRepo.getInstance().selectAll(this);
        initNoteList(noteModels);
        ConstraintLayout addNewBtn = findViewById(R.id.addNewNoteBtn);
        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddNoteActivity.class));
            }
        });

        EditText searchText = findViewById(R.id.searchBar);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();
                List<NoteModel> noteModel;
                if(newText.isEmpty()) {
                    noteModel = NoteRepo.getInstance().selectAll(MainActivity.this);
                } else {
                    noteModel = NoteRepo.getInstance().selectByTitle(MainActivity.this, newText);
                }
                initNoteList(noteModel);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initNoteList(List<NoteModel> noteModels) {
        List<Note> notes = noteModels.stream()
                .map(noteModel -> {
                    return new Note(noteModel.getId(), noteModel.getTitle(), noteModel.getContent(), noteModel.getLastModified());
                })
                .collect(Collectors.toList());
        recyclerView = findViewById(R.id.notesView);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
        noteAdapter = new NoteAdapter(notes);
        recyclerView.setAdapter(noteAdapter);
        noteAdapter.notifyDataSetChanged();

    }
}