package model;

public class FreeAccount  extends Account{
    private final int maxDevices = 1;

    public FreeAccount() {
        super("Free");
    }

    @Override
    public void setType(String type) {
        this.type = "Free";
    }
    public int getMaxDevices() {
        return maxDevices;
    }
}
