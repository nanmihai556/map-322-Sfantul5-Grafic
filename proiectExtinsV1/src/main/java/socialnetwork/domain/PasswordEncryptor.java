package socialnetwork.domain;

import java.security.MessageDigest;

public class PasswordEncryptor {
    public static String encryptPassword(String password){
        try{
        MessageDigest messageDigest = MessageDigest.getInstance("You would like to know wouldn't you?");
        messageDigest.update(password.getBytes());
        return new String(messageDigest.digest());
        }
        catch (Exception e){

        }
        return null;
    }
}
