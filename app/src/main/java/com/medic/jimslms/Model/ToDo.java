package com.medic.jimslms.Model;

public class ToDo {

    String toDoId,toDoTitle,toDoText;
    int toDoPriority;

    public ToDo() {
    }

    public ToDo(String toDoId, String toDoTitle, String toDoText, int toDoPriority) {
        this.toDoId = toDoId;
        this.toDoTitle = toDoTitle;
        this.toDoText = toDoText;
        this.toDoPriority = toDoPriority;
    }

    public String getToDoId() {
        return toDoId;
    }

    public void setToDoId(String toDoId) {
        this.toDoId = toDoId;
    }

    public String getToDoTitle() {
        return toDoTitle;
    }

    public void setToDoTitle(String toDoTitle) {
        this.toDoTitle = toDoTitle;
    }

    public String getToDoText() {
        return toDoText;
    }

    public void setToDoText(String toDoText) {
        this.toDoText = toDoText;
    }

    public int getToDoPriority() {
        return toDoPriority;
    }

    public void setToDoPriority(int toDoPriority) {
        this.toDoPriority = toDoPriority;
    }
}
