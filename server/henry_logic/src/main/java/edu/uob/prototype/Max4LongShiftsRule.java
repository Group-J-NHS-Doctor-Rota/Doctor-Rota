package edu.uob.prototype;

import java.util.HashMap;

public class Max4LongShiftsRule extends Rule {

    private final int cost = 300;
    private final HashMap<String, Shift> shifts;
    private boolean broken;

    public Max4LongShiftsRule(Shift day1, Shift day2, Shift day3, Shift day4,
                              Shift day5, Shift day6, Shift day7) {
        shifts = new HashMap<>();
        shifts.put("day1", day1);
        shifts.put("day2", day2);
        shifts.put("day3", day3);
        shifts.put("day4", day4);
        shifts.put("day5", day5);
        shifts.put("day6", day6);
        shifts.put("day7", day7);
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
        return shifts.get("day1").isOnCall() && shifts.get("day2").isOnCall() &&
                shifts.get("day3").isOnCall() && shifts.get("day4").isOnCall();
    }

    private boolean haveTwoDaysOff() {
        return shifts.get("day5").getHours() + shifts.get("day6").getHours() == 0;
    }

    private boolean have48HoursOff() {
        if (shifts.get("day4").getType().equals(ShiftTypes.NightOnCall)) {
            return isDay7NightOrNotWorking() && haveTwoDaysOff();
        }
        return haveTwoDaysOff();
    }

    private boolean isDay7NightOrNotWorking() {
        return switch (shifts.get("day7").getType()) {
            case NightOnCall, NotWorking -> true;
            default -> false;
        };
    }
}
