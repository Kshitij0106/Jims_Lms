package com.medic.jimslms.Model;

public class SubjectNotes {
    String noteLink,noteName;

    public SubjectNotes() {
    }

    public SubjectNotes(String noteLink, String noteName) {
        this.noteLink = noteLink;
        this.noteName = noteName;
    }

    public String getNoteLink() {
        return noteLink;
    }

    public void setNoteLink(String noteLink) {
        this.noteLink = noteLink;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }
}
