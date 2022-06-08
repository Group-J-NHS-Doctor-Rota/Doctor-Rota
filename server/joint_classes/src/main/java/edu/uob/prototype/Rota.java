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
    }

    public void addShift(Shift shift, LocalDate date, String EmployeeId) {
        if(!shifts.containsKey(EmployeeId)) {
            shifts.put(EmployeeId, new HashMap<>());
        }
        shifts.get(EmployeeId).put(date, shift);
    }

    // TODO complete this to update cost
    public void refreshRules() {

    }

    //TODO complete this
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
