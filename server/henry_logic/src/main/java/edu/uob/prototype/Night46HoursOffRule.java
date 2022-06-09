package edu.uob.prototype;

import java.util.HashMap;

public class Night46HoursOffRule  extends Rule {

    private final int cost = 150;
    private final HashMap<String, Shift> shifts;
    private boolean broken;

    public Night46HoursOffRule(Shift day1, Shift day2, Shift day3) {
        shifts = new HashMap<>();
        shifts.put("day1", day1);
        shifts.put("day2", day2);
        shifts.put("day3", day3);
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
        broken = shifts.get("day1").getType().equals(ShiftTypes.NightOnCall) &&
                !shifts.get("day2").getType().equals(ShiftTypes.NightOnCall) &&
                !haveTwoDaysOff();
    }

    private boolean haveTwoDaysOff() {
        return shifts.get("day2").getHours() + shifts.get("day3").getHours() == 0;
    }

}