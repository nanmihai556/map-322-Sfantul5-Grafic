package socialnetwork.repository.file;

import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;

import java.util.ArrayList;
import java.util.List;

public class UserFileRepository extends AbstractFileRepository<Long, User> {

    public UserFileRepository(String fileName, Validator<User> validator) {
        super(fileName, validator);
    }

    @Override
    public User extractEntity(List<String> attributes) {
        User user = new User(attributes.get(1), attributes.get(2));
        int friendsSize = Integer.parseInt(attributes.get(3));
        if (friendsSize > 0) {
            ArrayList<Long> friends = new ArrayList<Long>();
            for (int i = 0; i < friendsSize; i++) {
                friends.add(Long.parseLong(attributes.get(4 + i)));
            }
            user.setFriends(friends);
        }
        user.setId(Long.parseLong(attributes.get(0)));
        return user;
    }

    @Override
    protected String createEntityAsString(User entity) {
        String entityString;
        if (entity.getFriends() != null) {
            entityString = entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName() + ";" + entity.getFriends().size() + ";";
            for (Long id : entity.getFriends()) {
                entityString = entityString + id + ";";
            }
            return entityString;
        }
        return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName() + ";0";
    }
}
