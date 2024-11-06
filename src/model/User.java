package model;

import java.util.UUID;

public class User {
    private UUID id;
    private String username;
    private String password;
    private Account account;

    public User(String username, String password, Account account) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.account = account;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Account getAccount() {
        return account;
    }

}
