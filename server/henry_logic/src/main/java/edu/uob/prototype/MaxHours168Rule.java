package edu.uob.prototype;

public class MaxHours168Rule extends Rule {

    private final int cost = 200;
    private final Shift day1;
    private final Shift day2;
    private final Shift day3;
    private final Shift day4;
    private final Shift day5;
    private final Shift day6;
    private final Shift day7;
    private boolean broken;

    public MaxHours168Rule(Shift day1, Shift day2, Shift day3, Shift day4,
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
        // Hours from 08:00 till 08:00 7 days later
        double hours = 0;
        hours += day1.getHours();
        hours += day2.getHours();
        hours += day3.getHours();
        hours += day4.getHours();
        hours += day5.getHours();
        hours += day6.getHours();
        hours += day7.getHours();
        // 71.5 instead of 72, as on call has 15 mins overlap at start and end
        broken = hours > 71.5;
    }

    //TODO
    @Override
    public boolean resolve(Rota rota) {
        return false;
    }
}
