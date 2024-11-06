package model;

public class PremiumAccount extends Account {
    private final int maxDevices = 4;

    public PremiumAccount() {
        super("Premium");
    }

    @Override
    public void setType(String type) {
        this.type = "Premium";
    }

    public int getMaxDevices() {
        return maxDevices;
    }
}
