package com.medic.jimslms.Model;

public class User {

    String uid,userFullName,userEmail,userPhone,userCourse,userSem,userShift,userEnrollmentNo;

    public User() {
    }

    public User(String uid, String userFullName, String userEmail, String userPhone, String userCourse,
                String userSem, String userShift,String userEnrollmentNo) {
        this.uid = uid;
        this.userEnrollmentNo = userEnrollmentNo;
        this.userFullName = userFullName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userCourse = userCourse;
        this.userSem = userSem;
        this.userShift = userShift;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserEnrollmentNo() {
        return userEnrollmentNo;
    }

    public void setUserEnrollmentNo(String userEnrollmentNo) {
        this.userEnrollmentNo = userEnrollmentNo;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserCourse() {
        return userCourse;
    }

    public void setUserCourse(String userCourse) {
        this.userCourse = userCourse;
    }

    public String getUserShift() {
        return userShift;
    }

    public void setUserShift(String userShift) {
        this.userShift = userShift;
    }

    public String getUserSem() {
        return userSem;
    }

    public void setUserSem(String userSem) {
        this.userSem = userSem;
    }

}
