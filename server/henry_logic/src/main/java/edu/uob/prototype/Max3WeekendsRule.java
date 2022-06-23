package edu.uob.prototype;

public class Max3WeekendsRule extends Rule {

    private final int cost = 300;
    private final Shift saturday1;
    private final Shift sunday1;
    private final Shift saturday2;
    private final Shift sunday2;
    private final Shift saturday3;
    private final Shift sunday3;
    private boolean broken;

    public Max3WeekendsRule(Shift saturday1, Shift sunday1, Shift saturday2,
                            Shift sunday2, Shift saturday3, Shift sunday3) {
        this.saturday1 = saturday1;
        this.sunday1 = sunday1;
        this.saturday2 = saturday2;
        this.sunday2 = sunday2;
        this.saturday3 = saturday3;
        this.sunday3 = sunday3;
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
        broken = workingWeekend1() && workingWeekend2() &&
                workingWeekend3();
    }

    //TODO
    @Override
    public boolean resolve(Rota rota) {
        return false;
    }

    private boolean workingWeekend1() {
        return saturday1.isWorking() || sunday1.isWorking();
    }

    private boolean workingWeekend2() {
        return saturday2.isWorking() || sunday2.isWorking();
    }

    private boolean workingWeekend3() {
        return saturday3.isWorking() || sunday3.isWorking();
    }
}
