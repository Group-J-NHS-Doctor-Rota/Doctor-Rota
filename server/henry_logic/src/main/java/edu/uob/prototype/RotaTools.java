package edu.uob.prototype;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class RotaTools {

    public static void createAllShiftsNotWorking(Rota rota) {
        LocalDate startDate = rota.getStartDate();
        LocalDate endDate = rota.getEndDate();
        ArrayList<String> employeeIds = rota.getEmployeeIds();
        for(String id: employeeIds) {
            for(LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
                rota.addShift(ShiftTypes.NotWorking, date, id);
            }
        }
    }

    public static void createRules(Rota rota) {
        createNight46HoursOffRules(rota);
        rota.refreshRules();
    }

    private static void createNight46HoursOffRules(Rota rota) {
        LocalDate startDate = rota.getStartDate();
        LocalDate endDate = rota.getEndDate();
        ArrayList<String> employeeIds = rota.getEmployeeIds();
        Shift s1, s2, s3;
        for(String id: employeeIds) {
            // Last two days checked, but not last day
            for(LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
                s1 = rota.getShift(id, date);
                s2 = rota.getShift(id, date.plusDays(1));
                s3 = rota.getShift(id, date.plusDays(2));
                rota.addRule(new Night46HoursOffRule(s1, s2, s3));
            }
        }
    }

    // TODO simplify and functionalise this
    public static void createAllShifts(Rota rota) {
        ArrayList<String> employeeIds = rota.getEmployeeIds();
        int numberOfEmployees = employeeIds.size();
        int numberOfDays = rota.getNumberOfDays();
        double numberOfWeeks = rota.getNumberOfDays() / 7.0;
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
        //for(LocalDate date = rota.getStartDate(); LocalDateTools.isLarger(rota.getEndDate().plusDays(1), date); date = date.plusDays(1)) {
        LocalDate day = rota.getStartDate();
        while(day.isBefore(rota.getEndDate().plusDays(1))) {
            if(LocalDateTools.isWeekend(day)) {
                employeeId1 = weekendNights.get(0);
                rota.addShift(ShiftTypes.WeekendNight, day, employeeId1);
                rota.addShift(ShiftTypes.WeekendNight, day.plusDays(1), employeeId1);
                weekendNights.remove(0);
                employeeId2 = weekendDays.get(0);
                rota.addShift(ShiftTypes.WeekendDay, day, employeeId2);
                rota.addShift(ShiftTypes.WeekendDay, day.plusDays(1), employeeId2);
                weekendDays.remove(0);
                for(String id : employeeIds) {
                    if(!id.equals(employeeId1) && !id.equals(employeeId2)) {
                        rota.addShift(ShiftTypes.NotWorking, day, id);
                        rota.addShift(ShiftTypes.NotWorking, day.plusDays(1), id);
                        temp = employeeShiftTypes.get(id);
                        temp.put(ShiftTypes.NotWorking, temp.get(ShiftTypes.NotWorking) - 2);
                    }
                }
                day = day.plusDays(7);
            } else {
                day = day.plusDays(1);
            }
        }

        // Schedule weekdays
        //TODO guarantee no one has two on call shifts on the same day
        day = rota.getStartDate();
        while(day.isBefore(rota.getEndDate().plusDays(1))) {
            if(!LocalDateTools.isWeekend(day)) {
                employeeId1 = weekOnCallNights.get(0);
                rota.addShift(ShiftTypes.NightOnCall, day, employeeId1);
                weekOnCallNights.remove(0);
                employeeId2 = weekOnCallDays.get(0);
                rota.addShift(ShiftTypes.DayOnCall, day, employeeId2);
                weekOnCallDays.remove(0);
                for(String id : employeeIds) {
                    if(!id.equals(employeeId1) && !id.equals(employeeId2)) {
                        temp = employeeShiftTypes.get(id);
                        //Todo: this is prone to days with many people not working
                        if(temp.get(ShiftTypes.NotWorking) > temp.get(ShiftTypes.NormalShift)) {
                            rota.addShift(ShiftTypes.NotWorking, day, id);
                            temp.put(ShiftTypes.NotWorking, temp.get(ShiftTypes.NotWorking) - 1);
                        } else {
                            rota.addShift(ShiftTypes.NormalShift, day, id);
                            temp.put(ShiftTypes.NormalShift, temp.get(ShiftTypes.NormalShift) - 1);
                        }
                    }
                }
            }
            day = day.plusDays(1);
        }

        System.out.println(employeeShiftTypes.get("0006"));
        System.out.println(employeeShiftTypes.get("0007"));
        System.out.println(employeeShiftTypes.get("0004"));
        System.out.println(employeeShiftTypes.get("0005"));
        System.out.println(employeeShiftTypes.get("0002"));
        System.out.println(employeeShiftTypes.get("0003"));
        System.out.println(employeeShiftTypes.get("0001"));
        System.out.println(employeeShiftTypes.get("0008"));
    }

}
