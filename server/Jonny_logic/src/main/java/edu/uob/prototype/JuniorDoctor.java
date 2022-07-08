package edu.uob.prototype;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;

public class JuniorDoctor {
    private String name;
    private Hashtable<LocalDate, Shifts> shifts;
    private final Hashtable<LocalDate, NotOnCallRequestType> notOnCallRequest;
    private final Hashtable<LocalDate, LeaveType> studyOrAnnualLeave;
    private int longDays;
    private int nights;
    private int totalOnCall;
    private int theatre;
    private int weekends;
    private boolean painWeek;
    private final int setLongDays;
    private final int setNights;
    private final int setTheatre;
    private final int setWeekends;
    private LocalDate painWeekStartDate;

    JuniorDoctor(int hours){
        shifts = new Hashtable<>();
        this.setLongDays = 11 * hours;
        this.setNights = 11 * hours;
        this.setTheatre = 32 * hours;
        if(hours == 1) {
            this.setWeekends = 3;
        }else{
            this.setWeekends = 2;
        }
        notOnCallRequest = new Hashtable<>();
        studyOrAnnualLeave = new Hashtable<>();
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

    public void reduceTheatre(int value) {
        this.theatre = theatre-value;
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

    public void setPainWeek(){this.painWeek = true;}

    public boolean getPainWeek(){return this.painWeek;}

    public LocalDate getPainWeekStartDate(){return this.painWeekStartDate;}

    public void setPainWeekStartDate(LocalDate date){
        this.painWeekStartDate = date;
    }

    public void addNotOnCallRequest(LocalDate date, NotOnCallRequestType type){
        this.notOnCallRequest.put(date, type);
    }
    public Hashtable<LocalDate, NotOnCallRequestType> getNotOnCallRequest(){
        return this.notOnCallRequest;
    }

    public NotOnCallRequestType getNotOnCallRequestType(LocalDate date){
        return this.notOnCallRequest.get(date);
    }

    public void addAnnualOrStudyLeaveRequest(LocalDate date, LeaveType type){
        this.studyOrAnnualLeave.put(date, type);
    }
    public Hashtable<LocalDate, LeaveType> getAnnualOrStudyLeaveRequest(){
        return this.studyOrAnnualLeave;
    }

    public LeaveType getAnnualOrStudyLeaveRequestType(LocalDate date){
        return this.studyOrAnnualLeave.get(date);
    }

}
