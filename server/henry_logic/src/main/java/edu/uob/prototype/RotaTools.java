package edu.uob.prototype;

import java.time.LocalDate;
import java.util.ArrayList;

public class RotaTools {

    static void createAllShiftsNotWorking(Rota rota) {
        LocalDate startDate = rota.getStartDate();
        LocalDate endDate = rota.getEndDate();
        ArrayList<String> employeeIds = rota.getEmployeeIds();
        for(String id: employeeIds) {
            for(LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
                rota.addShift(ShiftTypes.NotWorking, date, id);
            }
        }
    }

    static void createRules(Rota rota) {

    }

}
