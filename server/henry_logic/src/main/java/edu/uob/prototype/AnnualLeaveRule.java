package edu.uob.prototype;

public class AnnualLeaveRule extends Rule {

    private final int cost = 50;
    private final Shift shift;
    private boolean broken;

    public AnnualLeaveRule(Shift day) {
        shift = day;
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
        broken = shift.isWorking();
    }

    //TODO
    @Override
    public boolean resolve(Rota rota) {
        return false;
    }
}
