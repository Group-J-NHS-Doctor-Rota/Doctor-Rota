package edu.uob.prototype;

public class Shift {

    private ShiftTypes type;

    public Shift(ShiftTypes type) {
        this.type = type;
    }

    public boolean isOnCall() {
        return switch (type) {
            case NotWorking, NormalShift -> false;
            default -> true;
        };
    }

    public boolean isWorking() {
        return !type.equals(ShiftTypes.NotWorking);
    }

    public double getHours() {
        return switch (type) {
            case NormalShift -> 10;
            case NotWorking -> 0;
            default -> 12.5;
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
