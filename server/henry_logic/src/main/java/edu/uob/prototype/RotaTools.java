package edu.uob.prototype;

import java.time.LocalDate;
import java.util.ArrayList;

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

}
