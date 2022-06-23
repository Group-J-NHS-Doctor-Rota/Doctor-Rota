package edu.uob.prototype;

public class Max4LongShiftsRule extends Rule {

    private final int cost = 300;
    private final Shift day1;
    private final Shift day2;
    private final Shift day3;
    private final Shift day4;
    private final Shift day5;
    private final Shift day6;
    private final Shift day7;
    private boolean broken;

    public Max4LongShiftsRule(Shift day1, Shift day2, Shift day3, Shift day4,
                              Shift day5, Shift day6, Shift day7) {
        this.day1 = day1;
        this.day2 = day2;
        this.day3 = day3;
        this.day4 = day4;
        this.day5 = day5;
        this.day6 = day6;
        this.day7 = day7;
        refresh();
    }

    @Override
    public boolean isBroken() {
        return broken;
    }

    @Override
    public int getCost() {
        if(broken) {
            return cost;
        }
        return 0;
    }

    @Override
    public void refresh() {
        if(!haveFourOnCallShifts()) {
            broken = false;
            return;
        }
        broken = !have48HoursOff();
    }

    //TODO
    @Override
    public boolean resolve(Rota rota) {
        return false;
    }

    private boolean haveFourOnCallShifts() {
        return day1.isOnCall() && day2.isOnCall() &&
                day3.isOnCall() && day4.isOnCall();
    }

    private boolean haveTwoDaysOff() {
        return day5.getHours() + day6.getHours() == 0;
    }

    private boolean have48HoursOff() {
        if (day4.getType().equals(ShiftTypes.NightOnCall)) {
            return isDay7NightOrNotWorking() && haveTwoDaysOff();
        }
        return haveTwoDaysOff();
    }

    private boolean isDay7NightOrNotWorking() {
        return switch (day7.getType()) {
            case NightOnCall, NotWorking -> true;
            default -> false;
        };
    }
}
