package edu.uob.prototype;

public abstract class Rule {

    public abstract boolean isBroken();
    public abstract int getCost();
    public abstract void refresh();
    public abstract boolean resolve(Rota rota);

}
