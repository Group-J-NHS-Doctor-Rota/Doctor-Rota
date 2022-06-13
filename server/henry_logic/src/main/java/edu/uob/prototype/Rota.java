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

    public ArrayList<String> getEmployeeIds() {
        return new ArrayList<>(employees.keySet());
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void addShift(ShiftTypes shiftType, LocalDate date, String EmployeeId) {
        if(!shifts.containsKey(EmployeeId)) {
            shifts.put(EmployeeId, new HashMap<>());
        }
        shifts.get(EmployeeId).put(date, new Shift(shiftType));
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public void refreshRules() {
        cost = 0;
        for(Rule rule: rules) {
            rule.refresh();
            cost += rule.getCost();
        }
    }

    public int getCost() {
        return cost;
    }

    // If date outside of rota, then employee is not working
    public Shift getShift(String EmployeeId, LocalDate date) {
        if(LocalDateTools.isLarger(startDate,date) || LocalDateTools.isLarger(date, endDate)) {
            return new Shift(ShiftTypes.NotWorking);
        }
        return shifts.get(EmployeeId).get(date);
    }

    //TODO complete this
    public void swapShifts() {

    }

}
