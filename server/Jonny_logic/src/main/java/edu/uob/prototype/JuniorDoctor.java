package edu.uob.prototype;

import java.time.LocalDate;
import java.util.Hashtable;

public class JuniorDoctor {
    private String name;
    public Hashtable<LocalDate, Shifts> shifts;
    private int longDays;
    private int nights;
    private int theatre;

    JuniorDoctor(){
        shifts = new Hashtable<>();
        this.longDays = 11;
        this.nights = 11;
        this.theatre = 32;
    }

    public boolean shiftTaken(LocalDate date){
        return this.shifts.containsKey(date);
    }
    public boolean onShift(LocalDate date){
        Shifts shift = shifts.get(date);
        return shift.equals(Shifts.DAYON) || shift.equals(Shifts.NIGHT) || shift.equals(Shifts.THEATRE);
    }

    public Shifts getShifts(LocalDate date) {
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
}
