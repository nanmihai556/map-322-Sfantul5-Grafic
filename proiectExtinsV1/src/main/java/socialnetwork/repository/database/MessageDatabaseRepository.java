package socialnetwork.repository.database;

import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.memory.InMemoryRepository;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class MessageDatabaseRepository extends InMemoryRepository<Long, Message> {
    private String url;
    private String username;
    private String password;
    private Validator<Message> validator;

    public MessageDatabaseRepository(String url, String username, String password, Validator<Message> validator) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Message findOne(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages WHERE id = (?)");) {
            statement.setInt(1, id.intValue());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long idAdd = Long.valueOf(resultSet.getInt("id"));
                Integer reply = resultSet.getInt("_reply");
                Long from = resultSet.getLong("_from");
                String message = resultSet.getString("_message");
                Timestamp ts = resultSet.getTimestamp("_data");
                LocalDateTime localDt = LocalDateTime.ofInstant(Instant.ofEpochMilli(ts.getTime()), ZoneOffset.UTC);
                String to = resultSet.getString("_to");
                ArrayList<Long> toList = toStringtoListConversion(to);
                Message messageObj = new Message(from, toList, message, localDt, reply);
                messageObj.setId(idAdd);
                return messageObj;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long idAdd = Long.valueOf(resultSet.getInt("id"));
                Integer reply = resultSet.getInt("_reply");
                Long from = resultSet.getLong("_from");
                String message = resultSet.getString("_message");
                Timestamp ts = resultSet.getTimestamp("_data");
                LocalDateTime localDt = LocalDateTime.ofInstant(Instant.ofEpochMilli(ts.getTime()), ZoneOffset.UTC);
                String to = resultSet.getString("_to");
                ArrayList<Long> toList = toStringtoListConversion(to);
                Message messageObj = new Message(from, toList, message, localDt, reply);
                messageObj.setId(idAdd);
                users.add(messageObj);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Message save(Message entity) {
        super.save(entity);
        String sql = "insert into messages (id, _from, _to, _message, _data, _reply) values (?, ?, ?, ?, ?, ?)";

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

    @Override
    public Message delete(Long id) {
        Message removed = this.findOne(id);
        super.delete(id);
        String sql = "DELETE FROM messages WHERE id = (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id.intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return removed;
    }


    public User update(Message entity, Integer status) {
        super.update(entity);
        String sql = "UPDATE messages SET id = ?, _from = ?, _to = ?, _message = ?, _data = ?, _reply = ? WHERE id = ?";
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
