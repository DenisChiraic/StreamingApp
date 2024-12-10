package org.stream.model;

/**
 * Clasa PremiumAccount reprezintă un cont premium cu o limită de 4 dispozitive.
 */
public class PremiumAccount extends Account {

    /**
     * Constructor pentru clasa PremiumAccount.
     * Setează tipul contului la "Premium" și numărul maxim de dispozitive la 4.
     */
    public PremiumAccount() {
        super("Premium", 5);
    }
}
