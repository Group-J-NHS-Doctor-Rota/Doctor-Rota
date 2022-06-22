package edu.uob.prototype;

public class ShiftTools {

    // Return false if swap not possible (i.e. incompatible or immutable)
    public static boolean swapShifts(Shift shift1, Shift shift2) {
        if(!bothSameEmployee(shift1, shift2)) {
            return false;
        }
        performSwap(shift1, shift2);
        return true;
    }

    private static boolean bothSameEmployee(Shift shift1, Shift shift2) {
        return shift1.getEmployeeId() == shift2.getEmployeeId();
    }

    private static boolean bothWeekendOrBothWeek(Shift shift1, Shift shift2) {
        if(LocalDateTools.isWeekend(shift1.getDate()) && LocalDateTools.isWeekend(shift2.getDate())) {
            return true;
        } else {
            return !LocalDateTools.isWeekend(shift1.getDate()) && !LocalDateTools.isWeekend(shift2.getDate());
        }
    }

    private static boolean isInvalidSwap(Shift shift1, Shift shift2) {
        return !bothSameEmployee(shift1, shift2) || !bothWeekendOrBothWeek(shift1, shift2);
    }

    private static void performSwap(Shift shift1, Shift shift2) {
        Shift shiftTemp = copyShift(shift1);
        shift1.updateShift(shift2.getType(), shift2.getEmployeeId(), shift2.getDate());
        shift2.updateShift(shiftTemp.getType(), shiftTemp.getEmployeeId(), shiftTemp.getDate());
    }

    public static Shift copyShift(Shift shift) {
        return new Shift(shift.getType(), shift.getEmployeeId(), shift.getDate());
    }
}
