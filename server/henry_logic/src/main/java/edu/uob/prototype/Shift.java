package edu.uob.prototype;

import java.time.LocalDate;

public class Shift {

    private ShiftTypes type;
    private String employeeId;
    private LocalDate date;

    public Shift(ShiftTypes type) {
        this.type = type;
    }

    public Shift(ShiftTypes type, String employeeId, LocalDate date) {
        this.type = type;
        this.employeeId = employeeId;
        this.date = date;
    }

    public boolean isOnCall() {
        return switch (type) {
            case NotWorking, NormalShift -> false;
            default -> true;
        };
    }

    public boolean isWorking() {
        return !type.equals(ShiftTypes.NotWorking);
    }

    public double getHours() {
        return switch (type) {
            case NormalShift -> 10;
            case NotWorking -> 0;
            default -> 12.5;
        };
    }

    public ShiftTypes getType() {
        return type;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void updateType(ShiftTypes type) {
        this.type = type;
    }

    public void updateShift(ShiftTypes type, String employeeId, LocalDate date) {
        this.type = type;
        this.employeeId = employeeId;
        this.date = date;
    }

    @Override
    public String toString() {
        return type.name();
    }

}
