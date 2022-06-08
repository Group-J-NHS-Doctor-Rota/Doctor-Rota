package edu.uob.prototype;

import java.time.LocalDate;
import java.util.ArrayList;

public class Employee {

    private String id;
    private ArrayList<LocalDate> leaveDays;
    private ArrayList<LocalDate> requestLeaveDays;

    public Employee(String id) {
        this.id = id;
        leaveDays = new ArrayList<>();
        requestLeaveDays = new ArrayList<>();
    }

    public void addLeaveDay(LocalDate day) {
        leaveDays.add(day);
    }

    public void addRequestLeaveDay(LocalDate day) {
        requestLeaveDays.add(day);
    }

    public String getId() {
        return id;
    }

    public ArrayList<LocalDate> getLeaveDays() {
        return leaveDays;
    }

    public ArrayList<LocalDate> getRequestLeaveDays() {
        return requestLeaveDays;
    }

}
