package socialnetwork.repository.database;

import socialnetwork.domain.Account;
import socialnetwork.domain.Message;
import socialnetwork.domain.PasswordEncryptor;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.memory.InMemoryRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class LogInDatabaseRepository extends InMemoryRepository<Long, Account> {
    private String url;
    private String username;
    private String password;
    private Validator<Account> validator;

    public LogInDatabaseRepository(String url, String username, String password, Validator<Account> validator) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    public Long LogIn(String accountUsername, String passwordNotEncrypted) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from accounts WHERE username = (?) AND password = (?)");
             ) {
            String passwordEncrypted = PasswordEncryptor.encryptPassword(passwordNotEncrypted);
            statement.setString(1, accountUsername);
            statement.setString(2, passwordEncrypted);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long idAdd = Long.valueOf(resultSet.getInt("user_id"));
                return idAdd;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account save(Account entity) {
        super.save(entity);
        String sql = "insert into accounts (id, username, password, user_id) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, entity.getId().intValue());
            ps.setString(2, entity.getUsername());
            ps.setString(3, PasswordEncryptor.encryptPassword(entity.getPassword()));
            ps.setInt(4, entity.getUser_id().intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean usernameTaken(String AccountUsername){
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from accounts WHERE username = (?)");
        ) {
            statement.setString(1, AccountUsername);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getUsername(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from accounts WHERE user_id = (?)");
        ) {
            statement.setInt(1, id.intValue());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
