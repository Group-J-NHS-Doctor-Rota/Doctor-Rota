package edu.uob.prototype;

import java.util.HashMap;

public class MaxHoursWeekRule extends Rule {

    private final int cost = 100;
    private final HashMap<String, Shift> shifts;
    private boolean broken;

    public MaxHoursWeekRule(Shift monday, Shift tuesday, Shift wednesday, Shift thursday,
                            Shift friday, Shift saturday, Shift sunday) {
        shifts = new HashMap<>();
        shifts.put("monday", monday);
        shifts.put("tuesday", tuesday);
        shifts.put("wednesday", wednesday);
        shifts.put("thursday", thursday);
        shifts.put("friday", friday);
        shifts.put("saturday", saturday);
        shifts.put("sunday", sunday);
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
        int hours = 0;
        for(Shift shift : shifts.values()) {
            hours += shift.getHours();
        }
        //TODO confirm this as broken in example rota
        broken = hours > 48;
    }

    //TODO
    @Override
    public boolean resolve(Rota rota) {
        return false;
    }
}
