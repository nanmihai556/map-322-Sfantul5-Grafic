package socialnetwork.repository.database;

import socialnetwork.domain.Account;
import socialnetwork.domain.Message;
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

    public Long LogIn(String username, String password_unhashed) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from accounts WHERE username = (?) AND password = (?)");
             ) {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password_unhashed.getBytes());
            String passwordHashed = new String(messageDigest.digest());
            statement.setString(1, username);
            statement.setString(2, passwordHashed);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long idAdd = Long.valueOf(resultSet.getInt("user_id"));
                return idAdd;
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account save(Account entity) {
        super.save(entity);
        String sql = "insert into accounts (username, password, user_id) values (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, entity.getId().intValue());
            ps.setInt(2, entity.getUsername().intValue());
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(entity.getPassword().getBytes());
            String passwordHashed = new String(messageDigest.digest());
            ps.setString(3, passwordHashed);
            ps.executeUpdate();
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account delete(Long id) {
        Message removed = this.findOne(id);
        super.delete(id);
        String sql = "DELETE FROM accounts WHERE id = (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id.intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return removed;
    }


    public User update(Account entity, Integer status) {
        super.update(entity);
        String sql = "UPDATE accounts SET id = ?, _from = ?, _to = ?, _message = ?, _data = ?, _reply = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, entity.getId().intValue());
            ps.setInt(2, entity.getFrom().intValue());
            ps.setString(3, entity.getToHashed());
            ps.setString(4, entity.getMessage());
            Timestamp ts = new Timestamp(entity.getDate().toInstant(ZoneOffset.UTC).toEpochMilli());
            ps.setTimestamp(5, ts);
            ps.setInt(6, entity.getReply());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList toStringtoListConversion(String toString) {
        ArrayList<Long> toList = new ArrayList<>();
        String[] toStringLIst = toString.split(",");
        for (String toElementString : toStringLIst) {
            toList.add(Long.valueOf(toElementString));
        }
        return toList;
    }
}
