package edu.uob.prototype;

public class Shift {

    private ShiftTypes type;

    public Shift(ShiftTypes type) {
        this.type = type;
    }

    public boolean isOnCall() {
        return switch (type) {
            case DayOnCall, NightOnCall -> true;
            default -> false;
        };
    }

    public boolean isWorking() {
        return !type.equals(ShiftTypes.NotWorking);
    }

    public double getHours() {
        return switch (type) {
            case DayOnCall, NightOnCall -> 12.5;
            case NormalShift -> 10;
            case NotWorking -> 0;
        };
    }

    public ShiftTypes getType() {
        return type;
    }

    public void updateType(ShiftTypes type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type.name();
    }

}
