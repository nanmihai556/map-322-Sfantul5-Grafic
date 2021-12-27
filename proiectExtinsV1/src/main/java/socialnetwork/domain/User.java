package socialnetwork.domain;

import java.util.List;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
    private List<Long> friends;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Long> getFriends() {
        return friends;
    }

    public void setFriends(List<Long> newFriends) {
        friends = newFriends;
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "id=" + getId() +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", friends=" + friends +
                '}';
    }
}