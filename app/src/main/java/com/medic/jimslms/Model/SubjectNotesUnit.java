package com.medic.jimslms.Model;

import java.util.List;

public class SubjectNotesUnit {
    String unitNo;
    List<SubjectNotes> notesList;

    public SubjectNotesUnit() {
    }

    public SubjectNotesUnit(String unitNo, List<SubjectNotes> notesList) {
        this.unitNo = unitNo;
        this.notesList = notesList;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public List<SubjectNotes> getNotesList() {
        return notesList;
    }

    public void setNotesList(List<SubjectNotes> notesList) {
        this.notesList = notesList;
    }
}
