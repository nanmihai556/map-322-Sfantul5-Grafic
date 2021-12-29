package socialnetwork.domain;

public final class CurrentUserSingleton {
    private Long id;
    private final static CurrentUserSingleton INSTANCE = new CurrentUserSingleton();

    private CurrentUserSingleton() {}

    public  static CurrentUserSingleton getInstance() {
        return INSTANCE;
    }

    public void setUser(Long u) {
        this.id = u;
    }

    public Long getUser() {
        return this.id;
    }
}
