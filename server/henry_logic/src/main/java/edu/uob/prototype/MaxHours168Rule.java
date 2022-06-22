package edu.uob.prototype;

import java.util.HashMap;

public class MaxHours168Rule extends Rule {

    private final int cost = 200;
    private final HashMap<String, Shift> shifts;
    private boolean broken;

    public MaxHours168Rule(Shift day1, Shift day2, Shift day3, Shift day4,
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
        // Hours from 08:00 till 08:00 7 days later
        double hours = 0;
        for(Shift shift : shifts.values()) {
            hours += shift.getHours();
        }
        // 71.5 instead of 72, as on call has 15 mins overlap at start and end
        broken = hours > 71.5;
    }

    //TODO
    @Override
    public boolean resolve(Rota rota) {
        return false;
    }
}
