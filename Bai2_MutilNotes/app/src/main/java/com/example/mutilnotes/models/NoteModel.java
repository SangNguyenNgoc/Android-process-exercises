package com.example.mutilnotes.models;

import java.time.LocalDateTime;

public class NoteModel {

    private Integer id;
    private String title;
    private String content;
    private String timer;
    private String lastModified;

    public NoteModel() {
    }

    public NoteModel(Integer id, String title, String content, String timer, String lastModified) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.timer = timer;
        this.lastModified = lastModified;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
}
