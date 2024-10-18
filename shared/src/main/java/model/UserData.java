package model;

public record UserData(String username, String password, String email) implements DataRepresentationObj {
}

//includes get functions