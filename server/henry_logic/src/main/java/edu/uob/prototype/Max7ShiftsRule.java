package edu.uob.prototype;

import java.util.HashMap;

public class Max7ShiftsRule extends Rule {

    private final int cost = 400;
    private final HashMap<String, Shift> shifts;
    private boolean broken;

    public Max7ShiftsRule(Shift day1, Shift day2, Shift day3, Shift day4, Shift day5,
                          Shift day6, Shift day7, Shift day8, Shift day9, Shift day10) {
        shifts = new HashMap<>();
        shifts.put("day1", day1);
        shifts.put("day2", day2);
        shifts.put("day3", day3);
        shifts.put("day4", day4);
        shifts.put("day5", day5);
        shifts.put("day6", day6);
        shifts.put("day7", day7);
        shifts.put("day8", day8);
        shifts.put("day9", day9);
        shifts.put("day10", day10);
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

    private boolean areDays1To7Working() {
        return shifts.get("day1").isWorking() && shifts.get("day2").isWorking() &&
                shifts.get("day3").isWorking() && shifts.get("day4").isWorking() &&
                shifts.get("day5").isWorking() && shifts.get("day6").isWorking() && shifts.get("day7").isWorking();
    }

    private boolean haveTwoDaysOff() {
        return shifts.get("day8").getHours() + shifts.get("day9").getHours() == 0;
    }

    private boolean have48HoursOff() {
        if (shifts.get("day7").getType().equals(ShiftTypes.NightOnCall)) {
            return isDay10NightOrNotWorking() && haveTwoDaysOff();
        }
        return haveTwoDaysOff();
    }

    private boolean isDay10NightOrNotWorking() {
        return switch (shifts.get("day10").getType()) {
            case NightOnCall, NotWorking -> true;
            default -> false;
        };
    }

}
