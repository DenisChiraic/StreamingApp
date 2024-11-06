package model;

public class User {
    private int id;
    private String username;
    private String password;
    private Account account;

    public User(int id, String username, String password, Account account) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.account = new FreeAccount(); //Cont implicit gratuit
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

    public void setAccount(Account account) {
        this.account = account;
    }
}
