package dataaccess;

import model.UserData;
import service.DatabaseManager;

import java.sql.Connection;

public class UserDAOSQL implements UserDAO {

    public void addUser(UserData newUser) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()){
            String statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            try (var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, newUser.username());
                preparedStatement.setString(2, newUser.password());
                preparedStatement.setString(3, newUser.email());
                preparedStatement.executeUpdate();
            }
        } catch (Exception ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT password, email FROM users WHERE username = ?";
            try(var preparedStatement = conn.prepareStatement(statement) ) {
                preparedStatement.setString(1, username);
                try(var rs = preparedStatement.executeQuery()){
                    if(rs.next()){
                        String foundPassword = rs.getString("password");
                        String foundEmail = rs.getString("email");
                        return new UserData(username, foundPassword, foundEmail);
                    } else {
                        return null;
                    }
                }
            }
        } catch (Exception ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE TABLE users";
            try(var preparedStatement = conn.prepareStatement(statement) ) {
                preparedStatement.executeUpdate();
            }

        } catch (Exception ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public int getNumUsers() throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT COUNT(0) AS count FROM users";
            try(var preparedStatement = conn.prepareStatement(statement) ) {
                try(var rs = preparedStatement.executeQuery()){
                    if(rs.next()){
                        return rs.getInt("count");
                    } else {
                        return 0;
                    }
                }
            }
        } catch (Exception ex){
            throw new DataAccessException(ex.getMessage());
        }
    }
}
