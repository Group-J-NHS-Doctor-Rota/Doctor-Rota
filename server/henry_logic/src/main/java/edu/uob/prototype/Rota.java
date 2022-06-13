package edu.uob.prototype;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Rota {

    private int cost;
    private ArrayList<Rule> rules;
    private HashMap<String, Employee> employees;
    // Employee id is first key, shift date is second
    private HashMap<String, HashMap<LocalDate, Shift>> shifts;
    private LocalDate startDate;
    private LocalDate endDate;

    public Rota(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        rules = new ArrayList<>();
        employees = new HashMap<>();
        shifts = new HashMap<>();
    }

    public void addEmployee(Employee employee) {
        employees.put(employee.getId(), employee);
        shifts.put(employee.getId(), new HashMap<>());
    }

    public void initialiseShiftsAndRules() {
        createAllShiftsNotWorking();
        createRules();
    }

    private void createAllShiftsNotWorking() {
        for(HashMap<LocalDate, Shift> map: shifts.values()) {
            for(LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
                map.put(date, new Shift(ShiftTypes.NotWorking));
            }
        }
    }

    public void addShift(Shift shift, LocalDate date, String EmployeeId) {
        if(!shifts.containsKey(EmployeeId)) {
            shifts.put(EmployeeId, new HashMap<>());
        }
        shifts.get(EmployeeId).put(date, shift);
    }

    // TODO complete this to generate all rules
    private void createRules() {
        rules = new ArrayList<>();
    }

    // TODO complete this to update cost
    public void refreshRules() {

    }

    public int getCost() {
        return cost;
    }

    public Shift getShift(String EmployeeId, LocalDate date) {
        return shifts.get(EmployeeId).get(date);
    }

    //TODO complete this
    public void swapShifts() {

    }

}
