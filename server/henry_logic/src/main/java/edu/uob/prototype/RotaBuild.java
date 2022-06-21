package edu.uob.prototype;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class RotaBuild {

    public static void main(String[] args) {
        Rota rota1 = new Rota(LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-28"));
        Employee emp1 = new Employee("0001"); rota1.addEmployee(emp1);
        Employee emp2 = new Employee("0002"); rota1.addEmployee(emp2);
        Employee emp3 = new Employee("0003"); rota1.addEmployee(emp3);
        Employee emp4 = new Employee("0004"); rota1.addEmployee(emp4);
        Employee emp5 = new Employee("0005"); rota1.addEmployee(emp5);
        Employee emp6 = new Employee("0006"); rota1.addEmployee(emp6);
        Employee emp7 = new Employee("0007"); rota1.addEmployee(emp7);
        Employee emp8 = new Employee("0008"); rota1.addEmployee(emp8);
        ArrayList<String> employeeIds = rota1.getEmployeeIds();
        int numberOfEmployees = employeeIds.size();
        int numberOfDays = rota1.getNumberOfDays();
        double numberOfWeeks = rota1.getNumberOfDays() / 7.0;
        int numberOfWeekendNights = (int) numberOfWeeks * 2;
        int numberOfWeekendDays = (int) numberOfWeeks * 2;
        int numberOfOnCallDays = numberOfDays;
        int numberOfOnCallNights = numberOfDays;
        int totalMaxHours = (int) (numberOfWeeks*48);
        HashMap<String, HashMap<ShiftTypes, Integer>> employeeShiftTypes = new HashMap<>();
        HashMap<ShiftTypes, Integer> temp;
        for(String id: employeeIds) {
            temp = new HashMap<>();
            temp.put(ShiftTypes.NightOnCall, 0);
            temp.put(ShiftTypes.DayOnCall, 0);
            temp.put(ShiftTypes.NotWorking, 0);
            temp.put(ShiftTypes.NormalShift, 0);
            //temp.put(ShiftTypes.WeekendDay, 0);
            //temp.put(ShiftTypes.WeekendNight, 0);
            employeeShiftTypes.put(id, temp);
        }

        // Get shifts for each employee
        for(int i = 0; i < numberOfDays; i++) {
            for(String id: employeeIds) {
                int hours = 0;
                temp = employeeShiftTypes.get(id);
                hours += temp.get(ShiftTypes.NightOnCall) * new Shift(ShiftTypes.NightOnCall).getHours();
                hours += temp.get(ShiftTypes.DayOnCall) * new Shift(ShiftTypes.DayOnCall).getHours();
                hours += temp.get(ShiftTypes.NormalShift) * new Shift(ShiftTypes.NormalShift).getHours();
                if(hours + 10 > totalMaxHours) {
                    temp.put(ShiftTypes.NotWorking, temp.get(ShiftTypes.NotWorking) + 1);
                } else if(numberOfOnCallNights > 0) {
                    temp.put(ShiftTypes.NightOnCall, temp.get(ShiftTypes.NightOnCall) + 1);
                    numberOfOnCallNights--;
                } else if (numberOfOnCallDays > 0) {
                    temp.put(ShiftTypes.DayOnCall, temp.get(ShiftTypes.DayOnCall) + 1);
                    numberOfOnCallDays--;
                } else {
                    temp.put(ShiftTypes.NormalShift, temp.get(ShiftTypes.NormalShift) + 1);
                }
            }
        }

        // Create lists of employees for each shift type
        ArrayList<String> weekendNights = new ArrayList<>();
        ArrayList<String> weekendDays = new ArrayList<>();
        ArrayList<String> weekOnCallNights = new ArrayList<>();
        ArrayList<String> weekOnCallDays = new ArrayList<>();
        // List for weekend nights
        int idIndex = 0;
        while(numberOfWeekendNights > 0){
            String employee = employeeIds.get(idIndex);
            temp = employeeShiftTypes.get(employee);
            temp.put(ShiftTypes.NightOnCall, temp.get(ShiftTypes.NightOnCall) - 2);
            //temp.put(ShiftTypes.WeekendNight, temp.get(ShiftTypes.WeekendNight) + 2);
            weekendNights.add(employee);
            idIndex = (idIndex + 1) % numberOfEmployees;
            numberOfWeekendNights -= 2;
        }
        int nightIdIndex = idIndex;
        // List for weekend days
        while(numberOfWeekendDays > 0){
            String employee = employeeIds.get(idIndex);
            temp = employeeShiftTypes.get(employee);
            temp.put(ShiftTypes.DayOnCall, temp.get(ShiftTypes.DayOnCall) - 2);
            //temp.put(ShiftTypes.WeekendDay, temp.get(ShiftTypes.WeekendDay) + 2);
            weekendDays.add(employee);
            idIndex = (idIndex + 1) % numberOfEmployees;
            numberOfWeekendDays -= 2;
        }
        // List for on call week days
        int skip = 0;
        idIndex = 0;
        while(skip < numberOfEmployees){
            String employee = employeeIds.get(idIndex);
            temp = employeeShiftTypes.get(employee);
            if(temp.get(ShiftTypes.NightOnCall) == 0) {
                skip++;
            } else {
                skip = 0;
                weekOnCallNights.add(employee);
                temp.put(ShiftTypes.NightOnCall, temp.get(ShiftTypes.NightOnCall) - 1);
            }
            idIndex = (idIndex + 1) % numberOfEmployees;
        }
        // List for on call week nights
        idIndex = numberOfEmployees / 2;
        skip = 0;
        while(skip < numberOfEmployees){
            String employee = employeeIds.get(idIndex);
            temp = employeeShiftTypes.get(employee);
            if(temp.get(ShiftTypes.DayOnCall) == 0) {
                skip++;
            } else {
                skip = 0;
                weekOnCallDays.add(employee);
                temp.put(ShiftTypes.DayOnCall, temp.get(ShiftTypes.DayOnCall) - 1);
            }
            idIndex = (idIndex + 1) % employeeIds.size();
        }

        System.out.println(weekendNights);
        System.out.println(weekendDays);
        System.out.println(weekOnCallDays);
        System.out.println(weekOnCallNights);

        System.out.println(employeeShiftTypes.get("0006"));
        System.out.println(employeeShiftTypes.get("0007"));
        System.out.println(employeeShiftTypes.get("0004"));
        System.out.println(employeeShiftTypes.get("0005"));
        System.out.println(employeeShiftTypes.get("0002"));
        System.out.println(employeeShiftTypes.get("0003"));
        System.out.println(employeeShiftTypes.get("0001"));
        System.out.println(employeeShiftTypes.get("0008"));

        // Schedule all shifts
        String employeeId1 = "";
        String employeeId2 = "";

        // Schedule weekends
        // TODO What if a Sunday?
        // TODO What if finishes on Saturday?
        // TODO stop all nights or all days weekends
        //for(LocalDate date = rota1.getStartDate(); LocalDateTools.isLarger(rota1.getEndDate().plusDays(1), date); date = date.plusDays(1)) {
        LocalDate day = rota1.getStartDate();
        while(day.isBefore(rota1.getEndDate().plusDays(1))) {
            if(LocalDateTools.isWeekend(day)) {
                employeeId1 = weekendNights.get(0);
                rota1.addShift(ShiftTypes.WeekendNight, day, employeeId1);
                rota1.addShift(ShiftTypes.WeekendNight, day.plusDays(1), employeeId1);
                weekendNights.remove(0);
                employeeId2 = weekendDays.get(0);
                rota1.addShift(ShiftTypes.WeekendDay, day, employeeId2);
                rota1.addShift(ShiftTypes.WeekendDay, day.plusDays(1), employeeId2);
                weekendDays.remove(0);
                for(String id : employeeIds) {
                    if(!id.equals(employeeId1) && !id.equals(employeeId2)) {
                        rota1.addShift(ShiftTypes.NotWorking, day, id);
                        rota1.addShift(ShiftTypes.NotWorking, day.plusDays(1), id);
                        temp = employeeShiftTypes.get(id);
                        temp.put(ShiftTypes.NotWorking, temp.get(ShiftTypes.NotWorking) - 1);
                    }
                }
                day = day.plusDays(7);
            } else {
                day = day.plusDays(1);
            }
        }

        // Schedule week on calls
        //TODO guarantee no one has two on call shifts on the same day
        day = rota1.getStartDate();
        while(day.isBefore(rota1.getEndDate().plusDays(1))) {
            if(!LocalDateTools.isWeekend(day)) {
                employeeId1 = weekOnCallNights.get(0);
                rota1.addShift(ShiftTypes.NightOnCall, day, employeeId1);
                weekOnCallNights.remove(0);
                employeeId2 = weekOnCallDays.get(0);
                rota1.addShift(ShiftTypes.DayOnCall, day, employeeId2);
                weekOnCallDays.remove(0);
            }
            day = day.plusDays(1);
        }

        // Schedule rest



        System.out.println(rota1);

        System.out.println(employeeShiftTypes.get("0006"));
        System.out.println(employeeShiftTypes.get("0007"));
        System.out.println(employeeShiftTypes.get("0004"));
        System.out.println(employeeShiftTypes.get("0005"));
        System.out.println(employeeShiftTypes.get("0002"));
        System.out.println(employeeShiftTypes.get("0003"));
        System.out.println(employeeShiftTypes.get("0001"));
        System.out.println(employeeShiftTypes.get("0008"));

        System.exit(0);
        // Lists of employees for each
        //LocalDate day = rota1.getStartDate();
        //int idIndex = 0;
        // Weekends
        // TODO What if a Sunday?
        // TODO What if finishes on Saturday?
        // TODO stop all nights or all days weekends
        /*idIndex = 0;
        while(day.isBefore(rota1.getEndDate().plusDays(1))) {
            if(LocalDateTools.isWeekend(day)) {
                String employee = employeeIds.get(idIndex);
                idIndex = (idIndex + 1) % employeeIds.size();
                HashMap<ShiftTypes, Integer> temp = employeeShiftTypes.get(employee);
                rota1.addShift(ShiftTypes.WeekendNight, day, employee);
                rota1.addShift(ShiftTypes.WeekendNight, day.plusDays(1), employee);
                temp.put(ShiftTypes.WeekendNight, temp.get(ShiftTypes.WeekendNight) - 2);
                day.plusDays(7);
            } else {
                day.plusDays(1);
            }
        }
        day = rota1.getStartDate();
        while(day.isBefore(rota1.getEndDate().plusDays(1))) {
            if(LocalDateTools.isWeekend(day)) {
                String employee = employeeIds.get(idIndex);
                idIndex = (idIndex + 1) % employeeIds.size();
                HashMap<ShiftTypes, Integer> temp = employeeShiftTypes.get(employee);
                rota1.addShift(ShiftTypes.WeekendDay, day, employee);
                rota1.addShift(ShiftTypes.WeekendDay, day.plusDays(1), employee);
                temp.put(ShiftTypes.WeekendDay, temp.get(ShiftTypes.WeekendDay) - 2);
                day.plusDays(7);
            } else {
                day.plusDays(1);
            }
        }/*
        /*while(day.isBefore(rota1.getEndDate().plusDays(1))) {
            // TODO What if a Sunday?
            // TODO What if finishes on Saturday?
            // TODO stop all nights or all days weekends
            if(LocalDateTools.isWeekend(day)) {
                String employee1 = employeeIds.get(idIndex);
                idIndex = (idIndex + 1) % employeeIds.size();
                HashMap<ShiftTypes, Integer> temp = employeeShiftTypes.get(employee1);
                rota1.addShift(ShiftTypes.NightOnCall, day, employee1);
                rota1.addShift(ShiftTypes.NightOnCall, day.plusDays(1), employee1);
                temp.put(ShiftTypes.NightOnCall, temp.get(ShiftTypes.NightOnCall) - 2);
                String employee2 = employeeIds.get(idIndex);
                idIndex = (idIndex + 1) % employeeIds.size();
                temp = employeeShiftTypes.get(employee2);
                rota1.addShift(ShiftTypes.DayOnCall, day, employee2);
                rota1.addShift(ShiftTypes.DayOnCall, day.plusDays(1), employee2);
                temp.put(ShiftTypes.NightOnCall, temp.get(ShiftTypes.NightOnCall) - 2);
                day = day.plusDays(7);
            } else {
                day = day.plusDays(1);
            }
        }*/
        /*day = rota1.getStartDate();
        idIndex = (idIndex + 1) % employeeIds.size();
        // Weekdays, on call
        while(day.isBefore(rota1.getEndDate().plusDays(1))) {
            if(!LocalDateTools.isWeekend(day)) {
                String employee1 = employeeIds.get(idIndex);
                idIndex = (idIndex + 1) % employeeIds.size();
                HashMap<ShiftTypes, Integer> temp = employeeShiftTypes.get(employee1);
                rota1.addShift(ShiftTypes.NightOnCall, day, employee1);
                temp.put(ShiftTypes.NightOnCall, temp.get(ShiftTypes.NightOnCall) - 1);
                String employee2 = employeeIds.get(idIndex);
                idIndex = (idIndex + 1) % employeeIds.size();
                temp = employeeShiftTypes.get(employee2);
                rota1.addShift(ShiftTypes.DayOnCall, day, employee2);
                temp.put(ShiftTypes.NightOnCall, temp.get(ShiftTypes.NightOnCall) - 1);
            }
            day = day.plusDays(1);
        }*/

        System.out.println(rota1);
        System.out.println(employeeShiftTypes.get("0001"));
        System.out.println(employeeShiftTypes.get("0002"));
        System.out.println(employeeShiftTypes.get("0003"));
        System.out.println(employeeShiftTypes.get("0004"));
        System.out.println(employeeShiftTypes.get("0005"));
        System.out.println(employeeShiftTypes.get("0006"));
        System.out.println(employeeShiftTypes.get("0007"));
        System.out.println(employeeShiftTypes.get("0008"));



        // Assign shifts to days

    }

}
