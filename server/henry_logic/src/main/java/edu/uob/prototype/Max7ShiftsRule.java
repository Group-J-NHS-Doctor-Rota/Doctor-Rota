package edu.uob.prototype;

public class Max7ShiftsRule extends Rule {

    private final int cost = 400;
    private final Shift day1;
    private final Shift day2;
    private final Shift day3;
    private final Shift day4;
    private final Shift day5;
    private final Shift day6;
    private final Shift day7;
    private final Shift day8;
    private final Shift day9;
    private final Shift day10;
    private boolean broken;

    public Max7ShiftsRule(Shift day1, Shift day2, Shift day3, Shift day4, Shift day5,
                          Shift day6, Shift day7, Shift day8, Shift day9, Shift day10) {
        this.day1 = day1;
        this.day2 = day2;
        this.day3 = day3;
        this.day4 = day4;
        this.day5 = day5;
        this.day6 = day6;
        this.day7 = day7;
        this.day8 = day8;
        this.day9 = day9;
        this.day10 = day10;
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
        if(!areDays1To7Working()) {
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

    private boolean areDays1To7Working() {
        return day1.isWorking() && day2.isWorking() &&
                day3.isWorking() && day4.isWorking() &&
                day5.isWorking() && day6.isWorking() && day7.isWorking();
    }

    private boolean haveTwoDaysOff() {
        return day8.getHours() + day9.getHours() == 0;
    }

    private boolean have48HoursOff() {
        if (day7.getType().equals(ShiftTypes.NightOnCall)) {
            return isDay10NightOrNotWorking() && haveTwoDaysOff();
        }
        return haveTwoDaysOff();
    }

    private boolean isDay10NightOrNotWorking() {
        return switch (day10.getType()) {
            case NightOnCall, NotWorking -> true;
            default -> false;
        };
    }

}
