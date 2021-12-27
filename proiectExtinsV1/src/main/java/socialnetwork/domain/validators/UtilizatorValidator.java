package socialnetwork.domain.validators;

import socialnetwork.domain.User;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilizatorValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        validateString(entity.getFirstName());
        validateString(entity.getLastName());
        if (entity.getFriends() != null) {
            Set<Long> users = new HashSet<>();
            for (Long user : entity.getFriends()) {
                if (!users.add(user))
                    throw new ValidationException("Can't have duplicates in friend lists");
                validateLong(user);

            }
        }
    }

    public void validateString(String text) {
        if (text == null) {
            throw new ValidationException("Text can't be null");
        }
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            throw new ValidationException("Text can't contain numbers");
        }

    }

    public void validateLong(Long number) {
        if (number == null) {
            throw new ValidationException("Text can't be null");
        }
    }
}
