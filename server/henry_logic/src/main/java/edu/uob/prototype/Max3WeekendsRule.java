package edu.uob.prototype;

import java.util.HashMap;

public class Max3WeekendsRule extends Rule {

    private final int cost = 300;
    private final HashMap<String, Shift> shifts;
    private boolean broken;

    public Max3WeekendsRule(Shift saturday1, Shift sunday1, Shift saturday2,
                            Shift sunday2, Shift saturday3, Shift sunday3) {
        shifts = new HashMap<>();
        shifts.put("saturday1", saturday1);
        shifts.put("sunday1", sunday1);
        shifts.put("saturday2", saturday2);
        shifts.put("sunday2", sunday2);
        shifts.put("saturday3", saturday3);
        shifts.put("sunday3", sunday3);
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
        broken = workingWeekend("1") && workingWeekend("2") &&
                workingWeekend("3");
    }

    //TODO
    @Override
    public boolean resolve(Rota rota) {
        return false;
    }

    private boolean workingWeekend(String number) {
        return shifts.get("saturday".concat(number)).isWorking() ||
                shifts.get("sunday".concat(number)).isWorking();
    }
}
