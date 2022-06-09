package edu.uob.prototype;

public class NotOnCallRequestRule extends Rule {

    private final int cost = 20;
    private final Shift shift;
    private boolean broken;

    public NotOnCallRequestRule(Shift day) {
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
}
