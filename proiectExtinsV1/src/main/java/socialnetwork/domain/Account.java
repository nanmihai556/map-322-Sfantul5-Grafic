package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Account extends Entity<Long> {
    private final String username;
    private final String password;
    private final Long user_id;

    public Account(String username, String password, Long user_id) {
        this.username = username;
        this.password = password;
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Long getUser_id() {
        return user_id;
    }
}
