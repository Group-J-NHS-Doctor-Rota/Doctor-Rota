package edu.uob.prototype;

import java.time.LocalDate;
import java.util.Hashtable;

public class JuniorDoctor {
    private String name;
    public Hashtable<LocalDate, Shifts> shifts;
    private int longDays;
    private int nights;
    private int totalOnCall;
    private int theatre;
    private int weekends;
    private final int setLongDays;
    private final int setNights;
    private final int setTheatre;
    private final int setWeekends;

    JuniorDoctor(){
        shifts = new Hashtable<>();
        this.setLongDays = 11;
        this.setNights = 11;
        this.setTheatre = 32;
        this.setWeekends = 3;
    }

    public void resetDoctor(){
        longDays = setLongDays;
        nights = setNights;
        theatre = setTheatre;
        weekends = setWeekends;
        totalOnCall = nights + longDays;
        shifts = new Hashtable<>();
    }

    public boolean shiftTaken(LocalDate date){
        return this.shifts.containsKey(date);
    }

    public boolean onShift(LocalDate date){
        Shifts shift = shifts.get(date);
        return shift.equals(Shifts.DAYON) || shift.equals(Shifts.NIGHT) || shift.equals(Shifts.THEATRE);
    }

    public int getWeekends(){return weekends;}

    public void reduceWeekends(){this.weekends--;}

    public Shifts getShiftType(LocalDate date) {
        return this.shifts.get(date);
    }

    public void setShifts(LocalDate date, Shifts shiftType) {
        this.shifts.put(date, shiftType);
    }

    public int getLongDays() {
        return longDays;
    }

    public void reduceLongDays() {
        this.longDays--;
    }

    public void reduceLongDays(int value) {
        this.longDays = this.longDays-value;
    }

    public int getNights() {
        return nights;
    }

    public void reduceNights(int value) {
        this.nights = nights-value;
    }

    public int getTheatre() {
        return theatre;
    }

    public void reduceTheatre() {
        this.theatre--;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfShifts(){
        return shifts.size();
    }

    public int getTotalOnCall(){return totalOnCall;}

    public void reduceTotalOnCall(){this.totalOnCall--;}

    public void reduceTotalOnCall(int value ){this.totalOnCall = totalOnCall-value;}

}
