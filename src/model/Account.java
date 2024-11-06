package model;

public abstract class Account {
    protected String type;

    public Account(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
    public abstract void setType(String type);

}
