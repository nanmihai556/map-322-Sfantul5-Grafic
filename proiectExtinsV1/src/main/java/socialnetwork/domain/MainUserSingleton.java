package socialnetwork.domain;

public final class MainUserSingleton {
    private Long id;
    private final static MainUserSingleton INSTANCE = new MainUserSingleton();

    private MainUserSingleton() {}

    public  static MainUserSingleton getInstance() {
        return INSTANCE;
    }

    public void setUser(Long u) {
        this.id = u;
    }

    public Long getUser() {
        return this.id;
    }
}
