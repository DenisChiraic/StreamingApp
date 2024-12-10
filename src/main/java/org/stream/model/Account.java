package org.stream.model;

/**
 * Clasa abstractă Account reprezintă un cont de utilizator, cu informații despre
 * tipul contului și numărul maxim de dispozitive permise.
 */
public abstract class Account {
    protected String type;
    protected int maxDevices;

    /**
     * Constructor pentru clasa Account.
     *
     * @param type        Tipul contului (e.g., "Free" sau "Premium").
     * @param maxDevices  Numărul maxim de dispozitive permise pentru acest cont.
     */
    public Account(String type, int maxDevices) {

        this.type = type;
        this.maxDevices = maxDevices;
    }

    /**
     * Returnează tipul contului.
     *
     * @return Tipul contului.
     */
    public String getType() {
        return type;
    }

    /**
     * Returnează numărul maxim de dispozitive permise.
     *
     * @return Numărul maxim de dispozitive permise.
     */
    public int getMaxDevices() {
        return maxDevices;
    }

}
