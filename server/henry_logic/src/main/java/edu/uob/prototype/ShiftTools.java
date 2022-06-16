package edu.uob.prototype;

public class ShiftTools {

    // Return false if swap not possible (i.e. incompatible or immutable)
    public static boolean swapShifts(Shift shift1, Shift shift2) {
        Shift shiftTemp = copyShift(shift1);
        shift1.updateType(shift2.getType());
        shift2.updateType(shiftTemp.getType());
        return true;
    }

    public static Shift copyShift(Shift shift) {
        return new Shift(shift.getType());
    }
}
