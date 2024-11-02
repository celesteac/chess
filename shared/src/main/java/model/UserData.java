package model;

public record UserData(String username, String password, String email)    implements DataRepresentationObj     {
    public UserData updateUserPassword(String encryptedPassword){
        return new UserData(username, encryptedPassword, email);
    }
}

//includes get functions