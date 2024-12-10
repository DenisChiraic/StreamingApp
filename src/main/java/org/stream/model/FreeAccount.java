package org.stream.model;

/**
 * Clasa FreeAccount reprezintă un cont gratuit cu o limită de 1 dispozitiv.
 */
public class FreeAccount  extends Account {

    /**
     * Constructor pentru clasa FreeAccount.
     * Setează tipul contului la "Free" și numărul maxim de dispozitive la 1.
     */
    public FreeAccount() {
        super("Free", 1);
    }
}