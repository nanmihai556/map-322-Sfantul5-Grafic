package socialnetwork.repository.database;

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

public class DatabaseRepository extends InMemoryRepository<Long, User> {
    private String url;
    private String username;
    private String password;
    private Validator<User> validator;

    public DatabaseRepository(String url, String username, String password, Validator<User> validator) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public User findOne(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users WHERE id = (?)");) {
            statement.setInt(1, id.intValue());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long idAdd = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                User utilizator = new User(firstName, lastName);
                utilizator.setId(idAdd);
                utilizator.setFriends(this.getFriends(utilizator));
                return utilizator;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                User utilizator = new User(firstName, lastName);
                utilizator.setId(id);
                utilizator.setFriends(this.getFriends(utilizator));
                users.add(utilizator);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User save(User entity) {
        super.save(entity);
        String sql = "insert into users (id, first_name, last_name ) values (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, entity.getId().intValue());
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getLastName());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User delete(Long id) {
        User removed = this.findOne(id);
        super.delete(id);
        String sql = "DELETE FROM users WHERE id = (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id.intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return removed;
    }


    public User update(User entity, Integer status) {
        super.update(entity);
        String sql = "UPDATE users SET first_name = ?, last_name = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setInt(3, entity.getId().intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateAddFriends(entity, status);
        updateRemoveFriends(entity);
        return null;
    }

    public void updateAddFriends(User entity, Integer status) {
        // status = 0->pending; 1->accepted; 2->rejected
        for (Long id : entity.getFriends()) {
            String sql = "SELECT * FROM friends WHERE id_friend_1 IN (?,?) AND id_friend_2 IN (?,?)";
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, entity.getId().intValue());
                ps.setInt(2, id.intValue());
                ps.setInt(3, entity.getId().intValue());
                ps.setInt(4, id.intValue());
                ResultSet resultSet = ps.executeQuery();
                if (!resultSet.isBeforeFirst()) {
                    PreparedStatement addFriend = connection.prepareStatement("insert into friends (id_friend_1, id_friend_2, date, status) values (?, ?, ?, ?)");
                    addFriend.setInt(1, entity.getId().intValue());
                    addFriend.setInt(2, id.intValue());
                    Timestamp ts = null;
                    LocalDateTime localDt = LocalDateTime.now();
                    ts = new Timestamp(localDt.toInstant(ZoneOffset.UTC).toEpochMilli());
                    addFriend.setTimestamp(3, ts);
                    addFriend.setInt(4, status);
                    addFriend.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public LocalDateTime dateOfFriendship(User user1, User user2) {
        String sql = "SELECT date FROM friends WHERE id_friend_1 IN (?,?) AND id_friend_2 IN (?,?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, user1.getId().intValue());
            ps.setInt(2, user2.getId().intValue());
            ps.setInt(3, user1.getId().intValue());
            ps.setInt(4, user2.getId().intValue());
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                Timestamp ts = resultSet.getTimestamp("date");
                LocalDateTime localDt = LocalDateTime.ofInstant(Instant.ofEpochMilli(ts.getTime()), ZoneOffset.UTC);
                return localDt;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean areFriends(Long userID1, Long userID2){
        String sql = "SELECT date FROM friends WHERE id_friend_1 IN (?,?) AND id_friend_2 IN (?,?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userID1.intValue());
            ps.setInt(2, userID2.intValue());
            ps.setInt(3, userID1.intValue());
            ps.setInt(4, userID2.intValue());
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setStatusFriendRequests(User user1, User user2, Integer status) {
        // status = 0->pending; 1->accepted; 2->rejected
        String sql = "UPDATE friends SET status = ? WHERE id_friend_1 IN (?,?) AND id_friend_2 IN (?,?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setInt(2, user1.getId().intValue());
            ps.setInt(3, user2.getId().intValue());
            ps.setInt(4, user1.getId().intValue());
            ps.setInt(5, user2.getId().intValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateRemoveFriends(User entity) {
        String sql = "SELECT * FROM friends WHERE id_friend_1 = (?) OR id_friend_2 = (?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, entity.getId().intValue());
            ps.setInt(2, entity.getId().intValue());
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                removeFriend1(entity, resultSet, connection);
                removeFriend2(entity, resultSet, connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeFriend1(User entity, ResultSet resultSet, Connection connection) {
        try {
            Long idF = resultSet.getLong("id_friend_1");
            boolean aux = false;
            for (Long id : entity.getFriends()) {
                if (idF.equals(id) || idF.equals(entity.getId())) {
                    aux = true;
                }
            }
            if (!aux) {
                String sql = "DELETE FROM friends WHERE id_friend_1 = (?) AND id_friend_2 = (?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, idF.intValue());
                ps.setInt(2, entity.getId().intValue());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Long> getFriends(User entity) {
        ArrayList<Long> list1 = getFriendsBase(entity, 1);
        list1.addAll(getFriendsBase(entity, 2));
        list1.addAll(getFriendsBase(entity, 0));
        return list1;
    }

    public ArrayList<Long> getFriendsAccepted(User entity) {
        return getFriendsBase(entity, 1);
    }

    public ArrayList<Long> getFriendsRejected(User entity) {
        return getFriendsBase(entity, 2);
    }

    public ArrayList<Long> getFriendsPending(User entity) {
        ArrayList<Long> friends = new ArrayList<>();
        String sql = "SELECT * FROM friends WHERE id_friend_2 = (?) AND status = 0";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, entity.getId().intValue());
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Long idf1 = (long) resultSet.getInt("id_friend_1");
                friends.add(idf1);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    public Boolean didF1RequestF2(Long F1, Long F2) {
        ArrayList<Long> friends = new ArrayList<>();
        String sql = "SELECT * FROM friends WHERE id_friend_1 = ? AND id_friend_2 = ? AND status = 0";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, F1.intValue());
            ps.setInt(2, F2.intValue());
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private ArrayList<Long> getFriendsBase(User entity, Integer Status) {
        ArrayList<Long> friends = new ArrayList<>();
        String sql = "SELECT * FROM friends WHERE (id_friend_1 = (?) OR id_friend_2 = (?)) AND status = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, entity.getId().intValue());
            ps.setInt(2, entity.getId().intValue());
            ps.setInt(3, Status);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Long idf1 = Long.valueOf(resultSet.getInt("id_friend_1"));
                Long idf2 = Long.valueOf(resultSet.getInt("id_friend_2"));
                if (idf1.equals(entity.getId())) {
                    friends.add(idf2);
                } else {
                    friends.add(idf1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    private void removeFriend2(User entity, ResultSet resultSet, Connection connection) {
        try {
            Long idF = resultSet.getLong("id_friend_2");
            boolean aux = false;
            for (Long id : entity.getFriends()) {
                if (idF.equals(id) || idF.equals(entity.getId())) {
                    aux = true;
                }
            }
            if (!aux) {
                String sql = "DELETE FROM friends WHERE id_friend_1 = (?) AND id_friend_2 = (?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(2, idF.intValue());
                ps.setInt(1, entity.getId().intValue());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
