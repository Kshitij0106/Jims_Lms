package com.medic.jimslms.Model;

public class Notes {
    String noteId,noteTitle,noteSubject,noteText;

    public Notes() {
    }

    public Notes(String noteId, String noteTitle, String noteSubject, String noteText) {
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.noteSubject = noteSubject;
        this.noteText = noteText;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteSubject() {
        return noteSubject;
    }

    public void setNoteSubject(String noteSubject) {
        this.noteSubject = noteSubject;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
}
