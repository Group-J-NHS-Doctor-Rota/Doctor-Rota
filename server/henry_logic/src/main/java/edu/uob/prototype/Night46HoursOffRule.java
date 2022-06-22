package edu.uob.prototype;

public class Night46HoursOffRule  extends Rule {

    private final int cost = 150;
    private Shift day1;
    private Shift day2;
    private Shift day3;
    private boolean broken;

    public Night46HoursOffRule(Shift day1, Shift day2, Shift day3) {
        this.day1 = day1;
        this.day2 = day2;
        this.day3 = day3;
        refresh();
    }

    @Override
    public boolean isBroken() {
        return broken;
    }

    @Override
    public int getCost() {
        if (broken) {
            return cost;
        }
        return 0;
    }

    @Override
    public void refresh() {
        broken = day1.getType().equals(ShiftTypes.NightOnCall) &&
                !day2.getType().equals(ShiftTypes.NightOnCall) &&
                !haveTwoDaysOff();
    }

    private boolean haveTwoDaysOff() {
        return day2.getHours() + day3.getHours() == 0;
    }

}