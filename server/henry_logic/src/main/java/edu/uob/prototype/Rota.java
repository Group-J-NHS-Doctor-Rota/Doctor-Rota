package edu.uob.prototype;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import static java.time.temporal.ChronoUnit.DAYS;

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

    public int getNumberOfDays() {
        // +1 as we inclusively want the start and end dates
        return (int) (DAYS.between(startDate, endDate) + 1);
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

    public Rule getNextBrokenRule() {
        Rule output = null;
        for(Rule rule: rules) {
            if(rule.isBroken()) {
                output = rule;
            }
        }
        return output;
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

    // Should not always be true
    public boolean resolveRules() {
        Rule rule = getNextBrokenRule();
        while(rule != null) {
            // TODO what if rule not fixed
            rule.resolve(this);
            rule = getNextBrokenRule();
        }
        return true;
    }

    //TODO complete this
    public void swapShifts() {

    }

    //TODO improve this
    @Override
    public String toString() {
        String str = "";
        String tempStr = "";
        Shift tempShift;
        int stringLength = 12;
        str = "Employees ";
        //https://www.baeldung.com/java-pad-string
        str = String.format("%1$-" + 10 + "s", "Employees");
        for(String employeeId : getEmployeeIds()) {
            str = str.concat("|");
            tempStr = String.format("%1$-" + stringLength + "s", employeeId);
            str = str.concat(tempStr);
        }
        for(LocalDate date = startDate; LocalDateTools.isLarger(endDate.plusDays(1), date); date = date.plusDays(1)) {
            str = str.concat("\n");
            str = str.concat(date.toString());
            for(String employeeId : getEmployeeIds()) {
                str = str.concat("|");
                tempShift = getShift(employeeId, date);
                if(tempShift != null) {
                    //https://www.baeldung.com/java-pad-string
                    tempStr = String.format("%1$-" + stringLength + "s", tempShift);
                } else {
                    //https://stackoverflow.com/questions/1235179/simple-way-to-repeat-a-string
                    tempStr = new String(new char[stringLength]).replace("\0", " ");
                }
                str = str.concat(tempStr);
            }
        }
        return str;
    }

}
