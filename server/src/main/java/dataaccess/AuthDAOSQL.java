package dataaccess;

import model.AuthData;
import service.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;

public class AuthDAOSQL implements AuthDAO  {

    public void addAuthData(AuthData authData) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()){
            String statement = "INSERT INTO auth (username, authtoken) VALUES (?, ?)";
            try (var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, authData.username());
                preparedStatement.setString(2, authData.authToken());
                preparedStatement.executeUpdate();
            }
        }catch (Exception ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public AuthData getAuthData(String authToken) throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT username FROM auth WHERE authtoken = ?";
            try(var preparedStatement = conn.prepareStatement(statement) ) {
                preparedStatement.setString(1, authToken);
                try(var rs = preparedStatement.executeQuery()){
                    if(rs.next()){
                        String foundUsername = rs.getString("username");
                        AuthData foundAuthData = new AuthData(authToken, foundUsername);
                        return foundAuthData;
                    } else {
                        return null;
                    }
                }
            }
        } catch (Exception ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public boolean deleteAuth(AuthData auth) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "DELETE FROM auth WHERE auth.authtoken = ?";
            try(var preparedStatement = conn.prepareStatement(statement) ) {
                preparedStatement.setString(1, auth.authToken());
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (Exception ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE TABLE auth";
            try(var preparedStatement = conn.prepareStatement(statement) ) {
                preparedStatement.executeUpdate();
            }

        } catch (Exception ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public int getNumAuths() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT COUNT(0) AS count FROM auth";
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
