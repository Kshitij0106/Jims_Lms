package com.medic.jimslms.Model;

import java.util.List;

public class AssignmentUnit {
    String unitNo;
    List<Assignment> assignmentList;

    public AssignmentUnit() {
    }

    public AssignmentUnit(String unitNo, List<Assignment> assignmentList) {
        this.unitNo = unitNo;
        this.assignmentList = assignmentList;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public List<Assignment> getAssignmentList() {
        return assignmentList;
    }

    public void setAssignmentList(List<Assignment> assignmentList) {
        this.assignmentList = assignmentList;
    }
}
