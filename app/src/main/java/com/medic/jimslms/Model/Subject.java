package com.medic.jimslms.Model;

public class Subject {

    private String subjectCode, subjectDescription, subjectName, subjectLink, subjectTime, subjectSyllabus;

    public Subject() {
    }

    public Subject(String subjectCode, String subjectDescription, String subjectName, String subjectLink, String subjectTime, String subjectSyllabus) {
        this.subjectCode = subjectCode;
        this.subjectDescription = subjectDescription;
        this.subjectName = subjectName;
        this.subjectLink = subjectLink;
        this.subjectTime = subjectTime;
        this.subjectSyllabus = subjectSyllabus;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectDescription() {
        return subjectDescription;
    }

    public void setSubjectDescription(String subjectDescription) {
        this.subjectDescription = subjectDescription;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectLink() {
        return subjectLink;
    }

    public void setSubjectLink(String subjectLink) {
        this.subjectLink = subjectLink;
    }

    public String getSubjectTime() {
        return subjectTime;
    }

    public void setSubjectTime(String subjectTime) {
        this.subjectTime = subjectTime;
    }

    public String getSubjectSyllabus() {
        return subjectSyllabus;
    }

    public void setSubjectSyllabus(String subjectSyllabus) {
        this.subjectSyllabus = subjectSyllabus;
    }
}
