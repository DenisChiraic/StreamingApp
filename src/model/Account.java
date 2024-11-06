package model;

public abstract class Account {
    protected String type;
    protected int maxDivices;

    public Account(String type, int maxDivices) {

        this.type = type;
        this.maxDivices = maxDivices;
    }
    public String getType() {
        return type;
    }

    public int getMaxDivices() {
        return maxDivices;
    }

}
