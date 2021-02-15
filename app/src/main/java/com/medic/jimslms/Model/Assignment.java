package com.medic.jimslms.Model;

public class Assignment {
    String assignmentName,assignmentLink;

    public Assignment() {
    }

    public Assignment(String assignmentName, String assignmentLink) {
        this.assignmentName = assignmentName;
        this.assignmentLink = assignmentLink;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getAssignmentLink() {
        return assignmentLink;
    }

    public void setAssignmentLink(String assignmentLink) {
        this.assignmentLink = assignmentLink;
    }
}
